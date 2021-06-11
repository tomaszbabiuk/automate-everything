package eu.geekhome.services.configurable;

import eu.geekhome.services.localization.Resource;

public class DoubleField extends FieldDefinition<Double> {

    public DoubleField(String name, Resource hint, int maxSize, Validator<Double>... validators) {
        super(name, hint, maxSize, Double.class, new DoubleFieldBuilder(), validators);
    }
}
