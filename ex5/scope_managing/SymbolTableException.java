package ex5.scope_managing;

import ex5.util.Constants;

/**
 * The SymbolTableException class represents exceptions related to symbol table operations.
 * These exceptions occur when there are issues with variable declarations, assignments,
 * or scope management within the symbol table.
 */
public class SymbolTableException extends Exception {

    /**
     * Constructs a new SymbolTableException with the specified error message.
     *
     * @param exceptionMessage The specific error message describing the symbol table issue.
     */
    public SymbolTableException(String exceptionMessage) {
        super(Constants.SYMBOL_TABLE_EXCEPTION_GENERAL_ERROR_MESSAGE + exceptionMessage);
    }
}
