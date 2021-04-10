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
        // TODO: Get JSON from session attribute
        HttpSession session = request.getSession();

        String json = (String)session.getAttribute("json");

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
        // TODO: Get JSON from session attribute
        HttpSession session = request.getSession();

        String json = (String)session.getAttribute("json");
        logger.debug(json);

        String finalJson = queryJson(json, parameters).toString();

        return Response.status(200).entity(finalJson).build();
    }

    /**
     * Returns JSON elements based on the JSON to filter and a list of columns and values to compare.
     * @param body The POST request body content.
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

        Map<String, String> parametersCopy = new HashMap<>(parameters);

        // remove empty queryParam
        for (Map.Entry<String,String> param : parameters.entrySet()) {
            //System.out.printf("{%s:%s}%n", param.getKey(), param.getValue());
            String queryVal = param.getValue();
            if (queryVal.isEmpty()) {
                parametersCopy.remove(param.getKey());
            }
        }

        logger.debug("New Parameters: {}", parametersCopy);

        JsonElement element = JsonParser.parseString(json);
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            // Retrieve every JSON element from the JSON array
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement currentElement = jsonArray.get(i);
                if (currentElement.isJsonObject()) {
                    JsonObject currentObject = currentElement.getAsJsonObject();

                    boolean allMatches = true;
                    for (Map.Entry<String, String> entry : parametersCopy.entrySet()) {
                        String column = entry.getKey();
                        String value = entry.getValue();
                        logger.debug("Looking for column '{}' in {}", column, currentObject);
                        if (currentObject.has(column)) {
                            String foundValue = currentObject.get(column).toString();
                            foundValue = foundValue.replaceAll("^\"|\"$", "");
                            logger.debug("Comparing value '{}' to '{}' in column '{}'", value, foundValue, column);
                            if (!foundValue.equals(value)) {
                                allMatches = false;
                            }
                        }
                    }
                    if (allMatches) {
                        results.add(currentObject);
                    }
                }
            }
        }

        logger.debug("Logging the result here {} ", results);
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

    /**
     * Converts a list of key strings and a list of value strings into a map of string keys and values. If there are
     * more keys than values, an empty string is substituted in. If there are more values than keys, only the keys that
     * exist are mapped.
     * @param keys The list of key strings.
     * @param values The list of value strings.
     * @return The map of string keys and values from both associated lists.
     */
    private Map<String, String> mapLists(List<String> keys, List<String> values) {
        Map<String, String> map = new HashMap<>();
        for (int index = 0; index < keys.size(); index++) {
            String key = keys.get(index);
            String value = "";
            if (index < values.size()) {
                value = values.get(index);
            }
            map.put(key, value);
        }
        return map;
    }

}
