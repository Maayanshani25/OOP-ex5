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

        // Define the regex to check the line's prefix

        // Compile the regex
        Pattern pattern = Pattern.compile(TYPE_REGEX);
        Matcher matcher = pattern.matcher(currentLine);

        // Check if the line starts with one of the keywords
        if (matcher.find()) {
            String keyword = matcher.group(1); // Get the matching keyword

            // Perform specific actions based on the keyword
            switch (keyword) {
                case "final":
                    // Handle final keyword logic here
                    break;
                case INT:
//                    parseInt();
                    parseType(INT_VALUE_REGEX, VariableType.INT, INT);
                    break;
                case DOUBLE:
                    // Handle double keyword logic here
                    parseType(DOUBLE_VALUE_REGEX, VariableType.DOUBLE, DOUBLE);
                    break;
                case STRING:
                    parseType(STRING_VALUE_REGEX, VariableType.STRING, STRING);
                    break;
                case CHAR:
                    // Handle char keyword logic here
                    break;
                case BOOLEAN:
                    // Handle boolean keyword logic here
                    break;
            }
        } else {
            throw new ParserException("Line does not start with a valid keyword.");
        }
    }

    private void parseType(String typeValueRegex, VariableType variableType, String type)
            throws ParserException, SymbolTableException {
        final String singleDeclaration = "(" + VARIABLE_NAME_REGEX + ")(\\s*=\\s*" + typeValueRegex + ")?";
        final String multipleDeclarations = singleDeclaration + "(\\s*,\\s*" + singleDeclaration + ")*";
        final String correctLineRegex = "^" + type + "\\s+" + multipleDeclarations + "\\s*;$";


        // Compile the regex to match the full declaration line
        Pattern pattern = Pattern.compile(correctLineRegex);
        Matcher matcher = pattern.matcher(currentLine);

        if (matcher.matches()) { // Ensure the entire line matches the correct structure
            // Skip the "type" and its trailing space
            String declarationsPart = currentLine.substring(matcher.end(1)).trim();

            // Extract individual declarations from the remaining part
            Pattern singlePattern = Pattern.compile(singleDeclaration);
            Matcher singleMatcher = singlePattern.matcher(declarationsPart);

            while (singleMatcher.find()) {
                String variable = singleMatcher.group(1); // Capture the variable name
                if (variable == null || variable.isEmpty()) {
                    throw new ParserException(PARSER_EXCEPTION_MESSAGE);
                }
//                // todo: for debugging
//                System.out.println("variable name: " + variable);

                // Add the variable to the symbol table
                symbolTable.addVarToScope(variable, variableType);
            }
        } else {
            throw new ParserException("Invalid variable declaration: " + currentLine);
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
