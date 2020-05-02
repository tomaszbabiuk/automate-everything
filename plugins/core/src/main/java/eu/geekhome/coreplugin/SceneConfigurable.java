package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

@Extension
public class SceneConfigurable extends NameDescriptionConfigurable {

    @Override
    public Class<? extends Configurable> getParent() {
        return null;
    }

    @Override
    public Resource getAddNewRes() {
        return R.configurable_scene_add;
    }

    @Override
    public Resource getDescription() {
        return R.configurable_scene_description;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_scene_title;
    }

    @Override
    public String getIconName() {
        return "scene";
    }
}
