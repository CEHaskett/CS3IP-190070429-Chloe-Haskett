package screenControllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;

public class LineGraphController {

	private ArrayList<Pair<String, ArrayList<Pair<String, Number>>>> seriesPointsList;
	private String xAxisTitle;
	
	@FXML public LineChart<String,Number> lineChart;
	@FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
	
	public LineGraphController(ArrayList<Pair<String, ArrayList<Pair<String, Number>>>> informationPassthrough, String xAxisLabel) {
		seriesPointsList = informationPassthrough;
		xAxisTitle = xAxisLabel;
	}

	@FXML
	public void initialize() {
		
		xAxis.setLabel(xAxisTitle);
        yAxis.setLabel("Number of Students");
        
        lineChart.setLegendVisible(false);
        if (seriesPointsList.size() > 1) { 
        	lineChart.setLegendVisible(true);
        }
				
		Iterator<Pair<String, ArrayList<Pair<String, Number>>>> i = seriesPointsList.iterator();
		while (i.hasNext()){
			XYChart.Series<String, Number> addingSeries = new XYChart.Series<String, Number>();
			Pair<String, ArrayList<Pair<String, Number>>> pointsList = i.next();
			addingSeries.setName(pointsList.getKey()); 
			
			Iterator<Pair<String, Number>> j = pointsList.getValue().iterator();
			while (j.hasNext()){
				Pair<String, Number> point = j.next();
				addingSeries.getData().add(new XYChart.Data<String, Number>(point.getKey(), point.getValue()));
			}
			lineChart.getData().addAll(addingSeries);
		}
	}
	
	@FXML
	public void saveGraphPressed() {
		try {   
            WritableImage wi = new WritableImage((int) lineChart.getWidth(), (int) lineChart.getHeight());
            WritableImage snapshot = lineChart.snapshot(new SnapshotParameters(), wi);
            
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
		lineChart.getScene().getWindow().hide();
	} 
}
