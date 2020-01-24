package io.javaclasses.filehub.given;

import io.javaclasses.filehub.user.Avatar;

import java.nio.charset.StandardCharsets;

/**
 * A utility class for tests.
 */
public final class TestEnvironment {

    public static final String LOGIN = "testLogin";
    public static final String PASSWORD = "testPassword";
    public static final String SHORT_LOGIN = "log";
    public static final String SHORT_PASSWORD = "psw";
    public static final String FIRST_FILE_NAME = "1.png";
    public static final String SECOND_FILE_NAME = "2.png";
    public static final String FIRST_FILE_LOCATION = "./src/test/resources/1.png";
    public static final String SECOND_FILE_LOCATION = "./src/test/resources/2.png";
    public static final String ROOT_FOLDER_NAME = "rootFolder";

    /**
     * Prevents instantiation of {@code TestEnvironment}.
     */
    private TestEnvironment() {
    }

    /**
     * Obtains the login string which contains 256 characters.
     */
    public static String longLogin() {
        return longString();
    }

    /**
     * Obtains the password string which contains 256 characters.
     */
    public static String longPassword() {
        return longString();
    }

    /**
     * Obtains the string which contains 256 characters.
     */
    private static String longString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            stringBuilder.append('l');
        }
        return stringBuilder.toString();
    }

    /**
     * Obtains the updated avatar contents.
     */
    public static Avatar updatedAvatar() {
        return Avatar.createAvatar("userUpdatedAvatar".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Obtains the {@link Avatar} instance to the new user.
     */
    public static Avatar avatar() {
        return Avatar.createAvatar("userAvatar".getBytes(StandardCharsets.UTF_8));
    }
}
