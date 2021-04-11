package com.ee_java.team_project.util;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
     * such as >, >=, <, and <=. If no operator is specified, the values are compared exactly.
     * @param querySearch The user-entered query to search with.
     * @param actualValue The actual value to compare against.
     * @return Whether or not the query search compares to the actual value.
     */
    private static boolean compareValue(String querySearch, String actualValue) {
        boolean matches;
        String queryValue = querySearch.replaceAll("(^<=?|>=?)|(<=?|>=?$)", "");
        String operator = querySearch.replaceAll("[0-9]+", "");
        // Check if actual value contains any of the values from the entered query search
        if (querySearch.contains("|")) {
            operator = "|";
            String[] values = querySearch.split("\\|");
            matches = false;
            // Perform comparison for every entered value of or operator
            for (int index = 0; index < values.length; index++) {
                String value = values[index].trim();
                // Verify that value is not empty
                if (!value.equals("")) {
                    matches = compareWithOperatorValue(value, actualValue, operator);
                    // Break out of loop when at least one expected value matches actual value
                    if (matches) {
                        break;
                    }
                }
            }
        // Check if actual value is greater than entered value
        } else if (querySearch.matches("(^>[0-9]+$)")) {
            matches = compareWithOperatorValue(actualValue, queryValue, operator);
        // Check if entered value is less than actual value
        } else if (querySearch.matches("(^[0-9]+<$)")) {
            matches = compareWithOperatorValue(queryValue, actualValue, operator);
        // Check if actual value is less than entered value
        } else if (querySearch.matches("(^<[0-9]+$)")) {
            matches = compareWithOperatorValue(actualValue, queryValue, operator);
        // Check if entered value is greater than actual value
        } else if (querySearch.matches("(^[0-9]+>$)")) {
            matches = compareWithOperatorValue(queryValue, actualValue, operator);
        // Check if actual value is greater than or equal to entered value
        } else if (querySearch.matches("(^>=[0-9]+$)")) {
            matches = compareWithOperatorValue(actualValue, queryValue, operator);
        // Check if entered value is less than or equal to actual value
        } else if (querySearch.matches("(^[0-9]+<=$)")) {
            matches = compareWithOperatorValue(queryValue, actualValue, operator);
        // Check if actual value is less than or equal to entered value
        } else if (querySearch.matches("(^<=[0-9]+$)")) {
            matches = compareWithOperatorValue(actualValue, queryValue, operator);
        // Check if entered value is greater than or equal to actual value
        } else if (querySearch.matches("(^[0-9]+>=$)")) {
            matches = compareWithOperatorValue(queryValue, actualValue, operator);
        } else {
            matches = (querySearch.equals(actualValue));
        }
        return matches;
    }

    /**
     * Compares two values against each other using a provided comparison operator. Valid operators are <, <=, >, >=,
     * and |. Numeric comparison operators to integers in the process of comparing and will return false
     * if they cannot be converted.
     * @param value1 The first value to compare.
     * @param value2 The second value to compare.
     * @param operator The operator to compare with (<, <=, >, >=).
     * @return How the first value compares to the second value.
     */
    private static boolean compareWithOperatorValue(String value1, String value2, String operator) {
        boolean result = false;
        //if OR operator then check if contains and out
        if (operator.equals("|")) {
            result = value1.contains(value2);
        } else {
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
        }

        return result;
    }


}
