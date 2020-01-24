package io.javaclasses.filehub.user;

import io.javaclasses.filehub.storage.AbstractInMemoryStorage;
import io.javaclasses.filehub.storage.TokenStorage;

/**
 * An in-memory implementation of {@link TokenStorage}.
 */
public final class InMemoryTokenStorage extends AbstractInMemoryStorage<Token, TokenRecord>
        implements TokenStorage {

}
