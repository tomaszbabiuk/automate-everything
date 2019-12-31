package eu.geekhome.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class App extends ResourceConfig {
    public App() {
        packages("eu.geekhome.rest");
        register(new DependencyInjectionBinder());
    }
}