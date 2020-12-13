package eu.geekhome.rest.configurable;

import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigurableDto {

    @SerializedName("fields")
    private List<FieldDto> _fields;

    @SerializedName("class")
    private String _clazz;

    @SerializedName("parentClass")
    private String _parentClazz;

    @SerializedName("addNewRes")
    private Resource _addNewRes;

    @SerializedName("titleRes")
    private Resource _titleRes;

    @SerializedName("descriptionRes")
    private Resource _descriptionRes;

    @SerializedName("iconRaw")
    private String _iconRaw;

    public ConfigurableDto(Resource titleRes, Resource descriptionRes, String clazz, String parentClazz,
                           List<FieldDto> fields, Resource addNewRes, String iconRaw) {
        _titleRes = titleRes;
        _descriptionRes = descriptionRes;
        _clazz = clazz;
        _parentClazz = parentClazz;
        _fields = fields;
        _addNewRes = addNewRes;
        _iconRaw = iconRaw;
    }

    public String getClazz() {
        return _clazz;
    }
}
