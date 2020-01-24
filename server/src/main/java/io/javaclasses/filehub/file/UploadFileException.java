package io.javaclasses.filehub.file;

/**
 * This exception is thrown if the user failed to write or update a file.
 *
 * <p>For example:
 *
 * <ol>
 *     <li>the file does not exist but information about this file is present;
 *     <li>the file exists but information about this file is not present;
 *     <li>user attempts to upload an already existing file.
 * </ol>
 */
public final class UploadFileException extends RuntimeException {

    private static final long serialVersionUID = 0L;
    private static final String MESSAGE_WITH_USER_LOGIN = "The user `%s` tried to upload file. %s";

    /**
     * Initializes an exception.
     *
     * @param userLogin
     *         a user login name whose file can't be processed
     * @param details
     *         additional details to describe the problem properly
     */
    public UploadFileException(String userLogin, String details) {
        super(String.format(MESSAGE_WITH_USER_LOGIN, userLogin, details));
    }
}
