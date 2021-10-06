package eu.automateeverything

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import kotlin.jvm.JvmStatic
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.PathResource
import java.nio.file.Paths
import org.eclipse.jetty.servlet.ServletContextHandler
import org.glassfish.jersey.servlet.ServletContainer
import java.lang.Exception

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val contexts = ContextHandlerCollection()
        val slowDown = args.contains("-slow")

        contexts.handlers = arrayOf<Handler>(
            buildWebContext(),
            buildRestContext(slowDown)
        )

        val server = Server(80)
        server.handler = contexts
        try {
            server.start()
            server.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun buildWebContext(): ContextHandler {
        val rh0 = ResourceHandler()
        rh0.isDirectoriesListed = true
        val webContext = ContextHandler()
        webContext.contextPath = "/"
        webContext.baseResource = PathResource(
            Paths.get("web")
        )
        webContext.handler = rh0
        return webContext
    }

    private fun buildRestContext(slowDown: Boolean): ContextHandler {
        val applicationClazz = if (slowDown) {
            AppSlowedDown::class.java
        } else {
            App::class.java
        }
        val restContext = ServletContextHandler()
        val serHol = restContext.addServlet(ServletContainer::class.java, "/rest/*")
        serHol.initOrder = 1
        serHol.setInitParameter("jakarta.ws.rs.Application", applicationClazz.name)
        return restContext
    }
}