package com.ee_java.team_project.csv_parser;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


/**
 * The type Rest application.
 */
@ApplicationPath("/rest")
//The java class declares root resource and provider classes
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add(Test.class);
        h.add(JSONQueryService.class);
        return h;
    }
}
