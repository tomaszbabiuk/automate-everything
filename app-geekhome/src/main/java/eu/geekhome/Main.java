package eu.geekhome;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.PathResource;
import org.glassfish.jersey.servlet.ServletContainer;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] {
                buildWebContext(),
                buildRestContext()
        });

        Server server = new Server(80);
        server.setHandler(contexts);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ContextHandler buildWebContext() {
        ResourceHandler rh0 = new ResourceHandler();
        rh0.setDirectoriesListed(true);

        ContextHandler webContext = new ContextHandler();
        webContext.setContextPath("/");
        webContext.setBaseResource(new PathResource(Paths.get(
                "web")));
        webContext.setHandler(rh0);

        return webContext;
    }

    private static ContextHandler buildRestContext() {
        ServletContextHandler restContext = new ServletContextHandler();
        ServletHolder serHol = restContext.addServlet(ServletContainer.class, "/rest/*");
        serHol.setInitOrder(1);
        serHol.setInitParameter("javax.ws.rs.Application", "eu.geekhome.rest.App");
        return restContext;
    }
}
