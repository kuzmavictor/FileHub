package io.javaclasses.filehub.web;

/**
 * This exception is thrown if the user tries to send a bad request.
 *
 * <p> For example:
 * <ol>
 * <li> the I/O error occurred during the retrieval of the requested {@code Part};
 * <li> this request is not of {@code multipart/form-data} type.
 * </ol>
 */
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    private static final String EXCEPTION_MESSAGE = "Cannot process a bad request. %s";

    /**
     * Initializes an exception instance.
     *
     * @param details
     *         additional details to describe the problem properly
     */
    public BadRequestException(String details) {
        super(String.format(EXCEPTION_MESSAGE, details));
    }
}
