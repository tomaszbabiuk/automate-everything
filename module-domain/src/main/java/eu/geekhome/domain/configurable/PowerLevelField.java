package eu.geekhome.domain.configurable;

import eu.geekhome.data.localization.Resource;

public class PowerLevelField extends FieldDefinition<Integer> {

    public PowerLevelField(String name, Resource hint, Validator<Integer>... validators) {
        super(name, hint, 0, Integer.class, new IntegerFieldBuilder(), validators);
    }
}
