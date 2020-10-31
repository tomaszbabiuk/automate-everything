package com.geekhome.common.configuration;

import com.geekhome.common.ConfigurationMetadata;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.configuration.CollectorCollection;

import java.util.ArrayList;

interface IConfigurationProvider {
    void save(ArrayList<Collector> collectors, String comment) throws Exception;
    ConfigurationMetadata load(ArrayList<Collector> targets, String backupId) throws Exception;
    CollectorCollection<BackupInfo> getBackups() throws Exception;
}