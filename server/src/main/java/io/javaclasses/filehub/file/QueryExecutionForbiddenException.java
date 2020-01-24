package io.javaclasses.filehub.file;

/**
 * This exception is thrown if the query execution is forbidden.
 *
 * <p>For example, user is not authenticated to send a query.
 */
public final class QueryExecutionForbiddenException extends RuntimeException {

    private static final long serialVersionUID = -5206725505962779670L;

    /**
     * Initializes an exception instance.
     *
     * @param message
     *         the message with a detailed description of the exception occurrence cause
     */
    public QueryExecutionForbiddenException(String message) {
        super(message);
    }
}
