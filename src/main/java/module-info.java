module com.maspro.masprojavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.testng;

    opens com.maspro.masprojavafx to javafx.fxml;
    exports com.maspro.masprojavafx;
}