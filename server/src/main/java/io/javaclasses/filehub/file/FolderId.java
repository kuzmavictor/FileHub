package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.storage.RecordId;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.hash.Hashing.sha256;

/**
 * A unique folder identifier.
 *
 * <p>The folder can contain folders with unique names only,
 * so the folder name and parent folder location are used as an identifier of a folder.
 */
@Immutable
public final class FolderId implements RecordId {

    private final String value;

    private FolderId(String identifier) {
        this.value = identifier;
    }

    /**
     * Creates a {@code FolderId} instance from the existing identifier value.
     *
     * @param identifier
     *         an identifier value
     */
    public static FolderId of(String identifier) {
        checkNotNull(identifier);

        return new FolderId(identifier);
    }

    /**
     * Creates a {@code FolderId} instance.
     *
     * @param folderName
     *         a name of folder
     * @param parentFolder
     *         a parent folder
     */
    public static FolderId createId(String folderName, FolderId parentFolder) {
        checkNotNull(folderName);
        checkNotNull(parentFolder);

        String hashableValue = parentFolder.value() + folderName;

        return new FolderId(generateIdentifier(hashableValue));
    }

    /**
     * Creates a {@code FolderId} instance if the folder is user root folder.
     *
     * <p>The parent folder is {@code null} in this case.
     *
     * @param folderName
     *         a name of folder
     */
    public static FolderId createRootFolderId(String folderName) {
        checkNotNull(folderName);

        return new FolderId(generateIdentifier(folderName));
    }

    private static String generateIdentifier(CharSequence hashableValue) {
        return sha256().hashString(hashableValue, UTF_8)
                       .toString();
    }

    /**
     * Obtains the folder identifier value.
     */
    public String value() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FolderId folderId = (FolderId) o;
        return Objects.equals(value, folderId.value);
    }
}
