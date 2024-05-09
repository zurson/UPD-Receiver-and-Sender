package org.example.udp_sender.UDP;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.example.udp_sender.Exceptions.IncorrectInputException;
import org.example.udp_sender.Utils.PortValidator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastSender {

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer;
    private String host;
    private int port;

    public MulticastSender(String host, int port) throws IncorrectInputException {
        this.host = host;
        this.port = port;

        if (!InetAddressValidator.getInstance().isValid(host))
            throw new IncorrectInputException("Invalid IP address");

        if (!PortValidator.isValidPort(port))
            throw new IncorrectInputException("Invalid port number");

    }

    public void send(String multicastMessage) throws IOException {
        datagramSocket = new DatagramSocket();

        datagramSocket.setReuseAddress(true);
        inetAddress = InetAddress.getByName(host);

        buffer = multicastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, port);

        datagramSocket.send(packet);
        datagramSocket.close();
    }

}
