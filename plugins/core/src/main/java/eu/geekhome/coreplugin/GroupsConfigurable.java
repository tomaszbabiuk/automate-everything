package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

@Extension
public class GroupsConfigurable extends CategoryConfigurable {

    @Override
    public Class<? extends Configurable> getParent() {
        return null;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_group_title;
    }

    @Override
    public String getIconName() {
        return "groups";
    }
}
