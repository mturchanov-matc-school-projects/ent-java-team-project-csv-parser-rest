package com.ee_java.team_project.util;

import com.google.gson.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonFilterTest {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private String testData;
    @BeforeEach
    void testSetUp() {
        testData = "[{\"claimId\":1,\"customerId\":140,\"closed\":\"false\",\"monthsOpen\":8},{\"claimId\":2," +
                "\"customerId\":310,\"closed\":\"true\",\"monthsOpen\":11},{\"claimId\":3,\"customerId\":353,\"closed" +
                "\":\"true\",\"monthsOpen\":4},{\"claimId\":4,\"customerId\":498,\"closed\":\"false\",\"monthsOpen" +
                "\":9},{\"claimId\":5,\"customerId\":106,\"closed\":\"false\",\"monthsOpen\":6},{\"claimId\":6," +
                "\"customerId\":266,\"closed\":\"false\",\"monthsOpen\":6},{\"claimId\":7,\"customerId\":72," +
                "\"closed\":\"true\",\"monthsOpen\":9},{\"claimId\":8,\"customerId\":102,\"closed\":\"true\"," +
                "\"monthsOpen\":12},{\"claimId\":9,\"customerId\":161,\"closed\":\"false\",\"monthsOpen\":10},{" +
                "\"claimId\":10,\"customerId\":199,\"closed\":\"false\",\"monthsOpen\":1},{\"claimId\":11," +
                "\"customerId\":145,\"closed\":\"true\",\"monthsOpen\":1},{\"claimId\":12,\"customerId\":238," +
                "\"closed\":\"true\",\"monthsOpen\":2},{\"claimId\":13,\"customerId\":463,\"closed\":\"true\"," +
                "\"monthsOpen\":8},{\"claimId\":14,\"customerId\":234,\"closed\":\"true\",\"monthsOpen\":12},{" +
                "\"claimId\":15,\"customerId\":348,\"closed\":\"true\",\"monthsOpen\":6},{\"claimId\":16," +
                "\"customerId\":328,\"closed\":\"false\",\"monthsOpen\":3},{\"claimId\":17,\"customerId\":435," +
                "\"closed\":\"true\",\"monthsOpen\":10},{\"claimId\":18,\"customerId\":85,\"closed\":\"true\"," +
                "\"monthsOpen\":3},{\"claimId\":19,\"customerId\":171,\"closed\":\"false\",\"monthsOpen\":10},{" +
                "\"claimId\":20,\"customerId\":481,\"closed\":\"false\",\"monthsOpen\":6},{\"claimId\":21," +
                "\"customerId\":106,\"closed\":\"false\",\"monthsOpen\":7},{\"claimId\":22,\"customerId\":475," +
                "\"closed\":\"false\",\"monthsOpen\":7},{\"claimId\":23,\"customerId\":263,\"closed\":\"false\"," +
                "\"monthsOpen\":9},{\"claimId\":24,\"customerId\":230,\"closed\":\"true\",\"monthsOpen\":8},{" +
                "\"claimId\":25,\"customerId\":345,\"closed\":\"false\",\"monthsOpen\":9},{\"claimId\":26," +
                "\"customerId\":424,\"closed\":\"false\",\"monthsOpen\":9},{\"claimId\":27,\"customerId\":125," +
                "\"closed\":\"true\",\"monthsOpen\":9},{\"claimId\":28,\"customerId\":42,\"closed\":\"true\"," +
                "\"monthsOpen\":3},{\"claimId\":29,\"customerId\":384,\"closed\":\"false\",\"monthsOpen\":8},{" +
                "\"claimId\":30,\"customerId\":453,\"closed\":\"true\",\"monthsOpen\":1},{\"claimId\":31," +
                "\"customerId\":218,\"closed\":\"false\",\"monthsOpen\":12},{\"claimId\":32,\"customerId\":23," +
                "\"closed\":\"false\",\"monthsOpen\":7},{\"claimId\":33,\"customerId\":98,\"closed\":\"false\"," +
                "\"monthsOpen\":8},{\"claimId\":34,\"customerId\":324,\"closed\":\"false\",\"monthsOpen\":9},{" +
                "\"claimId\":35,\"customerId\":175,\"closed\":\"true\",\"monthsOpen\":4},{\"claimId\":36," +
                "\"customerId\":76,\"closed\":\"false\",\"monthsOpen\":9},{\"claimId\":37,\"customerId\":342," +
                "\"closed\":\"false\",\"monthsOpen\":10},{\"claimId\":38,\"customerId\":9,\"closed\":\"false\"," +
                "\"monthsOpen\":8},{\"claimId\":39,\"customerId\":246,\"closed\":\"true\",\"monthsOpen\":7},{" +
                "\"claimId\":40,\"customerId\":44,\"closed\":\"false\",\"monthsOpen\":10},{\"claimId\":41," +
                "\"customerId\":54,\"closed\":\"true\",\"monthsOpen\":3},{\"claimId\":42,\"customerId\":34," +
                "\"closed\":\"false\",\"monthsOpen\":6},{\"claimId\":43,\"customerId\":399,\"closed\":\"true\"," +
                "\"monthsOpen\":7},{\"claimId\":44,\"customerId\":394,\"closed\":\"false\",\"monthsOpen\":8},{" +
                "\"claimId\":45,\"customerId\":462,\"closed\":\"false\",\"monthsOpen\":2},{\"claimId\":46," +
                "\"customerId\":6,\"closed\":\"false\",\"monthsOpen\":4},{\"claimId\":47,\"customerId\":68," +
                "\"closed\":\"false\",\"monthsOpen\":8},{\"claimId\":48,\"customerId\":308,\"closed\":\"false" +
                "\",\"monthsOpen\":12},{\"claimId\":49,\"customerId\":174,\"closed\":\"true\",\"monthsOpen" +
                "\":9},{\"claimId\":50,\"customerId\":222,\"closed\":\"false\",\"monthsOpen\":1},{\"claimId" +
                "\":51,\"customerId\":161,\"closed\":\"false\",\"monthsOpen\":7},{\"claimId\":52,\"customerId" +
                "\":109,\"closed\":\"false\",\"monthsOpen\":4}]";
    }

    //PASSING
    @Test
    void filterTestOneParameter() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("claimId", "25");
        JsonArray actualResults = JsonFilter.queryJson(testData, parameters);
        String stringResults = actualResults.toString();
        String expectedResults = "[{\"claimId\":25,\"customerId\":345,\"closed\":\"false\",\"monthsOpen\":9}]";
        assertEquals(expectedResults, stringResults);
    }

    //PASSING
    @Test
    void filterTestTwoParameter() {
        testData = "[{\"claimId\":3,\"customerId\":353,\"closed\":\"true\",\"monthsOpen\":4},{\"claimId\":225," +
                "\"customerId\":353,\"closed\":\"true\",\"monthsOpen\":12}]";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("customerId", "353");
        parameters.put("closed", "true");
        JsonArray actualResults = JsonFilter.queryJson(testData, parameters);
        String stringResults = actualResults.toString();
        String expectedResults = "[{\"claimId\":3,\"customerId\":353,\"closed\":\"true\",\"monthsOpen\":4},{" +
                "\"claimId\":225,\"customerId\":353,\"closed\":\"true\",\"monthsOpen\":12}]";
        assertEquals(expectedResults, stringResults);
    }

    //PASSING
    @Test
    void filterWithGreaterThan() {
        testData = "[{\"claimId\":994,\"customerId\":120,\"closed\":\"true\",\"monthsOpen\":7},{\"claimId\":995," +
                "\"customerId\":351,\"closed\":\"true\",\"monthsOpen\":2},{\"claimId\":996,\"customerId\":370," +
                "\"closed\":\"true\",\"monthsOpen\":5},{\"claimId\":997,\"customerId\":377,\"closed\":\"true\"," +
                "\"monthsOpen\":1},{\"claimId\":998,\"customerId\":377,\"closed\":\"false\",\"monthsOpen\":10},{" +
                "\"claimId\":999,\"customerId\":258,\"closed\":\"false\",\"monthsOpen\":7},{\"claimId\":1000," +
                "\"customerId\":207,\"closed\":\"true\",\"monthsOpen\":5}]";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("claimId", ">993");
        JsonArray actualResults = JsonFilter.queryJson(testData, parameters);
        String stringResults = actualResults.toString();
        String expectedResults = "[{\"claimId\":994,\"customerId\":120,\"closed\":\"true\",\"monthsOpen" +
                "\":7},{\"claimId\":995,\"customerId\":351,\"closed\":\"true\",\"monthsOpen\":2},{\"claimId\":996," +
                "\"customerId\":370,\"closed\":\"true\",\"monthsOpen\":5},{\"claimId\":997,\"customerId\":377,\"closed" +
                "\":\"true\",\"monthsOpen\":1},{\"claimId\":998,\"customerId\":377,\"closed\":\"false\",\"monthsOpen" +
                "\":10},{\"claimId\":999,\"customerId\":258,\"closed\":\"false\",\"monthsOpen\":7},{\"claimId\":1000," +
                "\"customerId\":207,\"closed\":\"true\",\"monthsOpen\":5}]";
        assertEquals(expectedResults, stringResults);
    }

    //PASSING
    @Test
    void filterWithLessThan() {
        testData = "[{\"claimId\":1,\"customerId\":140,\"closed\":\"false\",\"monthsOpen\":8},{\"claimId\":2," +
                "\"customerId\":310,\"closed\":\"true\",\"monthsOpen\":11},{\"claimId\":3,\"customerId\":353," +
                "\"closed\":\"true\",\"monthsOpen\":4},{\"claimId\":4,\"customerId\":498,\"closed\":\"false\"," +
                "\"monthsOpen\":9},{\"claimId\":5,\"customerId\":106,\"closed\":\"false\",\"monthsOpen\":6},{" +
                "\"claimId\":6,\"customerId\":266,\"closed\":\"false\",\"monthsOpen\":6},{\"claimId\":7," +
                "\"customerId\":72,\"closed\":\"true\",\"monthsOpen\":9},{\"claimId\":8,\"customerId\":102," +
                "\"closed\":\"true\",\"monthsOpen\":12},{\"claimId\":9,\"customerId\":161,\"closed\":\"false\"," +
                "\"monthsOpen\":10},{\"claimId\":10,\"customerId\":199,\"closed\":\"false\",\"monthsOpen\":1}]";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("claimId", "<10");
        JsonArray actualResults = JsonFilter.queryJson(testData, parameters);
        String stringResults = actualResults.toString();
        String expectedResults = "[{\"claimId\":1,\"customerId\":140,\"closed\":\"false\",\"monthsOpen\":8},{" +
                "\"claimId\":2,\"customerId\":310,\"closed\":\"true\",\"monthsOpen\":11},{\"claimId\":3," +
                "\"customerId\":353,\"closed\":\"true\",\"monthsOpen\":4},{\"claimId\":4,\"customerId\":498," +
                "\"closed\":\"false\",\"monthsOpen\":9},{\"claimId\":5,\"customerId\":106,\"closed\":\"false\"," +
                "\"monthsOpen\":6},{\"claimId\":6,\"customerId\":266,\"closed\":\"false\",\"monthsOpen\":6},{" +
                "\"claimId\":7,\"customerId\":72,\"closed\":\"true\",\"monthsOpen\":9},{\"claimId\":8," +
                "\"customerId\":102,\"closed\":\"true\",\"monthsOpen\":12},{\"claimId\":9,\"customerId" +
                "\":161,\"closed\":\"false\",\"monthsOpen\":10}]";
        assertEquals(expectedResults, stringResults);
    }

    //NOT WORKING CURRENTLY
    @Test
    void filterWithGreaterOrEqualTo() {
        testData = "[{\"claimId\":993,\"customerId\":127,\"closed\":\"true\",\"monthsOpen\":4},{\"claimId\":994," +
                "\"customerId\":120,\"closed\":\"true\",\"monthsOpen\":7},{\"claimId\":994,\"customerId\":120," +
                "\"closed\":\"true\",\"monthsOpen\":7},{\"claimId\":995,\"customerId\":351,\"closed\":\"true\"," +
                "\"monthsOpen\":2},{\"claimId\":996,\"customerId\":370,\"closed\":\"true\",\"monthsOpen\":5},{" +
                "\"claimId\":997,\"customerId\":377,\"closed\":\"true\",\"monthsOpen\":1},{\"claimId\":998," +
                "\"customerId\":377,\"closed\":\"false\",\"monthsOpen\":10},{\"claimId\":999,\"customerId\":258," +
                "\"closed\":\"false\",\"monthsOpen\":7},{\"claimId\":1000,\"customerId\":207,\"closed\":\"true\"," +
                "\"monthsOpen\":5}]";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("claimId", ">=993");
        JsonArray actualResults = JsonFilter.queryJson(testData, parameters);
        String stringResults = actualResults.toString();
        String expectedResults = "[{\"claimId\":993,\"customerId\":127,\"closed\":\"true\",\"monthsOpen\":4},{" +
                "\"claimId\":994,\"customerId\":120,\"closed\":\"true\",\"monthsOpen\":7},{\"claimId\":994," +
                "\"customerId\":120,\"closed\":\"true\",\"monthsOpen\":7},{\"claimId\":995,\"customerId\":351," +
                "\"closed\":\"true\",\"monthsOpen\":2},{\"claimId\":996,\"customerId\":370,\"closed\":\"true\"," +
                "\"monthsOpen\":5},{\"claimId\":997,\"customerId\":377,\"closed\":\"true\",\"monthsOpen\":1},{" +
                "\"claimId\":998,\"customerId\":377,\"closed\":\"false\",\"monthsOpen\":10},{\"claimId\":999," +
                "\"customerId\":258,\"closed\":\"false\",\"monthsOpen\":7},{\"claimId\":1000,\"customerId\":207," +
                "\"closed\":\"true\",\"monthsOpen\":5}]";
        assertEquals(expectedResults, stringResults);
    }

    //NOT WORKING CURRENTLY
    @Test
    void filterWithLessOrEqualTo() {
        testData = "[{\"claimId\":1,\"customerId\":140,\"closed\":\"false\",\"monthsOpen\":8},{\"claimId\":2," +
                "\"customerId\":310,\"closed\":\"true\",\"monthsOpen\":11},{\"claimId\":3,\"customerId\":353," +
                "\"closed\":\"true\",\"monthsOpen\":4},{\"claimId\":4,\"customerId\":498,\"closed\":\"false\"," +
                "\"monthsOpen\":9},{\"claimId\":5,\"customerId\":106,\"closed\":\"false\",\"monthsOpen\":6},{" +
                "\"claimId\":6,\"customerId\":266,\"closed\":\"false\",\"monthsOpen\":6},{\"claimId\":7," +
                "\"customerId\":72,\"closed\":\"true\",\"monthsOpen\":9},{\"claimId\":8,\"customerId\":102," +
                "\"closed\":\"true\",\"monthsOpen\":12},{\"claimId\":9,\"customerId\":161,\"closed\":\"false\"," +
                "\"monthsOpen\":10},{\"claimId\":10,\"customerId\":199,\"closed\":\"false\",\"monthsOpen\":1}]";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("claimId", "<=10");
        JsonArray actualResults = JsonFilter.queryJson(testData, parameters);
        String stringResults = actualResults.toString();
        String expectedResults = "[{\"claimId\":1,\"customerId\":140,\"closed\":\"false\",\"monthsOpen\":8},{" +
                "\"claimId\":2,\"customerId\":310,\"closed\":\"true\",\"monthsOpen\":11},{\"claimId\":3," +
                "\"customerId\":353,\"closed\":\"true\",\"monthsOpen\":4},{\"claimId\":4,\"customerId\":498," +
                "\"closed\":\"false\",\"monthsOpen\":9},{\"claimId\":5,\"customerId\":106,\"closed\":\"false\"," +
                "\"monthsOpen\":6},{\"claimId\":6,\"customerId\":266,\"closed\":\"false\",\"monthsOpen\":6},{" +
                "\"claimId\":7,\"customerId\":72,\"closed\":\"true\",\"monthsOpen\":9},{\"claimId\":8,\"customerId" +
                "\":102,\"closed\":\"true\",\"monthsOpen\":12},{\"claimId\":9,\"customerId\":161,\"closed\":\"false" +
                "\",\"monthsOpen\":10},{\"claimId\":10,\"customerId\":199,\"closed\":\"false\",\"monthsOpen\":1}]";
        assertEquals(expectedResults, stringResults);
    }

}