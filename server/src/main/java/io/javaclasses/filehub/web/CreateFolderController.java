package io.javaclasses.filehub.web;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import io.javaclasses.filehub.ServerEnvironment;
import io.javaclasses.filehub.file.CreateFolder;
import io.javaclasses.filehub.file.FolderCreation;
import io.javaclasses.filehub.file.FolderCreationException;
import io.javaclasses.filehub.file.FolderId;
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
 * A REST endpoint which is responsible for folder creation.
 */
@SuppressWarnings("UnstableApiUsage") //The Guava's implementation seems stable.
public final class CreateFolderController extends RestController {

    private static final Logger logger = LoggerFactory.getLogger(FolderContentController.class);

    private final FolderCreation folderCreation;

    /**
     * Creates a {@code CreateFolderController} instance.
     *
     * @param environment
     *         a server environment
     */
    public CreateFolderController(ServerEnvironment environment) {
        checkNotNull(environment);
        folderCreation = new FolderCreation(environment.folderMetadataStorage(),
                                            environment.tokenStorage());
        configure();
    }

    @Override
    protected void configure() {
        post("/folder/:folderId/new-folder", "multipart/form-data", this::handle);
    }

    @Override
    protected Object handle(Request request, Response response) {
        enableMultipartDir(request);
        response.type("application/json");

        try {
            CreateFolder createFolder = createCommand(request);
            folderCreation.handle(createFolder);

            return "{\"message\": \"OK\"}";
        } catch (BadRequestException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.BAD_REQUEST_400);

            return "{\"message\":\"" + e.getMessage() + "\"}";
        } catch (FolderCreationException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.FORBIDDEN_403);

            return "{\"message\": \"" + e.getMessage() + "\"}";
        }
    }

    /**
     * Creates a {@code CreateFolder} command.
     *
     * @param request
     *         an object that provides information about the HTTP request
     * @return the {@code CreateFolder} command
     * @throws BadRequestException
     *         if an I/O error occurred during the reading of the requested {@code Part}
     *         or this request is not of {@code multipart/form-data} type
     */
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    private static CreateFolder createCommand(Request request) throws BadRequestException {

        String tokenValue = request.headers("security-token");
        Token token = Token.of(tokenValue);
        FolderId parentFolder = FolderId.of(request.params(":folderId"));
        InputStream folderNameStream = readFormDataFieldValue(request, "folderName");

        try {
            String folderName =
                    CharStreams.toString(new InputStreamReader(folderNameStream, Charsets.UTF_8));
            return new CreateFolder(token, folderName, parentFolder);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new BadRequestException("Failed to read request data.");
        }
    }
}
