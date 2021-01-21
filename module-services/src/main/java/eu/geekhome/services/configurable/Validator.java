package eu.geekhome.services.configurable;


import eu.geekhome.services.localization.Resource;

public interface Validator<T> {
    Resource getReason();
    boolean validate(T fieldValue);
}
