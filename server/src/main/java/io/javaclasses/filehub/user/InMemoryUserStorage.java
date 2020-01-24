package io.javaclasses.filehub.user;

import io.javaclasses.filehub.storage.AbstractInMemoryStorage;
import io.javaclasses.filehub.storage.UserStorage;

/**
 * An in-memory implementation of {@link UserStorage}.
 */
public final class InMemoryUserStorage extends AbstractInMemoryStorage<UserId, UserRecord>
        implements UserStorage {

}
