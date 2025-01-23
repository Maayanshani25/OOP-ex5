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

            // SymbolTable error messages:
            SYMBOL_TABLE_EXCEPTION_GENERAL_ERROR_MESSAGE = "SymbolTable Error: ",
            SYMBOL_TABLE_VAR_ERROR_MESSAGE = "invalid var.",
            SYMBOL_TABLE_SCOPE_ERROR_MESSAGE = "invalid Scope.",
            FINAL_VARIABLE_ASSIGN_ERROR = "Cannot assign a value to final variable '%s'.",
            TYPE_MISMATCH_ASSIGN_ERROR = "Type mismatch: Variable '%s' is of type '%s', cannot assign a " +
                    "value of type '%s'.",

            // Scope error messages:
            SCOPE_MANAGER_EXCEPTION_GENERAL_ERROR_MESSAGE = "ScopeManager Error: ",
            INVALID_EXIT_SCOPE_MESSAGE = "invalid exit of scope.",

            // Parser error messages:
            PARSER_EXCEPTION_GENERAL_ERROR_MESSAGE = "Parser Error: ",
            INVALID_ENDLINE_ERROR_MESSAGE = "Line must end with '{', '}', or ';'. The line: ",

            // If and while parser error messages:
            OUT_OF_METHOD_SCOPE_ERROR_MESSAGE = "loop or condition out of method scope.",
            LOOP_OR_CONDITION_PARSER_EXCEPTION_MESSAGE = "Loop or condition syntax error.",

            // return error messages:
            INVALID_RETURN_STATEMENT_SYNTAX = "Invalid return statement syntax.",

            // Declaration parser error messages:
            WRONG_DECLARATION_FORMAT = "Line does not start with a valid keyword.",
            INVALID_VARIABLE_DECLARATION = "Invalid variable declaration format: ",
            PARSER_WRONG_LINE_FORMAT = "Invalid variable declaration: ",
            INVALID_TYPE_ERROR = "Invalid type keyword: ",
            FINAL_VARIABLE_ASSIGNMENT_ERROR = "Final variable '%s' must have an assigned value.",

            // Assignment parser error messages
            WRONG_ASSIGNMENT_FORMAT = "Invalid assignment line: ",
            VARIABLE_NOT_DECLARED_ERROR = "Variable '%s' is not declared.",
            INVALID_VALUE_ERROR = "Invalid value '%s' for variable '%s' with type '%s'.",

            // Method parser and Method reader error messages:
            METHOD_GENERAL_SYNTAX_ERROR = "Method syntax is invalid.",
            METHOD_DECLARE_SYNTAX_ERROR = "Method declaration syntax is invalid.",
            INVALID_METHOD_ENDING_ERROR = "Method ends without return;.",
            INVALID_METHOD_NAME_ERROR = "Method declaration syntax is invalid.",
            METHOD_DECLARE_IN_METHOD_SCOPE_ERROR = "Method can not be declare in another method scope.",
            METHOD_NAME_ALREADY_EXIST_ERROR = "Two methods with the same name are illegal",
            METHOD_PARAMETERS_ALREADY_EXIST_ERROR = "Method's parameters name already exist.",
            METHOD_INVALID_PARAMETERS_ERROR = "Method's parameters are invalid.",
            METHOD_CALL_SYNTAX_ERROR = "Method call syntax is invalid.",
            METHOD_CALL_OUT_OF_METHOD_SCOPE = "Method must be call in another method scope.",
            METHOD_NAME_DOESNT_EXIST = "Method name does not exists.";



    /** REGEX patterns */
    public static final String LINE_END_REGEX = ".*[{};]$";
    public static final String VARIABLE_NAME_REGEX =
            "(?!true$)(?!false$)([a-zA-Z]|_[a-zA-Z0-9])[a-zA-Z0-9_]*";
    public static final String TYPE_REGEX = "^(final\\s+)?(int|double|String|char|boolean)\\b";
    public static final String INT_VALUE_REGEX = "[-+]?\\d+";
    public static final String DOUBLE_VALUE_REGEX = "[-+]?(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)";
    public static final String STRING_VALUE_REGEX = "\"[^\\" + "\\'\",]*\"";
    public static final String BOOLEAN_VALUE_REGEX = "(true|false|[-+]?\\d+(\\.\\d+)?)";
    public static final String BOOLEAN_CONSTANT_REGEX = "(true|false)";
    public static final String CHAR_VALUE_REGEX = "'[^\\\"\\\\',]'";
    public static final String COMMA_SEPARATION_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String FINAL_START_REGEX = "^(final\\s+)?";
    public static final String CLOSE_BRACKET_REGEX = "\\}";
    public static final String OPEN_BRACKET_REGEX = "\\{";
    public static final String RETURN_LINE = "return;";
    public static final String METHOD_NAME_REGEX = "[a-zA-Z][a-zA-Z0-9_]*";
    public static final String METHOD_DECLARE_REGEX =
            "void\\s+(" + METHOD_NAME_REGEX + ")\\s*\\(([^)]*)\\)\\s*\\{";
    public static final String METHOD_CALL_REGEX = "(" + METHOD_NAME_REGEX + ")\\s*\\(([^)]*)\\)\\s*;";



    // saved words:
    public static final String
            METHOD = "Method",
            IF = "if",
            WHILE = "while",
            VOID = "void",
            INT = "int",
            DOUBLE = "double",
            STRING = "String",
            BOOLEAN = "boolean",
            TRUE = "true",
            FALSE = "false",
            CHAR = "char",
            RETURN = "return",
            FINAL = "final",
            END_OF_SCOPE = "}";

}
