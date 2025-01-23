package ex5.parsing;

import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ex5.util.Constants.*;

/**
 * todo: Local variables can only be assigned inside the
 *   method they were declared in, including any scopes nested in it (since they can only be
 *   accessed inside the scope they were declared in - see section 5.2.3). Global variables may be
 *   assigned multiple times both inside and outside a method
 */

/**
 * todo: maybe add another counter for the method number. to make sure we assign it correctly
 */
public class AssignmentParser implements Parser {

    private final SymbolTable symbolTable;
    private String currentLine;

    /**
     * Constructor for VariablesParser.
     *
     * @param symbolTable The SymbolTable instance used for managing variable scopes.
     */
    public AssignmentParser(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public void parse(String line) throws ParserException, SymbolTableException {
        final String singleAssignment = "(" + VARIABLE_NAME_REGEX + ")\\s*=\\s*("
                + INT_VALUE_REGEX + "|" + DOUBLE_VALUE_REGEX + "|" + STRING_VALUE_REGEX + "|"
                + BOOLEAN_VALUE_REGEX + "|" + CHAR_VALUE_REGEX + "|" + VARIABLE_NAME_REGEX + ")";
        final String multipleAssignment = singleAssignment + "(\\s*,\\s*" + singleAssignment + ")*";
        final String correctLineRegex = "^" + multipleAssignment + "\\s*;$";

        Pattern pattern = Pattern.compile(correctLineRegex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            // Loop through all assignments
            Pattern singlePattern = Pattern.compile(singleAssignment);
            Matcher singleMatcher = singlePattern.matcher(line.trim());

            while (singleMatcher.find()) {
                String variableName = singleMatcher.group(1); // Variable name
                String value = singleMatcher.group(2);       // Assigned value

                // Check if the variable is declared
                if (!symbolTable.isVariableDeclared(variableName)) {
                    throw new ParserException(String.format(VARIABLE_NOT_DECLARED_ERROR, variableName));
                }

                // Validate the variable type
                VariableType variableType = symbolTable.getVarType(variableName);

                // If value is a variable name, validate it
                if (isValueVariableName(value)) {
                    validateVariableNameForType(value, variableType);
                    continue; // Continue to the next assignment
                }

                // Validate the value itself
                if (!isValueValidForType(variableType, value)) {
                    throw new ParserException(String.format(
                            INVALID_VALUE_ERROR, value, variableName, variableType));
                }

                // Mark the variable as assigned in the symbol table
                symbolTable.assignVar(variableName, variableType);
            }
        } else {
            throw new ParserException(String.format(WRONG_ASSIGNMENT_FORMAT, line));
        }

    }

    private boolean isValueVariableName(String value) {
        return value.matches(VARIABLE_NAME_REGEX);
    }

    private void validateVariableNameForType(String variableName, VariableType type) throws ParserException {
        // If value is a variable, check if it's declared and assigned
        if (!symbolTable.isVariableDeclared(variableName)) {
            throw new ParserException(String.format(VARIABLE_NOT_DECLARED_ERROR, variableName));
        }
        // Ensure the source variable is assigned
        VariableType variableNameType = symbolTable.getVarType(variableName);
        if (variableNameType != type) {
            throw new ParserException(String.format(
                    TYPE_MISMATCH_ASSIGN_ERROR, variableName, type, variableNameType));

        }
    }

        private boolean isValueValidForType (VariableType variableType, String value){
            switch (variableType) {
                case INT:
                    return value.matches(INT_VALUE_REGEX);
                case DOUBLE:
                    return value.matches(DOUBLE_VALUE_REGEX);
                case STRING:
                    return value.matches(STRING_VALUE_REGEX);
                case BOOLEAN:
                    return value.matches(BOOLEAN_VALUE_REGEX);
                case CHAR:
                    return value.matches(CHAR_VALUE_REGEX);
                default:
                    return false;
        }
    }
}
