package ex5.scope_managing;

import ex5.util.Constants;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;

// todo: rotem exit scope in symbol table

public class ScopeManager {
    private final Deque<Constants.ScopeKind> scopeStack;
    private int methodsCounter;
    private SymbolTable symbolTable;

    public ScopeManager(SymbolTable symbolTable) {
        scopeStack = new ArrayDeque<>();
        methodsCounter = 0;
        this.symbolTable = symbolTable;
    }

    // being called from the parsers:
    public void enterNewScope(Constants.ScopeKind scopeKind) {
        scopeStack.push(scopeKind);
        symbolTable.addScope();
        if (scopeKind == Constants.ScopeKind.METHOD) {
            methodsCounter++;
        }
    }

    // being called from the Validator if a line is '}':
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

    public int getMethodsCounter() {
        return methodsCounter;
    }

    public boolean areInGlobalScope() {
        return (scopeStack.size() == 1);
    }

}
