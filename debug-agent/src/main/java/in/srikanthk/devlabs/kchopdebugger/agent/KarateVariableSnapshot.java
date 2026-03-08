package in.srikanthk.devlabs.kchopdebugger.agent;

import java.io.Serializable;

public class KarateVariableSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String type;
    private final String value;

    public KarateVariableSnapshot(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
