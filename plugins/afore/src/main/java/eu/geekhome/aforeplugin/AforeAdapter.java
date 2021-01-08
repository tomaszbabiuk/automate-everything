package eu.geekhome.aforeplugin;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.InputPortsCollection;
import com.geekhome.common.hardwaremanager.OutputPortsCollection;
import com.geekhome.common.hardwaremanager.TogglePortsCollection;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.OperationMode;
import com.geekhome.common.hardwaremanager.HardwareManager;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Calendar;

class AforeAdapter extends NamedObject implements IHardwareManagerAdapter {

    public static final String INVERTER_PORT_ID = "INV:192.168.1.4";
    private static ILogger _logger = LoggingService.getLogger();
    private final OkHttpClient _okClient;
    private long _lastRefresh;

    AforeAdapter() {
        super(new DescriptiveName("Afore Adapter", "Afore"));
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
    public void discover(final InputPortsCollection<Boolean> digitalInputPorts,
                         final OutputPortsCollection<Boolean> digitalOutputPorts,
                         final InputPortsCollection<Double> powerInputPorts,
                         final OutputPortsCollection<Integer> powerOutputPorts,
                         final InputPortsCollection<Double> temperaturePorts,
                         final TogglePortsCollection togglePorts,
                         final InputPortsCollection<Double> humidityPorts,
                         final InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {

        Double inverterPower = readInverterPower();
        SynchronizedInputPort<Double> inverterPort = new SynchronizedInputPort<>(INVERTER_PORT_ID, inverterPower, 0);
        powerInputPorts.add(inverterPort);
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
    public RefreshState getRefreshState() {
        return RefreshState.NA;
    }

    @Override
    public void resetLatches() {
    }

    @Override
    public void reconfigure(OperationMode operationMode) throws Exception {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isOperational() {
        return true;
    }

    @Override
    public void start() {
    }

    @Override
    public String getStatus() {
        return null;
    }
}
