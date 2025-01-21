package ex5;

// TODO: think about the relevant package

import ex5.util.Constants;

public class Variable {
    private String name;
    private Constants.VariableType type;
    private Constants.AssignmentStatus status;

    public Variable(String name, Constants.VariableType type, Constants.AssignmentStatus status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }

    // TODO: add gets and sets
}
