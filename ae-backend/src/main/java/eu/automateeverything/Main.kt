/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import kotlin.jvm.JvmStatic
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.util.resource.PathResource
import java.nio.file.Paths
import org.eclipse.jetty.servlet.ServletContextHandler
import org.glassfish.jersey.servlet.ServletContainer
import org.slf4j.LoggerFactory
import java.lang.Exception

object Main {
    private const val DEFAULT_PORT = 80
    private val logger = LoggerFactory.getLogger(Main::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val contexts = ContextHandlerCollection()
        val slowDown = args.contains("-slow")
        val port = extractPort(args)

        contexts.handlers = arrayOf<Handler>(
            buildWebContext(),
            buildRestContext(slowDown)
        )

        val server = Server(port)
        server.handler = contexts
        try {
            server.start()
            server.join()
        } catch (e: Exception) {
            logger.error("Starting automation server failed!", e)
        }
    }

    private fun extractPort(args: Array<String>): Int {

        return args
            .filter { it.startsWith("-port=") }
            .map { it.replace("-port=", "").toInt() }
            .firstOrNull() ?: DEFAULT_PORT
    }

    private fun buildWebContext(): ContextHandler {
        val rh0 = VuetifyWebAppHandler()
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