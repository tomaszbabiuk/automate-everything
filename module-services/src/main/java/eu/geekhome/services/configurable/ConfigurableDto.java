package eu.geekhome.services.configurable;

import com.google.gson.annotations.SerializedName;
import eu.geekhome.services.localization.Resource;

import java.util.List;

public class ConfigurableDto {

    @SerializedName("fields")
    private final List<FieldDto> _fields;

    @SerializedName("class")
    private final String _clazz;

    @SerializedName("parentClass")
    private final String _parentClazz;

    @SerializedName("addNewRes")
    private final Resource _addNewRes;

    @SerializedName("editRes")
    private final Resource _editRes;

    @SerializedName("titleRes")
    private final Resource _titleRes;

    @SerializedName("descriptionRes")
    private final Resource _descriptionRes;

    @SerializedName("iconRaw")
    private final String _iconRaw;

    @SerializedName("blockTargets")
    private final List<BlockTargetDto> _blockTargets;

    public ConfigurableDto(Resource titleRes, Resource descriptionRes, String clazz, String parentClazz,
                           List<FieldDto> fields, List<BlockTargetDto> blockTargets, Resource addNewRes, Resource editRes, String iconRaw) {
        _titleRes = titleRes;
        _descriptionRes = descriptionRes;
        _clazz = clazz;
        _parentClazz = parentClazz;
        _fields = fields;
        _blockTargets = blockTargets;
        _addNewRes = addNewRes;
        _editRes = editRes;
        _iconRaw = iconRaw;
    }
}
