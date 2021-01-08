package eu.geekhome.aforeplugin;

import com.geekhome.common.hardwaremanager.HardwareManager;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.localization.Resource;

import java.util.ArrayList;

class AforeAdapterFactory implements IHardwareManagerAdapterFactory {

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        AforeAdapter adapter = new AforeAdapter();
        result.add(adapter);

        return result;
    }

    @Override
    public Resource getName() {
        return R.factory_name;
    }
}