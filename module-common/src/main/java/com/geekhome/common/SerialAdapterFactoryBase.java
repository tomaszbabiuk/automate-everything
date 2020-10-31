package com.geekhome.common;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;

import java.io.File;
import java.util.ArrayList;

public abstract class SerialAdapterFactoryBase implements IHardwareManagerAdapterFactory {
    private ILogger _logger = LoggingService.getLogger();

    @Override
    public ArrayList<IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();

        String path = File.separator + "dev" + File.separator + "geekhome" + File.separator + getFactoryType();
        _logger.info("Checking path: " + path);
        File directory = new File(path);
        if (directory.exists()) {
            try {
                File[] aliases = directory.listFiles();
                if (aliases != null) {
                    for (File f : aliases) {
                        _logger.info("Serial port alias found: " + f.getName() + " <-> " + f.getCanonicalPath());
                        String adapterPort = f.getCanonicalPath();
                        String adapterAlias = f.getName();
                        IHardwareManagerAdapter adapter = createAdapter(adapterPort, adapterAlias);
                        result.add(adapter);
                    }
                }
            } catch (Exception e) {
                _logger.error("Adding 1-wire adapter exception", e);
            }
        }

        return result;
    }

    protected abstract String getFactoryType();

    protected abstract IHardwareManagerAdapter createAdapter(String adapterPort, String adapterAlias) throws Exception;
}
