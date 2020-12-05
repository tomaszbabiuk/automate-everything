package eu.geekhome.sqldelightplugin;

import com.geekhome.common.extensibility.PluginMetadata;
import com.geekhome.common.localization.Resource;

import org.pf4j.PluginWrapper;
import org.pf4j.Plugin;

public class SqlDelightPlugin extends Plugin implements PluginMetadata {

    public SqlDelightPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("SQLDelight plugin.start()");
    }

    @Override
    public void stop() {
        System.out.println("SQLDelight plugin.stop()");
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
