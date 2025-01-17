package ex5.scope_managing;

import ex5.util.Constants;

public class ScopeManagerException extends Exception {
    public ScopeManagerException(String exceptionMessage) {
        super(Constants.SCOPE_MANAGER_EXCEPTION_GENERAL_ERROR_MESSAGE + exceptionMessage);
    }
}
