package eu.geekhome.rest.configurable;

import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigurableDto {

    @SerializedName("fields")
    private List<FieldDto> _fields;

    @SerializedName("class")
    private String _clazz;

    @SerializedName("addNewRes")
    private Resource _addNewRes;

    @SerializedName("titleRes")
    private Resource _titleRes;

    @SerializedName("descriptionRes")
    private Resource _descriptionRes;

    @SerializedName("iconRaw")
    private String _iconRaw;

    @SerializedName("descendants")
    private List<ConfigurableDto> _descendants;

    public ConfigurableDto(Resource titleRes, Resource descriptionRes, String clazz, List<FieldDto> fields,
                           Resource addNewRes, String iconRaw, List<ConfigurableDto> descendants) {
        _titleRes = titleRes;
        _descriptionRes = descriptionRes;
        _clazz = clazz;
        _fields = fields;
        _addNewRes = addNewRes;
        _iconRaw = iconRaw;
        _descendants = descendants;
    }

    public String getClazz() {
        return _clazz;
    }
}
