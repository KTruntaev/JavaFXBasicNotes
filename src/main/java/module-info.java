module com.example.introsamples {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.introsamples to javafx.fxml;
    exports com.example.introsamples;
}