package io.javaclasses.filehub.user;

import com.google.errorprone.annotations.Immutable;

/**
 * The wrapper for the user's avatar.
 *
 * @implNote Avatar value is stored as a {@code String} value.
 */
@Immutable
public final class Avatar {

    private final String avatar;

    /**
     * Initializes a new {@code Avatar} instance.
     *
     * @param avatar
     *         a user avatar contents
     */
    private Avatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Creates a new instance of {@code Avatar}.
     *
     * @implNote Implemented as a static factory method.
     * @see Avatars#encodeAvatar(byte[])
     */
    public static Avatar createAvatar(byte[] avatarContents) {
        String temporaryAvatarContents = Avatars.encodeAvatar(avatarContents);
        return new Avatar(temporaryAvatarContents);
    }

    /**
     * Obtains the avatar contents.
     *
     * @see Avatars#decodeAvatar(String)
     */
    public byte[] contents() {
        return Avatars.decodeAvatar(avatar);
    }
}
