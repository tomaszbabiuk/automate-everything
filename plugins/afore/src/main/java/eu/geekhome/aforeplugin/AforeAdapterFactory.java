package eu.geekhome.aforeplugin;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.localization.Resource;
import eu.geekhome.services.hardware.HardwareAdapter;
import eu.geekhome.services.hardware.HardwareAdapterFactory;

import java.util.ArrayList;
import java.util.List;

class AforeAdapterFactory implements HardwareAdapterFactory {

    @Override
    public List<HardwareAdapter> createAdapters() {
        ArrayList<HardwareAdapter> result = new ArrayList<>();
        AforeAdapter adapter = new AforeAdapter();
        result.add(adapter);

        return result;
    }

    @Override
    public String getId() {
        return "AFORE";
    }
}