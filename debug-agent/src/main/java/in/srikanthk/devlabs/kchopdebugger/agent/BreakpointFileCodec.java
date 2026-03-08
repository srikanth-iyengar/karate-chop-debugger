package in.srikanthk.devlabs.kchopdebugger.agent;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class BreakpointFileCodec {
    private static final String ENTRY_SEPARATOR = "\t";
    private static final String VALUE_SEPARATOR = ",";

    private BreakpointFileCodec() {
    }

    public static String encode(Map<String, ? extends Collection<Integer>> breakpoints) {
        return breakpoints.entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> encodePath(entry.getKey()) + ENTRY_SEPARATOR + entry.getValue().stream()
                        .filter(line -> line != null)
                        .sorted()
                        .map(String::valueOf)
                        .collect(Collectors.joining(VALUE_SEPARATOR)))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public static Map<String, TreeSet<Integer>> decode(String serializedBreakpoints) {
        Map<String, TreeSet<Integer>> breakpoints = new TreeMap<>();
        if (serializedBreakpoints == null || serializedBreakpoints.isBlank()) {
            return breakpoints;
        }

        for (String line : serializedBreakpoints.split("\\R")) {
            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split(ENTRY_SEPARATOR, 2);
            String filePath = decodePath(parts[0]);
            TreeSet<Integer> lines = breakpoints.computeIfAbsent(filePath, ignored -> new TreeSet<>());
            if (parts.length < 2 || parts[1].isBlank()) {
                continue;
            }

            for (String value : parts[1].split(VALUE_SEPARATOR)) {
                if (!value.isBlank()) {
                    lines.add(Integer.parseInt(value));
                }
            }
        }

        return breakpoints;
    }

    private static String encodePath(String path) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(path.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodePath(String encodedPath) {
        return new String(Base64.getUrlDecoder().decode(encodedPath), StandardCharsets.UTF_8);
    }
}
