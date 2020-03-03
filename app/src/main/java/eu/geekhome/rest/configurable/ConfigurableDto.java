package eu.geekhome.rest.configurable;

import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigurableDto {

    @SerializedName("fields")
    private List<FieldDto> _fields;

    private Resource _descriptionRes;
    @SerializedName("class")
    private String _clazz;

    @SerializedName("addNewRes")
    private Resource _addNewRes;

    @SerializedName("titleRes")
    private Resource _titleRes;

    @SerializedName("iconName")
    private String _iconName;

    @SerializedName("children")
    private List<ConfigurableDto> _children;

    @SerializedName("description")
    private Resource _description;

    public ConfigurableDto(Resource titleRes, Resource descriptionRes, Resource addNewRes, String clazz,
                           List<FieldDto> fields, String iconName, List<ConfigurableDto> children) {
        _titleRes = titleRes;
        _descriptionRes = descriptionRes;
        _clazz = clazz;
        _fields = fields;
        _addNewRes = addNewRes;
        _iconName = iconName;
        _children = children;
    }

    public String getClazz() {
        return _clazz;
    }
}
