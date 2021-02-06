package eu.geekhome.coreplugin;

import eu.geekhome.services.configurable.Configurable;
import eu.geekhome.services.localization.Resource;
import org.pf4j.Extension;

@Extension
public class ConditionConfigurable extends CategoryConfigurable {

    @Override
    public Class<? extends Configurable> getParent() {
        return null;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_condition_title;
    }

    @Override
    public Resource getDescriptionRes() {
        return R.configurable_condition_description;
    }

    @Override
    public String getIconRaw() {
        return "<svg width=\"100\" height=\"100\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                " <g class=\"layer\">\n" +
                "  <title>Created by akash k from the Noun Project</title>\n" +
                "  <polygon id=\"svg_1\" points=\"99.761,79.929 96.717,76.942 85.998,87.613 81.754,83.386 78.709,86.375 85.998,93.632 \"/>\n" +
                "  <polygon id=\"svg_2\" points=\"2.939,92.933 7.919,87.974 12.901,92.933 15.6,90.245 10.62,85.287 15.6,80.328 12.901,77.641 7.919,82.599   2.939,77.641 0.239,80.328 5.22,85.287 0.239,90.245 \"/>\n" +
                "  <path d=\"m67.231,50.267l-17.116,-17.117l0,-10.149l-4,0l0,10.15l-17.116,17.116l-23.08,0l0,24.397l4,0l0,-20.397l19.079,0l19.116,19.116l19.116,-19.116l19.662,0l0,20.397l4,0l0,-24.397l-23.661,0zm-19.116,17.459l-15.459,-15.459l15.459,-15.459l15.459,15.459l-15.459,15.459z\" id=\"svg_3\"/>\n" +
                "  <g id=\"svg_4\">\n" +
                "   <path d=\"m48.406,20.304c-4.255,0 -7.718,-3.462 -7.718,-7.718s3.462,-7.718 7.718,-7.718c4.256,0 7.718,3.462 7.718,7.718s-3.462,7.718 -7.718,7.718zm0,-12.435c-2.602,0 -4.718,2.116 -4.718,4.718s2.116,4.718 4.718,4.718s4.718,-2.116 4.718,-4.718s-2.116,-4.718 -4.718,-4.718z\" id=\"svg_5\"/>\n" +
                "  </g>\n" +
                " </g>\n" +
                "</svg>";
    }
}