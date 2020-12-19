package eu.geekhome.services.repository;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IconCategoryDto {

    @SerializedName("id")
    private final long _id;

    @SerializedName("name")
    private final String _name;

    @SerializedName("iconIds")
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
