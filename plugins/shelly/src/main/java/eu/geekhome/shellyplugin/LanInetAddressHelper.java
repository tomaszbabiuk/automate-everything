package eu.geekhome.shellyplugin;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class LanInetAddressHelper {

    static List<InetAddress> getIpsInLan() throws SocketException {
        List<InetAddress> addresses = new ArrayList<>();

        for (final Enumeration<NetworkInterface> interfaces =
             NetworkInterface.getNetworkInterfaces();
             interfaces.hasMoreElements(); ) {
            final NetworkInterface cur = interfaces.nextElement();

            if (cur.isLoopback()) {
                continue;
            }

            for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                final InetAddress inet_addr = addr.getAddress();

                if (inet_addr instanceof Inet4Address) {
                    addresses.add(inet_addr);
                }
            }
        }

        return addresses;
    }
}
