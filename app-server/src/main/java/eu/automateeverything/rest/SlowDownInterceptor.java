package eu.automateeverything.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.*;
import java.io.IOException;

public class SlowDownInterceptor implements WriterInterceptor, ReaderInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context)
            throws IOException, WebApplicationException {
        return context.proceed();
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
        context.proceed();
    }
}