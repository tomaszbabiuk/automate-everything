package eu.geekhome.coreplugin;

import com.geekhome.common.localization.Resource;
import eu.geekhome.plugins.PluginMetadata;

import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;
import org.pf4j.Plugin;

public class CorePlugin extends Plugin implements PluginMetadata {

    public CorePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("WelcomePlugin.start()");
        // for testing the development mode
//        if (RuntimeMode.DEVELOPMENT.equals(wrapper.getRuntimeMode())) {
//            System.out.println(StringUtils.upperCase("WelcomePlugin"));
//        }
    }

    @Override
    public void stop() {
        System.out.println("WelcomePlugin.stop()");
    }

    @Override
    public Resource getName() {
        return new Resource("CORE:plugin_name", "Core", "Core");
    }

    @Override
    public Resource getDescription() {
        return new Resource("CORE:plugin_description",
                "Core definitions of devices and conditions",
                "Główne definicje urządzeń i warunków");
    }
}
