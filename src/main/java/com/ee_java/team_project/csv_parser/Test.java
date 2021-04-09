package com.ee_java.team_project.csv_parser;

import com.jayway.jsonpath.JsonPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.ws.rs.*;
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
    public Response test(@Context UriInfo uriInfo, @Context HttpServletRequest httpRequest) {
        CodingCompCsvUtil parser = new CodingCompCsvUtil();
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters(); //gets all queryPArams(url?p=v&etc)
        Map<String, String> map = prepareParameters(queryParams);
        Map<String, String> copyMap = new HashMap<>(map); //to avoid concurrent exception

        HttpSession session = httpRequest.getSession();
        String json = (String)session.getAttribute("json");

        // remove empty queryParam
        for (Map.Entry<String,String> param : map.entrySet()) {
            //System.out.printf("{%s:%s}%n", param.getKey(), param.getValue());
            String queryVal = param.getValue();
            if (queryVal.isEmpty()) {
                copyMap.remove(param.getKey());
            }
        }

        for (Map.Entry<String,String> param : copyMap.entrySet()) {
            List<String> updatedJson = (JsonPath.read(json, String.format("$.[?(@.%s == '%s')]", param.getKey(), param.getValue())));
            json = updatedJson.toString();
        }

        return Response.status(200).entity(json).build();
    }

    private Map<String,String> prepareParameters(MultivaluedMap<String, String> queryParameters) {

        Map<String,String> parameters = new HashMap<String,String>();
        for(String str : queryParameters.keySet()){
            parameters.put(str, queryParameters.getFirst(str));
        }
        return parameters;
    }
}
