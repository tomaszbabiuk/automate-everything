package eu.geekhome.shelly;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;


public class ShellyPlugin extends Plugin {

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
}
