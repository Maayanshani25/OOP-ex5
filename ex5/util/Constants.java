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

    /** <h2> String constants </h2>*/
    // Messeges:
    public static final String
            ILLEGAL_CODE_MESSAGE = "",
            IO_ERROR_MESSAGE = "",

            SYMBOL_TABLE_EXCEPTION_GENERAL_ERROR_MESSAGE = "SymbolTable Exception: ",
            SYMBOL_TABLE_VAR_ERROR_MESSAGE = "invalid var.",
            SYMBOL_TABLE_SCOPE_ERROR_MESSAGE = "invalid Scope.",

            SCOPE_MANAGER_EXCEPTION_GENERAL_ERROR_MESSAGE = "ScopeManager Exception: ",
            INVALID_EXIT_SCOPE_MESSAGE = "invalid exit of scope.";


    // saved words:
    public static final String
            METHOD = "Method",
            IF = "if",
            WHILE = "while",
            INT = "int",
            DOUBLE = "double",
            STRING = "String",
            BOOLEAN = "boolean",
            CHAR = "char";

}
