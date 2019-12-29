package eu.geekhome;

import com.geekhome.common.extensibility.RequiresFeature;
import com.geekhome.common.extensibility.RequiresMqttFeature;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.moquettemodule.MoquetteBroker;
import com.geekhome.moquettemodule.MqttBroker;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import java.util.List;
import java.util.Set;

/**
 * A boot class that start the application.
 */
public class Boot {

    public static void main(String[] args) {


        MqttBroker mqttBroker = new MoquetteBroker();

        PluginManager pluginManager = new DefaultPluginManager();
        pluginManager.loadPlugins();

        // enable a disabled plugin
//        pluginManager.enablePlugin("welcome-plugin");

        // start (active/resolved) the plugins
        pluginManager.startPlugins();

        // retrieves the extensions for Greeting extension point
        List<IHardwareManagerAdapter> greetings = pluginManager.getExtensions(IHardwareManagerAdapter.class);
        System.out.println(String.format("Found %d extensions for extension point '%s'", greetings.size(), IHardwareManagerAdapter.class.getName()));
        for (IHardwareManagerAdapter greeting : greetings) {
            if (greeting instanceof RequiresMqttFeature) {
                ((RequiresMqttFeature)greeting).setMqttBroker(mqttBroker);
            }

            if (greeting instanceof RequiresFeature) {
                ((RequiresFeature) greeting).allFeaturesInjected();
            }

            System.out.println(">>> " + greeting.getName());
        }

        // print extensions from classpath (non plugin)
        System.out.println("Extensions added by classpath:");
        Set<String> extensionClassNames = pluginManager.getExtensionClassNames(null);
        for (String extension : extensionClassNames) {
            System.out.println("   " + extension);
        }

        System.out.println("Extension classes by classpath:");
        List<Class<? extends IHardwareManagerAdapter>> greetingsClasses = pluginManager.getExtensionClasses(IHardwareManagerAdapter.class);
        for (Class<? extends IHardwareManagerAdapter> greeting : greetingsClasses) {
            System.out.println("   Class: " + greeting.getCanonicalName());
        }

        // print extensions ids for each started plugin
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            System.out.println(String.format("Extensions added by plugin '%s':", pluginId));
            extensionClassNames = pluginManager.getExtensionClassNames(pluginId);
            for (String extension : extensionClassNames) {
                System.out.println("   " + extension);
            }
        }

        // print extensions instances for Greeting extension point for each started plugin
        for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            System.out.println(String.format("Extensions instances added by plugin '%s' for extension point '%s':", pluginId, IHardwareManagerAdapter.class.getName()));
            List<IHardwareManagerAdapter> extensions = pluginManager.getExtensions(IHardwareManagerAdapter.class, pluginId);
            for (Object extension : extensions) {
                System.out.println("   " + extension);
            }
        }

        // print extensions instances from classpath (non plugin)
        System.out.println("Extensions instances added by classpath:");
        List extensions = pluginManager.getExtensions((String) null);
        for (Object extension : extensions) {
            System.out.println("   " + extension);
        }

        // print extensions instances for each started plugin
        for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            System.out.println(String.format("Extensions instances added by plugin '%s':", pluginId));
            extensions = pluginManager.getExtensions(pluginId);
            for (Object extension : extensions) {
                System.out.println("   " + extension);
            }
        }

        // stop the plugins
        pluginManager.stopPlugins();
        /*
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                pluginManager.stopPlugins();
            }

        });
        */
    }

}
