package screenControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HelpController {
	
	@FXML
	private Button btnBack;

	public void initialize() {}
	
	@FXML
	public void backPressed() {
		btnBack.getScene().getWindow().hide();
	}
}
