package ex5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileReader {

    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(filePath))) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                lines.add(currentLine);
            }
        // TODO: make sure needed to be cached (they catch it in TA11)
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static List<String> preProcessLines(List<String> lines) {
        List<String> newLines = new ArrayList<>();
        Pattern commentLinePattern = Pattern.compile("^//.*");

        for (String line : lines) {
            // Remove comments and empty lines:
            Matcher matcher = commentLinePattern.matcher(line);
            if (!line.isEmpty() && !matcher.find()) {
                newLines.add(line);
            }
        }
        return newLines;
    }

}
