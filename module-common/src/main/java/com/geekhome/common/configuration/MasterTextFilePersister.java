package com.geekhome.common.configuration;

import com.geekhome.common.*;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.utils.FileFinder;
import com.geekhome.common.utils.IFileFoundListener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

public class MasterTextFilePersister extends ConfigurationProviderBase {
    @SuppressWarnings("FieldCanBeLocal")
    private static final String CONFIGURATION_NAME = "Configuration";
    protected ILogger _logger = LoggingService.getLogger();
    private final ReentrantLock _lock = new ReentrantLock();
    private final String _fileName;
    private BufferedWriter _writer;

    private String getFileName() {
        return _fileName;
    }

    private String createFileName() throws IOException {
        String currentDir = new File(".").getCanonicalPath();
        return String.format("%s%s%s.dat", currentDir, File.separator, CONFIGURATION_NAME);
    }

    public MasterTextFilePersister(IdPool pool) throws IOException {
        super(pool);
        _fileName = createFileName();

    }

    private SectionMetadata createVersionMetadata(ConfigurationMetadata configurationMetadata) {
        INameValueSet versionProperties = new NameValueSet();
        versionProperties.add("version", "1");
        versionProperties.add("pool", String.valueOf(configurationMetadata.getMaxPool()));
        versionProperties.add("comment", String.valueOf(configurationMetadata.getComment()));
        versionProperties.add("from", String.valueOf(configurationMetadata.getFrom()));

        return new SectionMetadata("Version", null, versionProperties);
    }

    @Override
    protected void metadataExtracted(ConfigurationMetadata metadata) {
        String backupFileName = getFileName() + "." + getPool().getCurrentId();
        File backupFile = new File(backupFileName);
        if (backupFile.exists()) {
            backupFile.delete();
        }

        File file = new File(getFileName());
        if (file.exists() && !backupFile.exists()) {
            file.renameTo(backupFile);
        }

        _lock.tryLock();
        _writer = null;
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            _writer = new BufferedWriter(fw);

            persistSection(createVersionMetadata(metadata));
            for(SectionMetadata sectionMetadata : metadata.getSections()) {
                persistSection(sectionMetadata);
            }

            _writer.flush();

        } catch (Exception ex) {
            _logger.error("Exception while saving configuration" , ex);
        } finally {
            _lock.unlock();

            if (_writer != null) {
                try {
                    _writer.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private void persistSection(SectionMetadata sectionMetadata) throws IOException {
        if (sectionMetadata.getParentUniqueId() == null) {
            _writer.write("[" + sectionMetadata.getName() + "]\n");
        } else {
            _writer.write("[" + sectionMetadata.getName() + " = " + sectionMetadata.getParentUniqueId() + "]\n");
        }

        for (String key : sectionMetadata.getProperties().getKeys()) {
            _writer.write(key + " = " + sectionMetadata.getProperties().getValue(key) + "\n");
        }

        _writer.write("\n");
    }


    @Override
    protected ConfigurationMetadata loadMetadata(String backupId) throws Exception {
        File f;
        if (backupId == null) {
            f = new File(_fileName);
        } else {
            f = new File(backupId);
        }
        return loadMetadataFromFile(f, false);
    }

    private ConfigurationMetadata loadMetadataFromFile(File f, boolean mainSectionOnly) throws Exception {
        ConfigurationMetadata configurationMetadata = new ConfigurationMetadata();

        if (f.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                if ((line.length() > 1) && (line.charAt(0) == '[') && (line.charAt(line.length() - 1) == ']')) {
                    String sectionName = line.substring(1, line.length() - 1);
                    String sectionParameter = null;
                    if (sectionName.indexOf(" = ") > 0) {
                        String[] sectionFullNameSplitted = sectionName.split(" = ");
                        sectionParameter = sectionFullNameSplitted[1];
                        sectionName = sectionFullNameSplitted[0];
                    }

                    INameValueSet props = new NameValueSet();
                    while ((line = br.readLine()) != null && !line.equals("")) {
                        String key = line.substring(0,line.indexOf(" = "));
                        String value = line.substring(line.indexOf(" = ") + 3);
                        props.add(key, value);
                    }

                    SectionMetadata sectionMetadata = new SectionMetadata(sectionName, sectionParameter, props);
                    if (sectionMetadata.getName().equals("Version")) {
                        int maxPool = Integer.parseInt(sectionMetadata.getProperties().getValue("pool"));
                        String comment = sectionMetadata.getProperties().getValue("comment");
                        String from = sectionMetadata.getProperties().getValue("from");
                        configurationMetadata.setMaxPool(maxPool);
                        configurationMetadata.setComment(comment);
                        configurationMetadata.setFrom(from);

                        if (mainSectionOnly) {
                            break;
                        }
                    }

                    configurationMetadata.getSections().add(sectionMetadata);
                } else {
                    break;
                }
            }
            br.close();

        }

        return configurationMetadata;
    }

    @Override
    public CollectorCollection<BackupInfo> getBackups() throws Exception {
        final CollectorCollection<BackupInfo> result = new CollectorCollection<>();

        FileFinder backupsFinder = new FileFinder("Configuration.da*", new IFileFoundListener() {
            @Override
            public void fileFound(Path file) {
                final File f = file.toFile();
                try {
                    ConfigurationMetadata metadata = loadMetadataFromFile(f, true);
                    if (f.getName().equalsIgnoreCase("configuration.dat")) {
                        metadata.setCurrent(true);
                    }
                    DescriptiveName descriptiveName = new DescriptiveName(metadata.getComment(), f.getName());
                    BackupInfo backupInfo = new BackupInfo(descriptiveName, metadata.getMaxPool(), metadata.getFrom(), metadata.isCurrent());
                    result.add(backupInfo);
                } catch (Exception e) {
                    _logger.error("Problem creating backups list",e);
                }
            }
        });
        Files.walkFileTree(Paths.get("."), backupsFinder);

        return result;
    }
}