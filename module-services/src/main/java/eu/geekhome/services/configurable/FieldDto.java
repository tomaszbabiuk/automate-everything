package eu.geekhome.services.configurable;

import eu.geekhome.services.localization.Resource;
import com.google.gson.annotations.SerializedName;

public class FieldDto {

    @SerializedName("name")
    private final String _name;

    @SerializedName("hint")
    private final Resource _hint;

    @SerializedName("class")
    private final String _clazz;

    @SerializedName("maxSize")
    private final int _maxSize;

    public FieldDto(String clazz, String name, Resource hint, int maxSize) {
        _name = name;
        _hint = hint;
        _clazz = clazz;
        _maxSize = maxSize;
    }
}
