package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;

@Extension
public class DeviceConfigurable extends NameDescriptionConfigurable {

    @Override
    public List<Class<? extends Configurable>> attachableTo() {
        ArrayList<Class<? extends Configurable>> result = new ArrayList<>();
        result.add(DeviceConfigurable.class);
        result.add(GroupConfigurable.class);
        return result;
    }

    @Override
    public Resource getAddNewRes() {
        return R.configurable_group_add;
    }

    @Override
    public String getIconName() {
        return "group";
    }
}