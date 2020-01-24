package io.javaclasses.filehub;

import io.javaclasses.filehub.web.CorsFilter;
import io.javaclasses.filehub.web.CreateFolderController;
import io.javaclasses.filehub.web.FolderContentController;
import io.javaclasses.filehub.web.GetCurrentFolderController;
import io.javaclasses.filehub.web.LoginController;
import io.javaclasses.filehub.web.RegistrationController;
import io.javaclasses.filehub.web.RemoveFileController;
import io.javaclasses.filehub.web.RemoveFolderController;
import io.javaclasses.filehub.web.UploadFileController;

import static spark.Spark.options;

/**
 * An entry point into the FileHub application.
 *
 * <p>Initializes the REST-endpoints and
 * configures the necessary environment of the FileHub application.
 */
public class Application {

    private final ServerEnvironment serverEnvironment;

    /**
     * Creates an {@code Application} instance.
     */
    private Application() {
        serverEnvironment = ServerEnvironment.instance();
    }

    /**
     * Initializes the entry point to the FileHub application and creates the application context.
     *
     * @param args
     *         the command line arguments
     */
    public static void main(String[] args) {
        Application appFileHub = new Application();
        appFileHub.initApplicationContext();
    }

    private void initApplicationContext() {
        enableCors();

        new LoginController(serverEnvironment);
        new RegistrationController(serverEnvironment);
        new FolderContentController(serverEnvironment);
        new CreateFolderController(serverEnvironment);
        new UploadFileController(serverEnvironment);
        new RemoveFileController(serverEnvironment);
        new RemoveFolderController(serverEnvironment);
        new GetCurrentFolderController(serverEnvironment);
    }

    private static void enableCors() {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.apply();

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
    }
}
