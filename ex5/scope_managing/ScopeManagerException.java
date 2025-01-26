package ex5.scope_managing;

import ex5.util.Constants;

/**
 * The ScopeManagerException class represents exceptions specific to scope management operations.
 * These exceptions occur when there are errors related to entering or exiting scopes,
 * such as trying to exit a scope when none exists.
 */
public class ScopeManagerException extends Exception {

    /**
     * Constructs a new ScopeManagerException with the specified error message.
     *
     * @param exceptionMessage The specific error message describing the scope management issue.
     */
    public ScopeManagerException(String exceptionMessage) {
        super(Constants.SCOPE_MANAGER_EXCEPTION_GENERAL_ERROR_MESSAGE + exceptionMessage);
    }
}
