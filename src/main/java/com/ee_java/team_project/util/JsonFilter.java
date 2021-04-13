package com.ee_java.team_project.util;

import com.google.gson.*;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

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
            Map<String, String> parametersCopy = removeEmptyParameters(parameters);

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
                                String value = entry.getValue().replaceAll("^\"|\"$", "");
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
               // logger.error(String.format("Error occurred while parsing JSON %s", json), exception);
            } catch (Exception exception) {
               // logger.error(String.format("Unknown exception while parsing JSON %s", json), exception);
            }
        }

        return results;
    }

    /**
     * Creates a new parameters mapping without empty parameter values.
     * @param parameters The initial parameters to parse.
     * @return A new parameter mapping without empty-valued parameters.
     */
    private static Map<String, String> removeEmptyParameters(Map<String, String> parameters) {
        Map<String, String> parametersCopy = new HashMap<>(parameters);
        for (Map.Entry<String,String> param : parameters.entrySet()) {
            String queryVal = param.getValue();
            if (queryVal.isEmpty()) {
                parametersCopy.remove(param.getKey());
            }
        }
        return parametersCopy;
    }

    /**
     * Compares the user-entered query to the actual value. The query search can contain numerical comparison operators
     * such as >, >=, <, and <=. If no operator is specified, the values are compared exactly. Regex can also be entered
     * by putting Java-compatible regex inside forward slashes (/).
     * @param querySearch The user-entered query to search with.
     * @param actualValue The actual value to compare against.
     * @return Whether or not the query search compares to the actual value.
     */
    private static boolean compareValue(String querySearch, String actualValue) {
        boolean matches;
        String operator = "=";

        // Check if entered value contains regex and process actual value using it
        if (querySearch.matches("^\\/.+\\/$")) {
            String regex = querySearch.replaceAll("^\\/|\\/$", "");
            matches = actualValue.matches(regex);

            // Check if actual value contains any of the values from the entered query search (OR operator)
        } else if (querySearch.contains("|")) {
            operator = "=";

            String[] queryValues = querySearch.split("|");
            matches = false;
            for (int index = 0; index < queryValues.length; index++) {
                String value = queryValues[index].trim();
                if (!value.isEmpty()) {
                    if (compareWithOperatorValue(value, actualValue, operator)) {
                        matches = true;
                        break;
                    }
                }
            }

            // Invert answer if using NOT ANY operator
            if (querySearch.startsWith("!")) matches = !matches;

        // Check if entered value is not equal to actual value
        } else if (querySearch.startsWith("!")) {
            operator = "!=";
            String queryValue = querySearch.replaceAll("^!", "");
            matches = compareWithOperatorValue(queryValue, actualValue, operator);

        // Compare values using numeric comparison (LESS THAN, GREATER THAN, etc.)
        } else if (querySearch.startsWith("$")) {
            operator = "$";
            matches = compareWithOperatorValue(querySearch, actualValue, operator);

            // Compare values using numeric comparison (LESS THAN, GREATER THAN, etc.)
        }else if (querySearch.matches("[><]=?[0-9]+")) {
            String queryValue = querySearch.replaceAll("[><]=?", "");
            operator = querySearch.replaceAll("[0-9]+", "");
            matches = compareWithOperatorValue(actualValue, queryValue, operator);

        } else {
            matches = compareWithOperatorValue(actualValue, querySearch, operator);
        }

        return matches;
    }

    /**
     * Compares two values against each other using a provided comparison operator. Valid operators are <, <=, >, >=, =,
     * and !=. Numeric comparison operators convert String values to integers and will return false if they cannot be
     * converted.
     * @param userInput The first value to compare.
     * @param parsedJsonValue The second value to compare.
     * @param operator The operator to compare with (<, <=, >, >=).
     * @return How the first value compares to the second value.
     */
    private static boolean compareWithOperatorValue(String userInput, String parsedJsonValue, String operator) {
        boolean result = false;
        // If OR operator then check contained value
        if (operator.equals("|")) {
            result = userInput.contains(parsedJsonValue);
        // If NOT EQUAL operator then
        } else if (operator.equals("!=")) {
            result = !userInput.equals(parsedJsonValue);
            // If equals operator then check if contains value
        } else if (operator.equals("=")) {
            result = userInput.equals(parsedJsonValue);
            // uses jsonPath if parameter-column consist of JSON
        } else if (operator.equals("$")) {
            if (parsedJsonValue.isEmpty()) {
                return false;
            }
            List<String> updatedColumnJsonList = new ArrayList<>();
            parsedJsonValue = parsedJsonValue.replaceAll("\"\"", "\"")
                    .replaceFirst("^\"","")
                    .replaceFirst("\"$", "");
            updatedColumnJsonList = (JsonPath.read(parsedJsonValue, userInput));
            result = updatedColumnJsonList.size() > 0;
        } else {
            // Compare using numeric operators
            try {
                int number1 = Integer.parseInt(userInput);
                int number2 = Integer.parseInt(parsedJsonValue);
                if (operator.equals(">")) {
                    result = number1 > number2;
                } else if (operator.equals("<")) {
                    result = number1 < number2;
                } else if (operator.equals(">=")) {
                    result = number1 >= number2;
                } else if (operator.equals("<=")) {
                    result = number1 <= number2;
                }
            } catch (NumberFormatException exception) {
                logger.error("Invalid value provided while converting to integer!", exception);
            } catch (Exception exception) {
                logger.error("Unknown exception occurred while parsing values to integer!", exception);
            }
        }

        return result;
    }
}