package eu.geekhome.osgicontainer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class OSGiListener {
    public void start() {

        Map<String, String> osgiConfig = new HashMap<>();
        osgiConfig.put(Constants.FRAMEWORK_STORAGE, "/home/tbabiuk/work/geekhome-server2/app-felix/cache");
        osgiConfig.put(Constants.FRAMEWORK_STORAGE_CLEAN, "true");
        osgiConfig.put("felix.log.level", "0");
        try {
            FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class)
                    .iterator().next();
            Framework framework = frameworkFactory.newFramework(osgiConfig);
            framework.start();

            BundleContext bundleContext = framework.getBundleContext();
            BundleManager bundleManager = new BundleManager(bundleContext);
            bundleManager.load();
        } catch (Exception ex) {
            System.out.println("OSGi Failed to Start");
            System.out.println(ex);
        }
    }

    public class BundleManager {
        private BundleContext bundleContext = null;
        public BundleManager(BundleContext bundleContext) {
            this.bundleContext = bundleContext;
        }
        public void load() throws Exception {

            ArrayList<Bundle> availableBundles = new ArrayList<Bundle>();
            //get and open available bundles
            for (URL url : getBundles()) {
                Bundle bundle = bundleContext.installBundle(url.getFile(), url.openStream());
                availableBundles.add(bundle);
            }

            //start the bundles
            for (Bundle bundle : availableBundles) {
                try {
                    bundle.start();
                } catch (Exception ex) {
                    System.out.println("Failed to start bundle " + bundle.getSymbolicName());
                    System.out.println(ex);
                }
            }
        }

        private List<URL> getBundles() throws MalformedURLException {
            List<URL> bundleURLs = new ArrayList<>();
            bundleURLs.add(new URL("file:///home/tbabiuk/work/geekhome-server2/module-services/build/libs/module-services.jar"));
            bundleURLs.add(new URL("file:///home/tbabiuk/work/geekhome-server2/library-moquette/build/libs/library-moquette-all.jar"));
            return bundleURLs;
        }
    }
}
