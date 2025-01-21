package ex5.parsing;


// todo: when it's method tou should first open a new scope and then add the variables to the symbol
//      table in the new scope. p6 line 3

import ex5.scope_managing.ScopeManager;
import ex5.scope_managing.SymbolTable;
import ex5.scope_managing.SymbolTableException;

public class MethodParser implements Parser {
    private static final String METHOD_NAME_REGEX = "([a-zA-Z]|_[a-zA-Z0-9])[a-zA-Z0-9_]*"; // TODO: check - no _
    private static final String VARIABLE_NAME_REGEX = "([a-zA-Z]|_[a-zA-Z0-9])[a-zA-Z0-9_]*";

    private final SymbolTable symbolTable;
    private final ScopeManager scopeManager;

    public MethodParser(SymbolTable symbolTable, ScopeManager scopeManager) {
        this.symbolTable = symbolTable;
        this.scopeManager = scopeManager;
    }

    @Override
    public void parse(String line) throws ParserException, SymbolTableException {
        String currentLine = line.trim(); // Trim leading and trailing whitespace


    }

    private void parseMethodDeclaration() throws ParserException, SymbolTableException {
        // TODO: conditions to check:
        //  is not in other method
        //  is "void method_name (type parameter, type parameter...) {"
        //  ends with "return;" and then "}"
        //  method_name isnt already exists (here or in second pre-process?)
        //  parameters name arent already exist

        // TODO: needs to add:
        //  new scope
        //  parameters to scope vars


    }

    private void parseMethodCall() throws ParserException, SymbolTableException {
        // TODO: conditions to check:
        //  is in other method
        //  is "method_name (var, var...);"
        //  method_name exists
        //  vars exist/constants
        //  num of vars fit to the requiered
        //  var types fit to the types requiered (bool can get int/double, double can get int)


    }
}
