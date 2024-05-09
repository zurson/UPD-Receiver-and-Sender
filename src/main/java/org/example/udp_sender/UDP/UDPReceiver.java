package org.example.udp_sender.UDP;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.example.udp_sender.Exceptions.IncorrectInputException;
import org.example.udp_sender.Interfaces.Logger;
import org.example.udp_sender.UDPSenderApp;
import org.example.udp_sender.Utils.PortValidator;

import java.io.IOException;
import java.net.*;

public class UDPReceiver extends Thread {

    private final MulticastSocket multicastSocket;
    private final byte[] buffer = new byte[UDPSenderApp.MAX_BYTES];
    private final InetAddress inetAddress;
    private final Logger logger;

    public UDPReceiver(String host, int port, Logger logger) throws IncorrectInputException {
        this.logger = logger;

        if (!InetAddressValidator.getInstance().isValid(host))
            throw new IncorrectInputException("Invalid IP address");

        if (!PortValidator.isValidPort(port))
            throw new IncorrectInputException("Invalid port number");

        try {
            inetAddress = InetAddress.getByName(host);
            multicastSocket = new MulticastSocket(port);
            multicastSocket.setReuseAddress(true);
            multicastSocket.joinGroup(inetAddress);

        } catch (IOException e) {
            throw new IncorrectInputException(e.getMessage());
        }

    }

    @Override
    public void run() {
        try {
            DatagramPacket datagramPacket;

            while (true) {
                datagramPacket = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(datagramPacket);

                String received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                logger.log("[Received]: " + received);
            }


        } catch (IOException e) {
            logger.log(e.getMessage());
        } finally {
            stopThread();
        }
    }

    public void stopThread() {
        if (super.isInterrupted())
            return;

        try {
            multicastSocket.leaveGroup(inetAddress);
            multicastSocket.close();
        } catch (IOException e) {
            logger.log(e.getMessage());
        } finally {
            interrupt();
        }
    }

}
