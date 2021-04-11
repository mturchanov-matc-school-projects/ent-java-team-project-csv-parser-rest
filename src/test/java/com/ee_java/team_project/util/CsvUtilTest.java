package com.ee_java.team_project.util;

import com.ee_java.team_project.csv_parser.CodingCompCsvUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing class for CodingCompCsvUtil
 *
 * @author mturchanov
 */
public class CsvUtilTest {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private CodingCompCsvUtil csvUtil;


    @BeforeEach
    void csvUtilSetup() {
        csvUtil = new CodingCompCsvUtil();
    }


    /**
     * verifies that simple csv file -- without objects as values
     * is parsed to JSON correctly
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    @Test
    void testParsingCsvWithoutObjectsAsValues() throws URISyntaxException, IOException {
        URL resourceClaimsCsvUrl = CsvUtilTest.class.getResource("/DataFiles/claims.csv");
        String filePathForClaimsCsv = Paths.get(resourceClaimsCsvUrl.toURI()).toFile().getAbsolutePath();
        URL resourceClaimsJSONUrl = CsvUtilTest.class.getResource("/DataFiles/claimsJSON.txt");
        String filePathForClaimsJSON = Paths.get(resourceClaimsJSONUrl.toURI()).toFile().getAbsolutePath();

        Map<List<String>, String> parsedClaimsMap =  csvUtil.readCsvFileFileWithoutPojo(filePathForClaimsCsv);
        List<String> expectedClaimsColumns = Arrays.asList("claimId", "customerId", "closed", "monthsOpen");
        String expectedClaimsJSON =  new String(Files.readAllBytes(Paths.get(filePathForClaimsJSON)));

        List<String> resultColumnNames = null;
        String resultJSON = null;
        for (Map.Entry<List<String>, String> entry : parsedClaimsMap.entrySet()) {
            resultColumnNames = entry.getKey();
            resultJSON = entry.getValue();
        }

        assertEquals(expectedClaimsColumns, resultColumnNames);
        assertEquals(expectedClaimsJSON, resultJSON);
    }

    /**
     * verifies that comlex csv file -- with objects as values customers.csv(#dependants)
     * is parsed to JSON correctly
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    void testParsingCsvWithObjectsAsValues() throws IOException, URISyntaxException {
        URL resourceCustomersCsvUrl = CsvUtilTest.class.getResource("/DataFiles/customers.csv");
        String filePathForCustomersCsv = Paths.get(resourceCustomersCsvUrl.toURI()).toFile().getAbsolutePath();
        URL resourceCustomersJSONUrl = CsvUtilTest.class.getResource("/DataFiles/customersJSON.txt");
        String filePathForCustomersJSON = Paths.get(resourceCustomersJSONUrl.toURI()).toFile().getAbsolutePath();


        Map<List<String>, String> parsedCustomersMap =  csvUtil.readCsvFileFileWithoutPojo(filePathForCustomersCsv);
        List<String> expectedCustomersColumns = Arrays.asList("customerId", "firstName", "lastName", "age",
                "area", "agentId", "agentRating", "primaryLanguage", "dependents", "homePolicy",
                "autoPolicy", "rentersPolicy", "totalMonthlyPremium", "yearsOfService", "vehiclesInsured");
        String expectedCustomersJSON =  new String(Files.readAllBytes(Paths.get(filePathForCustomersJSON)));

        List<String> resultColumnNames = null;
        String resultJSON = null;
        for (Map.Entry<List<String>, String> entry : parsedCustomersMap.entrySet()) {
            resultColumnNames = entry.getKey();
            resultJSON = entry.getValue();
        }

        assertEquals(expectedCustomersColumns, resultColumnNames);
        assertEquals(expectedCustomersJSON, resultJSON);
    }
}
