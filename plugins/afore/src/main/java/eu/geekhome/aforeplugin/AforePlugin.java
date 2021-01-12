package eu.geekhome.aforeplugin;

import com.geekhome.common.extensibility.PluginMetadata;
import com.geekhome.common.localization.Resource;
import eu.geekhome.services.hardware.HardwareAdapterFactory;
import eu.geekhome.services.hardware.HardwarePlugin;
import org.pf4j.*;

public class AforePlugin extends HardwarePlugin implements PluginMetadata {

    private final AforeAdapterFactory _factory;

    public AforePlugin(PluginWrapper wrapper) {
        super(wrapper);
        _factory = new AforeAdapterFactory();
    }

    @Override
    public HardwareAdapterFactory getFactory() {
        return _factory;
    }

    @Override
    public void start() {
        System.out.println("Starting AFORE plugin");
    }

    @Override
    public void stop() {
        System.out.println("Stopping AFORE plugin");
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
