package screenControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class AddingCSVController {
	
	@FXML private Label lblFileAddress;
	@FXML private TextField txtFileDescription;
	
	@FXML private ToggleButton rbHecos;
	@FXML private ToggleButton rbJacs30;
	@FXML private ToggleButton rbJacs20;
	@FXML private ToggleButton rbJacs17;
	@FXML private ToggleButton rbHesacode;
	
	@FXML private ToggleGroup radioGroup;
	
	private final String fileAddressLocal;
	private String jacsEdition;
	private String description;
	private boolean confirmed;	
	
	public AddingCSVController(String fileAddress) {
		this.fileAddressLocal = fileAddress;
		this.confirmed = false;
	}
	
	@FXML
	public void initialize() {
		lblFileAddress.setText(fileAddressLocal);
		
		radioGroup = new ToggleGroup();
		radioGroup.getToggles().addAll(rbHecos, rbJacs30, rbJacs20, rbJacs17, rbHesacode);
		rbJacs30.setSelected(true);
	} 
	
	@FXML
	public void confirmPressed() {
		jacsEdition = ((ToggleButton)radioGroup.getSelectedToggle()).getText();
		description = txtFileDescription.getText();
		confirmed = true;
		lblFileAddress.getScene().getWindow().hide();
	}
	
	@FXML
	public void cancelPressed() {
		lblFileAddress.getScene().getWindow().hide();
	}
	
	public String getJacsEdition() {return jacsEdition;}
	public String getDescription() {return description;}
	public boolean isConfirmed() {return confirmed;}
}
