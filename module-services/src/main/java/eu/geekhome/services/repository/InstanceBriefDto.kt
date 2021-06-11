package eu.geekhome.services.repository;

public class InstanceBriefDto {

    private final long _id;
    private final String _clazz;
    private final String _name;

    public InstanceBriefDto(long id, String clazz, String name) {
        _id = id;
        _clazz = clazz;
        _name = name;
    }

    public String getClazz() {
        return _clazz;
    }

    public String getName() {
        return _name;
    }

    public long getId() {
        return _id;
    }

}