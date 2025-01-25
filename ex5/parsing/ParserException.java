package ex5.parsing;

import ex5.util.Constants;

/**
 * The ParserException class represents exceptions that occur during the parsing process.
 * This exception is used to indicate syntax or semantic errors in the provided code lines.
 */
public class ParserException extends Exception {

    /**
     * Constructs a new ParserException with a detailed error message.
     *
     * @param exceptionMessage A specific error message that provides details about the parsing issue.
     */
    public ParserException(String exceptionMessage) {
        super(Constants.PARSER_EXCEPTION_GENERAL_ERROR_MESSAGE + exceptionMessage);
    }
}
