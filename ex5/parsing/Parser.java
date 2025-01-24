package ex5.parsing;


import ex5.scope_managing.SymbolTableException;

public interface Parser {

    void parse(String line) throws ParserException, SymbolTableException; // No boolean, exceptions handle errors
}
