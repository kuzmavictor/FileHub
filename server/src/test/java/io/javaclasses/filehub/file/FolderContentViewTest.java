package io.javaclasses.filehub.file;

import com.google.common.io.Files;
import io.javaclasses.filehub.AbstractFileHubTest;
import io.javaclasses.filehub.user.Token;
import io.javaclasses.filehub.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.google.common.truth.Truth.assertThat;
import static io.javaclasses.filehub.given.TestEnvironment.FIRST_FILE_LOCATION;
import static io.javaclasses.filehub.given.TestEnvironment.FIRST_FILE_NAME;
import static io.javaclasses.filehub.given.TestEnvironment.ROOT_FOLDER_NAME;
import static io.javaclasses.filehub.given.TestEnvironment.SECOND_FILE_LOCATION;
import static io.javaclasses.filehub.given.TestEnvironment.SECOND_FILE_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("`FolderContentView` should")
class FolderContentViewTest extends AbstractFileHubTest {

    @TempDir
    @SuppressWarnings({"PackageVisibleField", "WeakerAccess"}) // This field must not be private.
            Path tempDir;
    private FileUpload fileUpload;
    private FolderCreation folderCreation;
    private Token token;
    private FolderId rootFolder;
    private FolderContentView folderContentView;
    private ReadFolderContent query;

    @BeforeEach
    void setUp() {
        FileSystem fileSystem = new FileSystem(tempDir);
        folderCreation = new FolderCreation(folderMetadataStorage(), tokenStorage());
        fileUpload = new FileUpload(tokenStorage(), fileSystem, fileMetaDataStorage());
        folderContentView = new FolderContentView(tokenStorage(), fileMetaDataStorage(),
                                                  folderMetadataStorage());
        UserId registeredUserId = registerUser();
        if (registeredUserId == null) {
            throw new IllegalStateException("The user should be registered successfully.");
        }

        token = authenticateUser();
        if (token == null) {
            throw new IllegalStateException("The user should be authenticated successfully.");
        }

        rootFolder = userStorage().read(registeredUserId)
                                  .orElseThrow(IllegalStateException::new)
                                  .userRootFolder();
        query = new ReadFolderContent(token, rootFolder.value());
    }

    @Test
    @DisplayName("successfully get content of a folder for an authenticated user")
    void getFolderContent() throws IOException {
        File firstFile = new File(FIRST_FILE_LOCATION);
        File secondFile = new File(SECOND_FILE_LOCATION);

        FileUploaded firstFileUploaded = uploadNewFile(firstFile, rootFolder, FIRST_FILE_NAME);
        assertThat(firstFileUploaded).isNotNull();

        FileUploaded secondFileUploaded = uploadNewFile(secondFile, rootFolder, SECOND_FILE_NAME);
        assertThat(secondFileUploaded).isNotNull();

        FolderCreated createdFirstParentFolder = createFolder(rootFolder, "nestedFolderNameOne");
        assertThat(createdFirstParentFolder).isNotNull();

        FolderCreated createdSecondParentFolder = createFolder(rootFolder, "nestedFolderNameTwo");
        assertThat(createdSecondParentFolder).isNotNull();

        FolderContent folderContent = folderContentView.execute(query);
        int expectedSize = 4;
        assertThat(folderContent.folderItems()).hasSize(expectedSize);
    }

    @Test
    @DisplayName("fail if the non-authenticated user tries to get the folder content")
    void failReadingFolderContent() {
        query = new ReadFolderContent(Token.generate(), ROOT_FOLDER_NAME);
        assertThrows(QueryExecutionForbiddenException.class,
                     () -> folderContentView.execute(query));
    }

    @Test
    @DisplayName("read the empty folder")
    void readEmptyFolder() {
        FolderContent folderContent = folderContentView.execute(query);

        assertThat(folderContent.folderItems()).isEmpty();
    }

    /**
     * Creates a new folder.
     */
    private FolderCreated createFolder(FolderId folderId, String folderName) {
        CreateFolder createFolderCommand = new CreateFolder(token, folderName, folderId);

        return folderCreation.handle(createFolderCommand);
    }

    /**
     * Uploads a new file.
     */
    private FileUploaded uploadNewFile(File testedFile, FolderId parentFolder, String fileName)
            throws IOException {
        byte[] fileContents = Files.toByteArray(testedFile);
        UploadFile uploadFileCommand = new UploadFile(token, fileName, fileContents, parentFolder);
        return fileUpload.handle(uploadFileCommand);
    }
}
