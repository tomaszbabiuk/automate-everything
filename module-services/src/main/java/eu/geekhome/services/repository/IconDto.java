package eu.geekhome.services.repository;

import com.google.gson.annotations.SerializedName;

public class IconDto {

    @SerializedName("id")
    private final long _id;

    @SerializedName("iconCategoryId")
    private final Long _iconCategoryId;

    @SerializedName("raw")
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
