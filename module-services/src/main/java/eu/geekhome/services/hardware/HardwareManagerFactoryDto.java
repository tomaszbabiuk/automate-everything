package eu.geekhome.services.hardware;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

public class HardwareManagerFactoryDto {

    @SerializedName("class")
    private final String _clazz;

    @SerializedName("name")
    private final Resource _name;

    public HardwareManagerFactoryDto(String clazz, Resource name) {
        _clazz = clazz;
        _name = name;
    }


    public Resource getName() {
        return _name;
    }

    public String getClazz() {
        return _clazz;
    }
}
