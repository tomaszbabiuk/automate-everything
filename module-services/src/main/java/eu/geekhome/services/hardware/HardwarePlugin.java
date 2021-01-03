package eu.geekhome.services.hardware;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public abstract class HardwarePlugin extends Plugin {

    public HardwarePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    public abstract IHardwareManagerAdapterFactory getFactory();
}
