package ex5;

// TODO: think about the relevant package

import static ex5.util.Constants.AssignmentStatus;
import static ex5.util.Constants.VariableType;

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
}
