package io.javaclasses.filehub;

import io.javaclasses.filehub.user.Token;

/**
 * An abstract base for the user commands to modify the data stored in the FileHub application.
 */
public interface Command {

    /**
     * Obtains security token of a user who attempts to send a command.
     */
    Token token();
}
