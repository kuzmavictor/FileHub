package io.javaclasses.filehub.web;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import io.javaclasses.filehub.ServerEnvironment;
import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.file.FolderMetadataRecord;
import io.javaclasses.filehub.user.Authentication;
import io.javaclasses.filehub.user.AuthenticationException;
import io.javaclasses.filehub.user.LogIn;
import io.javaclasses.filehub.user.Token;
import io.javaclasses.filehub.user.TokenRecord;
import io.javaclasses.filehub.user.UserId;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static spark.Spark.post;

/**
 * A REST endpoint which is responsible for user authentication.
 */
@SuppressWarnings("UnstableApiUsage") // Guava's implementation seems stable.
public final class LoginController extends RestController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final Authentication authentication;
    private final ServerEnvironment serverEnvironment;

    /**
     * Creates a {@code LoginController} instance.
     *
     * @param environment
     *         a server environment
     */
    public LoginController(ServerEnvironment environment) {
        serverEnvironment = checkNotNull(environment);
        authentication = new Authentication(serverEnvironment.userStorage(),
                                            environment.tokenStorage());
        configure();
    }

    /**
     * Creates a {@code LogIn} command.
     *
     * @param request
     *         an object that provides information about the HTTP request
     * @return the {@code LogIn} command
     * @throws BadRequestException
     *         if an I/O error occurred during the reading of the requested {@code Part}
     *         or this request is not of {@code multipart/form-data} type
     */
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    // The implementation is hidden.
    private static LogIn createCommand(Request request) throws BadRequestException {
        InputStream loginStream = readFormDataFieldValue(request, "login");
        InputStream passwordStream = readFormDataFieldValue(request, "password");

        try {
            String login = CharStreams.toString(new InputStreamReader(loginStream, Charsets.UTF_8));
            String password =
                    CharStreams.toString(new InputStreamReader(passwordStream, Charsets.UTF_8));

            return new LogIn(login, password);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new BadRequestException("Failed to read request data.");
        }
    }

    @Override
    protected void configure() {
        post("/login", "multipart/form-data", this::handle);
    }

    @Override
    protected Object handle(Request request, Response response) {
        enableMultipartDir(request);
        response.type("application/json");

        try {
            LogIn command = createCommand(request);
            Token userToken = authentication.handle(command);
            response.status(HttpStatus.OK_200);

            FolderMetadataRecord folderMetadataRecord = getRootFolder(userToken);

            Map<String, Object> map = new HashMap<>();
            map.put("token", userToken);
            map.put("rootFolder", folderMetadataRecord);

            return new Gson().toJson(map);
        } catch (BadRequestException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.BAD_REQUEST_400);

            return "{\"message\":\"" + e.getMessage() + "\"}";
        } catch (AuthenticationException | IllegalArgumentException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.UNAUTHORIZED_401);

            return "{\"message\": \"" + e.getMessage() + "\"}";
        }
    }

    private FolderMetadataRecord getRootFolder(Token userToken) {
        Optional<TokenRecord> tokenRecord = serverEnvironment.tokenStorage()
                                                             .read(userToken);
        checkArgument(tokenRecord.isPresent());

        UserId userId = tokenRecord.get()
                                   .userId();
        FolderId folderId = FolderId.createRootFolderId(userId.value());

        Optional<FolderMetadataRecord> folderMetadataRecord =
                serverEnvironment.folderMetadataStorage()
                                 .read(folderId);

        checkArgument(folderMetadataRecord.isPresent());
        return folderMetadataRecord.get();
    }
}
