package ex5;

// TODO: think about the relevant package

import static ex5.util.Constants.*;

public class Variable {
    private final String name;
    private final VariableType type;
    private final Boolean isFinal;
    private AssignmentStatus status;

    public Variable(String name, VariableType type, AssignmentStatus status, Boolean isFinal) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
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

    public Boolean getIsFinal() {
        return this.isFinal;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }


}
