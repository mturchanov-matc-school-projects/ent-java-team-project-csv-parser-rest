package com.ee_java.team_project.controller;

import com.ee_java.team_project.csv_parser.CodingCompCsvUtil;
import com.ee_java.team_project.util.CSVFileWriter;
import com.ee_java.team_project.util.JsonFilter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Creates a JSON query endpoint that can handle POST requests consisting of query parameters to organize the JSON.
 * @author pjcraig
 */
@Path("/jsonqueryservice")
public class JSONQueryService {
    private static final Logger logger = LogManager.getLogger();

    /**
     * Returns the number of elements found from performing a GET JSON query.
     * @param uriInfo The URI info that contains query parameters to search with.
     * @return The response containing the queried JSON element.
     */
    @GET
    @Path("/count")
    @Produces("application/json")
    public Response getCountJson(@Context UriInfo uriInfo, @Context HttpServletRequest request) {
        Map<String, String> parameters = prepareParameters(uriInfo.getQueryParameters());
        HttpSession session = request.getSession();

        String json = (String) session.getAttribute("json");

        int count = JsonFilter.queryJson(json, parameters).size();
        String finalJson = String.format("{\"count\": %d}", count);

        return Response.status(200).entity(finalJson).build();
    }

    /**
     * Returns a JSON array response containing the JSON elements found from performing a GET JSON query.
     * @param uriInfo The URI info that contains query parameters to search with.
     * @return The response containing the queried JSON element.
     */
    @GET
    @Path("/search")
    @Produces("application/json")
    public Response getSearchJson(@Context UriInfo uriInfo, @Context HttpServletRequest request) {
        Map<String, String> parameters = prepareParameters(uriInfo.getQueryParameters());
        HttpSession session = request.getSession();

        String json = (String) session.getAttribute("json");

        String finalJson = JsonFilter.queryJson(json, parameters).toString();

        return Response.status(200).entity(finalJson).build();
    }

    /**
     * Returns the number of elements found from performing a POST JSON query.
     * @param body The body content of the POST request.
     * @return The response containing the number of resulting JSON elements from the query.
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response postCountJson(String body) {
        Map<String, String> bodyParameters = processPostBody(body);
        String rawJson = bodyParameters.get("csv");
        String queryDataJson = bodyParameters.get("search");
        Map<String, String> queryParameters = processQueryParameters(queryDataJson);

        int count = JsonFilter.queryJson(rawJson, queryParameters).size();
        String finalJson = String.format("{\"count\": %d}", count);

        return Response.status(200).entity(finalJson).build();
    }

    /**
     * Returns JSON elements based on the JSON to filter and a list of columns and values to compare.
     * @param file The file path to the.
     * @return The response containing the queried JSON element.
     */
    @POST
    @Path("/search")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response postSearchJson(@FormDataParam("file") File file, @FormDataParam("search") String search) {

        // Write file to disk
        CSVFileWriter writer = new CSVFileWriter();
        String path = writer.write(file);

        // Verify that the file was written successfully
        if (path != null) {
            CodingCompCsvUtil parser = new CodingCompCsvUtil();
            Map<List<String>, String> values = parser.readCsvFileFileWithoutPojo(path);

            // Retrieves the raw JSON text from the values map, expecting only 1 entry in the map
            Map.Entry<List<String>, String> entry = values.entrySet().iterator().next();
            String rawJson = entry.getValue();

            // Retrieve search query parameters
            Map<String, String> parameters = processQueryParameters(search);

            String finalJson = JsonFilter.queryJson(rawJson, parameters).toString();

            return Response.status(200).entity(finalJson).build();
        }

        return Response.serverError().build();
    }

    /**
     * Parses a given POST request body and returns the values as body parameters.
     * @param body The body content received from a POST request.
     * @return The processed
     */
    private Map<String, String> processPostBody(String body) {
        Map<String, String> postParameters = new HashMap<>();
        String[] splitBody = body.split("&");
        for (int bodyIndex = 0; bodyIndex < splitBody.length; bodyIndex++) {
            String fullParameter = splitBody[bodyIndex];
            String[] splitParameter = fullParameter.split("=");
            if (splitParameter.length == 2) {
                String parameter = splitParameter[0];
                String value = splitParameter[1];
                postParameters.put(parameter, value);
            }
        }
        return postParameters;
    }

    /**
     * Converts a given JSON object into a map of query parameters.
     * @param json The string JSON object.
     * @return The map of query parameters.
     */
    private Map<String, String> processQueryParameters(String json) {
        Map<String, String> queryParameters = new HashMap<>();
        if (json != null) {
            try {
                JsonElement element = JsonParser.parseString(json);
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    for (String column : object.keySet()) {
                        String value = object.get(column).toString();
                        queryParameters.put(column, value);
                    }
                }
            } catch (JsonParseException exception) {
                logger.error(String.format("Error occurred while parsing JSON %s", json), exception);
            } catch (Exception exception) {
                logger.error(String.format("Unknown exception while parsing JSON %s", json), exception);
            }
        }
        return queryParameters;
    }

    /**
     * Converts a multivalued map of parameters into a single-valued map.
     * @param queryParameters The multivalued map of query parameters.
     * @return The converted map.
     */
    private Map<String,String> prepareParameters(MultivaluedMap<String, String> queryParameters) {
        Map<String,String> parameters = new HashMap<String,String>();
        for(String str : queryParameters.keySet()){
            parameters.put(str, queryParameters.getFirst(str));
        }
        return parameters;
    }

}
