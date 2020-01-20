package eu.geekhome.rest;

public class MappingException extends RuntimeException {

    public MappingException(Class<?> from, Class<?> to) {
        super("Cannot map from: " + from.getName() + " to " + to.getName());
    }
}
