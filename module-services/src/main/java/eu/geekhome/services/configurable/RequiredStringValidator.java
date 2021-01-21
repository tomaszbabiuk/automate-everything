package eu.geekhome.services.configurable;


import eu.geekhome.services.localization.Resource;

public class RequiredStringValidator implements Validator<String> {
    @Override
    public Resource getReason() {
        return new Resource("This field is required",
                "To pole jest wymagane");
    }

    @Override
    public boolean validate(String fieldValue) {
        return fieldValue != null && !fieldValue.isEmpty();
    }
}
