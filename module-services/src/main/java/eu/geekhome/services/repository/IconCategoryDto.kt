package eu.geekhome.services.repository;

import java.util.List;

public class IconCategoryDto {

    private final long _id;
    private final String _name;
    private final List<Long> _iconIds;

    public IconCategoryDto(long id, String name, List<Long> split) {
        _id = id;
        _name = name;
        _iconIds = split;
    }

    public long getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public List<Long> getIconIds() {
        return _iconIds;
    }
}
