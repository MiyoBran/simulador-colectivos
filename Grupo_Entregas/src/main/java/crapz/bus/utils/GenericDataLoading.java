package crapz.bus.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class GenericDataLoading<K, V> {
    /**
     * Loads data from a text file into a generic map.
     *
     * @param filePath    The path or name of the file to read.
     * @param keyMapper   A function that converts the key string to type K.
     * @param valueMapper A function that converts each value string to type V.
     * @return A map where the key is of type K and the value is a list of type V.
     * @throws IOException              If an error occurs while reading the file
     *                                  (e.g., not found, permissions).
     * @throws IllegalArgumentException If a line has an incorrect format or the key
     *                                  or a value cannot be converted.
     */
    private Map<K, List<V>> loadDataFromFile(String filePath, Function<String, K> keyMapper,
            Function<String, V> valueMapper) throws IOException {

        Map<K, List<V>> dataMap = new TreeMap<>();
        int lineNumber = 0; // Line counter for better debugging

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            if (is == null) {
                throw new IOException("Unable to find " + filePath + " in classpath.");
            }
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                lineNumber++; // Increment the line counter

                if (currentLine.trim().isEmpty() || currentLine.trim().startsWith("#")) {
                    continue; // Ignore empty lines or comments/headers
                }

                // We use -1 as the limit so that split does not discard trailing empty parts if
                // there are ";;" or ";" at the end
                String[] parts = currentLine.split(";", -1);

                // Validate that the line has at least the key and a delimiter
                // If parts.length < 2, it means there was no ';' delimiter or nothing after it.
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Line " + lineNumber
                            + " has incorrect format (missing delimiter ';' or expected values): '" + currentLine
                            + "'");
                }

                String keyString = parts[0].trim();

                // Validate that the key is not empty
                if (keyString.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Line " + lineNumber + " has an empty key after the delimiter: '" + currentLine + "'");
                }

                K key = null;
                try {
                    key = keyMapper.apply(keyString); // Convert the key
                } catch (Exception e) {
                    // Catch any exception from the key mapping function and rethrow as
                    // IllegalArgumentException
                    throw new IllegalArgumentException("Line " + lineNumber + ": Error converting key '"
                            + keyString + "'. Details: " + e.getMessage() + ". Full line: '" + currentLine + "'",
                            e);
                }

                List<V> values = new ArrayList<>();

                // Iterate from the second part (index 1) to get the values
                for (int i = 1; i < parts.length; i++) {
                    String valuePartString = parts[i];

                    if (!valuePartString.trim().isEmpty()) {
                        try {
                            values.add(valueMapper.apply(valuePartString.trim()));
                        } catch (Exception e) {
                            // Catch any exception from the value mapping function and rethrow
                            throw new IllegalArgumentException("Line " + lineNumber + ": Value '"
                                    + valuePartString.trim() + "' could not be converted. Details: " + e.getMessage()
                                    + ". Full line: '" + currentLine + "'", e);
                        }
                    }
                }

                dataMap.put(key, values);
            }
        }

        return dataMap;
    }

    public Map<K, List<V>> loadFromFile(String filePath, Function<String, K> keyMapper, Function<String, V> valueMapper)
            throws IOException {
        return loadDataFromFile(filePath, keyMapper, valueMapper);
    }
}