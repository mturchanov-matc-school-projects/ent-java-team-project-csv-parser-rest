package com.ee_java.team_project.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import com.google.gson.stream.JsonReader;
import javax.ws.rs.*;
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
    public Response getCountJSON(@Context UriInfo uriInfo) {
        String json = "{\"count\": 0}";
        return Response.status(200).entity(json).build();
    }

    /**
     * Returns a JSON array response containing the JSON elements found from performing a GET JSON query.
     * @param uriInfo The URI info that contains query parameters to search with.
     * @return The response containing the queried JSON element.
     */
    @GET
    @Path("/search")
    @Produces("application/json")
    public Response getSearchJSON(@Context UriInfo uriInfo) {
        String json = "[]";
        return Response.status(200).entity(json).build();
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
    public Response postSearchJSON(Form form) {
        MultivaluedMap<String, String> formParameters = form.asMap();
        Map<String, String> parameters = prepareParameters(formParameters);
        String queryType = parameters.get("queryType");
        String json = parameters.get("json");
        String finalJson = json;

        //TODO: endpoint getItemByID -> return 1 JSON object
        //TODO: endpoint count return -> count based on query params
        //TODO: endpoint getItemsBasedOnParams -> return JSON objects based on query params
        switch (queryType) {
            case "value":
                String column = parameters.get("queryColumn");
                String value = parameters.get("queryColumnValue");
                logger.debug("Querying JSON in column '{}' for value '{}'", column, value);
                JsonElement element = JsonParser.parseString(json);
                if (element.isJsonArray()) {
                    logger.debug("Element is an array");
                    JsonArray jsonArray = element.getAsJsonArray();
                    JsonArray jsonResults = new JsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonElement currentElement = jsonArray.get(i);
                        if (currentElement.isJsonObject()) {
                            JsonObject currentObject = currentElement.getAsJsonObject();
                            JsonElement foundValue = currentObject.get(column);
                            if (foundValue != null) {
                                if (foundValue.toString().equals(value)) {
                                    jsonResults.add(currentObject);
                                    logger.debug("Logging the result here {} ", jsonResults);
                                }
                            }
                        }
                    }
                };


                break;
            case "count":
                String columnTwo = parameters.get("queryColumn");
                String valueTwo = parameters.get("queryColumnValue");
                break;
            case "all":
            default:
                break;
        }



        return Response.status(200).entity(finalJson).build();
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
