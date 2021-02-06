package eu.geekhome.services.configurable;

import eu.geekhome.services.localization.Resource;
import com.google.gson.annotations.SerializedName;

public class BlockTargetDto {
    @SerializedName("id")
    private String _id;

    @SerializedName("label")
    private Resource _label;

    public BlockTargetDto(String id, Resource label) {
        _id = id;
        _label = label;
    }

    public Resource getLabel() {
        return _label;
    }

    public String getId() {
        return _id;
    }
}