package io.javaclasses.filehub.storage;

import io.javaclasses.filehub.user.UserId;
import io.javaclasses.filehub.user.UserRecord;

/**
 * A storage, which contains user identifiers and user records.
 */
public interface UserStorage extends Storage<UserId, UserRecord> {

}
