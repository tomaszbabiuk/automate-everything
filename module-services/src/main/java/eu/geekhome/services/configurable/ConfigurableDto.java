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
    private final List<ResourceWithIdDto> _blockTargets;

    public ConfigurableDto(Resource titleRes, Resource descriptionRes, String clazz, String parentClazz,
                           List<FieldDto> fields, List<ResourceWithIdDto> blockTargets, Resource addNewRes,
                           Resource editRes, String iconRaw) {
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
