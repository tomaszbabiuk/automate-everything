package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.*;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

@Extension
public class GroupConfigurable extends NameDescriptionConfigurable {

    @Override
    public Class<? extends Configurable> getParent() {
        return GroupConfigurable.class;
    }

    @Override
    public Resource getAddNewRes() {
        return R.configurable_group_add;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_group_title;
    }

    @Override
    public String getIconName() {
        return "group";
    }
}
