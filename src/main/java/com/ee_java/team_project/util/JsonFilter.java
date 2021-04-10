package com.ee_java.team_project.util;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a variety of methods to aid in filtering JSON data.
 */
public class JsonFilter {
    private static final Logger logger = LogManager.getLogger();

    /**
     * Searches a given JSON string with a provided map of query parameters and returns a JsonArray of matching results.
     * @param json The String JSON element.
     * @param parameters The map of query parameters to search with.
     * @return A JsonArray containing the results of the query.
     */
    public static JsonArray queryJson(String json, Map<String, String> parameters) {
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

                                //logger.debug("Querying for column {} in {}", column, currentObject);
                                // Verify that the given column exists on the object as a property
                                if (currentObject.has(column)) {
                                    String foundValue = currentObject.get(column).toString().replaceAll("^\"|\"$", "");
                                    //logger.debug("Comparing value {} to expected value {}", foundValue, value);
                                    // Break out of loop if a single value does not match
                                    if (value.matches("(^>[0-9]+$)|(^[0-9]+<$)")) {
                                        // Greater than search initiated
                                        value = value.replaceAll("^>|<$", "");
                                        int foundValueInt = Integer.parseInt(foundValue);
                                        int valueInt = Integer.parseInt(value);
                                        if (foundValueInt <= valueInt) {
                                            logger.debug("if found value is greater than valueInt + valueInt {} {}", foundValueInt, valueInt);
                                            allMatches = false;
                                            break;
                                        }
                                    } else if (value.matches("(^<[0-9]+$)|(^[0-9]+>$)")) {
                                        value = value.replaceAll("^<|>$", "");
                                        int foundValueInt = Integer.parseInt(foundValue);
                                        int valueInt = Integer.parseInt(value);
                                        // Less than search initiated
                                        if (foundValueInt >= valueInt) {
                                            logger.debug("if found value is less than valueInt + valueInt {} {}", foundValueInt, valueInt);
                                            allMatches = false;
                                            break;
                                        }
                                    } else if (!foundValue.equals(value)) {
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
}
