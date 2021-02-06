package eu.geekhome.services.repository;


public class FieldInstanceDto {

    private final long _id;
    private final String _name;
    private final String _valueAsString;
    private final long _instanceId;

    public FieldInstanceDto(long id, String name, String valueAsString, long instanceId) {
        _id = id;
        _name = name;
        _valueAsString = valueAsString;
        _instanceId = instanceId;
    }
}
