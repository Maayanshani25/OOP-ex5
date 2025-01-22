package ex5.parsing;

import ex5.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodReader {
    public static final String METHOD_NAME_REGEX = "[a-zA-Z][a-zA-Z0-9_]*";
    public static final String METHOD_DECLARE_REGEX =
            "void\\s+(" + METHOD_NAME_REGEX + ")\\s*\\(([^)]*)\\)\\s*\\{";


    public static Map<String, ArrayList<Constants.VariableType>> readMethods(List<String> lines)
            throws ParserException {
        Map<String, ArrayList<Constants.VariableType>> methods = new HashMap<>();
        Pattern declarePattern = Pattern.compile(METHOD_DECLARE_REGEX);

        for (String line: lines) {
            String currentLine = line.trim();

            Matcher declareMatcher = declarePattern.matcher(currentLine);

            if (declareMatcher.matches()) {
                MethodParser.addToMethodMap(currentLine, methods);
            }
        }

        return methods;
    }
}
