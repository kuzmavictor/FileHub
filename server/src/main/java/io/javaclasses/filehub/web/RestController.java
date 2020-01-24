package io.javaclasses.filehub.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * An abstract base for all REST endpoints in the FileHub application.
 */
public abstract class RestController {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    /**
     * Configures the controller.
     */
    protected abstract void configure();

    /**
     * Handles a request that was made on the route's corresponding path.
     *
     * @param request
     *         an object that provides information about the HTTP request
     * @param response
     *         an object that provides functionality for modifying the response
     * @return the content to be set in the response
     */
    protected abstract Object handle(Request request, Response response);

    /**
     * Enables the handling of the {@code multipart/form-data} requests.
     */
    protected static void enableMultipartDir(Request request) {
        String attribute = "org.eclipse.jetty.multipartConfig";
        if (request.raw()
                   .getAttribute(attribute) == null) {
            MultipartConfigElement multipartConfigElement =
                    new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
            request.raw()
                   .setAttribute(attribute, multipartConfigElement);
        }
    }

    /**
     * Reads data from the {@code form-data} by a field name.
     *
     * @param request
     *         an object that provides information about the HTTP request
     * @param fieldName
     *         the name of the requested field
     * @return the stream of field content from the request
     * @throws BadRequestException
     *         if an I/O error occurred during the retrieval of the requested {@code Part}
     *         or this request is not of {@code multipart/form-data} type
     */
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    // The implementation is hidden.
    protected static InputStream readFormDataFieldValue(Request request, String fieldName)
            throws BadRequestException {
        try {
            Part requestPart = request.raw()
                                      .getPart(fieldName);
            if (Objects.isNull(requestPart)) {
                String exceptionMessage =
                        String.format("Cannot obtain the data from the field: '%s'.", fieldName);
                throw new BadRequestException(exceptionMessage);
            }

            return requestPart.getInputStream();
        } catch (IOException | ServletException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            String exceptionMessage =
                    String.format("Cannot obtain the data from the field: '%s'.", fieldName);
            throw new BadRequestException(exceptionMessage);
        }
    }
}
