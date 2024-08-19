package fxIndividualProject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FXController {
	
	@FXML
    private Label label;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }
}
