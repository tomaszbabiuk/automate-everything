package eu.geekhome.services.configurable;

import eu.geekhome.services.localization.Resource;

import java.util.List;

public class ConfigurableDto {

    private final List<FieldDto> _fields;
    private final String _clazz;
    private final String _parentClazz;
    private final Resource _addNewRes;
    private final Resource _editRes;
    private final Resource _titleRes;
    private final Resource _descriptionRes;
    private final String _iconRaw;
    private final List<BlockTargetDto> _blockTargets;
    private final ConfigurableType _type;

    public ConfigurableDto(Resource titleRes, Resource descriptionRes, String clazz, String parentClazz, ConfigurableType type,
                           List<FieldDto> fields, List<BlockTargetDto> blockTargets, Resource addNewRes, Resource editRes, String iconRaw) {
        _titleRes = titleRes;
        _descriptionRes = descriptionRes;
        _clazz = clazz;
        _parentClazz = parentClazz;
        _type = type;
        _fields = fields;
        _blockTargets = blockTargets;
        _addNewRes = addNewRes;
        _editRes = editRes;
        _iconRaw = iconRaw;
    }
}
