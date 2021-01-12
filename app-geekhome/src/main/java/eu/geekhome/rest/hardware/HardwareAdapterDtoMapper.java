package eu.geekhome.rest.hardware;

import eu.geekhome.services.hardware.HardwareAdapter;
import eu.geekhome.services.hardware.HardwareAdapterDto;

import javax.inject.Inject;

public class HardwareAdapterDtoMapper {

    @Inject
    HardwareAdapterDtoMapper() {
    }

    public HardwareAdapterDto map(HardwareAdapter adapter) {
        return new HardwareAdapterDto(adapter.getState(), adapter.getLastDiscoveryTime(), adapter.getLastError().toString());
    }
}
