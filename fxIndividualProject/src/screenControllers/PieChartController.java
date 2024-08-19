package screenControllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;

public class PieChartController {

	@FXML
	public PieChart pieChart;
	
	public PieChartController(ArrayList<Pair<String, ArrayList<Pair<String, Number>>>> informationPassthrough) {
		//seriesPointsList = informationPassthrough;
	}

	@FXML
	public void initialize() {
		//TODO create chart
		
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30));
		pieChart = new PieChart(pieChartData);
		//pieChart.setTitle("Imported Fruits");
		pieChart.setLabelsVisible(true);
		System.out.println("Help");
	}
	
	@FXML
	public void saveGraphPressed() {
		try {
			WritableImage wi = new WritableImage((int) pieChart.getWidth(), (int) pieChart.getHeight());
            WritableImage snapshot = pieChart.snapshot(new SnapshotParameters(), wi);
            
            final JFileChooser fileChooser = new JFileChooser();
    		fileChooser.showSaveDialog(null);
    		
    		if (fileChooser.getSelectedFile() != null) {
    			File file = new File (fileChooser.getSelectedFile().getAbsoluteFile() + ".png");
    			ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);      
    		}
            
        } catch (IOException ex) {
            //TODO error handling
        }
	}
	
	@FXML
	public void closePressed() {
		pieChart.getScene().getWindow().hide();
	} 
}
