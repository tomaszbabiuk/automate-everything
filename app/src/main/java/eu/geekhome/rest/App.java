package eu.geekhome.rest;

import org.glassfish.jersey.server.ResourceConfig;

@SuppressWarnings("unused")
public class App extends ResourceConfig {
    public App() {
        packages("eu.geekhome.rest");
        register(new DependencyInjectionBinder());
        register(new GsonMessageBodyHandler());
    }
}