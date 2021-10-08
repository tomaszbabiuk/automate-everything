package eu.automateeverything.rest

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper

class ResourceNotFoundExceptionMapper : ExceptionMapper<ResourceNotFoundException?> {
    override fun toResponse(ex: ResourceNotFoundException?): Response {
        return Response
            .status(404)
            .build()
    }
}