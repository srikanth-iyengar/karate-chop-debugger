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

    /**
     * Prevents instantiation of this utility class.
     */
    private BreakpointFileCodec() {
    }

    /**
     * Encodes a mapping of file paths to breakpoint line numbers into a compact, deterministic string form.
     *
     * The output contains one entry per (non-null) file path, sorted by path. Each entry is the URL-safe
     * Base64 (no padding) encoding of the file path, followed by a tab character, followed by the file's
     * breakpoint line numbers sorted in ascending order and joined by commas. Null line entries are ignored.
     *
     * @param breakpoints map from file path to a collection of breakpoint line numbers; entries with a null key are ignored
     * @return a line-separated string representation of the provided breakpoints as described above
     */
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

    /**
     * Parses a serialized breakpoint representation into a sorted map of file paths to line numbers.
     *
     * The input is expected as zero or more lines separated by any line terminator. Each non-blank line
     * must contain an encoded file path (Base64 URL-safe, no padding) and an optional tab-separated
     * value list. The value list, if present, is a comma-separated sequence of integers (line numbers).
     * Blank or missing lines/value entries are ignored.
     *
     * @param serializedBreakpoints the serialized breakpoint string to parse; may be null or blank
     * @return a TreeMap whose keys are decoded file paths and whose values are TreeSet<Integer> of
     *         sorted line numbers for that file; returns an empty map for null or blank input
     * @throws NumberFormatException if any non-blank numeric token in the value list cannot be parsed as an integer
     */
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

    /**
     * Encodes a file path into a URL-safe Base64 string without padding.
     *
     * @param path the file path to encode; converted to bytes using UTF-8
     * @return the URL-safe Base64 representation of the path with padding removed
     */
    private static String encodePath(String path) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(path.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode a URL-safe Base64 string into the original file path.
     *
     * @param encodedPath the URL-safe Base64 (no-padding) representation of the path
     * @return the decoded file path as a UTF-8 string
     */
    private static String decodePath(String encodedPath) {
        return new String(Base64.getUrlDecoder().decode(encodedPath), StandardCharsets.UTF_8);
    }
}
