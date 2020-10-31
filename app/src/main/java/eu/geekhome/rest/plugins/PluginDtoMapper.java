//package eu.geekhome.rest.plugins;
//
//import eu.geekhome.rest.PluginMetadata;
//import eu.geekhome.rest.plugins.PluginDto;
//import org.pf4j.Plugin;
//import org.pf4j.PluginState;
//import org.pf4j.PluginWrapper;
//
//public class PluginDtoMapper {
//
//    public PluginDto map(PluginWrapper pluginWrapper) {
//        Plugin plugin = pluginWrapper.getPlugin();
//        String id = pluginWrapper.getPluginId();
//        String version = pluginWrapper.getDescriptor().getVersion();
//        String provider = pluginWrapper.getDescriptor().getProvider();
//        boolean enabled = pluginWrapper.getPluginState() == PluginState.STARTED;
//
//        if (plugin instanceof PluginMetadata) {
//            PluginMetadata metadata = (PluginMetadata)plugin;
//            return new PluginDto(id, metadata.getName(), metadata.getDescription(), provider, version, enabled);
//        } else {
//            return new PluginDto(id,null, null, provider, version, enabled);
//        }
//    }
//}
