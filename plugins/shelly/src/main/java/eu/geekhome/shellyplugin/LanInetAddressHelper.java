package eu.geekhome.shellyplugin;

import java.net.*;
import java.util.Enumeration;

public class LanInetAddressHelper {

    static InetAddress getIpInLan() throws SocketException {
        for (final Enumeration<NetworkInterface> interfaces =
             NetworkInterface.getNetworkInterfaces();
             interfaces.hasMoreElements(); ) {
            final NetworkInterface cur = interfaces.nextElement();

            if (cur.isLoopback()) {
                continue;
            }

            for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                final InetAddress inet_addr = addr.getAddress();

                if (!(inet_addr instanceof Inet4Address)) {
                    continue;
                }

                return inet_addr;
            }
        }

        return null;
    }
}
