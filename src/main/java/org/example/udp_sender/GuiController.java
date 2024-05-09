package org.example.udp_sender;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.udp_sender.Interfaces.Logger;
import org.example.udp_sender.UDP.BroadcastSender;
import org.example.udp_sender.UDP.MulticastSender;
import org.example.udp_sender.UDP.UDPReceiver;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GuiController implements Initializable, Logger {
    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField ipTextField, portTextField, messageTextField;

    @FXML
    private Button stopListeningButton, startListeningButton;

    @FXML
    private TextArea logsTextArea;

    private UDPReceiver udpReceiver;

    private Lock lock;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lock = new ReentrantLock();
        logsTextArea.setEditable(false);
        setButtonEnabled(stopListeningButton, false);
        setButtonEnabled(startListeningButton, true);
    }


    @FXML
    private void onClearButtonClick() {
        logsTextArea.clear();
    }


    @FXML
    private void onSendButtonClick() {
        String message = messageTextField.getText();

        if (messageTextField.getText().isEmpty()) {
            log("First insert text to send!");
            return;
        }

        try {
            String host = ipTextField.getText();
            int port = Integer.parseInt(portTextField.getText());

            if (useBroadcast()) {
                new BroadcastSender(host, port).send(message);
                log("[Broadcast]: " + message);
            } else {
                new MulticastSender(host, port).send(message);
                log("[Multicast]: " + message);
            }

        } catch (NumberFormatException e) {
            log("Invalid port number!");
        } catch (Exception e) {
            log(e.getMessage());
        }

    }


    @FXML
    private void onStopListeningButtonClick() {
        setButtonEnabled(stopListeningButton, false);
        setButtonEnabled(startListeningButton, true);

        if (udpReceiver == null)
            return;

        stopReceiverThread();

        log("Listener stopped!");
    }


    @FXML
    private void onStartListeningButtonClick() {
        setButtonEnabled(startListeningButton, false);
        setButtonEnabled(stopListeningButton, true);

        if (udpReceiver != null)
            return;

        String host = ipTextField.getText();
        int port;

        try {
            port = Integer.parseInt(portTextField.getText());
            udpReceiver = new UDPReceiver(host, port, this);
            udpReceiver.start();
            log("Listener started!");
        } catch (Exception e) {
            log(e.getMessage());
            udpReceiver = null;
            setButtonEnabled(startListeningButton, true);
            setButtonEnabled(stopListeningButton, false);
        }

    }


    private boolean useBroadcast() {
        return checkBox.isSelected();
    }


    @Override
    public void log(String message) {
        Platform.runLater(() -> {
            lock.lock();
            logsTextArea.appendText(message + "\n");
            lock.unlock();
        });
    }

    private void setButtonEnabled(Button button, boolean enabled) {
        button.setDisable(!enabled);
    }

    public void stopReceiverThread() {
        if (udpReceiver == null || !udpReceiver.isAlive())
            return;

        udpReceiver.stopThread();
        udpReceiver = null;
    }

}