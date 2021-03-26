package com.ee_java.team_project.csv_parser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The test endpoint.
 */
@Path("/parsed_items")
public class Test {
    
    /**
     * Test response.
     *
     * @param uriInfo the uri info
     * @return the response
     */
    @GET
    @Produces("application/json")
    //TODO: endpoint getItemByID -> return 1 JSON object
    //TODO: endpoint count return -> count based on query params
    //TODO: endpoint getItemsBasedOnParams -> return JSON objects based on query params
    public Response test(@Context UriInfo uriInfo) {
        CodingCompCsvUtil parser = new CodingCompCsvUtil();
        Map<List<String>, String> parsed = parser.readCsvFileFileWithoutPojo("/home/student/Desktop/java_ent_2021/2020-StateFarm-CodingCompetitionProblem/src/main/resources/DataFiles/claims.csv");
        String parsedItemsRawJSON = null;
        List<String> keys;
        for (Map.Entry<List<String>, String> entry : parsed.entrySet()) { // parse once to init
            keys = entry.getKey();
            parsedItemsRawJSON = entry.getValue();
        }

        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters(); //gets all queryPArams(url?p=v&etc)
        Map<String, String> map = prepareParameters(queryParams);


        for (Map.Entry<String,String> param : map.entrySet()) {
            //TODO: parse through query params. if param is on of the #keys
            // then filter #parsedItemsRawJSON (either by making it JSON and parsing or another way)
            // TODO/extra: handling numeric comparison (e.g: query param #idLessThan=100 then filter and leave ids < 100)
            System.out.printf("{%s:%s}%n", param.getKey(), param.getValue());
        }
        return Response.status(200).entity(parsedItemsRawJSON).build();
    }

    private Map<String,String> prepareParameters(MultivaluedMap<String, String> queryParameters) {

        Map<String,String> parameters = new HashMap<String,String>();
        for(String str : queryParameters.keySet()){
            parameters.put(str, queryParameters.getFirst(str));
        }
        return parameters;
    }
}
