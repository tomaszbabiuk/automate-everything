package eu.geekhome.shellyplugin;

import com.geekhome.common.extensibility.PluginMetadata;
import com.geekhome.common.localization.Resource;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class ShellyPlugin extends Plugin implements PluginMetadata {

    public ShellyPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("Shelly plugin.start()");
    }

    @Override
    public void stop() {
        System.out.println("Shelly plugin.stop()");
    }

    @Override
    public Resource getName() {
        return new Resource("Shelly", "Shelly");
    }

    @Override
    public Resource getDescription() {
        return new Resource(
                "Unofficial support for Shelly devices",
                "Nieoficjalne wsparcie dla urządzeń Shelly");
    }
}
