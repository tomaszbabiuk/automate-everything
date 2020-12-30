package eu.geekhome.rest.configurable;

import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

public class FieldDto {

    @SerializedName("name")
    private String _name;

    @SerializedName("hint")
    private Resource _hint;

    @SerializedName("class")
    private String _clazz;

    @SerializedName("maxSize")
    private int _maxSize;

    public FieldDto(String clazz, String name, Resource hint, int maxSize) {
        _name = name;
        _hint = hint;
        _clazz = clazz;
        _maxSize = maxSize;
    }
}
