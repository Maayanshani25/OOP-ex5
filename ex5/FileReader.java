package ex5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileReader {

    public static List<String> readLines(String filePath) throws IOException{
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
