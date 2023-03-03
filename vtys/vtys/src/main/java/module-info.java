module com.example.vtys {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.vtys to javafx.fxml;
    exports com.example.vtys;
}