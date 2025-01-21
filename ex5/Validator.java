package ex5;

import ex5.scope_managing.SymbolTable;

public class Validator {
    public Validator(SymbolTable symbolTable) {

    }


    // TODO: from the instructions - should use the relevant Exceptions and not just true or false
    public static boolean isValidLine(String line) {
        // TODO: write this method
        //    - check is "()*//" - or how it should be -comment line legal according to 5.1 last line in page
        //     - can create a keyword regex like
        //     (int|double|String|boolean|char|final|void|if|while|return)
        // if is Method syntax - call MethodParser, and use ScopeManager.getMethodsCounter()
        // when it's method tou should first open a new scope and then add the variables to the symbol
        // table in the new scope. p6 line 3

        // if is "while" syntax - call WhileParser

        // if is "if" syntax - call IfParser

        // if is declaration syntax - call declareParser
        // if the declaration didnt assign the value the next line have to be his assignment

        // if is assignment syntax - call AssignmentParser


        // return ? what should be

        // if is "}" - call ScopeManager.exitScope()

        return false;
    }
}
