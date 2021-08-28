package eu.geekhome.domain.hardware;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public abstract class HardwarePlugin extends Plugin implements HardwareAdapterFactory {

    public HardwarePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
}
