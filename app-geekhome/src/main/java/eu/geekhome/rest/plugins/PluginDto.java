package eu.geekhome.rest.plugins;

import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

public class PluginDto {

    @SerializedName("provider")
    private String _provider;

    @SerializedName("version")
    private String _version;

    @SerializedName("enabled")
    private boolean _enabled;

    @SerializedName("name")
    private Resource _name;

    @SerializedName("description")
    private Resource _description;

    @SerializedName("id")
    private String _id;

    @SerializedName("isHardwareFactory")
    private boolean _isHardwareFactory;

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
