package eu.geekhome.coreplugin;

import eu.geekhome.services.configurable.*;
import eu.geekhome.services.localization.Resource;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;

@Extension
public class TwilightConditionConfigurable extends NameDescriptionConfigurable {

    @Override
    public List<FieldDefinition<?>> getFieldDefinitions() {
        ArrayList<FieldDefinition<?>> result = new ArrayList<>(super.getFieldDefinitions());
        result.add(longitudeField);
        result.add(latitudeField);
        return result;
    }

    @Override
    public Class<? extends Configurable> getParent() {
        return ConditionConfigurable.class;
    }

    @Override
    public Resource getAddNewRes() {
        return R.configurable_twilightcondition_add;
    }

    @Override
    public Resource getEditRes() {
        return R.configurable_twilightcondition_edit;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_twilightcondition_title;
    }

    @Override
    public Resource getDescriptionRes() {
        return R.configurable_twilightcondition_description;
    }

    @Override
    public List<BlockTarget> getBlockTargets() {
        return null;
    }

    @Override
    public String getIconRaw() {
        return "<svg width=\"100\" height=\"100\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                " <g>\n" +
                "  <title>Layer 1</title>\n" +
                "  <path fill=\"black\" d=\"m61,100c-27.57,0 -50,-22.43 -50,-50s22.43,-50 50,-50c4.80099,0 9.575,0.691 14.188,2.054l12.019,3.552l-12.019,3.552c-17.96,5.307 -30.50401,22.102 -30.50401,40.842c0,18.739 12.544,35.533 30.503,40.84l12.01501,3.549l-12.014,3.555c-4.614,1.364 -9.38701,2.056 -14.188,2.056l0,0zm0,-92.593c-23.486,0 -42.592,19.108 -42.592,42.593s19.106,42.59299 42.592,42.59299c0.043,0 0.086,0 0.13,0c-14.488,-8.958 -23.853,-25.04999 -23.853,-42.59299c0,-17.542 9.365,-33.633 23.851,-42.593c-0.044,0 -0.086,0 -0.128,0l0,0z\" id=\"svg_1\"/>\n" +
                " </g>\n" +
                "</svg>";
    }

    @Override
    public ConfigurableType getType() {
        return ConfigurableType.Condition;
    }

    private final DoubleField longitudeField = new DoubleField("longitude", R.field_latitude_hint, 0, new RequiredDoubleValidator());

    private final DoubleField latitudeField = new DoubleField("latitude", R.field_latitude_hint, 0, new RequiredDoubleValidator());

}
