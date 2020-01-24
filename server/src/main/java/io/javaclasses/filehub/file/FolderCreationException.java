package io.javaclasses.filehub.file;

/**
 * This exception is thrown if the authenticated user failed to create a new folder.
 *
 * <p>For example, a user attempts to create an already existing folder.
 */
public class FolderCreationException extends RuntimeException {

    private static final long serialVersionUID = 0L;
    private static final String EXCEPTION_MESSAGE =
            "The user `%s` tried to create a new folder. %s";

    /**
     * Initializes an exception.
     *
     * @param userLogin
     *         a login name of the user whose folder cannot be created
     * @param details
     *         additional details to describe the problem properly
     */
    public FolderCreationException(String userLogin, String details) {
        super(String.format(EXCEPTION_MESSAGE, userLogin, details));
    }
}
