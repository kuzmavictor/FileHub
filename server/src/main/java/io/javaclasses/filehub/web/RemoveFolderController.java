package io.javaclasses.filehub.web;

import io.javaclasses.filehub.ServerEnvironment;
import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.file.FolderRemoving;
import io.javaclasses.filehub.file.RemoveFolder;
import io.javaclasses.filehub.security.ProcessExecutionNotAllowedException;
import io.javaclasses.filehub.user.Token;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static com.google.common.base.Preconditions.checkNotNull;
import static spark.Spark.delete;

/**
 * A REST endpoint which is responsible for the folder removing.
 */
public final class RemoveFolderController extends RestController {

    private final Logger logger = LoggerFactory.getLogger(RemoveFolderController.class);
    private final FolderRemoving folderRemoving;

    /**
     * Creates a {@code RemoveFolderController} instance.
     *
     * @param environment
     *         a server environment
     */
    public RemoveFolderController(ServerEnvironment environment) {
        checkNotNull(environment);

        folderRemoving = new FolderRemoving(environment.tokenStorage(),
                                            environment.folderMetadataStorage());
        configure();
    }

    @Override
    protected void configure() {
        delete("/folder/:folderId", this::handle);
    }

    @Override
    protected Object handle(Request request, Response response) {
        String folderIdentifierValue = request.params(":folderId");
        FolderId folderId = FolderId.of(folderIdentifierValue);
        String tokenValue = request.headers("security-token");
        Token token = Token.of(tokenValue);
        try {
            RemoveFolder removeFolder = new RemoveFolder(token, folderId);
            folderRemoving.handle(removeFolder);

            response.status(HttpStatus.OK_200);
            return "OK";
        } catch (IllegalStateException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.BAD_REQUEST_400);

            return "{\"message\":\"" + e.getMessage() + "\"}";
        } catch (ProcessExecutionNotAllowedException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.FORBIDDEN_403);

            return "{\"message\":\"" + e.getMessage() + "\"}";
        }
    }
}
