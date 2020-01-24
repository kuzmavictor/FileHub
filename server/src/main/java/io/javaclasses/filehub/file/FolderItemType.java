package io.javaclasses.filehub.file;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The types of folder items.
 */
public enum FolderItemType {
    @SerializedName("folder")
    @Expose
    FOLDER,

    @SerializedName("file")
    @Expose
    FILE;
}
