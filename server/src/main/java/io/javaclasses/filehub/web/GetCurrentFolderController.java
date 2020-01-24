package io.javaclasses.filehub.web;

import com.google.gson.Gson;
import io.javaclasses.filehub.ServerEnvironment;
import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.file.FolderMetadataRecord;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static spark.Spark.get;

/**
 * A REST endpoint which is responsible for obtaining the current folder metadata.
 */
public final class GetCurrentFolderController extends RestController {

    private final FolderMetadataStorage folderMetadataStorage;

    /**
     * Creates a {@code GetCurrentFolderController} instance.
     *
     * @param environment
     *         a server environment
     */
    public GetCurrentFolderController(ServerEnvironment environment) {
        checkNotNull(environment);

        folderMetadataStorage = environment.folderMetadataStorage();
        configure();
    }

    @Override
    protected void configure() {
        get("/folder/:folderId", this::handle);
    }

    @Override
    protected Object handle(Request request, Response response) {
        String folderIdentifierValue = request.params(":folderId");
        FolderId folderId = FolderId.of(folderIdentifierValue);
        Map<String, Object> responseMap = new HashMap<>();
        Gson gson = new Gson();

        try {
            FolderMetadataRecord folderMetadataRecord =
                    folderMetadataStorage.read(folderId)
                                         .orElseThrow(IllegalStateException::new);
            responseMap.put("folder", folderMetadataRecord);

            return gson.toJson(responseMap);
        } catch (IllegalStateException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseMap.put("message", "Cannot obtain the requested folder.");

            return gson.toJson(responseMap);
        }
    }
}
