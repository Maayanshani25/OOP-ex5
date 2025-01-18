package ex5.parsing;

import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;
import ex5.util.Constants;

import static ex5.util.Constants.DOUBLE;
import static ex5.util.Constants.INT;
import static ex5.util.Constants.STRING;
import static ex5.util.Constants.BOOLEAN;
import static ex5.util.Constants.CHAR;

public class VariablesParser implements Parser {
    // Regex components

    private static final String VARIABLE_NAME = "([a-zA-Z]|_[a-zA-Z0-9])[a-zA-Z0-9_]*";

    private static final String INTEGER_VALUE = "[-+]?\\d+";
    private static final String DOUBLE_VALUE = "[-+]?(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)";
    private static final String STRING_VALUE = "\"[^\\" + "\\'\",]*\"";
    private static final String BOOLEAN_VALUE = "(true|false|[-+]?\\d+(\\.\\d+)?)";
    private static final String CHAR_VALUE = "'[^\\\"\\\\',]'";

    private static final String VALUE = "(" + INTEGER_VALUE + "|" + DOUBLE_VALUE + "|" + STRING_VALUE + "|" + BOOLEAN_VALUE + "|" + CHAR_VALUE + ")";
    private static final String SINGLE_DECLARATION = VARIABLE_NAME + "(\\s*=\\s*" + VALUE + ")?";
    private static final String MULTIPLE_DECLARATIONS = SINGLE_DECLARATION + "(\\s*,\\s*" + SINGLE_DECLARATION + ")*";
    private static final String VARIABLE_DECLARATION = "^(int|double|String|boolean|char)\\s+" + MULTIPLE_DECLARATIONS + "\\s*;$";

    private final SymbolTable symbolTable;
    private String currentLine;

    /**
     * Constructor for VariablesParser.
     * @param symbolTable The SymbolTable instance used for managing variable scopes.
     */
    public VariablesParser(SymbolTable symbolTable, String currentLine) {
        this.symbolTable = symbolTable;
    }

    /**
     * Parses a line of variable declarations and adds valid variables to the symbol table.
     * @param line The line of code to parse.
     * @throws ParserException If the syntax or semantics of the line are invalid.
     */
    public void parse(String line) throws ParserException, SymbolTableException {
        currentLine = line;
        String[] parts = line.trim().split("\\s+"); // Split into type and the rest
        String type = parts[0];

        parseTypes(parts[0]);

    }

    private void parseTypes(String type) throws ParserException {
        switch (type) {
            case INT:
                parseInt();
                break;
            case DOUBLE:
                parseDouble();
                break;
            case STRING:
                parseString();
                break;
            case BOOLEAN:
                parseBoolean();
                break;
            case CHAR:
                parseChar();
                break;
            default:
                throw new ParserException("Unsupported type: " + type);
        }
    }


    private void parseInt() {

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
