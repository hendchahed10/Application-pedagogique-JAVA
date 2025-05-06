module com.example.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.example.projet to javafx.fxml;
    opens com.example.projet.Controller to javafx.fxml;
    exports com.example.projet;
    exports com.example.projet.Controller;
}