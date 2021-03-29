package com.ee_java.team_project.csv_parser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/agents")
public class AgentEndpoint {
    // The Java method will process HTTP GET requests
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @GET
    @Produces("application/json")
    public Response getAgent(@Context UriInfo uriInfo) {
        // Return a simple message
        String output = "Hello World";
        return Response.status(200).entity(output).build();
    }
}

