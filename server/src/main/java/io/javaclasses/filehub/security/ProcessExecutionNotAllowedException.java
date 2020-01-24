package io.javaclasses.filehub.security;

import io.javaclasses.filehub.FileHubProcess;
import io.javaclasses.filehub.user.Token;

/**
 * This exception is thrown if the user does not have enough permissions to execute the process.
 */
public class ProcessExecutionNotAllowedException extends RuntimeException {

    private static final long serialVersionUID = 8195356164652781119L;

    private static final String MESSAGE_WITHOUT_TOKEN =
            "The user is not authorized to execute process `%s`.";
    private static final String MESSAGE_WITH_TOKEN =
            "The user with token `%s` has no permissions to execute process `%s`.";

    /**
     * Initializes an exception for authorized user.
     *
     * @param token
     *         an authorized user token
     * @param process
     *         a process which authorized user wants to execute
     */
    public ProcessExecutionNotAllowedException(Token token,
                                               Class<? extends FileHubProcess> process) {
        super(String.format(MESSAGE_WITH_TOKEN, token, process));
    }

    /**
     * Initializes an exception for non-authorized user.
     *
     * @param process
     *         a process which non-authorized user wants to execute
     */
    public ProcessExecutionNotAllowedException(Class<? extends FileHubProcess> process) {
        super(String.format(MESSAGE_WITHOUT_TOKEN, process));
    }
}
