package ex5.parsing;

import ex5.scope_managing.SymbolTableException;

/**
 * The Parser interface defines a contract for parsing a line of code.
 * Classes implementing this interface must provide an implementation for the {@code parse} method
 * to validate and process a given line of code.
 */
public interface Parser {

    /**
     * Parses and validates a single line of code.
     *
     * @param line The line of code to be parsed.
     * @throws ParserException      If the syntax or semantics of the line are invalid.
     * @throws SymbolTableException If there are issues related to the symbol table, such as scope management.
     */
    void parse(String line) throws ParserException, SymbolTableException; // No boolean, exceptions handle errors
}
