package eu.geekhome;

import com.geekhome.common.OperationMode;
import com.geekhome.common.alerts.DashboardAlertService;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.common.extensibility.RequiresFeature;
import com.geekhome.common.extensibility.RequiresMqttFeature;
import com.geekhome.common.hardwaremanager.HardwareManager;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.localization.ResourceLocalizationProvider;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.settings.TextFileAutomationSettingsPersister;
import com.geekhome.moquettemodule.MoquetteBroker;
import eu.geekhome.plugins.PluginsManager;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.PathResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.nio.file.Paths;
import java.util.List;

public class Boot {

    public static void main(String[] args) {
        PluginManager pluginManager = new DefaultPluginManager();
        pluginManager.loadPlugins();

        // enable a disabled plugin
        // pluginManager.enablePlugin("welcome-plugin");

        // start (active/resolved) the plugins
        pluginManager.startPlugins();
        try {
            final MoquetteBroker mqttBroker = new MoquetteBroker();
            mqttBroker.start();

            List<IHardwareManagerAdapterFactory> hmaFactories = pluginManager.getExtensions(IHardwareManagerAdapterFactory.class);
            for (IHardwareManagerAdapterFactory factory : hmaFactories) {
                if (factory instanceof RequiresMqttFeature) {
                    ((RequiresMqttFeature) factory).setMqttBroker(mqttBroker);
                }

                if (factory instanceof RequiresFeature) {
                    ((RequiresFeature) factory).allFeaturesInjected();
                }
            }


            TextFileAutomationSettingsPersister settingsPersister = new TextFileAutomationSettingsPersister();
            AutomationSettings automationSettings = new AutomationSettings(settingsPersister);
            ILocalizationProvider localizationProvider = new ResourceLocalizationProvider();
            DashboardAlertService dashboardAlertService = new DashboardAlertService(localizationProvider);
            HardwareManager hardwareManager = new HardwareManager(dashboardAlertService);
            hardwareManager.initialize(hmaFactories);
            SystemInfo systemInfo = new SystemInfo(OperationMode.Diagnostics, dashboardAlertService);

            startServer();

        } catch (Exception ex) {
            System.out.println(ex);
        }

        // stop the plugins
        pluginManager.stopPlugins();
    }

    private static void startServer() {
        Server server = new Server(80);

        ResourceHandler rh0 = new ResourceHandler();
        rh0.setDirectoriesListed(false);

        ContextHandler webContext = new ContextHandler();
        webContext.setContextPath("/");
        webContext.setBaseResource(new PathResource(Paths.get("src/main/resources/dist")));
        webContext.setHandler(rh0);

//        ServletContextHandler streamContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        streamContext.setContextPath("/stream");
//        streamContext.addServlet(new ServletHolder(new MySSEServlet()), "/*");

        ResourceConfig resourceConfig = new ResourceConfig();
        ServletContextHandler restContext = new ServletContextHandler();
        ServletHolder serHol = restContext.addServlet(ServletContainer.class, "/rest/*");
        serHol.setInitOrder(1);
//        serHol.setInitParameter("jersey.config.server.provider.packages","eu.geekhome.rest");
        serHol.setInitParameter("javax.ws.rs.Application", "eu.geekhome.rest.App");

        //http://www.appsdeveloperblog.com/dependency-injection-hk2-jersey-jax-rs/
//             <param-name>javax.ws.rs.Application</param-name>
//      <param-value>com.appsdeveloperblog.app.ws.App</param-value>



        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { webContext, /*streamContext,*/ restContext });


//        JettyHttpServer server = new JettyHttpServer(80, contexts);
//        try {
//            server.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        server.setHandler(contexts);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ResourceConfig resourceConfig() {
        return new ResourceConfig().register(new PluginsManager());
    }

}
