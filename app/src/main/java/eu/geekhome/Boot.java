package eu.geekhome;

import com.geekhome.common.extensibility.RequiresFeature;
import com.geekhome.common.extensibility.RequiresMqttFeature;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.moquettemodule.MoquetteBroker;
import com.geekhome.moquettemodule.MqttBroker;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.util.List;

public class Boot {

    public static void main(String[] args) {
        MqttBroker mqttBroker = new MoquetteBroker();

        PluginManager pluginManager = new DefaultPluginManager();
        pluginManager.loadPlugins();

        // enable a disabled plugin
//        pluginManager.enablePlugin("welcome-plugin");

        // start (active/resolved) the plugins
        pluginManager.startPlugins();

        List<IHardwareManagerAdapterFactory> hmaFactories = pluginManager.getExtensions(IHardwareManagerAdapterFactory.class);
        for (IHardwareManagerAdapterFactory factory : hmaFactories) {
            if (factory instanceof RequiresMqttFeature) {
                ((RequiresMqttFeature)factory).setMqttBroker(mqttBroker);
            }

            if (factory instanceof RequiresFeature) {
                ((RequiresFeature) factory).allFeaturesInjected();
            }

            //System.out.println(">>> " + factory.getName());
        }

        // stop the plugins
        pluginManager.stopPlugins();
    }

}
