package eu.geekhome.services.configurable;


import eu.geekhome.services.localization.Resource;

public class RequiredDoubleValidator implements Validator<Double> {
    @Override
    public Resource getReason() {
        return new Resource("This field is required",
                "To pole jest wymagane");
    }

    @Override
    public boolean validate(Double fieldValue) {
        return fieldValue != null;
    }
}