package eu.geekhome.rest.configurable;

import eu.geekhome.services.configurable.BlockTarget;
import eu.geekhome.services.configurable.BlockTargetDto;
import eu.geekhome.services.configurable.FieldDto;

public class BlockTargetDtoMapper {

    public BlockTargetDto map(BlockTarget blockTarget) {
        return new BlockTargetDto(blockTarget.getId(), blockTarget.getLabel());
    }
}
