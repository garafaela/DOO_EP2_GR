module garafaela.doo_ep2_gr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires com.google.gson;

    opens garafaela.doo_ep2_gr to javafx.fxml;
    opens garafaela.doo_ep2_gr.controller to javafx.fxml;
    opens garafaela.doo_ep2_gr.model to com.google.gson;
    opens garafaela.doo_ep2_gr.dao to com.google.gson;

    exports garafaela.doo_ep2_gr;
    exports garafaela.doo_ep2_gr.controller;
}