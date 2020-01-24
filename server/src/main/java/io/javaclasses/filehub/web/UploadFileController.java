package io.javaclasses.filehub.web;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import io.javaclasses.filehub.ServerEnvironment;
import io.javaclasses.filehub.file.FileUpload;
import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.file.UploadFile;
import io.javaclasses.filehub.file.UploadFileException;
import io.javaclasses.filehub.user.Token;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.common.base.Preconditions.checkNotNull;
import static spark.Spark.post;

/**
 * A REST endpoint which is responsible for the file uploading.
 */
@SuppressWarnings("UnstableApiUsage")
public final class UploadFileController extends RestController {

    private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);
    private final FileUpload fileUpload;

    /**
     * Creates a {@code UploadFileController} instance.
     *
     * @param environment
     *         a server environment
     */
    public UploadFileController(ServerEnvironment environment) {
        checkNotNull(environment);

        fileUpload = new FileUpload(environment.tokenStorage(), environment.fileStorage(),
                                    environment.fileMetadataStorage());
        configure();
    }

    @Override
    protected void configure() {
        post("/folder/:folderId/upload", "multipart/form-data", this::handle);
    }

    @Override
    protected Object handle(Request request, Response response) {
        enableMultipartDir(request);
        response.type("application/json");
        String tokenValue = request.headers("security-token");
        Token token = Token.of(tokenValue);
        FolderId parentFolder = FolderId.of(request.params(":folderId"));

        InputStream fileContent = readFormDataFieldValue(request, "fileContent");
        InputStream fileNameStream = readFormDataFieldValue(request, "fileName");
        try {
            String fileName =
                    CharStreams.toString(new InputStreamReader(fileNameStream, Charsets.UTF_8));
            UploadFile uploadFile = new UploadFile(token, fileName, fileContent, parentFolder);

            fileUpload.handle(uploadFile);

            return "{\"message\": \"OK\"}";
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.BAD_REQUEST_400);

            return "{\"message\": \"Failed to read request data.\"}";
        } catch (UploadFileException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }

            response.status(HttpStatus.FORBIDDEN_403);
            return "{\"message\": \"" + e.getMessage() + "\"}";
        }
    }
}
