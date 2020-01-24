package io.javaclasses.filehub.web;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import io.javaclasses.filehub.ServerEnvironment;
import io.javaclasses.filehub.user.Avatar;
import io.javaclasses.filehub.user.RegisterUser;
import io.javaclasses.filehub.user.Registration;
import io.javaclasses.filehub.user.RegistrationException;
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
 * A REST endpoint which is responsible for user registration.
 */
@SuppressWarnings("UnstableApiUsage") // Guava's implementation seems stable.
public final class RegistrationController extends RestController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final Registration registration;

    /**
     * Creates a {@code RegistrationController} instance.
     *
     * @param environment
     *         a server environment
     */
    public RegistrationController(ServerEnvironment environment) {
        checkNotNull(environment);
        registration = new Registration(environment.tokenStorage(), environment.userStorage(),
                                        environment.folderMetadataStorage());
        configure();
    }

    @Override
    protected void configure() {
        post("/register", "multipart/form-data", this::handle);
    }

    @Override
    protected Object handle(Request request, Response response) {
        enableMultipartDir(request);
        response.type("application/json");

        try {
            RegisterUser command = createCommand(request);
            registration.handle(command);
            response.status(HttpStatus.OK_200);

            return "{\"message\": \"The user successfully registered.\"}";
        } catch (BadRequestException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.BAD_REQUEST_400);

            return "{\"message\":\"" + e.getMessage() + "\"}";
        } catch (RegistrationException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            response.status(HttpStatus.UNAUTHORIZED_401);

            return "{\"message\": \"" + e.getMessage() + "\"}";
        }
    }

    /**
     * Creates a {@code RegisterUser} command.
     *
     * @param request
     *         an object that provides information about the HTTP request
     * @return the {@code RegisterUser} command
     * @throws BadRequestException
     *         if an I/O error occurred during the reading of the requested {@code Part}
     *         or this request is not of {@code multipart/form-data} type
     */
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    // The implementation is hidden.
    private static RegisterUser createCommand(Request request) throws BadRequestException {
        InputStream loginStream = readFormDataFieldValue(request, "login");
        InputStream passwordStream = readFormDataFieldValue(request, "password");
        InputStream avatarContentStream = readFormDataFieldValue(request, "avatar");

        try {
            String login = CharStreams.toString(
                    new InputStreamReader(loginStream, Charsets.UTF_8));
            String password = CharStreams.toString(
                    new InputStreamReader(passwordStream, Charsets.UTF_8));
            byte[] avatarContents = ByteStreams.toByteArray(avatarContentStream);
            Avatar avatar = Avatar.createAvatar(avatarContents);

            return new RegisterUser(login, password, avatar);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new BadRequestException("Failed to read request data.");
        }
    }
}
