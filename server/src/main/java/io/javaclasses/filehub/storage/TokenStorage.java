package io.javaclasses.filehub.storage;

import io.javaclasses.filehub.user.Token;
import io.javaclasses.filehub.user.TokenRecord;

/**
 * A storage, which contains tokens and token records.
 */
public interface TokenStorage extends Storage<Token, TokenRecord> {

}
