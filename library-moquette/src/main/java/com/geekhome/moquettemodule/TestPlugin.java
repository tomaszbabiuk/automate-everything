package com.geekhome.moquettemodule;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class TestPlugin implements BundleActivator {
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("STARTING TEST PLUGIN");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }
}
