package fxIndividualProject;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxmlScreens/ManageSourcesScreen.fxml"));
						
	        primaryStage.setTitle("Manage Sources");
	        primaryStage.setScene(new Scene(root, 900, 500));
	        primaryStage.show();//*/
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		String name = "Chloe";
		System.out.println("Hello " + name);
		
		DatabaseController.initialize();
		launch(args);
		
		System.out.println("Bye " + name);
	}
}
