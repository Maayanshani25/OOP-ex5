package ex5;

import ex5.scope_managing.SymbolTable;

public class Validator {
    public Validator(SymbolTable symbolTable) {

    }


    // TODO: from the instructions - should use the relevant Exceptions and not just true or false
    public static boolean isValidLine(String line) {
        // TODO: write this method
        // if is Method syntax - call MethodParser, and use ScopeManager.getMethodsCounter()

        // if is "while" syntax - call WhileParser

        // if is "if" syntax - call IfParser

        // if is Var syntax - call MethodParser

        // if is "}" - call ScopeManager.exitScope()

        return false;
    }
}
