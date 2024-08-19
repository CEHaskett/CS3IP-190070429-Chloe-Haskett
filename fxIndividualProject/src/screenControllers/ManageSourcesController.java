package screenControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fxIndividualProject.DatabaseController;
import fxIndividualProject.Triplet;

public class ManageSourcesController {

	@FXML private ListView<Pair<Integer, String>> listRecordSources;
	@FXML private Button btnDelete;
	@FXML private Button btnToMain;
		
	
	public void initialize() {
		listRecordSources.getItems().removeAll(); //TODO try and understand then fix or just leave your choice
		List<Pair<Integer, String>> recordSourceList = DatabaseController.recordSourceList();
		listRecordSources.setItems(FXCollections.observableList(recordSourceList));
		
		if (recordSourceList.isEmpty()){ //TODO test this works when empty
			btnDelete.setDisable(true);
			btnToMain.setDisable(true);	
		} else {
			btnDelete.setDisable(false);
			btnToMain.setDisable(false);
		}	
		
	}
	
	@FXML
	public void addCSVPressed() {
		Window window = listRecordSources.getScene().getWindow();
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(window);
		
        if (file != null) {
        	final FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxmlScreens/AddingCSVScreen.fxml"));
			final AddingCSVController controller = new AddingCSVController(file.getPath());
			loader.setController(controller);
			try {
				Parent root = (Parent)loader.load();
				
				final Stage addingCSVStage = new Stage();
				addingCSVStage.setScene(new Scene(root, 400, 270));
				addingCSVStage.showAndWait();

				if (controller.isConfirmed()) {
					Triplet csvAdded = DatabaseController.addCSVFile(file.getPath(), controller.getJacsEdition(), controller.getDescription());
					if (csvAdded.getSuccess() == true) {
						btnDelete.setDisable(false);
						btnToMain.setDisable(false);
						listRecordSources.getItems().add(new Pair<Integer, String>(csvAdded.getSourceID(), csvAdded.getDescription()));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	@FXML
	public void deletePressed() {
		int selectedIdx = listRecordSources.getSelectionModel().getSelectedIndex();
		if (selectedIdx >= 0) {
			Pair<Integer, String> selectedSource = listRecordSources.getSelectionModel().getSelectedItem();
			DatabaseController.deleteSelectedRecord(selectedSource.getKey());
			listRecordSources.getItems().remove(listRecordSources.getItems().indexOf(selectedSource));
			
			if (listRecordSources.getItems().isEmpty()) {
				btnDelete.setDisable(true);
				btnToMain.setDisable(true);
			}
		}
	}
	
	@FXML
	public void toMainPressed() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/fxmlScreens/MainAnalysisScreen.fxml"));
			final Stage mainStage = new Stage();
			mainStage.setTitle("Main");
			mainStage.setScene(new Scene(root, 600, 500));
			mainStage.show();
			listRecordSources.getScene().getWindow().hide();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
