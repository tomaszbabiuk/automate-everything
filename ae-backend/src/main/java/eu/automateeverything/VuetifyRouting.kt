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

import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.Resource
import kotlin.Throws
import java.io.IOException
import java.util.Locale

interface IndexRedirectRule {
    fun match(pathLowercase: String) : Boolean
}

class AllEndingWithHtml : IndexRedirectRule {
    override fun match(pathLowercase: String): Boolean {
        return pathLowercase.endsWith(".html") ||
                pathLowercase.endsWith(".htm")
    }
}

class AllMainPaths : IndexRedirectRule {
    override fun match(pathLowercase: String): Boolean {
        return pathLowercase.startsWith("/inbox") ||
                pathLowercase.startsWith("/timeline") ||
                pathLowercase.startsWith("/control") ||
                pathLowercase.startsWith("/tags") ||
                pathLowercase.startsWith("/icons") ||
                pathLowercase.startsWith("/objects") ||
                pathLowercase.startsWith("/plugins") ||
                pathLowercase.startsWith("/discover")
    }
}

class VuetifyWebAppHandler : ResourceHandler() {
    private val rules = arrayOf(AllEndingWithHtml(), AllMainPaths())

    @Throws(IOException::class)
    override fun getResource(path: String): Resource {
        val pathLowercase = path.lowercase(Locale.ENGLISH)

        rules.forEach {
            if (it.match(pathLowercase)) {
                return super.getResource("/index.html")
            }
        }

        return super.getResource(path)
    }

}