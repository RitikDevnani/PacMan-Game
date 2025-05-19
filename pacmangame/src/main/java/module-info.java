module com.example.packamngame {
        requires javafx.fxml;
        requires javafx.controls;
        requires javafx.graphics;

        // Export the new package name to javafx.graphics
        exports com.example.packamngame to javafx.graphics;

        opens com.example.packamngame to javafx.fxml;
}
