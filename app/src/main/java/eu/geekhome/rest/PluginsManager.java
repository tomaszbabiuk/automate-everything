//package eu.geekhome.rest;
//
//
//import com.geekhome.common.configurable.Configurable;
//import org.jvnet.hk2.annotations.Service;
//
//import java.util.List;
//
//@Service
//public class PluginsManager {
//
//    private  DefaultPluginManager _pluginManager;
//
//    public PluginsManager() {
//        _pluginManager = null; //TODO: new DefaultPluginManager();
//        _pluginManager.loadPlugins();
//    }
//
//    public List<PluginWrapper> getPlugins() {
//        return _pluginManager
//                .getPlugins();
//    }
//
//
//    public PluginWrapper getPlugin(String id) {
//        return _pluginManager.getPlugin(id);
//    }
//
//    public PluginWrapper enablePlugin(String id) {
//        _pluginManager.enablePlugin(id);
//        _pluginManager.startPlugin(id);
//        return getPlugin(id);
//    }
//
//    public PluginWrapper disablePlugin(String id) {
//        _pluginManager.stopPlugin(id);
//        _pluginManager.disablePlugin(id);
//        return getPlugin(id);
//    }
//
//    public List<Configurable> getConfigurables() {
//        return _pluginManager
//                .getExtensions(Configurable.class);
//    }
//}
