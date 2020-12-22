package eu.geekhome.coreplugin;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.localization.Resource;
import org.pf4j.Extension;

@Extension
public class DeviceConfigurable extends CategoryConfigurable {

    @Override
    public Class<? extends Configurable> getParent() {
        return null;
    }

    @Override
    public Resource getTitleRes() {
        return R.configurable_device_title;
    }

    @Override
    public Resource getDescriptionRes() {
        return R.configurable_device_description;
    }

    @Override
    public String getIconRaw() {
        return "<svg width=\"100\" height=\"100\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                " <title>Smarthome</title>\n" +
                " <g class=\"layer\">\n" +
                "  <title>Created by Alfan Zulkarnain\n" +
                "from the Noun Project</title>\n" +
                "  <g id=\"svg_1\">\n" +
                "   <path d=\"m64.36041,56.75399l0,17.50778c0,11.77276 -7.87154,18.90369 -15.4711,18.90369a11.71132,12.31094 0 0 1 -8.73549,-3.93547a15.88707,16.7005 0 0 1 -3.75978,-11.63822c0,-9.21639 -3.29581,-16.17914 -9.05547,-19.08869a12.31928,12.95004 0 0 0 -13.13523,1.68182a1.59991,1.68182 0 0 1 -2.25587,-0.26909a1.59991,1.68182 0 0 1 0.25599,-2.35455a15.4711,16.26323 0 0 1 16.52704,-2.08546c6.9116,3.48137 10.86337,11.55412 10.86337,22.11597a12.79925,13.45458 0 0 0 2.97583,9.43503a8.5435,8.98093 0 0 0 6.39963,2.8591c5.95165,0 12.27129,-5.44911 12.27129,-15.54004l0,-17.59187a13.37522,14.06004 0 0 0 3.19981,0l-0.08,0z\" id=\"svg_13\"/>\n" +
                "   <path d=\"m7.40372,69.92266a1.59991,1.68182 0 0 1 -0.47997,0a1.59991,1.68182 0 0 1 -1.03994,-2.1191a26.03048,27.36326 0 0 1 0.99194,-2.70774a1.59991,1.68182 0 0 1 2.91183,1.37909a20.79879,21.8637 0 0 0 -0.84795,2.35455a1.59991,1.68182 0 0 1 -1.53591,1.09318z\" id=\"svg_12\"/>\n" +
                "   <path d=\"m6.26779,76.0445l-0.14399,0a1.59991,1.68182 0 0 1 -1.45592,-1.81637c0,-0.43727 0.08,-0.84091 0.12799,-1.26137a1.59991,1.68182 0 0 1 3.19981,0.43727c0,0.37 -0.09599,0.74 -0.12799,1.12682a1.59991,1.68182 0 0 1 -1.59991,1.51364z\" id=\"svg_11\"/>\n" +
                "   <path d=\"m71.54399,8.56976l0,14.16095l-3.19981,0l0,-14.16095a1.59991,1.68182 0 0 1 3.19981,0z\" id=\"svg_10\"/>\n" +
                "   <path d=\"m57.27282,8.56976l0,14.16095l-3.19981,0l0,-14.16095a1.59991,1.68182 0 0 1 3.19981,0z\" id=\"svg_9\"/>\n" +
                "   <path d=\"m50.44122,24.41253l0,17.84414a12.38328,13.01731 0 0 0 24.75056,0l0,-17.84414l-24.75056,0zm10.39939,9.18275a1.98388,2.08546 0 1 1 1.91989,2.06864a1.96789,2.06864 0 0 1 -1.91989,-2.06864zm11.05536,9.23321a11.32734,11.90731 0 0 1 -7.02359,8.56048a1.80789,1.90046 0 0 1 -0.57597,0.10091a1.59991,1.68182 0 0 1 -0.55997,-3.24592a8.12753,8.54366 0 0 0 5.03971,-6.13865a1.59991,1.68182 0 0 1 1.98388,-1.27819a1.59991,1.68182 0 0 1 1.13593,2.00137z\" id=\"svg_8\"/>\n" +
                "   <path d=\"m10.79553,63.14492a1.7119,1.79955 0 0 1 -1.00794,-0.37a1.59991,1.68182 0 0 1 -0.23999,-2.37137a16.81502,17.67596 0 0 1 2.71984,-2.8591a1.59991,1.68182 0 0 1 1.99988,2.57319a14.49516,15.23732 0 0 0 -2.22387,2.38819a1.59991,1.68182 0 0 1 -1.24793,0.63909z\" id=\"svg_7\"/>\n" +
                "   <path d=\"m93.6547,13.37977a1.59991,1.68182 0 0 1 -1.02394,-0.40364a11.19935,11.77276 0 0 0 -14.39916,0a1.59991,1.68182 0 1 1 -2.04788,-2.57319a14.39916,15.13641 0 0 1 18.57492,0a1.59991,1.68182 0 0 1 -1.02394,2.97683l-0.08,0z\" id=\"svg_6\"/>\n" +
                "   <path d=\"m91.11085,17.75251a1.59991,1.68182 0 0 1 -1.13593,-0.48773a6.52762,6.86184 0 0 0 -9.02347,0a1.59991,1.68182 0 0 1 -2.27187,0a1.59991,1.68182 0 0 1 0,-2.37137a9.59944,10.09094 0 0 1 13.53521,0a1.59991,1.68182 0 0 1 -1.11993,2.87592l0.016,-0.01682z\" id=\"svg_5\"/>\n" +
                "   <path d=\"m88.85498,21.80571a1.59991,1.68182 0 0 1 -1.13593,-0.48773a3.19981,3.36365 0 0 0 -4.51174,0a1.59991,1.68182 0 0 1 -2.25587,-2.37137a6.52762,6.86184 0 0 1 9.02347,0a1.59991,1.68182 0 0 1 -1.11993,2.8591z\" id=\"svg_4\"/>\n" +
                "   <circle cx=\"85.46318\" cy=\"25.38799\" id=\"svg_3\" r=\"2.28787\"/>\n" +
                "   <path d=\"m6.1238,8.56976l0,37.57193l35.74192,0l0,-37.57193l-35.74192,0zm24.91055,30.03736l-0.30398,0.20182a1.59991,1.68182 0 0 1 -0.83195,0.25227a1.59991,1.68182 0 0 1 -1.35992,-0.80728a0.33598,0.35318 0 0 1 0,-0.11773a1.59991,1.68182 0 0 1 0.57597,-2.20319l0.19199,-0.13455a1.59991,1.68182 0 0 1 2.14388,0.38682a0.19199,0.20182 0 0 1 0.08,0.08409a1.59991,1.68182 0 0 1 -0.49597,2.33773zm-3.3598,-9.16594a1.98388,2.08546 0 1 1 1.96789,-2.08546a1.98388,2.08546 0 0 1 -1.96789,2.08546zm7.16758,5.04547l-0.17599,0.30273a1.59991,1.68182 0 0 1 -1.32792,0.74a1.59991,1.68182 0 0 1 -0.89595,-0.26909a0.27198,0.28591 0 0 1 -0.09599,-0.10091a1.59991,1.68182 0 0 1 -0.35198,-2.23682l0.14399,-0.21864a1.59991,1.68182 0 0 1 2.09588,-0.58864l0.09599,0a1.59991,1.68182 0 0 1 0.51197,2.40501l0,-0.03364zm1.7759,-4.7932a1.59991,1.68182 0 0 1 -1.85589,1.36228l-0.11199,0a1.59991,1.68182 0 0 1 -1.18393,-1.91728a10.11141,10.62912 0 0 0 0.14399,-1.78273a9.59944,10.09094 0 1 0 -9.59944,10.09094a12.12729,12.74822 0 0 0 1.34392,-0.08409a1.59991,1.68182 0 0 1 1.7759,1.295a0.30398,0.31955 0 0 1 0,0.13455a1.59991,1.68182 0 0 1 -1.35992,1.90046a13.15123,13.82459 0 0 1 -1.7919,0.11773a12.79925,13.45458 0 1 1 12.79925,-13.45458a13.27923,13.95913 0 0 1 -0.15999,2.37137l0,-0.03364zm-14.30317,-2.37137a1.98388,2.08546 0 1 1 -1.98388,-2.08546a1.99988,2.10228 0 0 1 1.98388,2.1191l0,-0.03364z\" fill=\"black\" id=\"svg_2\"/>\n" +
                "  </g>\n" +
                " </g>\n" +
                "</svg>";
    }
}