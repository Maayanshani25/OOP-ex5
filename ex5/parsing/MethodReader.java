package ex5.parsing;

import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import ex5.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ex5.util.Constants.*;

/**
 * The MethodReader class is responsible for reading and validating method declarations from a list of code lines.
 * It ensures that:
 * - Methods are correctly declared.
 * - Methods end with a valid "return;" statement.
 * - Brackets in method definitions are balanced.
 */
public class MethodReader {

    /**
     * Reads and parses method declarations from a list of code lines.
     *
     * @param lines A list of code lines to analyze for method declarations.
     * @param symbolTable The SymbolTable instance used for managing variable scopes.
     * @return A map where the keys are method names and the values are lists of parameter types for each method.
     * @throws ParserException If a method declaration is invalid or ends improperly.
     */
    public static Map<String, ArrayList<Constants.VariableType>> readMethods(
            List<String> lines, SymbolTable symbolTable) throws ParserException, SymbolTableException {
        Map<String, ArrayList<Constants.VariableType>> methods = new HashMap<>();
        Pattern declarePattern = Pattern.compile(METHOD_DECLARE_REGEX);
        Pattern closeBracketPattern = Pattern.compile(CLOSE_BRACKET_REGEX);
        Pattern openBracketPattern = Pattern.compile(OPEN_BRACKET_REGEX);
        Pattern returnPattern = Pattern.compile(RETURN_LINE);

        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i).trim();

            Matcher declareMatcher = declarePattern.matcher(currentLine);

            if (declareMatcher.matches()) {
                // Add the method declaration to the methods map
                MethodParser.addToMethodMap(currentLine, methods);

                // Check that the method ends with "return;" and has balanced brackets
                int unclosedBrackets = 1; // Starts with the first opening bracket
                int currentLineIdx = i + 1;

                while (currentLineIdx < lines.size()) {
                    String nextLine = lines.get(currentLineIdx).trim();

                    // Count opening brackets
                    if (openBracketPattern.matcher(nextLine).find()) {
                        unclosedBrackets++;
                        currentLineIdx++;
                        continue;
                    }

                    // Count closing brackets
                    if (closeBracketPattern.matcher(nextLine).find()) {
                        unclosedBrackets--;
                        if (unclosedBrackets != 0) {
                            currentLineIdx++;
                            continue;
                        }
                        break; // All brackets are balanced, exit the loop
                    }

                    currentLineIdx++;
                }

                // Verify that the last line inside the method is "return;"
                String lastLineInMethod = lines.get(currentLineIdx - 1).trim();
                Matcher returnMatcher = returnPattern.matcher(lastLineInMethod);
                if (!returnMatcher.matches()) {
                    throw new ParserException(INVALID_METHOD_ENDING_ERROR);
                }

                // Move the index to the end of the method
                i = currentLineIdx;
            }

            // while out of declaration:
            if (i >= lines.size()) {
                continue;
            }
            AssignmentParser assignmentsParser = new AssignmentParser(symbolTable);
            DeclarationParser declarationParser = new DeclarationParser(symbolTable);
            currentLine = lines.get(i).trim();

            // Check for valid declaration
            Pattern keywordPattern = Pattern.compile(KEYWORDS_REGEX);
            Matcher keywordMatcher = keywordPattern.matcher(currentLine);

            if (keywordMatcher.find()) {
                String keyword = keywordMatcher.group();

                // Handle variable declaration syntax
                if (keyword.matches(DECLARATION_REGEX)) {
                    declarationParser.parse(currentLine);
                    continue;
                }

                // Handle assignment syntax
                if (keyword.matches(VARIABLE_NAME_REGEX)) {
                    assignmentsParser.parse(currentLine);
                    continue;
                }
            }
        }
        return methods;
    }
}

