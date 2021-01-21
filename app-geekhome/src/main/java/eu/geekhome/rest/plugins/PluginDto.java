package eu.geekhome.rest.plugins;

import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

public class PluginDto {

    @SerializedName("provider")
    private final String _provider;

    @SerializedName("version")
    private final String _version;

    @SerializedName("enabled")
    private final boolean _enabled;

    @SerializedName("name")
    private final Resource _name;

    @SerializedName("description")
    private final Resource _description;

    @SerializedName("id")
    private final String _id;

    @SerializedName("isHardwareFactory")
    private final boolean _isHardwareFactory;

    public PluginDto(String id, Resource name, Resource description, String provider, String version, boolean isHardwareFactory, boolean enabled) {
        _name = name;
        _description = description;
        _id = id;
        _provider = provider;
        _version = version;
        _isHardwareFactory = isHardwareFactory;
        _enabled = enabled;
    }

    public String getProvider() {
        return _provider;
    }

    public String getVersion() {
        return _version;
    }

    public boolean isEnabled() {
        return _enabled;
    }

    public Resource getName() {
        return _name;
    }

    public Resource getDescription() {
        return _description;
    }

    public String getId() {
        return _id;
    }
}
