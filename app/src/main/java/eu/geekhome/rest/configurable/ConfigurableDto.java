package eu.geekhome.rest.configurable;

import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigurableDto {

    @SerializedName("fields")
    private List<FieldDto> _fields;

    @SerializedName("class")
    private String _clazz;

    @SerializedName("attachableTo")
    private List<String> _attachableTo;

    @SerializedName("addNewRes")
    private Resource _addNewRes;

    @SerializedName("iconName")
    private String _iconName;

    public ConfigurableDto(String clazz, List<FieldDto> fields, List<String> attachableTo,
                           Resource addNewRes, String iconName) {
        _clazz = clazz;
        _fields = fields;
        _attachableTo = attachableTo;
        _addNewRes = addNewRes;
        _iconName = iconName;
    }
}
