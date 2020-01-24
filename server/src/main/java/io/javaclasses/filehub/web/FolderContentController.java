package io.javaclasses.filehub.web;

import com.google.gson.Gson;
import io.javaclasses.filehub.ServerEnvironment;
import io.javaclasses.filehub.file.FolderContent;
import io.javaclasses.filehub.file.FolderContentView;
import io.javaclasses.filehub.file.QueryExecutionForbiddenException;
import io.javaclasses.filehub.file.ReadFolderContent;
import io.javaclasses.filehub.user.Token;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static spark.Spark.get;

/**
 * A REST endpoint which is responsible for obtaining the contents of a requested folder.
 */
public final class FolderContentController extends RestController {

    private static final Logger logger = LoggerFactory.getLogger(FolderContentController.class);

    private final FolderContentView folderContentView;

    /**
     * Creates a {@code FolderContentController} instance.
     *
     * @param environment
     *         a server environment
     */
    public FolderContentController(ServerEnvironment environment) {
        checkNotNull(environment);
        folderContentView = new FolderContentView(environment.tokenStorage(),
                                                  environment.fileMetadataStorage(),
                                                  environment.folderMetadataStorage());
        configure();
    }

    @Override
    protected void configure() {
        get("/folder/:folderId/files", this::handle);
    }

    @Override
    protected Object handle(Request request, Response response) {
        response.type("application/json");
        Map<String, Object> responseMap = new HashMap<>();
        String tokenValue = request.headers("security-token");
        Gson gson = new Gson();

        if (Objects.isNull(tokenValue) || tokenValue.isEmpty()) {
            response.status(HttpStatus.BAD_REQUEST_400);
            String badRequestMessage = "The token value is not present.";
            responseMap.put("message", badRequestMessage);

            return gson.toJson(responseMap);
        }

        Token token = Token.of(tokenValue);
        String folderId = request.params(":folderId");
        ReadFolderContent readFolderContentQuery = new ReadFolderContent(token, folderId);

        try {
            FolderContent folderContent = folderContentView.execute(readFolderContentQuery);
            response.status(HttpStatus.OK_200);
            responseMap.put("folderContent", folderContent);

            return gson.toJson(responseMap);
        } catch (QueryExecutionForbiddenException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.UNAUTHORIZED_401);
            responseMap.put("message", e.getMessage());

            return gson.toJson(responseMap);
        }
    }
}
