package com.ee_java.team_project.controller;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
    public Response getSearchJson(@Context UriInfo uriInfo, @Context HttpServletRequest request) {
        Map<String, String> parameters = prepareParameters(uriInfo.getQueryParameters());
        HttpSession session = request.getSession();

        String json = (String) session.getAttribute("json");

        String finalJson = queryJson(json, parameters).toString();

        return Response.status(200).entity(finalJson).build();
    }

    /**
     * Returns the number of elements found from performing a POST JSON query.
     * @param body The body content of the POST request.
     * @return The response containing the number of resulting JSON elements from the query.
     */
    @POST
    @Path("/count")
    @Produces("application/json")
    public Response postCountJson(String body) {
        Map<String, String> bodyParameters = processPostBody(body);
        String rawJson = bodyParameters.get("json");
        String queryDataJson = bodyParameters.get("search");
        Map<String, String> queryParameters = processQueryParameters(queryDataJson);

        int count = queryJson(rawJson, queryParameters).size();
        String finalJson = String.format("{\"count\": %d}", count);

        return Response.status(200).entity(finalJson).build();
    }

    /**
     * Returns JSON elements based on the JSON to filter and a list of columns and values to compare.
     * @param body The body content of the POST request.
     * @return The response containing the queried JSON element.
     */
    @POST
    @Path("/search")
    @Produces("application/json")
    public Response postSearchJson(String body) {
        Map<String, String> bodyParameters = processPostBody(body);
        String rawJson = bodyParameters.get("json");
        String queryDataJson = bodyParameters.get("search");
        Map<String, String> queryParameters = processQueryParameters(queryDataJson);

        String finalJson = queryJson(rawJson, queryParameters).toString();

        return Response.status(200).entity(finalJson).build();
    }

    /**
     * Searches a given JSON string with a provided map of query parameters and returns a JsonArray of matching results.
     * @param json The String JSON element.
     * @param parameters The map of query parameters to search with.
     * @return A JsonArray containing the results of the query.
     */
    private JsonArray queryJson(String json, Map<String, String> parameters) {
        JsonArray results = new JsonArray();

        // Verify that JSON data exists
        if (json != null) {
            Map<String, String> parametersCopy = new HashMap<>(parameters);

            // Remove empty query parameters
            for (Map.Entry<String,String> param : parameters.entrySet()) {
                String queryVal = param.getValue();
                if (queryVal.isEmpty()) {
                    parametersCopy.remove(param.getKey());
                }
            }

            logger.debug("Searching JSON with parameters {}", parametersCopy);

            // Attempt to parse provided JSON element as JSON
            try {
                JsonElement element = JsonParser.parseString(json);
                if (element.isJsonArray()) {
                    JsonArray jsonArray = element.getAsJsonArray();
                    // Retrieve every JSON element from the JSON array
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonElement currentElement = jsonArray.get(i);

                        if (currentElement.isJsonObject()) {
                            JsonObject currentObject = currentElement.getAsJsonObject();

                            // Add JSON object if all properties match
                            boolean allMatches = true;
                            for (Map.Entry<String, String> entry : parametersCopy.entrySet()) {
                                String column = entry.getKey();
                                String value = entry.getValue();

                                logger.debug("Querying for column {} in {}", column, currentObject);
                                // Verify that the given column exists on the object as a property
                                if (currentObject.has(column)) {
                                    String foundValue = currentObject.get(column).toString().replaceAll("^\"|\"$", "");
                                    logger.debug("Comparing value {} to expected value {}", foundValue, value);
                                    // Break out of loop if a single value does not match
                                    if (!foundValue.equals(value)) {
                                        allMatches = false;
                                        break;
                                    }
                                }
                            }
                            if (allMatches) {
                                results.add(currentObject);
                            }
                        }
                    }
                }
            } catch (JsonParseException exception) {
                logger.error(String.format("Error occurred while parsing JSON %s", json), exception);
            } catch (Exception exception) {
                logger.error(String.format("Unknown exception while parsing JSON %s", json), exception);
            }
        }

        return results;
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
