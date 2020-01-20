package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

import java.util.List;

@Extension
public class SceneConfigurable extends NameDescriptionConfigurable {

    @Override
    public List<Class<? extends Configurable>> attachableTo() {
        return null;
    }

    @Override
    public Resource getAddNewRes() {
        return R.configurable_scene_add;
    }

    @Override
    public String getIconName() {
        return "scene";
    }
}
