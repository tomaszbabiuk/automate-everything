package eu.geekhome.domain.configurable;

import eu.geekhome.data.localization.Resource;

public class DoubleField extends FieldDefinition<Double> {

    public DoubleField(String name, Resource hint, int maxSize, Validator<Double>... validators) {
        super(name, hint, maxSize, Double.class, new DoubleFieldBuilder(), validators);
    }
}
