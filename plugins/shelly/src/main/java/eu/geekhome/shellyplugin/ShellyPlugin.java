package eu.geekhome.shellyplugin;

import com.geekhome.common.extensibility.PluginMetadata;
import com.geekhome.common.localization.Resource;
import org.pf4j.Plugin;
import org.pf4j.PluginManager;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;

public class ShellyPlugin extends Plugin implements PluginMetadata {

    public static final String PLUGIN_ID_MQTT = "mqtt";

    public ShellyPlugin(PluginWrapper wrapper) {
        super(wrapper);

        PluginManager manager = wrapper.getPluginManager();

        PluginWrapper mqtt = manager.getPlugin(PLUGIN_ID_MQTT);
        if (mqtt.getPluginState() != PluginState.STARTED) {
            manager.startPlugin(PLUGIN_ID_MQTT);
        }
    }

    @Override
    public void start() {
        System.out.println("Starting SHELLY plugin");
    }

    @Override
    public void stop() {
        System.out.println("Stopping SHELLY plugin");
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
