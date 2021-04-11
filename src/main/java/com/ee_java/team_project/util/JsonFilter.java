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

                                // Verify that the given column exists on the object as a property
                                if (currentObject.has(column)) {
                                    String foundValue = currentObject.get(column).toString().replaceAll("^\"|\"$", "");
                                    // Break out of loop if a single value does not match
                                    if (!compareValue(value, foundValue)) {
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

    private static boolean compareValue(String querySearch, String actualValue) {
        boolean matches;
        String queryValue = querySearch.replaceAll("(^<=?|>=?)|(<=?|>=?$)", "");
        String operator = querySearch.replaceAll("[0-9]+", "");
        logger.debug("Query value: {}", queryValue);
        logger.debug("Operator: {}", operator);
        // Check if query value is greater than entered value
        if (querySearch.matches("(^>[0-9]+$)")) {
            matches = compareWithOperatorValue(actualValue, queryValue, operator);
            // Check if query value value is less than actual value
        } else if (querySearch.matches("(^[0-9]+<$)")) {
            matches = compareWithOperatorValue(queryValue, actualValue, operator);
            // Check if query value is greater than entered value
        } else if (querySearch.matches("(^<[0-9]+$)")) {
            matches = compareWithOperatorValue(actualValue, queryValue, operator);
            // Check if query value is greater than entered value
        } else if (querySearch.matches("(^[0-9]+>$)")) {
            matches = compareWithOperatorValue(queryValue, actualValue, operator);
            // Check if query value is greater than entered value
        } else if (querySearch.matches("(^>=[0-9]+$)")) {
            matches = compareWithOperatorValue(actualValue, queryValue, operator);
            // Check if query value is less than actual value
        } else if (querySearch.matches("(^[0-9]+<=$)")) {
            matches = compareWithOperatorValue(queryValue, actualValue, operator);
            // Check if query value is less than actual value
        } else if (querySearch.matches("(^<=[0-9]+$)")) {
            matches = compareWithOperatorValue(actualValue, queryValue, operator);
            // Check if value exactly matches search query
        } else if (querySearch.matches("(^[0-9]+>=$)")) {
            matches = compareWithOperatorValue(queryValue, actualValue, operator);
            // Check if value exactly matches search query
        } else {
            matches = (querySearch.equals(actualValue));
        }
        return matches;
    }

    private static boolean compareWithOperatorValue(String value1, String value2, String operator) {
        boolean result = false;
        try {
            int number1 = Integer.parseInt(value1);
            int number2 = Integer.parseInt(value2);
            if (operator.equals(">")) {
                result = (number1 > number2);
            } else if (operator.equals("<")) {
                result = (number1 < number2);
            } else if (operator.equals(">=")) {
                result = (number1 >= number2);
            } else if (operator.equals("<=")) {
                result = (number1 <= number2);
            }
        } catch (NumberFormatException exception) {
            logger.error("Invalid value provided while converting to integer!", exception);
        } catch (Exception exception) {
            logger.error("Unknown exception occurred while parsing values to integer!", exception);
        }
        return result;
    }
}
