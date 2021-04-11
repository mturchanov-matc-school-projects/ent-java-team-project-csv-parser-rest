package com.ee_java.team_project.controller;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

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
        HashSet classes = new HashSet<Class<?>>();
        classes.add(MultiPartFeature.class);
        classes.add(JSONQueryService.class);
        return classes;
    }
}
