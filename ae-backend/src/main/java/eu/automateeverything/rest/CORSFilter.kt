/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
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

package eu.automateeverything.rest

import jakarta.ws.rs.container.PreMatching
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider

@Provider
@PreMatching
class CORSFilter : ContainerRequestFilter, ContainerResponseFilter {
    /**
     * Method for ContainerRequestFilter.
     */
    override fun filter(request: ContainerRequestContext) {

        // If it's a preflight request, we abort the request with
        // a 200 status, and the CORS headers are added in the
        // response filter method below.
        if (isPreflightRequest(request)) {
            request.abortWith(Response.ok().build())
        }
    }

    /**
     * Method for ContainerResponseFilter.
     */
    override fun filter(request: ContainerRequestContext, response: ContainerResponseContext) {

        // if there is no Origin header, then it is not a
        // cross origin request. We don't do anything.
        if (request.getHeaderString("Origin") == null) {
            return
        }

        // If it is a preflight request, then we add all
        // the CORS headers here.
        if (isPreflightRequest(request)) {
            response.headers.add("Access-Control-Allow-Credentials", "true")
            response.headers.add(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD"
            )
            response.headers.add(
                "Access-Control-Allow-Headers",  // Whatever other non-standard/safe headers (see list above)
                // you want the client to be able to send to the server,
                // put it in this list. And remove the ones you don't want.
                "X-Requested-With, Authorization, " +
                        "Accept-Version, Content-MD5, CSRF-Token"
            )
        }

        // Cross origin requests can be either simple requests
        // or preflight request. We need to add this header
        // to both type of requests. Only preflight requests
        // need the previously added headers.
        response.headers.add("Access-Control-Allow-Origin", "*")
    }

    companion object {
        /**
         * A preflight request is an OPTIONS request
         * with an Origin header.
         */
        private fun isPreflightRequest(request: ContainerRequestContext): Boolean {
            return (request.getHeaderString("Origin") != null
                    && request.method.equals("OPTIONS", ignoreCase = true))
        }
    }
}