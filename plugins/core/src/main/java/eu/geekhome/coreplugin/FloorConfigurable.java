package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

@Extension
public class FloorConfigurable extends NameDescriptionConfigurable {

    @Override
    public Class<? extends Configurable> getParent() {
        return null;
    }

    @Override
    public Resource getAddNewRes() {
        return R.configurable_floor_add;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_floor_title;
    }

    @Override
    public String getIconName() {
        return "group";
    }
}