package eu.geekhome.services.hardware;

import java.util.List;

public interface HardwareAdapterFactory {

    List<HardwareAdapter> createAdapters();
    String getId();
}