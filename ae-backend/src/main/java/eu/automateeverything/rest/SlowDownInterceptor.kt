package eu.automateeverything.rest

import jakarta.ws.rs.ext.WriterInterceptor
import jakarta.ws.rs.ext.ReaderInterceptor
import kotlin.Throws
import java.io.IOException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.ext.ReaderInterceptorContext
import jakarta.ws.rs.ext.WriterInterceptorContext
import java.lang.InterruptedException

class SlowDownInterceptor : WriterInterceptor, ReaderInterceptor {
    @Throws(IOException::class, WebApplicationException::class)
    override fun aroundReadFrom(context: ReaderInterceptorContext): Any {
        return context.proceed()
    }

    @Throws(IOException::class, WebApplicationException::class)
    override fun aroundWriteTo(context: WriterInterceptorContext) {
        try {
            Thread.sleep(5000)
        } catch (ignored: InterruptedException) {
        }
        context.proceed()
    }
}