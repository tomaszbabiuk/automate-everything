package eu.geekhome.rest.configurable;

import eu.geekhome.services.configurable.ResourceWithIdDto;
import eu.geekhome.services.localization.ResourceWithId;

public class BlockTargetDtoMapper {

    public ResourceWithIdDto map(ResourceWithId resourceWithId) {
        return new ResourceWithIdDto(resourceWithId.getId(), resourceWithId);
    }
}
