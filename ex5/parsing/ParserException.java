package ex5.parsing;

import ex5.util.Constants;

public class ParserException extends Exception {
    public ParserException(String exceptionMessage) {
        super(Constants.PARSER_EXCEPTION_GENERAL_ERROR_MESSAGE + exceptionMessage);
    }
}
