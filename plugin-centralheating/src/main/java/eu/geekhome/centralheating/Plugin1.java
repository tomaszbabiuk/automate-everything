package eu.geekhome.centralheating;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class Plugin1 implements BundleActivator, ServiceListener {

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Starting PLUGIN1");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Stopping PLUGIN1");
    }

    @Override
    public void serviceChanged(ServiceEvent event) {

    }
}
