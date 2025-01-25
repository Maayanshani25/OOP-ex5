package ex5;

// TODO: think about the relevant package

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ex5.util.Constants.*;

/**
 * The Variable class represents a variable with its name, type, assignment status, and immutability.
 * It encapsulates the details of a variable and provides methods to access and modify its properties.
 */
public class Variable {
    private final String name; // The name of the variable
    private final VariableType type; // The type of the variable (e.g., INT, DOUBLE, STRING)
    private final Boolean isFinal; // Whether the variable is declared as final (immutable)
    private AssignmentStatus status; // The assignment status of the variable (e.g., DECLARED, ASSIGNED)

    /**
     * Constructs a new Variable instance.
     *
     * @param name   The name of the variable.
     * @param type   The type of the variable (e.g., INT, DOUBLE, STRING).
     * @param status The initial assignment status of the variable (e.g., DECLARED, ASSIGNED).
     * @param isFinal Whether the variable is declared as final (immutable).
     */
    public Variable(String name, VariableType type, AssignmentStatus status, Boolean isFinal) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.status = status;
    }

    /**
     * Gets the name of the variable.
     *
     * @return The variable name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the variable.
     *
     * @return The variable type.
     */
    public VariableType getType() {
        return type;
    }

    /**
     * Gets the current assignment status of the variable.
     *
     * @return The assignment status of the variable.
     */
    public AssignmentStatus getStatus() {
        return status;
    }

    /**
     * Checks if the variable is declared as final (immutable).
     *
     * @return True if the variable is final, false otherwise.
     */
    public Boolean isFinal() {
        return this.isFinal;
    }

    /**
     * Sets the assignment status of the variable.
     *
     * @param status The new assignment status to set.
     */
    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    /**
     * Determines the type of a constant parameter.
     *
     * @param parameter The constant parameter string.
     * @return The VariableType of the constant, or null if invalid.
     */
    public static VariableType ConstantParameter(String parameter) {
        parameter = parameter.trim();
        Pattern intPattern = Pattern.compile(INT_VALUE_REGEX);
        Matcher intMatcher = intPattern.matcher(parameter);
        Pattern doublePattern = Pattern.compile(DOUBLE_VALUE_REGEX);
        Matcher doubleMatcher = doublePattern.matcher(parameter);
        Pattern boolPattern = Pattern.compile(BOOLEAN_CONSTANT_REGEX);
        Matcher boolMatcher = boolPattern.matcher(parameter);
        Pattern charPattern = Pattern.compile(CHAR_VALUE_REGEX);
        Matcher charMatcher = charPattern.matcher(parameter);
        Pattern stringPattern = Pattern.compile(STRING_VALUE_REGEX);
        Matcher stringMatcher = stringPattern.matcher(parameter);

        if (intMatcher.matches()) {
            return VariableType.INT;
        } else if (doubleMatcher.matches()) {
            return VariableType.DOUBLE;
        } else if (boolMatcher.matches()) {
            return VariableType.BOOLEAN;
        } else if (charMatcher.matches()) {
            return VariableType.CHAR;
        } else if (stringMatcher.matches()) {
            return VariableType.STRING;
        } else {
            return null;
        }
    }

    /**
     * Determines if a variable type is compatible with an expected type.
     *
     * @param actual   The actual type of the variable.
     * @param expected The expected type for the variable.
     * @return true if the types are compatible, false otherwise.
     */
    public static boolean isTypeCompatible(VariableType actual, VariableType expected) {
        if (actual == expected) return true;
        if (expected == VariableType.DOUBLE && actual == VariableType.INT) return true;
        if (expected == VariableType.BOOLEAN && (actual == VariableType.INT || actual == VariableType.DOUBLE))
            return true;
        return false;
    }

}
