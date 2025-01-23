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
        final String multipleAssignments = singleAssignment + "(\\s*,\\s*" + singleAssignment + ")*";
        final String correctLineRegex = "^" + multipleAssignments + "\\s*;$";

        Pattern pattern = Pattern.compile(correctLineRegex);
        Matcher matcher = pattern.matcher(line.trim());
        if (matcher.matches()) {
            // Remove trailing semicolon
            String assignmentsPart = line.trim().substring(0, line.trim().length() - 1).trim();

            // Split assignments by commas
            String[] assignments = assignmentsPart.split(COMMA_SEPARATION_REGEX);

            for (String assignment : assignments) {
                assignment = assignment.trim();
                Pattern singlePattern = Pattern.compile(singleAssignment);
                Matcher singleMatcher = singlePattern.matcher(assignment);

                if (singleMatcher.matches()) {
                    String variableName = singleMatcher.group(1); // Extract variable name
                    String value = singleMatcher.group(3);       // Extract assigned value


                    // Check if the variable is declared
                    if (!symbolTable.isVariableDeclared(variableName)) {
                        throw new ParserException(String.format(VARIABLE_NOT_DECLARED_ERROR, variableName));
                    }

                    // Validate the variable type
                    VariableType variableType = symbolTable.getVarType(variableName);

                    // Handle if the value is another variable name
                    if (isValueVariableName(value)) {
                        validateVariableNameForType(value, variableType);
                    } else if (!isValueValidForType(variableType, value)) {
                        throw new ParserException(String.format(
                                INVALID_VALUE_ERROR, value, variableName, variableType));
                    }

                    // Mark the variable as assigned in the symbol table
                    symbolTable.assignVar(variableName, variableType);
                } else {
                    throw new ParserException(String.format(WRONG_ASSIGNMENT_FORMAT, assignment));
                }
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

        // Allow valid type conversions or exact matches
        boolean isValidConversion = (variableNameType == type) ||
                (type == VariableType.DOUBLE && variableNameType == VariableType.INT) ||
                (type == VariableType.BOOLEAN &&
                        (variableNameType == VariableType.DOUBLE || variableNameType == VariableType.INT));

        if (!isValidConversion) {
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
