package com.geekhome.common.settings;

import com.geekhome.common.INameValueSet;
import com.geekhome.common.NameValueSet;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.configuration.ObjectNotFoundException;

import java.io.*;
import java.util.Hashtable;

public class TextFileAutomationSettingsPersister  {
    private final String _fileName;
    private ILogger _logger = LoggingService.getLogger();

    public TextFileAutomationSettingsPersister() throws IOException {
        _fileName = createFileName();
    }

    private static String createFileName() throws IOException {
        String currentDir = new File(".").getCanonicalPath();
        return currentDir + File.separator + "Settings.dat";
    }

    public void save(Hashtable<String, Hashtable<String, String>> tables) {
        _logger.info("Saving automation settings");
        File file = new File(_fileName);

        _logger.debug("Saving core configuration");
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter writer = new BufferedWriter(fw);
            for (String tableName : tables.keySet()) {
                Hashtable<String, String> tableContent = tables.get(tableName);
                writeHashtable(writer, tableContent, tableName);
            }
            writer.flush();
        } catch (Exception ex) {
            _logger.error("Exception while saving automation settings", ex);
        }

        _logger.info("Automation settings metadataExtracted");
    }

    protected interface LoadSectionDelegate {
        void loadSection(String sectionName, INameValueSet values) throws Exception;
    }

    public void load(final Hashtable<String, Hashtable<String, String>> target) throws Exception {
        _logger.info("Loading automation settings");

        final LoadSectionDelegate loadingDelegate = new LoadSectionDelegate() {
            @Override
            public void loadSection(String sectionName, INameValueSet values) throws ObjectNotFoundException {
                final Hashtable<String, String> result = new Hashtable<>();

                for (String key : values.getKeys()) {
                    String value = values.getValue(key);
                    result.put(key, value);
                }

                target.put(sectionName, result);
            }
        };

        loading(loadingDelegate);

        _logger.info("Automation settings loaded");
    }

    protected void loading(LoadSectionDelegate loadingFunction) throws Exception {
        File f = new File(_fileName);
        if (f.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                if ((line.length() > 1) && (line.charAt(0) == '[') && (line.charAt(line.length() - 1) == ']')) {
                    String sectionName = line.substring(1, line.length() - 1);
                    INameValueSet props = new NameValueSet();

                    while ((line = br.readLine()) != null && !line.equals("")) {
                        String key = "";
                        try {
                            key = line.substring(0, line.indexOf(" = "));
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        String value = line.substring(line.indexOf(" = ") + 3, line.length());
                        props.add(key, value);
                    }

                    loadingFunction.loadSection(sectionName, props);
                } else {
                    break;
                }
            }
            br.close();
        }
    }

    protected static void writeHashtable(BufferedWriter writer, Hashtable<String, String> hashtable, String sectionName) throws Exception {
        writer.write("[" + sectionName + "]" + "\n");
        for (String key : hashtable.keySet())
        {
            String value = hashtable.get(key);
            writer.write(String.format("%s = %s\n", key, value));
        }
        writer.write("\n");
    }
}