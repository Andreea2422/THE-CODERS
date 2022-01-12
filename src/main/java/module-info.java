module ro.ubbcluj.map.thecoders {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens ro.ubbcluj.map.thecoders to javafx.fxml;
    exports ro.ubbcluj.map.thecoders;
    exports ro.ubbcluj.map.thecoders.controller;
    opens ro.ubbcluj.map.thecoders.controller to javafx.fxml;
    opens ro.ubbcluj.map.thecoders.domain to javafx.base;

}