package org.example.udp_sender;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UDPSenderApp extends Application {

    public static final int MAX_BYTES = 1000;
    private FXMLLoader fxmlLoader;


    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(UDPSenderApp.class.getResource("sender-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("UDP");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        GuiController controller = fxmlLoader.getController();
        controller.stopReceiverThread();
    }

}