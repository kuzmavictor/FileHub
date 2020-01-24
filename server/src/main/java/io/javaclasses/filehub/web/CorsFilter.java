package io.javaclasses.filehub.web;

import spark.Filter;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

/**
 * Enables the CORS requests for all Spark endpoints.
 *
 * <p>Allows handling the requests obtained from other domains without rejection.
 */
public final class CorsFilter {

    /**
     * Contains headers needed to allow the handling of CORS requests.
     */
    private final Map<String, String> corsHeaders = new HashMap<>();

    /**
     * Initializes a {@code CorsFilter} instance.
     *
     * <p>Adds necessary CORS headers.
     */
    public CorsFilter() {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers",
                        "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    /**
     * Applies the filter to all Spark responses.
     */
    public void apply() {
        Filter filter = (request, response) -> corsHeaders.forEach(response::header);
        Spark.after(filter);
    }
}
