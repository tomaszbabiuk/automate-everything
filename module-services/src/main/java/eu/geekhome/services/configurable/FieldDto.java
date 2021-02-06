package eu.geekhome.services.configurable;

import eu.geekhome.services.localization.Resource;

public class FieldDto {

    private final String _name;
    private final Resource _hint;
    private final String _clazz;
    private final int _maxSize;

    public FieldDto(String clazz, String name, Resource hint, int maxSize) {
        _name = name;
        _hint = hint;
        _clazz = clazz;
        _maxSize = maxSize;
    }
}
