package io.javaclasses.filehub.user;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A utility class to encode and decode the user avatar contents.
 */
final class Avatars {

    /**
     * Prevents {@code Avatar} instantiation.
     */
    private Avatars() {
    }

    /**
     * Encodes the user avatar contents from {@code byte[]} to {@code String}.
     *
     * @implNote Uses {@code Base64 encoder}.
     */
    static String encodeAvatar(byte[] avatar) {
        checkNotNull(avatar);
        return Base64.getEncoder()
                     .encodeToString(avatar);
    }

    /**
     * Decodes the user avatar contents from {@code String} to {@code byte[]}.
     *
     * @implNote Uses {@code Base64 decoder}.
     */
    static byte[] decodeAvatar(String avatar) {
        checkNotNull(avatar);
        return Base64.getDecoder()
                     .decode(avatar.getBytes(StandardCharsets.UTF_8));
    }
}
