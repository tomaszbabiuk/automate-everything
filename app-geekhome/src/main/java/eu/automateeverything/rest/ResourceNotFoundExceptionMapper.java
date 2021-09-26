package eu.automateeverything.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException ex) {
        return Response
                .status(404)
                .build();
    }
}
