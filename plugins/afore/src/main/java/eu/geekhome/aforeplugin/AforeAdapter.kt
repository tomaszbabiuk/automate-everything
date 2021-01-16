package eu.geekhome.aforeplugin

import com.geekhome.common.OperationMode
import com.geekhome.common.logging.LoggingService
import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.hardware.*
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class AforeAdapter : HardwareAdapterBase() {

//    private val okClient: OkHttpClient = createAuthenticatedClient("admin", "admin")
    private var lastRefresh: Long = 0

    override suspend fun internalDisvovery(idBuilder: PortIdBuilder, eventsSink: EventsSink<String>) : MutableList<Port<*, *>> {
        val result = ArrayList<Port<*,*>>()
//        println("AFORE START")
//
//        eventsSink.broadcastEvent("Starting discovery")
//        val portId = idBuilder.buildPortId(INVERTER_PORT_PREFIX)
//        val inverterPower = readInverterPower()
//        val inverterPort: Port<Double, Wattage> = WattageInputPort(portId, inverterPower)
//        ports.add(inverterPort)
//        eventsSink.broadcastEvent("Done")
//
//        println("AFORE END")

        println("afore discovery, this will take 10 sec")
        repeat(100) {
            delay(1000)
            println("afore $it")
        }

        println("afore discovery")

        return result
    }

//    private fun readInverterPower(): Double {
//        try {
//            val inverterResponse = doRequest(okClient, "http://192.168.1.4/status.html")
//            val lines = inverterResponse.split(";").toTypedArray()
//            for (line in lines) {
//                if (line.contains("webdata_now_p")) {
//                    val s = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
//                    return java.lang.Double.valueOf(s)
//                }
//            }
//        } catch (ex: Exception) {
//            _logger.warning("A problem reading inverter power: ", ex)
//        }
//        return 0.0
//    }

    @Throws(Exception::class)
    override fun refresh(now: Calendar) {
        if (now.timeInMillis - lastRefresh > 15000) {

//            Double inverterPower = readInverterPower();
//            SynchronizedInputPort<Double> inverterPort =  (SynchronizedInputPort<Double>)_hardwareManager.findPowerInputPort(INVERTER_PORT_ID);
//            inverterPort.setValue(inverterPower);
            lastRefresh = now.timeInMillis
        }
    }

    override fun resetLatches() {
    }


    @Throws(Exception::class)
    override fun reconfigure(operationMode: OperationMode) {
    }

//    companion object {
//        const val INVERTER_PORT_PREFIX = "192.168.1.4"
//        private val _logger = LoggingService.getLogger()
//        private fun createAuthenticatedClient(
//            username: String,
//            password: String
//        ): OkHttpClient {
//            return OkHttpClient.Builder().authenticator { route: Route?, response: Response ->
//                val credential = Credentials.basic(username, password)
//                response.request.newBuilder().header("Authorization", credential).build()
//            }.build()
//        }
//
//        @Throws(Exception::class)
//        private fun doRequest(httpClient: OkHttpClient, anyURL: String): String {
//            val request = Request.Builder().url(anyURL).build()
//            val response = httpClient.newCall(request).execute()
//            if (!response.isSuccessful) {
//                throw IOException("Unexpected code $response")
//            }
//            return response.body!!.string()
//        }
//    }
}