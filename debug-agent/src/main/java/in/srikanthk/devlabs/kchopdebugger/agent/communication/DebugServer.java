package in.srikanthk.devlabs.kchopdebugger.agent.communication;

import in.srikanthk.devlabs.kchopdebugger.agent.DebugMessageBus;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class DebugServer {
    private static final DebugServer INSTANCE = new DebugServer();
    private static final Logger logger = LoggerFactory.getLogger(DebugServer.class);
    @Getter
    private Integer port;
    Socket socket;
    private Thread subscriber;
    private DebugRequest requestForwarder;

    DebugServer() {
    }

    public static DebugServer getInstance() {
        return INSTANCE;
    }

    public DebugServer start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        this.port = serverSocket.getLocalPort();
        Thread serverThread = new Thread(() -> {
            try {
                this.socket = serverSocket.accept();
                startSubscriber();
                registerForwarder();
            } catch (IOException e) {
            }
        });
        serverThread.start();
        return this;
    }

    public void startSubscriber() {
        var publisher = DebugMessageBus.getInstance().publisher(DebugResponse.TOPIC);
        Thread subscriberThread = new Thread(() -> {
            try (ObjectInputStream stream = new ObjectInputStream(socket.getInputStream())) {
                while (!Thread.currentThread().isInterrupted()) {
                    RemoteCall call = (RemoteCall) stream.readObject();
                    Method[] methods = DebugResponse.class.getMethods();
                    for (Method method : methods) {
                        if (method.getName().startsWith(call.getMethodName())) {
                            logger.info("method: {}", method);
                            method.invoke(publisher, call.getArgs().toArray(new Object[0]));
                            break;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException | InvocationTargetException |
                     IllegalAccessException ignored) {
            }
        });
        subscriberThread.setDaemon(true);
        subscriberThread.start();

        this.subscriber = subscriberThread;
    }

    public void registerForwarder() throws IOException {
        this.requestForwarder = new DebugRequest() {
            final ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());

            private void sendCall(String methodName, Object... args) {
                RemoteCall call = RemoteCall.builder()
                        .methodName(methodName)
                        .args(List.of(args))
                        .build();
                try {
                    stream.writeObject(call);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void publishKarateVariables() {
                sendCall("publishKarateVariables");
            }

            @Override
            public void stepInto() {
                sendCall("stepInto");
            }

            @Override
            public void stepOver() {
                sendCall("stepOver");
            }

            @Override
            public void resume() {
                sendCall("resume");
            }

            @Override
            public void evaluateExpression(String expression) {
                sendCall("evaluateExpression", expression);
            }

            @Override
            public void addBreakpoint(String fileName, Integer lineNumber) {
                sendCall("addBreakpoint", fileName, lineNumber);
            }

            @Override
            public void removeBreakpoint(String fileName, Integer lineNumber) {
                sendCall("removeBreakpoint", fileName, lineNumber);
            }

            @Override
            public void stepBack() {
                sendCall("stepBack");
            }

            @Override
            public void hotReload() {
                sendCall("hotReload");
            }

            @Override
            public void setShouldSkipBreakpoints(boolean skipBreakpoints) {
                sendCall("setShouldSkipBreakpoints", skipBreakpoints);
            }

            public void stepOut() {
                sendCall("stepOut");
            }
        };

        DebugMessageBus.getInstance().subscribe(DebugRequest.TOPIC, requestForwarder);
    }

    public void stop() {
        try {
            this.requestForwarder = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
