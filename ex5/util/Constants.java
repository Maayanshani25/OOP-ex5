package ex5.util;

/**
 * A utility class to store commonly used constants throughout the project.
 *
 * <h2>How to Use</h2>
 * <p>You can access the constants using the class name directly, as they are
 * declared as <code>public static final</code>. Example usage:</p>
 *
 * <pre>
 * // Access a string constant
 * String example = Constants.EXAMPLE;
 *
 * // Use a numeric constant
 * int doubledValue = Constants.NUM_EXAMPLE * 2;
 *
 * // Use a boolean constant
 * if (Constants.DEBUG_MODE) {
 *     System.out.println("Debug mode is enabled.");
 * }
 * </pre>
 */


public class Constants {
    /** <h2> Enums </h2> */
    public enum ScopeKind {
        METHOD, IF, WHILE
    }

    public enum VariableType {
        INT, DOUBLE, STRING, BOOLEAN, CHAR
    }

    public enum AssignmentStatus {
        ASSIGNED, DECLARED, DECLARED_LAST_ROW
    }

    /** <h2> String constants </h2>*/
    // Messeges:
    public static final String
            ILLEGAL_CODE_MESSAGE = "",
            IO_ERROR_MESSAGE = "",

            SYMBOL_TABLE_EXCEPTION_GENERAL_ERROR_MESSAGE = "SymbolTable Error: ",
            SYMBOL_TABLE_VAR_ERROR_MESSAGE = "invalid var.",
            SYMBOL_TABLE_SCOPE_ERROR_MESSAGE = "invalid Scope.",
            SYMBOL_TABLE_ASSIGN_ERROR_MESSAGE = "invalid assign.",

            SCOPE_MANAGER_EXCEPTION_GENERAL_ERROR_MESSAGE = "ScopeManager Error: ",
            INVALID_EXIT_SCOPE_MESSAGE = "invalid exit of scope.",
            OUT_OF_METHOD_SCOPE_ERROR_MESSAGE = "loop or condition out of method scope.",

            PARSER_EXCEPTION_GENERAL_ERROR_MESSAGE = "Parser Error: ",
            WRONG_DECLARATION_FORMAT = "Line does not start with a valid keyword.",
            INVALID_VARIABLE_DECLARATION = "Invalid variable declaration format: ",
            PARSER_WRONG_LINE_FORMAT = "Invalid variable declaration: ",
            INVALID_TYPE_ERROR = "Invalid type keyword: ",
            FINAL_VARIABLE_ASSIGNMENT_ERROR = "Final variable '%s' must have an assigned value.",
            LOOP_OR_CONDITION_PARSER_EXCEPTION_MESSAGE = "Loop or condition syntax error.";

    /** REGEX patterns */
    public static final String VARIABLE_NAME_REGEX = "([a-zA-Z]|_[a-zA-Z0-9])[a-zA-Z0-9_]*";
    public static final String TYPE_REGEX = "^(final\\s+)?(int|double|String|char|boolean)\\b";
    public static final String INT_VALUE_REGEX = "[-+]?\\d+";
    public static final String DOUBLE_VALUE_REGEX = "[-+]?(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)";
    public static final String STRING_VALUE_REGEX = "\"[^\\" + "\\'\",]*\"";
    public static final String BOOLEAN_VALUE_REGEX = "(true|false|[-+]?\\d+(\\.\\d+)?)";
    public static final String CHAR_VALUE_REGEX = "'[^\\\"\\\\',]'";
    public static final String COMMA_SEPARATION_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String FINAL_START_REGEX = "^(final\\s+)?";



    // saved words:
    public static final String
            METHOD = "Method",
            IF = "if",
            WHILE = "while",
            INT = "int",
            DOUBLE = "double",
            STRING = "String",
            BOOLEAN = "boolean",
            TRUE = "true",
            FALSE = "false",
            CHAR = "char",
            FINAL = "final";

}
