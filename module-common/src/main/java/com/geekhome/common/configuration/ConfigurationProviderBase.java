package com.geekhome.common.configuration;

import com.geekhome.common.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class ConfigurationProviderBase implements IConfigurationProvider {
    private final IdPool _pool;
    private final SimpleDateFormat _sdf;

    protected IdPool getPool() {
        return _pool;
    }

    public ConfigurationProviderBase(IdPool pool) {
        _pool = pool;
        _sdf = new SimpleDateFormat();
    }

    @Override
    public void save(ArrayList<Collector> collectors, String comment) throws Exception {
        String from = _sdf.format(Calendar.getInstance().getTime());
        ConfigurationMetadata configurationMetadata = new ConfigurationMetadata("1.0", _pool.getCurrentId(), comment, from);

        for (Collector collector : collectors) {
            extractSectionMetadata(collector, null, configurationMetadata.getSections());
        }

        metadataExtracted(configurationMetadata);
    }

    private void extractSectionMetadata(Object target, String parentId, ArrayList<SectionMetadata> metadata) throws Exception {
        Class c = target.getClass();
        for (Method f : c.getMethods()) {
            ConfigurationSaver configurationSection = f.getAnnotation(ConfigurationSaver.class);
            if (configurationSection != null) {
                CollectorCollection collectorCollection = (CollectorCollection) f.invoke(target);
                reflectCollectionData(collectorCollection, configurationSection.sectionName(), parentId, metadata);
                if (configurationSection.hasChildren()) {
                    for (Object child : collectorCollection.values()) {
                        if (child instanceof NamedObject) {
                            String id = ((NamedObject)child).getName().getUniqueId();
                            extractSectionMetadata(child, id, metadata);
                        }
                    }
                }
            }
        }
    }

    private void reflectCollectionData(CollectorCollection collection, String sectionName, String parentUniqueId, ArrayList<SectionMetadata> metadata) throws Exception {
        for (Object item : collection.values()) {
            final INameValueSet properties = new NameValueSet();
            PersistableReflector.reflect(item, new PersistableReflector.IObjectReflectedDelegate() {
                @Override
                public void execute(String persistableName, boolean readOnly, Object value) throws IOException {
                if (!readOnly) {
                    if (value instanceof DescriptiveName) {
                        DescriptiveName dn = (DescriptiveName) value;
                        properties.add("name", dn.getName());
                        properties.add("description", dn.getDescription());
                        properties.add("uniqueid", dn.getUniqueId());
                    } else {
                        if (!(value instanceof CollectorCollection)) {
                            properties.add(persistableName.toLowerCase(), value.toString());
                        }
                    }
                }
                }
            });

            if (properties.getKeys().size() > 0) {
                SectionMetadata sectionMetadata = new SectionMetadata(sectionName, parentUniqueId, properties);
                metadata.add(sectionMetadata);
            }
        }
    }

    public ConfigurationMetadata load(ArrayList<Collector> targets, String backupId) throws Exception {
        ConfigurationMetadata configurationMetadata = loadMetadata(backupId);

        if (configurationMetadata != null) {
            getPool().setCurrentId(configurationMetadata.getMaxPool());
            for (Collector target : targets) {
                Class targetClass = target.getClass();
                Method[] methods = targetClass.getMethods();

                for (SectionMetadata sectionMetadata : configurationMetadata.getSections()) {
                    loadSection(target, methods, sectionMetadata);
                }
            }
        }

        return configurationMetadata;
    }

    private void loadSection(Collector target, Method[] methods, SectionMetadata sectionMetadata) throws InvocationTargetException, IllegalAccessException {
        for (Method f : methods) {
            ConfigurationLoader configurationLoader = f.getAnnotation(ConfigurationLoader.class);
            if (configurationLoader != null && configurationLoader.sectionName().equals(sectionMetadata.getName())) {
                if (!configurationLoader.parentId().equals("")) {
                    sectionMetadata.getProperties().add(configurationLoader.parentId(), sectionMetadata.getParentUniqueId());
                }
                f.invoke(target, CrudAction.AddOrCreate, sectionMetadata.getProperties());
            }
        }
    }

    protected abstract void metadataExtracted(ConfigurationMetadata metadata) throws Exception;
    protected abstract ConfigurationMetadata loadMetadata(String backupId) throws Exception;
}
