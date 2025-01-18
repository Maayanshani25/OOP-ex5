package ex5.scope_managing;

import ex5.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A SymbolTable manages variables and their types across multiple nested scopes.
 * Each scope is represented as a map, with variable names as keys and their types as values.
 * Scopes are managed as a stack, where new scopes can be added and removed as needed.
 */
public class SymbolTable {
    private final List<Map<String, Constants.VariableType>> table;

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
            throw new SymbolTableException(Constants.SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }
    }

    /**
     * Adds a variable to the current scope.
     *
     * @param var  The variable name.
     * @param type The type of the variable.
     * @throws SymbolTableException if no scopes exist or the variable already exists in the current scope.
     */
    public void addVarToScope(String var, Constants.VariableType type) throws SymbolTableException {
        if (table.isEmpty()) {
            throw new SymbolTableException(Constants.SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }

        Map<String, Constants.VariableType> currentScope = table.get(table.size() - 1);
        if (currentScope.containsKey(var)) {
            throw new SymbolTableException(Constants.SYMBOL_TABLE_VAR_ERROR_MESSAGE + ": " + var);
        }
        currentScope.put(var, type);
    }

    /**
     * Checks if a variable is declared in any scope.
     *
     * @param var The variable name.
     * @return true if the variable exists in any scope, false otherwise.
     */
    public boolean isVariableDeclared(String var) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(var)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the type of a variable from the nearest scope.
     *
     * @param var The variable name.
     * @return The variable type if found, null otherwise.
     */
    public Constants.VariableType getVarType(String var) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(var)) {
                return table.get(i).get(var);
            }
        }
        return null; // Variable not found
    }
}
