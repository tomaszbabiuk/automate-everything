package eu.geekhome.shellyplugin;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.utils.Sleeper;
import com.geekhome.common.hardwaremanager.*;
import com.geekhome.common.OperationMode;
import com.google.gson.Gson;
import eu.geekhome.services.mqtt.MqttBrokerService;
import okhttp3.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import eu.geekhome.services.mqtt.MQTTListener;

public class ShellyAdapter extends NamedObject implements IHardwareManagerAdapter, MQTTListener {

    private interface PortIterateListener {
        void onIteratePort(IShellyPort port);
    }

    private static ILogger _logger = LoggingService.getLogger();
    private final InetAddress _brokerIP;
    private final OkHttpClient _okClient;
    private final Gson _gson;
    private MqttBrokerService _mqttBroker;
    private ArrayList<ShellyDigitalOutputPort> _ownedDigitalOutputPorts = new ArrayList<>();
    private ArrayList<ShellyPowerInputPort> _ownedPowerInputPorts = new ArrayList<>();
    private ArrayList<ShellyPowerOutputPort> _ownedPowerOutputPorts = new ArrayList<>();

    public ShellyAdapter(final MqttBrokerService mqttBroker) {
        super(new DescriptiveName("Shelly Adapter", "SHELLY"));
        _mqttBroker = mqttBroker;
        _brokerIP = resolveIpInLan();
        _okClient = createAnonymousClient();
        _gson = new Gson();
        _mqttBroker = mqttBroker;
    }

    private InetAddress resolveIpInLan() {
        try {
            return LanInetAddressHelper.getIpInLan();
        } catch (Exception ex) {
            _logger.error("Problem resolving this machine IP in LAN", ex);
        }

        return null;
    }

    @Override
    public void discover(final InputPortsCollection<Boolean> digitalInputPorts,
                         final OutputPortsCollection<Boolean> digitalOutputPorts,
                         final InputPortsCollection<Double> powerInputPorts,
                         final OutputPortsCollection<Integer> powerOutputPorts,
                         final InputPortsCollection<Double> temperaturePorts,
                         final TogglePortsCollection togglePorts,
                         final InputPortsCollection<Double> humidityPorts,
                         final InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {

        _logger.info("Starting SHELLY discovery");

        try {
            final AtomicInteger probedIPs = new AtomicInteger(0);
            Callback shellyCheckCallback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    probedIPs.incrementAndGet();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    probedIPs.incrementAndGet();

                    if (response.isSuccessful()) {
                        try {
                            System.out.println(response.body().string());
                            ShellySettingsResponse settingsResponse = _gson.fromJson(response.body().string(), ShellySettingsResponse.class);
                            response.body().close();
                            if (settingsResponse != null && settingsResponse.getDevice() != null && settingsResponse.getDevice().getType() != null) {
                                _logger.info("Shelly FOUND: " + response.request().url());

                                InetAddress shellyIP = InetAddress.getByName(response.request().url().host());

                                hijackShellyIfNeeded(settingsResponse, shellyIP);

                                //TODO: check if that's battery powered device to calculate connection interval
                                boolean isBatteryPowered = false;

                                long connectionLostInterval = isBatteryPowered ? 60*60*1000L : 40*60*1000L;

                                if (settingsResponse.getRelays() != null) {
                                    for (int i = 0; i < settingsResponse.getRelays().size(); i++) {
                                        ShellyDigitalOutputPort output = new ShellyDigitalOutputPort(settingsResponse, i, connectionLostInterval);
                                        digitalOutputPorts.add(output);
                                        _ownedDigitalOutputPorts.add(output);
                                    }
                                }

                                if (settingsResponse.getLights() != null) {
                                    for (int i = 0; i < settingsResponse.getLights().size(); i++) {
                                        ShellyLightResponse lightResponse = callForLightResponse(shellyIP, i);
                                        ShellyPowerOutputPort output = new ShellyPowerOutputPort(settingsResponse, lightResponse, i, connectionLostInterval);
                                        powerOutputPorts.add(output);
                                        _ownedPowerOutputPorts.add(output);
                                    }
                                }

                                if (settingsResponse.getMeters() != null) {
                                    for (int i = 0; i < settingsResponse.getDevice().getNumMeters(); i++) {
                                        ShellyPowerInputPort input = new ShellyPowerInputPort(settingsResponse, i, connectionLostInterval);
                                        powerInputPorts.add(input);
                                        _ownedPowerInputPorts.add(input);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            _logger.warning("Exception during shelly discovery, 99% it's not a shelly device", ex);
                        }
                    } else {
                        response.body().close();
                    }
                }
            };

            for (int i = 0; i < 255; i++) {
                InetAddress ipToCheck = InetAddress.getByAddress(new byte[]{
                        _brokerIP.getAddress()[0],
                        _brokerIP.getAddress()[1],
                        _brokerIP.getAddress()[2],
                        (byte) i
                });

                _logger.debug("Checking shelly: " + ipToCheck.getHostAddress());
                checkIfItsShelly(ipToCheck, shellyCheckCallback);
            }

            while (probedIPs.get() == 255) {
                Sleeper.trySleep(100);
            }
            _logger.info("DONE (SHELLY discovery)");
        } catch (UnknownHostException e) {
            throw new DiscoveryException("Error discovering shelly devices", e);
        }
    }

    @Override
    public boolean canBeRediscovered() {
        return true;
    }

    private void hijackShellyIfNeeded(ShellySettingsResponse settings, InetAddress shellyIP) throws IOException {
        if (settings.getCloud().isEnabled()) {
            disableCloud(shellyIP);
        }

        String expectedMqttServerSetting = _brokerIP.getHostAddress() + ":1883";
        if (!settings.getMqtt().isEnable() || !settings.getMqtt().getServer().equals(expectedMqttServerSetting)) {
            enableMQTT(shellyIP);
        }
    }

    private void checkIfItsShelly(InetAddress possibleShellyIP, Callback callback) {
        Request request = new Request.Builder()
                .url("http://" + possibleShellyIP + "/settings")
                .build();

        _okClient.newCall(request).enqueue(callback);
        Sleeper.trySleep(10);
    }

    private void enableMQTT(InetAddress shellyIP) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + shellyIP + "/settings/mqtt?mqtt_enable=1&mqtt_server=" + _brokerIP.getHostAddress() + "%3A1883")
                .build();

        Response response = _okClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
    }

    private void disableCloud(InetAddress shellyIP) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + shellyIP + "/settings/cloud?enabled=0")
                .build();

        Response response = _okClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
    }

    private ShellyLightResponse callForLightResponse(InetAddress possibleShellyIP, int channel) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + possibleShellyIP + "/light/" + channel)
                .build();

        Response response = _okClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
        return _gson.fromJson(json, ShellyLightResponse.class);
    }

    @Override
    public void refresh(Calendar now) throws Exception {
    }

    @Override
    public RefreshState getRefreshState() {
        return RefreshState.NA;
    }

    @Override
    public void resetLatches() {
        for (ShellyDigitalOutputPort port : _ownedDigitalOutputPorts) {
            resetShellyLatch(port);
        }

        for (ShellyPowerOutputPort port : _ownedPowerOutputPorts) {
            resetShellyLatch(port);
        }
    }

    private void resetShellyLatch(IShellyOutputPort shellyOutput) {
        if (shellyOutput.didChangeValue()) {
            String mqttPayload = shellyOutput.convertValueToMqttPayload();
            String topic = shellyOutput.getWriteTopic();
            _mqttBroker.publish(topic, mqttPayload);
            shellyOutput.resetLatch();
        }
    }

    @Override
    public void reconfigure(OperationMode operationMode) throws Exception {
    }

    @Override
    public void start() {
        _mqttBroker.addMqttListener(this);
    }

    @Override
    public void stop() {
        _mqttBroker.removeMqttListener(this);
    }

    @Override
    public boolean isOperational() {
        return true;
    }

    @Override
    public String getStatus() {
        return null;
    }

    private static OkHttpClient createAnonymousClient() {
        return new OkHttpClient.Builder().build();
    }

    @Override
    public void onPublish(String topicName, String payload) {
        _logger.info("MQTT message: " + topicName + ", content: " + payload);
        boolean isRelayTopic = topicName.contains("/relay/");
        boolean isLightTopic = topicName.contains("/light/");
        long now = Calendar.getInstance().getTimeInMillis();

        if (!isRelayTopic && !isLightTopic) {
            return;
        }

        iterateAllOwnedPorts(port -> {
            if (topicName.equals(port.getReadTopic())) {
                port.setValueFromMqttPayload(payload);
                port.updateLastSeenTimestamp(now);
            }
        });
    }

    @Override
    public void onDisconnected(String clientID) {
        iterateAllOwnedPorts(port -> {
            if (port.getId().startsWith(clientID)) {
                port.markDisconnected();
            }
        });
    }

    private void iterateAllOwnedPorts(PortIterateListener listener ) {
        for (IShellyPort port : _ownedDigitalOutputPorts) {
            listener.onIteratePort(port);
        }
        for (IShellyPort port : _ownedPowerOutputPorts) {
            listener.onIteratePort(port);
        }
        for (IShellyPort port : _ownedPowerInputPorts) {
            listener.onIteratePort(port);
        }
    }
}

/*
shellies/shellyplug-s-376CC2/relay/0/command:on|off
 */

/*
{
   "device":{
      "type":"SHPLG-S",
      "mac":"CC50E3376CC2",
      "hostname":"shellyplug-s-376CC2",
      "num_outputs":1,
      "num_meters":1
   },
   "wifi_ap":{
      "enabled":false,
      "ssid":"shellyplug-s-376CC2",
      "key":""
   },
   "wifi_sta":{
      "enabled":true,
      "ssid":"lte",
      "ipv4_method":"dhcp",
      "ip":null,
      "gw":null,
      "mask":null,
      "dns":null
   },
   "wifi_sta1":{
      "enabled":false,
      "ssid":null,
      "ipv4_method":"dhcp",
      "ip":null,
      "gw":null,
      "mask":null,
      "dns":null
   },
   "mqtt":{
      "enable":true,
      "server":"192.168.1.7:1883",
      "user":"",
      "reconnect_timeout_max":60.000000,
      "reconnect_timeout_min":2.000000,
      "clean_session":true,
      "keep_alive":60,
      "will_topic":"shellies/shellyplug-s-376CC2/online",
      "will_message":"false",
      "max_qos":0,
      "retain":false,
      "update_period":30
   },
   "sntp":{
      "server":"time.google.com"
   },
   "login":{
      "enabled":false,
      "unprotected":false,
      "username":"admin",
      "password":"admin"
   },
   "pin_code":"{tkW0N",
   "coiot_execute_enable":false,
   "name":"",
   "fw":"20190821-095311/v1.5.2@4148d2b7",
   "build_info":{
      "build_id":"20190821-095311/v1.5.2@4148d2b7",
      "build_timestamp":"2019-08-21T09:53:11Z",
      "build_version":"1.0"
   },
   "cloud":{
      "enabled":false,
      "connected":false
   },
   "timezone":"Europe/Warsaw",
   "lat":52.406399,
   "lng":16.925200,
   "tzautodetect":true,
   "time":"09:19",
   "hwinfo":{
      "hw_revision":"prod-190325-test",
      "batch_id":1
   },
   "max_power":2500,
   "led_status_disable":true,
   "led_power_disable":false,
   "relays":[
      {
         "ison":true,
         "has_timer":false,
         "overpower":false,
         "default_state":"on",
         "auto_on":0.00,
         "auto_off":0.00,
         "btn_on_url":null,
         "out_on_url":null,
         "out_off_url":null,
         "schedule":false,
         "schedule_rules":[

         ],
         "max_power":2500
      }
   ],
   "meters":[
      {
         "power":0.00,
         "is_valid":true,
         "timestamp":1575191987,
         "counters":[
            0.000,
            0.000,
            0.000
         ],
         "total":0
      }
   ],
   "lights":[
      {
         "name":"",
         "ison":true,
         "default_state":"off",
         "auto_on":0.00,
         "auto_off":0.00,
         "btn1_on_url":"",
         "btn1_off_url":"",
         "btn2_on_url":"",
         "btn2_off_url":"",
         "out_on_url":"",
         "out_off_url":"",
         "schedule":false,
         "schedule_rules":[

         ],
         "btn_type":"edge",
         "swap_inputs":0
      }
   ],
}
 */

/*

http://192.168.1.2/light/0

{"ison":false,"mode":"white","brightness":22}
 */