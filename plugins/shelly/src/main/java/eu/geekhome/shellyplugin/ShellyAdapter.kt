package eu.geekhome.shellyplugin

import com.geekhome.common.OperationMode
import com.geekhome.common.logging.LoggingService
import com.google.gson.Gson
import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.hardware.HardwareAdapterBase
import eu.geekhome.services.hardware.Port
import eu.geekhome.services.hardware.PortIdBuilder
import eu.geekhome.services.mqtt.MqttBrokerService
import eu.geekhome.services.mqtt.MqttListener
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.*
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList

class ShellyAdapter(private val mqttBroker: MqttBrokerService) : HardwareAdapterBase(), MqttListener {
    private interface PortIterateListener {
        fun onIteratePort(port: IShellyPort)
    }

    private var finder: ShellyFinder

    val client = HttpClient(CIO) {
        install(JsonFeature)
        engine {
            maxConnectionsCount = 1000

            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
        }
    }

    private val brokerIP: InetAddress?
    private val gson: Gson
    private val ownedDigitalOutputPorts = ArrayList<ShellyDigitalOutputPort>()
    private val ownedPowerInputPorts = ArrayList<ShellyPowerInputPort>()
    private val ownedPowerOutputPorts = ArrayList<ShellyPowerOutputPort>()

    private fun resolveIpInLan(): InetAddress? {
        try {
            val addresses = LanInetAddressHelper.getIpsInLan();
            return addresses[addresses.size - 1]
        } catch (ex: Exception) {
            logger.error("Problem resolving this machine IP in LAN", ex)
        }
        return null
    }

//    @Throws(DiscoveryException::class)
//    fun discover(
//        digitalInputPorts: InputPortsCollection<Boolean?>?,
//        digitalOutputPorts: OutputPortsCollection<Boolean?>,
//        powerInputPorts: InputPortsCollection<Double?>,
//        powerOutputPorts: OutputPortsCollection<Int?>,
//        temperaturePorts: InputPortsCollection<Double?>?,
//        togglePorts: TogglePortsCollection?,
//        humidityPorts: InputPortsCollection<Double?>?,
//        luminosityPorts: InputPortsCollection<Double?>?
//    ) {
//        logger.info("Starting SHELLY discovery")
//        try {
//            val probedIPs = AtomicInteger(0)
//            val shellyCheckCallback: Callback = object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    probedIPs.incrementAndGet()
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    probedIPs.incrementAndGet()
//                    if (response.isSuccessful) {
//                        try {
//                            println(response.body!!.string())
//                            val settingsResponse = gson.fromJson(
//                                response.body!!.string(), ShellySettingsResponse::class.java
//                            )
//                            response.body!!.close()
//                            if (settingsResponse != null && settingsResponse.device != null && settingsResponse.device.type != null) {
//                                logger.info("Shelly FOUND: " + response.request.url)
//                                val shellyIP = InetAddress.getByName(response.request.url.host)
//                                hijackShellyIfNeeded(settingsResponse, shellyIP)
//
//                                //TODO: check if that's battery powered device to calculate connection interval
//                                val isBatteryPowered = false
//                                val connectionLostInterval = if (isBatteryPowered) 60 * 60 * 1000L else 40 * 60 * 1000L
//                                if (settingsResponse.relays != null) {
//                                    for (i in settingsResponse.relays.indices) {
//                                        val output =
//                                            ShellyDigitalOutputPort(settingsResponse, i, connectionLostInterval)
//                                        digitalOutputPorts.add(output)
//                                        ownedDigitalOutputPorts.add(output)
//                                    }
//                                }
//                                if (settingsResponse.lights != null) {
//                                    for (i in settingsResponse.lights.indices) {
//                                        val lightResponse = callForLightResponse(shellyIP, i)
//                                        val output = ShellyPowerOutputPort(
//                                            settingsResponse,
//                                            lightResponse,
//                                            i,
//                                            connectionLostInterval
//                                        )
//                                        powerOutputPorts.add(output)
//                                        ownedPowerOutputPorts.add(output)
//                                    }
//                                }
//                                if (settingsResponse.meters != null) {
//                                    for (i in 0 until settingsResponse.device.numMeters) {
//                                        val input = ShellyPowerInputPort(settingsResponse, i, connectionLostInterval)
//                                        powerInputPorts.add(input)
//                                        ownedPowerInputPorts.add(input)
//                                    }
//                                }
//                            }
//                        } catch (ex: Exception) {
//                            logger.warning("Exception during shelly discovery, 99% it's not a shelly device", ex)
//                        }
//                    } else {
//                        response.body!!.close()
//                    }
//                }
//            }
//            for (i in 0..254) {
//                val ipToCheck = InetAddress.getByAddress(
//                    byteArrayOf(
//                        brokerIP!!.address[0],
//                        brokerIP.address[1],
//                        brokerIP.address[2],
//                        i.toByte()
//                    )
//                )
//                logger.debug("Checking shelly: " + ipToCheck.hostAddress)
////                checkIfItsShelly(ipToCheck, shellyCheckCallback)
//            }
//            while (probedIPs.get() == 255) {
//                Sleeper.trySleep(100)
//            }
//            logger.info("DONE (SHELLY discovery)")
//        } catch (e: UnknownHostException) {
//            throw DiscoveryException("Error discovering shelly devices", e)
//        }
//    }

//    @Throws(IOException::class)
//    private fun hijackShellyIfNeeded(settings: ShellySettingsResponse, shellyIP: InetAddress) {
//        if (settings.cloud.isEnabled) {
//            disableCloud(shellyIP)
//        }
//        val expectedMqttServerSetting = brokerIP!!.hostAddress + ":1883"
//        if (!settings.mqtt.isEnable || settings.mqtt.server != expectedMqttServerSetting) {
//            enableMQTT(shellyIP)
//        }
//    }

//    @Suppress("BlockingMethodInNonBlockingContext")
//    private fun checkIfItsShelly(possibleShellyIP: InetAddress): Response {
//        val request = Request.Builder()
//            .url("http://$possibleShellyIP/settings")
//            .build()
//
//        return okClient.newCall(request).execute()
//    }

//    @Throws(IOException::class)
//    private fun enableMQTT(shellyIP: InetAddress) {
//        val request = Request.Builder()
//            .url("http://" + shellyIP + "/settings/mqtt?mqtt_enable=1&mqtt_server=" + brokerIP!!.hostAddress + "%3A1883")
//            .build()
//        val response = okClient.newCall(request).execute()
//        if (!response.isSuccessful) {
//            throw IOException("Unexpected code $response")
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun disableCloud(shellyIP: InetAddress) {
//        val request = Request.Builder()
//            .url("http://$shellyIP/settings/cloud?enabled=0")
//            .build()
//        val response = okClient.newCall(request).execute()
//        if (!response.isSuccessful) {
//            throw IOException("Unexpected code $response")
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun callForLightResponse(possibleShellyIP: InetAddress, channel: Int): ShellyLightResponse {
//        val request = Request.Builder()
//            .url("http://$possibleShellyIP/light/$channel")
//            .build()
//        val response = okClient.newCall(request).execute()
//        val json = response.body!!.string()
//        response.body!!.close()
//        return gson.fromJson(json, ShellyLightResponse::class.java)
//    }



    @ExperimentalCoroutinesApi
    override suspend fun internalDisvovery(
        idBuilder: PortIdBuilder,
        eventsSink: EventsSink<String>
    ): MutableList<Port<*, *>> = coroutineScope {
        val result = ArrayList<Port<*, *>>()

        val discoveryJob = async { finder.searchForShellies(eventsSink) }
        val shellies = discoveryJob.await()

        result
    }





    @Throws(Exception::class)
    override fun refresh(now: Calendar) {
    }

    override fun resetLatches() {
        for (port in ownedDigitalOutputPorts) {
            resetShellyLatch(port)
        }
        for (port in ownedPowerOutputPorts) {
            resetShellyLatch(port)
        }
    }

    private fun resetShellyLatch(shellyOutput: IShellyOutputPort) {
        if (shellyOutput.didChangeValue()) {
            val mqttPayload = shellyOutput.convertValueToMqttPayload()
            val topic = shellyOutput.writeTopic
            mqttBroker.publish(topic, mqttPayload)
            shellyOutput.resetLatch()
        }
    }

    @Throws(Exception::class)
    override fun reconfigure(operationMode: OperationMode) {
    }

    fun start() {
        mqttBroker.addMqttListener(this)
    }

    fun stop() {
        mqttBroker.removeMqttListener(this)
    }

    override fun onPublish(topicName: String, payload: String) {
        logger.info("MQTT message: $topicName, content: $payload")
        val isRelayTopic = topicName.contains("/relay/")
        val isLightTopic = topicName.contains("/light/")
        val now = Calendar.getInstance().timeInMillis
        if (!isRelayTopic && !isLightTopic) {
            return
        }
        iterateAllOwnedPorts(object : PortIterateListener {
            override fun onIteratePort(port: IShellyPort) {
                if (topicName == port.readTopic) {
                    port.setValueFromMqttPayload(payload)
                    port.updateLastSeenTimestamp(now)
                }
            }
        })
    }

    override fun onDisconnected(clientID: String) {
        iterateAllOwnedPorts(object : PortIterateListener {
            override fun onIteratePort(port: IShellyPort) {
                if (port.id.startsWith(clientID)) {
                    port.markDisconnected()
                }
            }
        })
    }

    private fun iterateAllOwnedPorts(listener: PortIterateListener) {
        for (port in ownedDigitalOutputPorts) {
            listener.onIteratePort(port)
        }
        for (port in ownedPowerOutputPorts) {
            listener.onIteratePort(port)
        }
        for (port in ownedPowerInputPorts) {
            listener.onIteratePort(port)
        }
    }

    companion object {
        private val logger = LoggingService.getLogger()
    }

    init {
        brokerIP = resolveIpInLan()
        finder = ShellyFinder(client, brokerIP!!)
        gson = Gson()
    }
}