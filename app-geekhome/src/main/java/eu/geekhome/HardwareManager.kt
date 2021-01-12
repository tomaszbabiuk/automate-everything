package eu.geekhome;

import eu.geekhome.services.events.EventsSink;
import eu.geekhome.services.events.NumberedEventsSink;
import eu.geekhome.services.hardware.*;
import org.pf4j.PluginManager;
import org.pf4j.PluginStateEvent;
import org.pf4j.PluginStateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HardwareManager implements PluginStateListener {

    private final Map<HardwareAdapterFactory, List<AdapterBundle>> _factories =
            new HashMap<>();

    private final PluginManager _pluginManager;

    public List<HardwareAdapter> getFactories() {
        return _factories
                .entrySet()
                .stream()
                .flatMap(factory -> factory.getValue().stream())
                .map(bundle ->  bundle.adapter)
                .collect(Collectors.toList());
    }

    public void discover() {
        _factories.forEach((factory, adapterBundles) -> {
            adapterBundles.forEach(bundle -> {
                PortIdBuilder builder = new PortIdBuilder(factory.getId(), bundle.adapter.getId());
                bundle.adapter.discover(builder, bundle.ports, bundle.sink);
            });
        });
    }

    public HardwareManager(PluginManager pluginManager) {
        _pluginManager = pluginManager;
        pluginManager.addPluginStateListener(this);
        reloadAdapters();
    }

    private void reloadAdapters() {
        _pluginManager
            .getPlugins()
            .stream()
            .filter(plugin -> plugin.getPlugin() instanceof HardwarePlugin)
            .map(plugin -> ((HardwarePlugin) plugin.getPlugin()).getFactory())
            .forEach(factory -> {
                _factories.remove(factory);

                List<HardwareAdapter> adaptersInFactory = factory.createAdapters();
                List<AdapterBundle> adapterBundles = adaptersInFactory
                        .stream()
                        .map(adapter -> new AdapterBundle(adapter, new NumberedEventsSink<>(), new ArrayList<>()))
                        .collect(Collectors.toList());
                _factories.put(factory, adapterBundles);
            });
    }

    @Override
    public void pluginStateChanged(PluginStateEvent event) {
        reloadAdapters();
    }

    static class AdapterBundle {

        private final HardwareAdapter adapter;
        private final EventsSink<String> sink;
        private final List<Port> ports;

        public AdapterBundle(HardwareAdapter adapter, EventsSink<String> sink, List<Port> ports) {
            this.adapter = adapter;
            this.sink = sink;
            this.ports = ports;
        }
    }
}