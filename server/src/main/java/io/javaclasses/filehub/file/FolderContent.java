package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Contains the collection of files and folders metadata stored in a folder.
 */
@Immutable
@SuppressWarnings("Immutable") // This class is effectively immutable.
public final class FolderContent {

    @SerializedName("items")
    @Expose
    private final Collection<FolderItem> folderItems;

    /**
     * Creates a {@code FolderContent} instance.
     *
     * @param folderItems
     *         a collection of the folder items
     */
    public FolderContent(Collection<FolderItem> folderItems) {
        checkNotNull(folderItems);
        this.folderItems = Collections.unmodifiableCollection(folderItems);
    }

    /**
     * Obtains the collection of files and folders metadata stored in a folder.
     */
    public Collection<FolderItem> folderItems() {
        return Collections.unmodifiableCollection(folderItems);
    }
}
