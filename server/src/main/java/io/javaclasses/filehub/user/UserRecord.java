package io.javaclasses.filehub.user;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.storage.Record;
import io.javaclasses.filehub.storage.Storage;

import java.nio.charset.StandardCharsets;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A user data designed to be written and read from a {@linkplain Storage storage}.
 */
@SuppressWarnings("UnstableApiUsage") // Guava's implementation seems stable.
public final class UserRecord extends Record<UserId> {

    /**
     * A key which is used to create password hasher.
     */
    private static final byte[] KEY = "SomeSecurityString".getBytes(StandardCharsets.UTF_8);

    /**
     * A hash function which encodes obtained string by SHA256 algorithm.
     */
    private static final HashFunction HASHER = Hashing.hmacSha256(KEY);

    private final String passwordHash;
    private final Avatar avatar;
    private final FolderId userRootFolder;

    /**
     * Creates an instance of user record.
     *
     * @param id
     *         a user identifier
     * @param password
     *         a user password
     * @param avatar
     *         a user avatar
     * @param userRootFolder
     *         a user root folder identifier
     */
    public UserRecord(UserId id, CharSequence password, Avatar avatar, FolderId userRootFolder) {
        super(id);
        checkNotNull(password);
        this.passwordHash = hashOf(password);
        this.avatar = checkNotNull(avatar);
        this.userRootFolder = checkNotNull(userRootFolder);
    }

    /**
     * Updates the user record with a new avatar.
     *
     * @param original
     *         a user record, which contains 'old' avatar
     * @param avatar
     *         a new user avatar
     */
    private UserRecord(UserRecord original, Avatar avatar) {
        super(original.id());
        this.passwordHash = original.passwordHash();
        this.avatar = avatar;
        this.userRootFolder = original.userRootFolder;
    }

    /**
     * Generates a hash of obtained password.
     *
     * @param password
     *         a not hashed string containing user password
     * @implNote Encoded using {@code UTF-8} charset and hash function.
     */
    private static String hashOf(CharSequence password) {
        HashCode hashCode = HASHER.hashString(password, StandardCharsets.UTF_8);
        return new String(hashCode.asBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Returns the user password hash.
     */
    private String passwordHash() {
        return passwordHash;
    }

    /**
     * Checks if the obtained password matches the user password.
     */
    public boolean passwordMatches(String password) {
        checkNotNull(password);
        String obtainedPasswordHash = hashOf(password);
        return this.passwordHash.equals(obtainedPasswordHash);
    }

    /**
     * Returns the user avatar contents.
     */
    public Avatar avatar() {
        return avatar;
    }

    /**
     * Modifies the current user avatar and updates the user record.
     *
     * @param avatar
     *         a new avatar contents
     */
    public UserRecord updateAvatar(Avatar avatar) {
        checkNotNull(avatar);
        return new UserRecord(this, avatar);
    }

    /**
     * Obtains the identifier of the root folder.
     */
    public FolderId userRootFolder() {
        return userRootFolder;
    }
}
