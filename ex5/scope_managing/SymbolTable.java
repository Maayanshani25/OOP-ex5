package ex5.scope_managing;

import ex5.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private final List<Map<String, Constants.VariableType>> table;

    public SymbolTable() {
        table = new ArrayList<Map<String, Constants.VariableType>>();
    }

    public void addScope() {
        table.addLast(new HashMap<>());
    }

    public void removeScope() throws SymbolTableException {
        if (!table.isEmpty()) {
            table.remove(table.size() - 1);
        } else {
            throw new IllegalStateException(Constants.SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }
    }

    public void addVarToScope(String var, Constants.VariableType type) throws SymbolTableException {
        // Add var to the last scope:
        if (table.isEmpty()) {
            throw new SymbolTableException(Constants.SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }

        Map<String, Constants.VariableType> currentScope = table.get(table.size() - 1);
        // if already exists, throw error:
        if(currentScope.containsKey(var)) {
            throw new SymbolTableException(Constants.SYMBOL_TABLE_VAR_ERROR_MESSAGE);
        }
        currentScope.put(var, type);
    }

    public boolean isVariableDeclared(String var) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(var)) {
                return true;
            }
        }
        return false;
    }

    public Constants.VariableType getVarType(String var) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(var)) {
                return table.get(i).get(var);
            }
        }
        // if not exists:
        return null;
    }
}
