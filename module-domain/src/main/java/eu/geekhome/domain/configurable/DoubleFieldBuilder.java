package eu.geekhome.domain.configurable;

public class DoubleFieldBuilder implements FieldBuilder<Double> {

    @Override
    public Double fromPersistableString(String value) {
        if (value == null) {
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    @Override
    public String toPersistableString(Double value) {
        return value.toString();
    }
}
