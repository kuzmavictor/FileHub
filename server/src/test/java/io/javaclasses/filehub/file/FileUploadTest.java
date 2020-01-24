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
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static io.javaclasses.filehub.given.TestEnvironment.FIRST_FILE_LOCATION;
import static io.javaclasses.filehub.given.TestEnvironment.FIRST_FILE_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("`FileUpload` should")
class FileUploadTest extends AbstractFileHubTest {

    @TempDir
    @SuppressWarnings({"PackageVisibleField", "WeakerAccess"}) // This field must not be private.
            Path tempDir;
    private FileSystem fileSystem;
    private FileUpload fileUpload;
    private FileId fileId;
    private UploadFile uploadFileCommand;

    @BeforeEach
    void setUp() throws IOException {
        fileSystem = new FileSystem(tempDir);
        File file = new File(FIRST_FILE_LOCATION);
        fileUpload = new FileUpload(tokenStorage(), fileSystem, fileMetaDataStorage());

        byte[] fileContents = Files.toByteArray(file);

        UserId registeredUserId = registerUser();
        if (registeredUserId == null) {
            throw new IllegalStateException("The user should be registered successfully.");
        }

        Token token = authenticateUser();
        if (token == null) {
            throw new IllegalStateException("The user should be authenticated successfully.");
        }

        FolderId folderId = userStorage().read(registeredUserId)
                                         .orElseThrow(IllegalStateException::new)
                                         .userRootFolder();
        fileId = FileId.createId(FIRST_FILE_NAME, folderId);
        uploadFileCommand = new UploadFile(token, FIRST_FILE_NAME, fileContents, folderId);
    }

    @Test
    @DisplayName("upload file for authenticated user successfully")
    void uploadFileForAuthenticatedUser() {
        FileUploaded fileUploaded = fileUpload.handle(uploadFileCommand);
        assertThat(fileUploaded).isNotNull();

        Optional<FileContentRecord> fileRecord = fileSystem.read(fileUploaded.fileId());
        assertThat(fileRecord).isPresent();

        fileRecord.ifPresent(record -> {
            FileId recordedFileId = record.id();
            assertThat(recordedFileId).isEqualTo(fileId);
        });

        Optional<FileMetadataRecord> fileInfoRecord = fileMetaDataStorage().read(
                fileUploaded.fileId());
        assertThat(fileInfoRecord).isPresent();

        fileInfoRecord.ifPresent(record -> {
            FileId recordedFileId = record.id();
            assertThat(recordedFileId).isEqualTo(fileId);
        });
    }

    @Test
    @DisplayName("fail if the user attempts to upload the same file")
    void failIfUploadTheSameFile() {
        FileUploaded fileUploaded = fileUpload.handle(uploadFileCommand);
        assertThat(fileUploaded).isNotNull();

        assertThrows(UploadFileException.class,
                     () -> fileUpload.handle(uploadFileCommand));
    }

    @Test
    @DisplayName("fail if the file does not exist in the file system but its metadata is present")
    void failIfFileInformationIsNotPresent() {
        FileUploaded fileUploaded = fileUpload.handle(uploadFileCommand);
        assertThat(fileUploaded).isNotNull();

        fileSystem.clear();
        Optional<FileContentRecord> record = fileSystem.read(fileUploaded.fileId());
        assertThat(record).isEmpty();

        assertThrows(UploadFileException.class,
                     () -> fileUpload.handle(uploadFileCommand));
    }
}
