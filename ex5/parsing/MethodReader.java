package ex5.parsing;

import ex5.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static ex5.util.Constants.*;


public class MethodReader {

    public static Map<String, ArrayList<Constants.VariableType>> readMethods(List<String> lines)
            throws ParserException {
        Map<String, ArrayList<Constants.VariableType>> methods = new HashMap<>();
        Pattern declarePattern = Pattern.compile(METHOD_DECLARE_REGEX);
        Pattern closeBrecketPattern = Pattern.compile(CLOSE_BRACKET_REGEX);
        Pattern openBrecketPattern = Pattern.compile(OPEN_BRACKET_REGEX);

        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i).trim();

            Matcher declareMatcher = declarePattern.matcher(currentLine);

            if (declareMatcher.matches()) {
                MethodParser.addToMethodMap(currentLine, methods);

                // Checks that ends with "return;":
                int unclosedBrackets = 1;
                int currentLineIdx = i+1;

                while (currentLineIdx < lines.size()) {
                    String nextLine = lines.get(currentLineIdx).trim();

                    if (openBrecketPattern.matcher(nextLine).find()) {
                        unclosedBrackets++;
                        currentLineIdx++;
                        continue;
                    }
                    if (closeBrecketPattern.matcher(nextLine).find()) {
                        unclosedBrackets--;
                        if (unclosedBrackets != 0) {
                            currentLineIdx++;
                            continue;
                        }
                        break;
                    }
                    currentLineIdx++;
                }
                String lastLineInMethod = lines.get(currentLineIdx-1).trim();
                if (!lastLineInMethod.equals(RETURN_LINE)) {
                    throw new ParserException(INVALID_METHOD_ENDING_ERROR);
                }
                i = currentLineIdx;
            }
        }

        return methods;
    }
}
