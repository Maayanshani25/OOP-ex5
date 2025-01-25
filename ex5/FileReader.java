package ex5;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The FileReader class is responsible for reading and preprocessing files for validation.
 * It includes methods to read lines from a file and preprocess those lines by removing comments
 * and empty lines.
 */
public class FileReader {

    /**
     * Reads all lines from the specified file and returns them as a list of strings.
     *
     * @param filePath The path to the file to be read.
     * @return A list of strings containing the lines of the file.
     * @throws IOException If there is an issue reading the file.
     */
    public static List<String> readLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(filePath))) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                lines.add(currentLine);
            }
        } catch (IOException e) {
            throw e;
        }

        return lines;
    }

    /**
     * Preprocesses a list of lines by removing comments and empty lines.
     *
     * @param lines The original list of lines from the file.
     * @return A new list of strings containing only valid, non-commented, and non-empty lines.
     */
    public static List<String> preProcessLines(List<String> lines) {
        List<String> newLines = new ArrayList<>();
        Pattern commentLinePattern = Pattern.compile("^//.*");

        for (String line : lines) {
            // Remove comments and empty lines:
            Matcher matcher = commentLinePattern.matcher(line);
            if (!line.trim().isEmpty() && !matcher.find()) {
                newLines.add(line);
            }
        }
        return newLines;
    }
}
