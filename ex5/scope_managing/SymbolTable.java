package ex5.scope_managing;

import ex5.Variable;
import static ex5.util.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: add new documentation


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
        if (!table.isEmpty()) {
            throw new SymbolTableException(SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }

        Map<String, Variable> currentScope = table.get(table.size() - 1);
        if (currentScope.containsKey(varName)) {
            throw new SymbolTableException(SYMBOL_TABLE_VAR_ERROR_MESSAGE + ": " + varName);
        }
        currentScope.put(varName, var);
    }

    /**
     * Adds a variable to the current scope with specific attributes.
     *
     * @param varName  The name of the variable.
     * @param type     The type of the variable.
     * @param status   The assignment status of the variable.
     * @param isFinal  Whether the variable is declared as final.
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

        Variable var = new Variable(varName, type, status, isFinal);

        Map<String, Variable> currentScope = table.get(table.size() - 1);
        if (currentScope.containsKey(varName)) {
            throw new SymbolTableException(SYMBOL_TABLE_VAR_ERROR_MESSAGE + ": " + varName);
        }
        currentScope.put(varName, var);
    }

    /**
     * Assigns a value to a variable in the nearest scope where it is declared.
     * Ensures that final variables cannot be reassigned and that the assigned value matches the variable's type.
     *
     * @param varName      The name of the variable.
     * @param variableType The type of the value being assigned.
     * @throws SymbolTableException If the variable is not declared, if it is final, or if the type does not match.
     */
    public void assignVar(String varName, VariableType variableType) throws SymbolTableException {
        // todo: implement.
        //      check if it's inside the correct method

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
     * Retrieves the type of a variable from the nearest scope where it is declared.
     *
     * @param varName The variable name.
     * @return The type of the variable, or null if the variable is not found.
     */
    public boolean isVariableAssigned(String varName) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(varName)) {
                return (table.get(i).get(varName).getStatus()==AssignmentStatus.ASSIGNED);
            }
        }
        return false;
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
