module org.example.udp_sender {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.validator;


    opens org.example.udp_sender to javafx.fxml;
    exports org.example.udp_sender;
    exports org.example.udp_sender.UDP;
    opens org.example.udp_sender.UDP to javafx.fxml;
}