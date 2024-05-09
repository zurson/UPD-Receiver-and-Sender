package org.example.udp_sender.UDP;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.example.udp_sender.Exceptions.IncorrectInputException;
import org.example.udp_sender.Utils.PortValidator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class BroadcastSender {

    private DatagramSocket datagramSocket;
    private final int port;
    private final InetAddress inetAddress;

    public BroadcastSender(String host, int port) throws IncorrectInputException {
        this.port = port;

        if (!InetAddressValidator.getInstance().isValid(host))
            throw new IncorrectInputException("Invalid IP address");

        if (!PortValidator.isValidPort(port))
            throw new IncorrectInputException("Invalid port number");

        try {
            this.inetAddress = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new IncorrectInputException(e.getMessage());
        }
    }

    public void send(String broadcastMessage) throws IOException {
        datagramSocket = new DatagramSocket();

        datagramSocket.setReuseAddress(true);
        datagramSocket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, port);

        datagramSocket.send(packet);
        datagramSocket.close();
    }

}
