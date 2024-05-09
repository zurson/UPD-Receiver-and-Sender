package org.example.udp_sender.Utils;

public class PortValidator {
    public static boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }
}
