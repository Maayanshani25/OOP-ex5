package ex5;

import ex5.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private final List<Map<String, String>> table;

    public SymbolTable() {
        table = new ArrayList<Map<String, String>>();
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

    public void addVarToScope(String var, String type) throws SymbolTableException {
        // Add var to the last scope:
        if (table.isEmpty()) {
            throw new SymbolTableException(Constants.SYMBOL_TABLE_SCOPE_ERROR_MESSAGE);
        }

        Map<String, String> currentScope = table.get(table.size() - 1);
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

    public String getVarType(String var) {
        for (int i = table.size() - 1; i >= 0; i--) {
            if (table.get(i).containsKey(var)) {
                return table.get(i).get(var);
            }
        }
        // if not exists:
        return null;
    }
}
