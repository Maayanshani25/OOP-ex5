package ex5.scope_managing;

import ex5.util.Constants;

public class SymbolTableException extends Exception {
    public SymbolTableException(String exceptionMessage) {
        super(Constants.SYMBOL_TABLE_EXCEPTION_GENERAL_ERROR_MESSAGE + exceptionMessage);
    }
}
