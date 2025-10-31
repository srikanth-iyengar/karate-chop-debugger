package in.srikanthk.devlabs.kchopdebugger.agent;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class SessionState {
    @Getter
    private static final SessionState instance = new SessionState();

    private Map<String, TreeSet<Integer>> breakpoints = new ConcurrentHashMap<>();
    private String projectPath;
    private String featureClassPath;
    private boolean skipBreakpoints = false;

    // singleton
    private SessionState() {

    }
}
