package eu.geekhome.services.repository;

import com.google.gson.annotations.SerializedName;

public class IconCategoryDto {

    @SerializedName("id")
    private final long _id;

    @SerializedName("name")
    private final String _name;

    public IconCategoryDto(long id, String name) {
        _id = id;
        _name = name;
    }

    public long getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }
}
