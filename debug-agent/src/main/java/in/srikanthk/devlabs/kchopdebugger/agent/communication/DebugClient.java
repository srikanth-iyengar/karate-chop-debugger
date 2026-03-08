package in.srikanthk.devlabs.kchopdebugger.agent.communication;

import in.srikanthk.devlabs.kchopdebugger.agent.DebugMessageBus;
import in.srikanthk.devlabs.kchopdebugger.agent.DebuggerState;
import in.srikanthk.devlabs.kchopdebugger.agent.KarateVariableSnapshot;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DebugClient extends DebugServer {
    private Thread subscriber;
    private final static Logger logger = LoggerFactory.getLogger(DebugClient.class);

    public DebugClient(String host, int port) throws IOException, InterruptedException {
        super();
        int retryCount = 0;
        while (retryCount++ < 10) {
            try {
                this.socket = new Socket(host, port);
                this.startSubscriber();
                this.registerForwarder();
                logger.info("Started debugging on port {}", port);
                break;
            } catch (Exception e) {
                Thread.sleep(retryCount * 300L);
            }
        }
    }

    public void startSubscriber() {
        var publisher = DebugMessageBus.getInstance().publisher(DebugRequest.TOPIC);
        Thread subscriberThread = new Thread(() -> {
            try(ObjectInputStream stream = new ObjectInputStream(socket.getInputStream())) {
                while(!Thread.currentThread().isInterrupted()) {
                    RemoteCall call = (RemoteCall) stream.readObject();
                    Method[] methods = DebugRequest.class.getMethods();
                    for(Method method: methods){
                        if(method.getName().startsWith(call.getMethodName())){
                            logger.info("method: {}", method);
                            method.invoke(publisher, call.getArgs().toArray(new Object[0]));
                            break;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException | InvocationTargetException |
                     IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        });
        subscriberThread.setDaemon(true);
        subscriberThread.start();

        this.subscriber = subscriberThread;
    }

    /**
     * Registers and subscribes a DebugResponse forwarder that forwards debugger callbacks to the remote endpoint.
     *
     * <p>The forwarder serializes RemoteCall objects to the client's socket output stream for the following callbacks:
     * <ul>
     *   <li>{@code updateKarateVariable} — forwards the provided map of variable snapshots.</li>
     *   <li>{@code updateState} — forwards the debugger state.</li>
     *   <li>{@code navigateTo} — forwards the file path and line number.</li>
     *   <li>{@code appendLog} — forwards the log message and a success flag.</li>
     *   <li>{@code evaluationResult} — forwards the evaluation result and any error string.</li>
     * </ul>
     *
     * @throws IOException if creating the output stream to the socket fails
     */
    public void registerForwarder() throws IOException {
        var requestForwarder = new DebugResponse() {
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            @Override
            public void updateKarateVariable(HashMap<String, KarateVariableSnapshot> vars) {
                RemoteCall call = RemoteCall.builder().args(List.of(vars)).methodName("updateKarateVariable").build();
                try {
                    stream.writeObject(call);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void updateState(DebuggerState state) {
                RemoteCall call = RemoteCall.builder().args(List.of(state)).methodName("updateState").build();
                try {
                    stream.writeObject(call);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void navigateTo(String filePath, Integer lineNumber) {
                RemoteCall call = RemoteCall.builder().args(List.of(filePath, lineNumber)).methodName("navigateTo").build();
                try {
                    stream.writeObject(call);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void appendLog(String log, Boolean isSuccess) {
                RemoteCall call = RemoteCall.builder().args(List.of(log, isSuccess)).methodName("appendLog").build();
                try {
                    stream.writeObject(call);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void evaluationResult(String result, String error) {
                List<Object> args = new ArrayList<>();
                args.add(result);
                args.add(error);
                RemoteCall call = RemoteCall.builder().args(args).methodName("evaluationResult").build();
                try {
                    stream.writeObject(call);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        DebugMessageBus.getInstance().subscribe(DebugResponse.TOPIC, requestForwarder);
    }

    @Override
    public void stop() {
        try {
            if (this.subscriber != null) {
                this.subscriber.interrupt();
            }
        } catch(Exception e) {

        }
    }
}
