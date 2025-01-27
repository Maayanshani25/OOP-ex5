package ex5.scope_managing;

import ex5.util.Constants;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;

/**
 * The ScopeManager class is responsible for managing nested scopes within the program.
 * It maintains a stack of scope kinds and ensures proper scope entry and exit operations,
 * including handling method scopes and interactions with the symbol table.
 */
public class ScopeManager {
    private final Deque<Constants.ScopeKind> scopeStack;
    private int methodsCounter;
    private final SymbolTable symbolTable;

    /**
     * Constructs a new ScopeManager instance with the provided symbol table.
     *
     * @param symbolTable The symbol table used for managing variables within different scopes.
     */
    public ScopeManager(SymbolTable symbolTable) {
        scopeStack = new ArrayDeque<>();
        methodsCounter = 0;
        this.symbolTable = symbolTable;
    }

    /**
     * Enters a new scope of the specified kind. This adds a new scope to the stack and the symbol table.
     *
     * @param scopeKind The kind of scope being entered (e.g., METHOD, IF, WHILE, etc.).
     */
    public void enterNewScope(Constants.ScopeKind scopeKind) throws SymbolTableException {
        scopeStack.push(scopeKind);
        symbolTable.addScope();
        if (scopeKind == Constants.ScopeKind.METHOD) {
            methodsCounter++;
        }
    }

    /**
     * Exits the most recent scope. This removes the scope from the stack and the symbol table.
     *
     * @throws ScopeManagerException If there are no scopes to exit.
     * @throws SymbolTableException  If an error occurs while removing the scope from the symbol table.
     */
    public void exitScope() throws ScopeManagerException, SymbolTableException {
        try {
            Constants.ScopeKind lastScopeKind = scopeStack.pop();
            symbolTable.removeScope();
            if (lastScopeKind == Constants.ScopeKind.METHOD) {
                methodsCounter--;
            }
        } catch (EmptyStackException e) {
            throw new ScopeManagerException(Constants.INVALID_EXIT_SCOPE_MESSAGE);
        }
    }

    /**
     * Gets the current number of active method scopes.
     *
     * @return The number of active method scopes.
     */
    public int getMethodsCounter() {
        return methodsCounter;
    }

    /**
     * Checks if the current scope is the global scope.
     *
     * @return True if the current scope is the global scope, false otherwise.
     */
    public boolean areInGlobalScope() {
        return (scopeStack.size() == 1);
    }
}
