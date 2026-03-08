package in.srikanthk.devlabs.kchopdebugger.agent;

import java.io.Serializable;

public class KarateVariableSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String type;
    private final String value;

    /**
     * Creates a KarateVariableSnapshot holding the variable's runtime type and its stringified value.
     *
     * @param type  the variable's runtime type name
     * @param value the variable's value as a string
     */
    public KarateVariableSnapshot(String type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the variable's declared type.
     *
     * @return the stored type string
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieve the stored variable value as a string.
     *
     * @return the variable's value as a `String`
     */
    public String getValue() {
        return value;
    }
}
