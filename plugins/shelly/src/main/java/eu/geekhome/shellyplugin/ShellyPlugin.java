package eu.geekhome.shellyplugin;

import com.geekhome.common.extensibility.PluginMetadata;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.Resource;
import eu.geekhome.services.hardware.HardwarePlugin;
import eu.geekhome.services.mqtt.MqttBrokerPlugin;
import eu.geekhome.services.mqtt.MqttBrokerService;
import org.pf4j.*;

public class ShellyPlugin extends HardwarePlugin implements PluginMetadata, PluginStateListener {

    public static final String PLUGIN_ID_MQTT = "mqtt";
    public static final String PLUGIN_ID_SHELLY = "shelly";

    private final ShellyAdapterFactory _factory;

    public ShellyPlugin(PluginWrapper wrapper) {
        super(wrapper);
        PluginManager manager = wrapper.getPluginManager();
        manager.addPluginStateListener(this);

        PluginWrapper mqttPluginWrapper = manager.getPlugin(PLUGIN_ID_MQTT);
        MqttBrokerService broker = ((MqttBrokerPlugin) mqttPluginWrapper.getPlugin()).getBroker();
        _factory = new ShellyAdapterFactory(broker);
    }

    @Override
    public IHardwareManagerAdapterFactory getFactory() {
        return _factory;
    }

    @Override
    public void start() {
        System.out.println("Starting SHELLY plugin");

        PluginManager manager = wrapper.getPluginManager();

        PluginWrapper mqttPluginWrapper = manager.getPlugin(PLUGIN_ID_MQTT);

        if (mqttPluginWrapper.getPluginState() != PluginState.STARTED) {
            manager.startPlugin(PLUGIN_ID_MQTT);
        }
    }

    @Override
    public void stop() {
        System.out.println("Stopping SHELLY plugin");
    }

    @Override
    public Resource getName() {
        return R.plugin_name;
    }

    @Override
    public Resource getDescription() {
        return R.plugin_description;
    }

    @Override
    public void pluginStateChanged(PluginStateEvent event) {
        if (event.getPluginState() == PluginState.STOPPED) {
            if (event.getPlugin().getPluginId().equals(PLUGIN_ID_MQTT)) {
                System.out.println("MQTT plugin stopped, shelly cannot continue... Stopping");
                getWrapper().getPluginManager().stopPlugin(PLUGIN_ID_SHELLY);
            }
        }
    }
}
