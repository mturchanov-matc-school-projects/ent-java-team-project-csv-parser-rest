package com.ee_java.team_project.csv_parser;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * The RESTful application that handles endpoints by declaring root resources and provider classes.
 * @author mturchanov
 * @author pjcraig
 */
@ApplicationPath("/rest")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add(JSONQueryService.class);
        return h;
    }
}
