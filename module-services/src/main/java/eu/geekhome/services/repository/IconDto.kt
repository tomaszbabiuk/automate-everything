package eu.geekhome.services.repository;

public class IconDto {

    private final long _id;
    private final Long _iconCategoryId;
    private final String _raw;

    public IconDto(long id, long iconCategoryId, String raw) {
        _id = id;
        _iconCategoryId = iconCategoryId;
        _raw = raw;
    }

    public long getId() {
        return _id;
    }

    public Long getIconCategoryId() {
        return _iconCategoryId;
    }

    public String getRaw() {
        return _raw;
    }
}
