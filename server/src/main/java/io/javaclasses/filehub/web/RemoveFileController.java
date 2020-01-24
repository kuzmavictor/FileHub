package io.javaclasses.filehub.web;

import io.javaclasses.filehub.ServerEnvironment;
import io.javaclasses.filehub.file.FileId;
import io.javaclasses.filehub.file.FileRemoving;
import io.javaclasses.filehub.file.RemoveFile;
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
 * A REST endpoint which is responsible for the file removing.
 */
public final class RemoveFileController extends RestController {

    private final Logger logger = LoggerFactory.getLogger(RemoveFileController.class);
    private final FileRemoving fileRemoving;

    /**
     * Creates a {@code RemoveFileController} instance.
     *
     * @param environment
     *         a server environment
     */
    public RemoveFileController(ServerEnvironment environment) {
        checkNotNull(environment);

        fileRemoving = new FileRemoving(environment.tokenStorage(),
                                        environment.fileMetadataStorage(),
                                        environment.fileStorage());
        configure();
    }

    @Override
    protected void configure() {
        delete("/file/:fileId", this::handle);
    }

    @Override
    protected Object handle(Request request, Response response) {
        String tokenValue = request.headers("security-token");
        Token token = Token.of(tokenValue);
        FileId fileId = FileId.of(request.params(":fileId"));

        try {
            RemoveFile removeFile = new RemoveFile(token, fileId);
            fileRemoving.handle(removeFile);

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
