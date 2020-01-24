package io.javaclasses.filehub.file;

import io.javaclasses.filehub.AbstractFileHubTest;
import io.javaclasses.filehub.user.Token;
import io.javaclasses.filehub.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static io.javaclasses.filehub.given.TestEnvironment.ROOT_FOLDER_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("`FolderCreation` should")
class CreateNewFolderTest extends AbstractFileHubTest {

    private FolderCreation folderCreation;
    private Token token;
    private CreateFolder createParentFolder;
    private FolderId userRootFolder;

    @BeforeEach
    void setUp() {

        UserId registeredUserId = registerUser();
        if (registeredUserId == null) {
            throw new IllegalStateException("The user should be registered successfully.");
        }

        token = authenticateUser();
        if (token == null) {
            throw new IllegalStateException("The user should be authenticated successfully.");
        }

        folderCreation = new FolderCreation(folderMetadataStorage(), tokenStorage());
        userRootFolder = userStorage().read(registeredUserId)
                                      .orElseThrow(IllegalStateException::new)
                                      .userRootFolder();
        createParentFolder = new CreateFolder(token, ROOT_FOLDER_NAME, userRootFolder);
    }

    @Test
    @DisplayName("create a new folder for the authenticated user successfully")
    void createNewFolder() {
        FolderCreated folderCreated = folderCreation.handle(createParentFolder);
        assertThat(folderCreated).isNotNull();

        Optional<FolderMetadataRecord> fileInfoRecord = folderMetadataStorage().read(
                folderCreated.folderId());
        assertThat(fileInfoRecord).isPresent();

        fileInfoRecord.ifPresent(record -> {
            FolderId actualFileId = record.id();
            assertThat(actualFileId).isEqualTo(folderCreated.folderId());
        });
    }

    @Test
    @DisplayName("create the nested folder for an authenticated user successfully")
    void createNestedFolder() {
        FolderCreated parentFolderCreated = folderCreation.handle(createParentFolder);
        assertThat(parentFolderCreated).isNotNull();

        String nestedFolderName = "nestedFolderName";
        CreateFolder createNestedFolderCommand = new CreateFolder(token, nestedFolderName,
                                                                  userRootFolder);

        FolderCreated createdNestedFolder = folderCreation.handle(createNestedFolderCommand);
        assertThat(createdNestedFolder).isNotNull();

        Optional<FolderMetadataRecord> nestedFolderRecord =
                folderMetadataStorage().read(createdNestedFolder.folderId());

        assertThat(nestedFolderRecord).isPresent();
        nestedFolderRecord.ifPresent(record -> {
            FolderId actualFolderId = record.id();
            assertThat(actualFolderId).isEqualTo(createdNestedFolder.folderId());
        });
    }

    @Test
    @DisplayName("create folders with the same name in separate folders")
    void createSimilarFoldersInSeparateFolders() {
        FolderCreated rootFolder = folderCreation.handle(createParentFolder);
        assertThat(rootFolder).isNotNull();

        FolderCreated createdFirstParentFolder = createFolder(rootFolder.folderId(),
                                                              "nestedFolderNameOne");
        assertThat(createdFirstParentFolder).isNotNull();

        FolderCreated createdSecondParentFolder = createFolder(rootFolder.folderId(),
                                                               "nestedFolderNameTwo");
        assertThat(createdSecondParentFolder).isNotNull();

        String sameFolderName = "sameFolderName";
        FolderCreated createdFirstNestedFolder = createFolder(createdFirstParentFolder.folderId(),
                                                              sameFolderName);
        assertThat(createdFirstNestedFolder).isNotNull();

        FolderCreated createdSecondNestedFolder = createFolder(createdSecondParentFolder.folderId(),
                                                               sameFolderName);
        assertThat(createdSecondNestedFolder).isNotNull();
    }

    @Test
    @DisplayName("fail if a folder with the same name is created inside the current folder")
    void failIfSuchFolderExists() {
        FolderCreated folderCreated = folderCreation.handle(createParentFolder);
        assertThat(folderCreated).isNotNull();

        Optional<FolderMetadataRecord> fileInfoRecord = folderMetadataStorage().read(
                folderCreated.folderId());
        assertThat(fileInfoRecord).isPresent();

        assertThrows(FolderCreationException.class,
                     () -> folderCreation.handle(createParentFolder));
    }

    /**
     * Returns the folder creating result using the obtained root folder and new folder name.
     */
    private FolderCreated createFolder(FolderId folderId, String folderName) {
        CreateFolder createFolderCommand = new CreateFolder(token, folderName, folderId);

        return folderCreation.handle(createFolderCommand);
    }
}
