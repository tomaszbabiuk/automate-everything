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
    public Resource getDescriptionRes() {
        return R.configurable_floor_description;
    }

    @Override
    public String getIconRaw() {
        return "<svg width=\"100\" height=\"100\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                " <g>\n" +
                "  <title>Layer 1</title>\n" +
                "  <path d=\"m25.2,5v1.6h-16.4v71.5h27.5v16.9h54.9v-90h-66l0,0zm-5.8,32.3h-4.5v-3.1h4.5v3.1l0,0zm0,0.9v4.2h-4.5v-4.2h4.5l0,0zm0,-4.9h-4.5v-3.1h4.5v3.1l0,0zm0,-4h-4.5v-3.1h4.5v3.1l0,0zm0,-4h-4.5v-3.1h4.5v3.1l0,0zm0,-4h-4.5v-3.1h4.5v3.1l0,0zm0,-8.6v4.6h-4.5v-4.6h4.5l0,0zm23,76.2v-13.9v-3v-25.8h-6.1v25.8h-21.4v-23.5h10.6v-8.9h16.9v-8.9h-6.1v2.8h-10.8v-20.8h5.8v-1.6h53.7v6.5h-26.1v31l-0.1,0c0,0 -0.2,1.6 -1.3,3.2c-1.4,2.1 -3.4,3.1 -6.2,3.1v2.9c4.7,0 7.3,-2.4 8.6,-4.5c0.4,-0.6 0.7,-1.1 0.9,-1.7h6.8v-5.5h-3.3v-23h20.7v22.8h-10.7v6.1h10.7v36.8h-42.6l0,0.1z\" id=\"svg_1\"/>\n" +
                " </g>\n" +
                "</svg>";
    }
}
