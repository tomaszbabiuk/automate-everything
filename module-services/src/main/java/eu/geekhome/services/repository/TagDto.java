package eu.geekhome.services.repository;

public class TagDto {

    private final long _id;
    private final Long _parentId;
    private final String _name;

    public TagDto(long id, Long parentId, String name) {
        _id = id;
        _parentId = parentId;
        _name = name;
    }

    public long getId() {
        return _id;
    }

    public Long getParentId() {
        return _parentId;
    }

    public String getName() {
        return _name;
    }
}
