package com.ee_java.team_project.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.HashMap;
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
    public Response getCountJson(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> parameters = uriInfo.getQueryParameters();
        // TODO: Get JSON from session attribute
        String json = "[]";

        int count = queryJson(json, parameters).size();
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
    public Response getSearchJson(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> parameters = uriInfo.getQueryParameters();
        // TODO: Get JSON from session attribute
        String json = "[]";

        String finalJson = queryJson(json, parameters).toString();

        return Response.status(200).entity(finalJson).build();
    }
    
    /**
     * Returns JSON elements based on the query type and additional parameters provided.
     *
     * @param form The form element consisting of the parameters.
     * @return The response
     */
    @POST
    @Path("/search")
    @Produces("application/json")
    public Response postSearchJson(Form form) {
        MultivaluedMap<String, String> parameters = form.asMap();
        // TODO: Add error handling if no JSON parameter is passed (null)
        String json = parameters.getFirst("json");

        // Perform query with parameters
        String finalJson = queryJson(json, parameters).toString();

        return Response.status(200).entity(finalJson).build();
    }

    /**
     * Searches a given JSON string with a provided map of query parameters and returns a JsonArray of matching results.
     * @param json The String JSON element.
     * @param multivaluedParameters The MultivaluedMap of query parameters to search with.
     * @return A JsonArray containing the results of the query.
     */
    private JsonArray queryJson(String json, MultivaluedMap<String, String> multivaluedParameters) {
        Map<String, String> parameters = prepareParameters(multivaluedParameters);
        JsonArray results = new JsonArray();
        String column1 = parameters.get("column1");
        String value1 = parameters.get("value1");

        logger.debug("Querying JSON in column '{}' for value '{}'", column1, value1);
        JsonElement element = JsonParser.parseString(json);
        if (element.isJsonArray()) {
            logger.debug("Element is an array");
            JsonArray jsonArray = element.getAsJsonArray();
            // Retrieve every JSON element from the JSON array
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement currentElement = jsonArray.get(i);
                if (currentElement.isJsonObject()) {
                    JsonObject currentObject = currentElement.getAsJsonObject();
                    JsonElement foundValue = currentObject.get(column1);
                    if (foundValue != null) {
                        if (foundValue.toString().equals(value1)) {
                            results.add(currentObject);
                        }
                    }
                }
            }
        }

        logger.debug("Logging the result here {} ", results);
        return results;
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
