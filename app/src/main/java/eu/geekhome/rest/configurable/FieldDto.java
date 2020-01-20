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

    public FieldDto(String clazz, String name, Resource hint) {
        _name = name;
        _hint = hint;
        _clazz = clazz;
    }
}
