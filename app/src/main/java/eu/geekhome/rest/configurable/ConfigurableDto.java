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

    @SerializedName("persistenceId")
    private Long _persistenceId;

    @SerializedName("addNewRes")
    Resource _addNewRes;

    @SerializedName("iconName")
    String _iconName;

    public ConfigurableDto(String clazz, List<FieldDto> fields, List<String> attachableTo,
                           Long persistenceId, Resource addNewRes, String iconName) {
        _clazz = clazz;
        _fields = fields;
        _attachableTo = attachableTo;
        _persistenceId = persistenceId;
        _addNewRes = addNewRes;
        _iconName = iconName;
    }
}
