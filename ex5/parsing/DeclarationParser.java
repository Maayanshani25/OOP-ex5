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

    public static final String INTEGER_VALUE = "[-+]?\\d+";
    public static final String DOUBLE_VALUE = "[-+]?(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)";
    public static final String STRING_VALUE = "\"[^\\" + "\\'\",]*\"";
    public static final String BOOLEAN_VALUE = "(true|false|[-+]?\\d+(\\.\\d+)?)";
    public static final String CHAR_VALUE = "'[^\\\"\\\\',]'";

    private static final String VALUE = "(" + INTEGER_VALUE + "|" + DOUBLE_VALUE + "|" + STRING_VALUE + "|" + BOOLEAN_VALUE + "|" + CHAR_VALUE + ")";
    private static final String SINGLE_DECLARATION = VARIABLE_NAME_REGEX + "(\\s*=\\s*" + VALUE + ")?";
    private static final String MULTIPLE_DECLARATIONS = SINGLE_DECLARATION + "(\\s*,\\s*" + SINGLE_DECLARATION + ")*";
    private static final String VARIABLE_DECLARATION = "^(int|double|String|boolean|char)\\s+" + MULTIPLE_DECLARATIONS + "\\s*;$";

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
            System.out.println("Line starts with keyword: " + keyword);

            // Perform specific actions based on the keyword
            switch (keyword) {
                case "final":
                    // Handle final keyword logic here
                    break;
                case "int":
                    parseInt();
                    break;
                case "double":
                    // Handle double keyword logic here
                    break;
                case "string":
                    // Handle string keyword logic here
                    break;
                case "char":
                    // Handle char keyword logic here
                    break;
                case "boolean":
                    // Handle boolean keyword logic here
                    break;
            }
        } else {
            throw new ParserException("Line does not start with a valid keyword.");
        }
    }


    // todo: change it to func that get the regex int/double/boolean/value and check if the line is valid
    //  instead of parseInt/parseDouble/parseBoolean/parseString/parseChar
    private void parseInt() throws ParserException, SymbolTableException {
        final String singleDeclaration = "(" + VARIABLE_NAME_REGEX + ")(\\s*=\\s*" + INTEGER_VALUE + ")?";
        final String multipleDeclarations = singleDeclaration + "(\\s*,\\s*" + singleDeclaration + ")*";
        final String correctLineRegex = "^(int)\\s+" + multipleDeclarations + "\\s*;$";


        // Compile the regex to match the full declaration line
        Pattern pattern = Pattern.compile(correctLineRegex);
        Matcher matcher = pattern.matcher(currentLine);

        if (matcher.matches()) { // Ensure the entire line matches the correct structure
            // Skip the "int" and its trailing space
            String declarationsPart = currentLine.substring(matcher.end(1)).trim();

            // Extract individual declarations from the remaining part
            Pattern singlePattern = Pattern.compile(singleDeclaration);
            Matcher singleMatcher = singlePattern.matcher(declarationsPart);

            while (singleMatcher.find()) {
                String variable = singleMatcher.group(1); // Capture the variable name
                if (variable == null || variable.isEmpty()) {
                    throw new ParserException("Missing variable name in declaration.");
                }
                // todo: for debuging
                System.out.println("variable name: " + variable);

                // Add the variable to the symbol table
                symbolTable.addVarToScope(variable, VariableType.INT);
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
