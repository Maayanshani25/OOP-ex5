package ex5.scope_managing;

import ex5.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ex5.util.Constants.*;

/**
 * A SymbolTable manages variables and their types across multiple nested scopes.
 * Each scope is represented as a map, with variable names as keys and their types as values.
 * Scopes are managed as a stack, where new scopes can be added and removed as needed.
 */
public class SymbolTable {
    private final List<Map<String, Variable>> table;

    /**
     * Constructs an empty SymbolTable with no scopes.
     * Use {@link #addScope()} to add an initial scope before adding variables.
     */
    public SymbolTable() {
        table = new ArrayList<>();
    }

    /**
     * Adds a new scope to the symbol table.
     */
    public void addScope() {
        table.add(new HashMap<>());
    }

    /**
     * Removes the most recent scope from the symbol table.
     *
     * @throws SymbolTableException if no scopes exist.
     */
    // todo: rotem use this func
    public void removeScope() throws SymbolTableException {
        if (!table.isEmpty()) {
            table.remove(table.size() - 1);
        } else {
            throw new SymbolTableException(SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }
    }

    /**
     * Adds a variable to the current scope.
     *
     * @param varName The name of the variable.
     * @param var     The Variable object to add.
     * @throws SymbolTableException If no scopes exist or if the variable already exists in the current scope.
     */
    // todo: rotem what is this func?
    public void addVarToScope(String varName, Variable var) throws SymbolTableException {
        if (table.isEmpty()) {
            throw new SymbolTableException(SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }

        Map<String, Variable> currentScope = table.get(table.size() - 1);
        if (currentScope.containsKey(varName)) {
            throw new SymbolTableException(String.format(ASSIGN_TO_EXIST_VARNAME_ERROR, varName));
        }
        currentScope.put(varName, var);
    }

    /**
     * Adds a variable to the current scope with specific attributes.
     * Variables with the status `CANT_BE_ASSIGNED` or invalid types cannot be added.
     *
     * @param varName The name of the variable.
     * @param type    The type of the variable.
     * @param status  The assignment status of the variable (`DECLARED`, `ASSIGNED`, etc.).
     * @param isFinal Whether the variable is declared as final.
     * @throws SymbolTableException If no scopes exist or if the variable already exists in the current scope.
     */
    public void addVarToScope(String varName,
                              VariableType type,
                              AssignmentStatus status,
                              Boolean isFinal)
            throws SymbolTableException {
        if (table.isEmpty()) {
            throw new SymbolTableException(SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }

        Map<String, Variable> currentScope = table.get(table.size() - 1);
        if (currentScope.containsKey(varName)) {
            throw new SymbolTableException(String.format(ASSIGN_TO_EXIST_VARNAME_ERROR, varName));
        }

        Variable var = new Variable(varName, type, status, isFinal);
        currentScope.put(varName, var);
    }

    /**
     * Assigns a value to a variable in the nearest scope where it is declared.
     * Ensures:
     * - Final variables cannot be reassigned.
     * - Variables with the status `CANT_BE_ASSIGNED` cannot be assigned.
     * - The type of the value being assigned matches the variable's type.
     *
     * @param varName      The name of the variable.
     * @param variableType The type of the value being assigned.
     * @throws SymbolTableException If the variable is not declared, is final, has an invalid status, or the type does not match.
     */
    public void assignVar(String varName, VariableType variableType) throws SymbolTableException {
        // todo:  check if it's inside the correct method.
        //     HOW?

        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(varName)) {
                Variable curVar = table.get(i).get(varName);

                // Check if the variable is final
                if (curVar.isFinal()) {
                    throw new SymbolTableException(
                            String.format(FINAL_VARIABLE_ASSIGN_ERROR, varName)
                    );
                }

                if (curVar.getStatus() == AssignmentStatus.CANT_BE_ASSIGNED) {
                    throw new SymbolTableException(
                            String.format(CANNOT_BE_ASSIGNED_ERROR, varName)
                    );
                }

                // Check if the type matches
                if (curVar.getType() != variableType) {
                    throw new SymbolTableException(
                            String.format(TYPE_MISMATCH_ASSIGN_ERROR, varName, curVar.getType(), variableType)
                    );
                }
                curVar.setStatus(AssignmentStatus.ASSIGNED);
                table.get(i).put(varName, curVar);
                return;
            }
        }
        // If not found, throw error
        throw new SymbolTableException(String.format(VARIABLE_NOT_DECLARED_ERROR, varName));
    }

    /**
     * Checks if a variable is declared in any scope.
     *
     * @param varName The variable name.
     * @return true if the variable exists in any scope, false otherwise.
     */
    public boolean isVariableDeclared(String varName) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(varName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a variable has been assigned a value in the nearest scope where it is declared.
     *
     * @param varName The name of the variable.
     * @return true if the variable is assigned, false otherwise.
     */
    public boolean isVariableAssigned(String varName) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(varName)) {
                return (table.get(i).get(varName).getStatus() == AssignmentStatus.ASSIGNED);
            }
        }
        return false;
    }

    /**
     * Updates the status of all variables in the current scope.
     * - Variables with the status `DECLARED` are changed to `DECLARED_LAST_ROW`.
     * - Variables with the status `DECLARED_LAST_ROW` are changed to `CANT_BE_ASSIGNED`.
     * This method ensures that variables cannot be reassigned after their allowed assignment window.
     */
    public void updateVarsStatus() {
        for (Variable var : table.get(table.size() - 1).values()) {
            if (var.getStatus() == AssignmentStatus.DECLARED) {
                var.setStatus(AssignmentStatus.DECLARED_LAST_ROW);
            }
            if (var.getStatus() == AssignmentStatus.DECLARED_LAST_ROW) {
                var.setStatus(AssignmentStatus.CANT_BE_ASSIGNED);
            }
        }
    }

    /**
     * Retrieves the type of variable from the nearest scope.
     *
     * @param varName The variable name.
     * @return The variable type if found, null otherwise.
     */
    public VariableType getVarType(String varName) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(varName)) {
                return table.get(i).get(varName).getType();
            }
        }
        return null; // Variable not found
    }
}