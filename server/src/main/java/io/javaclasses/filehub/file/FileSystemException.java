package io.javaclasses.filehub.file;

/**
 * This exception is thrown if the file system failed to execute its operation.
 *
 * <p>For example:
 *
 * <ol>
 *     <li>the file system failed to find file location;
 *     <li>the file system failed to create a new file.
 * </ol>
 */
public final class FileSystemException extends RuntimeException {

    private static final long serialVersionUID = 1706699323844326042L;

    /**
     * Initializes an exception.
     *
     * @param message
     *         the message with a detailed description of the exception occurrence cause
     */
    public FileSystemException(String message) {
        super(message);
    }
}
