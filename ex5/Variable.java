package ex5;

// TODO: think about the relevant package

import static ex5.util.Constants.*;

public class Variable {
    private final String name;
    private final VariableType type;
    private AssignmentStatus status;

    public Variable(String name, VariableType type, AssignmentStatus status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }
}
