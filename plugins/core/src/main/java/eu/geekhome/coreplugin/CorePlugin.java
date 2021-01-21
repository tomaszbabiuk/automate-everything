package eu.geekhome.coreplugin;

import eu.geekhome.services.extensibility.PluginMetadata;
import eu.geekhome.services.localization.Resource;

import org.pf4j.PluginWrapper;
import org.pf4j.Plugin;

public class CorePlugin extends Plugin implements PluginMetadata {

    public CorePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("WelcomePlugin.start()");
    }

    @Override
    public void stop() {
        System.out.println("WelcomePlugin.stop()");
    }

    @Override
    public Resource getName() {
        return R.plugin_name;
    }

    @Override
    public Resource getDescription() {
        return R.plugin_description;
    }
}
