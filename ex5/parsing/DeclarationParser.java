package ex5.parsing;

import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ex5.util.Constants.*;


// todo: we dont care about the value of the variables. just want them to be legal
public class DeclarationParser implements Parser {
    // Regex components


    private static final String VARIABLE_NAME_REGEX = "([a-zA-Z]|_[a-zA-Z0-9])[a-zA-Z0-9_]*";

    // TODO MAAYAN: should be private?
    public static final String INT_VALUE_REGEX = "[-+]?\\d+";
    public static final String DOUBLE_VALUE_REGEX = "[-+]?(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)";
    public static final String STRING_VALUE_REGEX = "\"[^\\" + "\\'\",]*\"";
    public static final String BOOLEAN_VALUE_REGEX = "(true|false|[-+]?\\d+(\\.\\d+)?)";
    public static final String CHAR_VALUE_REGEX = "'[^\\\"\\\\',]'";
    public static final String COMMA_SEPARATION_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    private final SymbolTable symbolTable;
    private String currentLine;


    /**
     * Constructor for VariablesParser.
     * @param symbolTable The SymbolTable instance used for managing variable scopes.
     */
    public DeclarationParser(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * Parses a line of variable declarations and adds valid variables to the symbol table.
     * @param line The line of code to parse.
     * @throws ParserException If the syntax or semantics of the line are invalid.
     */

    @Override
    public void parse(String line) throws ParserException, SymbolTableException{
        currentLine = line.trim(); // Trim leading and trailing whitespace

        // Compile the regex
        Pattern pattern = Pattern.compile(TYPE_REGEX);
        Matcher matcher = pattern.matcher(currentLine);

        // Check if the line starts with one of the keywords
        if (matcher.find()) {
            String keyword = matcher.group(1); // Get the matching keyword

            // Perform specific actions based on the keyword
            switch (keyword) {
                case FINAL:
                    // Handle final keyword logic here
                    // add enformcemnt that the final variable is assigned
                    break;
                case INT:
                    parseType(INT_VALUE_REGEX, VariableType.INT, INT);
                    break;
                case DOUBLE:
                    parseType(DOUBLE_VALUE_REGEX, VariableType.DOUBLE, DOUBLE);
                    break;
                case STRING:
                    parseType(STRING_VALUE_REGEX, VariableType.STRING, STRING);
                    break;
                case CHAR:
                    parseType(CHAR_VALUE_REGEX, VariableType.CHAR, CHAR);
                    break;
                case BOOLEAN:
                    parseType(BOOLEAN_VALUE_REGEX, VariableType.BOOLEAN, BOOLEAN);
                    break;
            }
        } else {
            throw new ParserException("Line does not start with a valid keyword.");
        }
    }

    private void parseType(String typeValueRegex, VariableType variableType, String type)
            throws ParserException, SymbolTableException {
        // Match a single variable name, optionally followed by an assignment
        final String singleDeclaration = "(" + VARIABLE_NAME_REGEX + ")(\\s*=\\s*(" + typeValueRegex + "))?";
        // Allow multiple declarations separated by commas
        final String multipleDeclarations = singleDeclaration + "(\\s*,\\s*" + singleDeclaration + ")*";
        // Match the complete line including type and semicolon
        final String correctLineRegex = "^" + type + "\\s+" + multipleDeclarations + "\\s*;$";

        Pattern pattern = Pattern.compile(correctLineRegex);
        Matcher matcher = pattern.matcher(currentLine);

        // todo: for debugging
//        System.out.println("currentLine: " + currentLine);
        if (matcher.matches()) {
            // Remove the type keyword and trailing semicolon
            String declarationsPart = currentLine.substring(type.length()).trim();
            declarationsPart = declarationsPart.substring(0, declarationsPart.length() - 1).trim();

            // Split by comma, but not within value expressions
            String[] declarations = declarationsPart.split(COMMA_SEPARATION_REGEX);

            for (String declaration : declarations) {
                declaration = declaration.trim();
                Pattern singlePattern = Pattern.compile("(" + VARIABLE_NAME_REGEX + ")(\\s*=\\s*(" + typeValueRegex + "))?");
                Matcher singleMatcher = singlePattern.matcher(declaration);

                if (singleMatcher.matches()) {
                    String variableName = singleMatcher.group(1);
                    if (variableName == null || variableName.isEmpty()) {
                        throw new ParserException("Invalid variable declaration: variable name is missing.");
                    }

                    String value = singleMatcher.group(3); // Group 3 contains just the value, not the equals sign
                    boolean hasValue = value != null && !value.trim().isEmpty();
                    AssignmentStatus assignmentStatus = hasValue ? AssignmentStatus.ASSIGNED : AssignmentStatus.DECLARED;

                    // todo: for debugging
//                    System.out.println("Variable: " + variableName +
//                            ", Value: " + (hasValue ? value : "none") +
//                            ", Has value: " + hasValue);

                    // Add the variable to the symbol table
                     symbolTable.addVarToScope(variableName, variableType, assignmentStatus, false);
                } else {
                    throw new ParserException("Invalid variable declaration format: " + declaration);
                }
            }
        } else {
            throw new ParserException("Invalid variable declaration line: " + currentLine);
        }
    }


    private void parseDouble() {

    }

    private void parseString() {

    }

    private void parseBoolean() {

    }

    private void parseChar() {

    }


}
