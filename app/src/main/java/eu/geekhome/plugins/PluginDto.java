package eu.geekhome.plugins;

import com.geekhome.common.NamedObject;
import com.geekhome.common.configuration.DescriptiveName;
import com.google.gson.annotations.SerializedName;

public class PluginDto extends NamedObject {

    @SerializedName("enabled")
    private boolean _enabled;

    public PluginDto(DescriptiveName name, boolean enabled) {
        super(name);
        _enabled = enabled;
    }

    public boolean isEnabled() {
        return _enabled;
    }
}
