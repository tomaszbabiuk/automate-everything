package eu.geekhome.aforeplugin;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.OperationMode;
import eu.geekhome.services.events.EventsSink;
import eu.geekhome.services.hardware.*;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

class AforeAdapter implements HardwareAdapter {

    public static final String INVERTER_PORT_PREFIX = "192.168.1.4";
    private static ILogger _logger = LoggingService.getLogger();
    private final OkHttpClient _okClient;
    private long _lastRefresh;
    private final AdapterState _state = AdapterState.Initialized;

    AforeAdapter() {
        _okClient = createAuthenticatedClient("admin", "admin");
    }

    private static OkHttpClient createAuthenticatedClient(final String username,
                                                          final String password) {
        return new OkHttpClient.Builder().authenticator((route, response) -> {
            String credential = Credentials.basic(username, password);
            return response.request().newBuilder().header("Authorization", credential).build();
        }).build();
    }

    private static String doRequest(OkHttpClient httpClient, String anyURL) throws Exception {
        Request request = new Request.Builder().url(anyURL).build();
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }

    @Override
    public String getId() {
        return "0";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void discover(PortIdBuilder builder, List<Port> ports, EventsSink<String> eventsSink) {
        System.out.println("AFORE START");
        eventsSink.broadcastEvent("Starting discovery");

        String portId = builder.buildPortId(INVERTER_PORT_PREFIX);
        Double inverterPower = readInverterPower();
        Port<Double, Wattage> inverterPort =
                new WattageInputPort(portId, inverterPower);
        ports.add(inverterPort);

        eventsSink.broadcastEvent("Done");
        System.out.println("AFORE END");
    }

    @Override
    public boolean canBeRediscovered() {
        return false;
    }

    private Double readInverterPower() {
        try {
            String inverterResponse = doRequest(_okClient, "http://192.168.1.4/status.html");

            String[] lines = inverterResponse.split(";");
            for (String line : lines) {
                if (line.contains("webdata_now_p")) {
                    String s = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                    return Double.valueOf(s);
                }
            }
        } catch (Exception ex) {
            _logger.warning("A problem reading inverter power: ", ex);
        }

        return 0.0;
    }

    @Override
    public void refresh(Calendar now) throws Exception {
        if (now.getTimeInMillis() - _lastRefresh > 15000) {

//            Double inverterPower = readInverterPower();
//            SynchronizedInputPort<Double> inverterPort =  (SynchronizedInputPort<Double>)_hardwareManager.findPowerInputPort(INVERTER_PORT_ID);
//            inverterPort.setValue(inverterPower);

            _lastRefresh = now.getTimeInMillis();
        }
    }

    @Override
    public AdapterState getState() {
        return _state;
    }

    @Override
    public void resetLatches() {
    }

    @Override
    public void reconfigure(OperationMode operationMode) throws Exception {
    }

    @Override
    public long getLastDiscoveryTime() {
        return 0;
    }

    @Override
    public Throwable getLastError() {
        return null;
    }
}
