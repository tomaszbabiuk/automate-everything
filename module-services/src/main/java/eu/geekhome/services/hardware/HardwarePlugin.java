package eu.geekhome.services.hardware;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public abstract class HardwarePlugin extends Plugin {

    public HardwarePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    public abstract HardwareAdapterFactory getFactory();
}
