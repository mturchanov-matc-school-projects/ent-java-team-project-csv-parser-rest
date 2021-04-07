package com.ee_java.team_project.csv_parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Creates a JSON query endpoint that can handle POST requests consisting of query parameters to organize the JSON.
 * @author pjcraig
 */
@Path("/query")
public class JSONQueryService {
    private static final Logger logger = LogManager.getLogger();
    
    /**
     * Returns JSON elements based on the query type and additional parameters provided.
     *
     * @param form The form element consisting of the parameters.
     * @return The response
     */
    @POST
    @Produces("application/json")
    public Response queryJSON(Form form) {
        MultivaluedMap<String, String> formParameters = form.asMap();
        Map<String, String> parameters = prepareParameters(formParameters);
        String queryType = parameters.get("queryType");
        String json = parameters.get("json");
        String finalJson = json;

        switch (queryType) {
            case "value":
                String column = parameters.get("queryColumn");
                String value = parameters.get("queryColumnValue");
                logger.debug("Querying JSON in column '{}' for value '{}'", column, value);
                break;
            case "all":
            default:
                break;
        }

        return Response.status(200).entity(finalJson).build();
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
