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
    public void addScope() throws SymbolTableException {
        table.add(new HashMap<>());
        for (Variable var: table.getLast().values()) {
            addVarToScope(var.getName(), var.getType(), var.getStatus(), var.isFinal());
        }
    }

    /**
     * Removes the most recent scope from the symbol table.
     *
     * @throws SymbolTableException if no scopes exist.
     */
    public void removeScope() throws SymbolTableException {
        if (!table.isEmpty()) {
            updateVarsStatus();
            table.removeLast();
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
    public void addVarToScope(String varName, Variable var) throws SymbolTableException {
        if (table.isEmpty()) {
            throw new SymbolTableException(SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }

        Map<String, Variable> currentScope = table.getLast();
        if (currentScope.containsKey(varName)) {
            throw new SymbolTableException(String.format(ASSIGN_TO_EXIST_VARNAME_ERROR, varName));
        }
        currentScope.put(varName, var);
    }

    /**
     * Adds a variable to the current scope with specific attributes.
     * <p>
     * For variables in non-global scopes:
     * - Ensures the variable is not already declared in the current scope.
     * <p>
     * For variables in the global scope:
     * - If the variable has already been declared as `GLOBAL_DECLARED` or `GLOBAL_ASSIGNED`,
     * it updates the status based on the provided `status`.
     * - If the variable has not been declared, it adds the variable with the provided attributes.
     * <p>
     * Rules:
     * - Variables cannot be redeclared in the same scope unless updating their status is explicitly
     * allowed (e.g., global variables).
     *
     * @param varName The name of the variable to add.
     * @param type    The type of the variable (e.g., `int`, `double`, etc.).
     * @param status  The assignment status of the variable (`DECLARED`, `ASSIGNED`, `GLOBAL_DECLARED`, etc.).
     * @param isFinal Whether the variable is declared as final (immutable).
     * @throws SymbolTableException If:
     *                              - No scopes exist.
     *                              - A variable with the same name already exists in the current scope
     *                              and cannot be redeclared or updated.
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
        // if not in global scope:
        if (table.size() > 1) {
            // if already exits:
            if (currentScope.containsKey(varName)) {
                throw new SymbolTableException(String.format(ASSIGN_TO_EXIST_VARNAME_ERROR, varName));
            }

            Variable var = new Variable(varName, type, status, isFinal);
            currentScope.put(varName, var);
        }

        // if in global scope:
        else {
            // if already been declared
            if (currentScope.containsKey(varName)) {
                // if only in MethodReader, change to Declared but don't add again:
                if (currentScope.get(varName).getStatus() == AssignmentStatus.GLOBAL_DECLARED &&
                        status == AssignmentStatus.ASSIGNED_THIS_SCOPE) {
                    currentScope.get(varName).setStatus(AssignmentStatus.GLOBAL_ASSIGNED);
                } else if (currentScope.get(varName).getStatus() == AssignmentStatus.GLOBAL_DECLARED ||
                        currentScope.get(varName).getStatus() == AssignmentStatus.GLOBAL_ASSIGNED) {
                    currentScope.get(varName).setStatus(status);
                }

                // if already been declared/assigned more than in MethodReader,
                else {
                    throw new SymbolTableException(String.format(ASSIGN_TO_EXIST_VARNAME_ERROR, varName));
                }
            }
            // If hasn't been declared
            else {
                Variable var = new Variable(varName, type, AssignmentStatus.GLOBAL_DECLARED, isFinal);
                if (status == AssignmentStatus.ASSIGNED_THIS_SCOPE) {
                    var.setStatus(AssignmentStatus.GLOBAL_ASSIGNED);
                }
                currentScope.put(varName, var);
            }
        }
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
     * @throws SymbolTableException If the variable is not declared, is final, has an invalid status,
     * or the type does not match.
     */
    public void assignVar(String varName, VariableType variableType) throws SymbolTableException {

        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(varName)) {
                Variable curVar = table.get(i).get(varName);

                // Check if the variable is final
                if (curVar.isFinal()) {
                    throw new SymbolTableException(
                            String.format(FINAL_VARIABLE_ASSIGN_ERROR, varName)
                    );
                }

                // Check if the type matches
                if (curVar.getType() != variableType) {
                    throw new SymbolTableException(
                            String.format(TYPE_MISMATCH_ASSIGN_ERROR, varName, curVar.getType(), variableType)
                    );
                }
                if (curVar.getStatus() == AssignmentStatus.GLOBAL_DECLARED) {
                    curVar.setStatus(AssignmentStatus.GLOBAL_ASSIGNED);
                } else {
                    curVar.setStatus(AssignmentStatus.ASSIGNED_THIS_SCOPE);
                }
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
//        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.getLast().containsKey(varName)) {
                return (table.getLast().get(varName).getStatus() == AssignmentStatus.ASSIGNED_THIS_SCOPE ||
                        table.getLast().get(varName).getStatus() == AssignmentStatus.GLOBAL_ASSIGNED);
            }
//        }
        return false;
    }

    /**
     * Updates the status of all variables in the current scope.
     * - Variables with the status `DECLARED` are changed to `DECLARED_LAST_ROW`.
     * - Variables with the status `DECLARED_LAST_ROW` are changed to `CANT_BE_ASSIGNED`.
     * This method ensures that variables cannot be reassigned after their allowed assignment window.
     */
    private void updateVarsStatus() {
        if (!table.isEmpty()) {
            for (Variable var : table.getLast().values()) {
                if (var.getStatus() == AssignmentStatus.ASSIGNED_THIS_SCOPE) {
                    var.setStatus(AssignmentStatus.DECLARED);
                }
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