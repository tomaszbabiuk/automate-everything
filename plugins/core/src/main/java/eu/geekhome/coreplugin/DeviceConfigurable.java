package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

@Extension
public class DeviceConfigurable extends CategoryConfigurable {

    @Override
    public Class<? extends Configurable> getParent() {
        return null;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_device_title;
    }

    @Override
    public String getIconName() {
        return "device";
    }
}