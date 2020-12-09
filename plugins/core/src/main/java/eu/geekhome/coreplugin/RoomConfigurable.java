package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

@Extension
public class RoomConfigurable extends NameDescriptionConfigurable {

    @Override
    public Class<? extends Configurable> getParent() {
        return FloorConfigurable.class;
    }

    @Override
    public Resource getAddNewRes() {
        return R.configurable_room_add;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_room_title;
    }

    @Override
    public Resource getDescriptionRes() {
        return R.configurable_room_description;
    }

    @Override
    public String getIconRaw() {
        return "<svg width=\"100\" height=\"100\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                " <g class=\"layer\">\n" +
                "  <title>Created by Carl Holderness</title>\n" +
                "  <path d=\"m86,12l-72,0c-1.1,0 -2,0.9 -2,2l0,71.9c0,1.1 0.9,2 2,2l43.6,0c1.1,0 2,-0.9 2,-2s-0.9,-2 -2,-2l-8.9,0l0,-2.3c0,-0.6 -0.5,-1.1 -1.1,-1.1s-1.1,0.5 -1.1,1.1l0,2.3l-30.5,0l0,-32.8l30.3,0l0,17.6c0,0.6 0.5,1.1 1.1,1.1s1.1,-0.5 1.1,-1.1l0,-17.6l1.5,0c0.6,0 1.1,-0.5 1.1,-1.1c0,-0.3 -0.1,-0.5 -0.3,-0.7l14.4,-14.4l0,5.5c0,0 -0.1,0.1 -0.1,0.1l-8.7,8.7c-0.2,0.2 -0.3,0.5 -0.3,0.8c0,0.3 0.1,0.6 0.3,0.8c0.2,0.2 0.5,0.3 0.8,0.3s0.6,-0.1 0.8,-0.3l0.8,-0.8l6.5,-6.5l0,0.6l0,4.5l-0.3,0.4l-2,0c-0.6,0 -1.1,0.5 -1.1,1c0,0 0,0.1 0,0.1c0,0.6 0.5,1.1 1.1,1.1l2.3,0l0,4.9l0,6l0,5.9c0,0.6 0.5,1.1 1.1,1.1s1.1,-0.5 1.1,-1.1l0,-4.8l16.5,0l0,20.8l-8.7,0c-1.1,0 -2,0.9 -2,2s0.9,2 2,2l10.5,0c0,0 0.1,0 0.1,0c0,0 0.1,0 0.1,0c1.1,0 2,-0.9 2,-2l0,-72c0,-1.1 -0.9,-2 -2,-2zm-70,21.4l17.4,-17.4l5.3,0l-22.7,22.7l0,-5.3zm0,-8.4l8.9,-8.9l5.3,0l-14.2,14.1l0,-5.2zm0,-8.5l0.5,-0.5l5.3,0l-5.8,5.8l0,-5.3zm25.9,-0.5l5.3,0l-31.2,31.1l0,-5.3l25.9,-25.8zm8.4,0l5.3,0l-32.8,32.9l-5.3,0l32.8,-32.9zm8.5,0l5.3,0l-32.9,32.9l-5.3,0l32.9,-32.9zm6.5,2l0,5.3l-25.6,25.6l-5.3,0l30.9,-30.9zm2.2,33.2l0.5,0l8.2,0c0.6,0 1.1,-0.5 1.1,-1.1c0,-0.6 -0.5,-1.1 -1.1,-1.1l-8.2,0l-0.4,0l0,-3.7l3.8,0c0.6,0 1.1,-0.5 1.1,-1.1s-0.5,-1.1 -1.1,-1.1l-3.8,0l0,-27l16.4,0l0,38.9l-16.5,0l0,-3.8zm-19.4,-2.3l-0.7,0l-4.6,0l22.4,-22.4l0,5.3l-17.1,17.1zm19.4,12l0,-3.7l16.5,0l0,3.7l-16.5,0z\" fill=\"black\" id=\"svg_1\"/>\n" +
                " </g>\n" +
                "</svg>";
    }
}
