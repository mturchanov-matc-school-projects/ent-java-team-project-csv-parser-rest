package com.ee_java.team_project.controller;

import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
        classes.add(JSONQueryService.class);
        classes.add(MultiPartFeature.class);
        return classes;
    }
}
