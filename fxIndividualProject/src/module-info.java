module fxIndividualProject {
	requires javafx.controls;
	requires org.xerial.sqlitejdbc;
	requires javafx.fxml;
	requires javafx.base;
	requires java.desktop;
	requires jdk.compiler;
	requires javafx.graphics;
	requires javafx.swing;
	
	opens fxIndividualProject to javafx.graphics, javafx.fxml;
	opens screenControllers to javafx.graphics, javafx.fxml;
}
