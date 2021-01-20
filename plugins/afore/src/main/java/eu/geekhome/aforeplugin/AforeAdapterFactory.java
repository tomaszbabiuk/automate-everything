package eu.geekhome.aforeplugin;

import eu.geekhome.services.hardware.HardwareAdapter;
import eu.geekhome.services.hardware.HardwareAdapterFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class AforeAdapterFactory implements HardwareAdapterFactory {

    @NotNull
    public static final String ID = "AFORE";

    @Override
    public List<HardwareAdapter> createAdapters() {
        ArrayList<HardwareAdapter> result = new ArrayList<>();
        AforeAdapter adapter = new AforeAdapter();
        result.add(adapter);

        return result;
    }

    @Override
    public String getId() {
        return ID;
    }
}