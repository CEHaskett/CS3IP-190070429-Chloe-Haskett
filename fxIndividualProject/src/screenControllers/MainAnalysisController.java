package screenControllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import fxIndividualProject.DatabaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainAnalysisController {

	@FXML private Label lblErrorMessage;
	
	@FXML private HBox hBoxAcademic;
	private ArrayList<CheckBox> academicYearsButtonsList;
	
	@FXML private HBox hBoxCourse;
	private ArrayList<CheckBox> courseNamesButtonsList;
	
	
	@FXML private CheckBox cbFemale;
	@FXML private CheckBox cbMale;
	
	@FXML private CheckBox cbDistinction;
	@FXML private CheckBox cbMerit;
	@FXML private CheckBox cbPass;
	@FXML private CheckBox cbLowerAward;
	
	@FXML private ToggleGroup xAxisRadioGroup;
	@FXML private ToggleButton rbAcademicYear;
	@FXML private ToggleButton rbCourse;
	@FXML private ToggleButton rbGender;
	@FXML private ToggleButton rbAward;
	
	@FXML private ToggleGroup graphRadioGroup;
	@FXML private ToggleButton rbBarChart;
	@FXML private ToggleButton rbLineChart;
	@FXML private ToggleButton rbPieChart;
	
	@FXML private ToggleGroup seperatesRadioGroup;
	@FXML private ToggleButton rbSeperatesSimple;
	@FXML private ToggleButton rbSeperatesAcademicYear;
	@FXML private ToggleButton rbSeperatesCourse;
	@FXML private ToggleButton rbSeperatesGender;
	@FXML private ToggleButton rbSeperatesAward;
	
	
	public void initialize() {
		
		Pair<ArrayList<String>, ArrayList<Pair<String, ArrayList<String>>>> academicYearsAndCourses = DatabaseController.courseAcademicYear();
		if (academicYearsAndCourses != null) {
			
			ArrayList<String> academicYears = (ArrayList<String>) academicYearsAndCourses.getKey();
			ArrayList<Pair<String, ArrayList<String>>> courseNameAndCodes = (ArrayList<Pair<String, ArrayList<String>>>) academicYearsAndCourses.getValue();
			
			//academicYears
			academicYearsButtonsList = new ArrayList<CheckBox>();
			Iterator<String> i = academicYears.iterator();
			while (i.hasNext()){
				academicYearsButtonsList.add(new CheckBox(i.next()));
			}
			hBoxAcademic.getChildren().addAll(academicYearsButtonsList);
			
			//courses
			courseNamesButtonsList = new ArrayList<CheckBox>();
			Iterator<Pair<String, ArrayList<String>>> j = courseNameAndCodes.iterator();
			while (j.hasNext()){
				courseNamesButtonsList.add(new CheckBox(j.next().getKey()));
			}
			hBoxCourse.getChildren().addAll(courseNamesButtonsList);
		}
		
		xAxisRadioGroup = new ToggleGroup();
		xAxisRadioGroup.getToggles().addAll(rbAcademicYear, rbCourse, rbGender, rbAward);
		rbGender.setSelected(true);
		
		graphRadioGroup = new ToggleGroup();
		graphRadioGroup.getToggles().addAll(rbBarChart, rbLineChart, rbPieChart);
		rbBarChart.setSelected(true);
		
		seperatesRadioGroup = new ToggleGroup();
		seperatesRadioGroup.getToggles().addAll(rbSeperatesSimple, rbSeperatesAcademicYear, rbSeperatesCourse, rbSeperatesGender, rbSeperatesAward);
		rbSeperatesSimple.setSelected(true);
	}
	
	@FXML
	public void generateGraphPressed() {	
		String xAxis = "";
		String seperates = null; 
		
		ArrayList<String> arrayAcademicYears = new ArrayList<String>();
		ArrayList<String> arrayJacsCodes = new ArrayList<String>();
		ArrayList<String> arrayIsMale = new ArrayList<String>();
		ArrayList<String> arrayAward = new ArrayList<String>();
		
		//Academic Year
		Iterator<CheckBox> i = academicYearsButtonsList.iterator();
		while (i.hasNext()) {
			CheckBox rose = i.next();
			if (rose.isSelected() == true){
				arrayAcademicYears.add(rose.getText());
			}
		}
		
		//Course
		Iterator<CheckBox> j = courseNamesButtonsList.iterator();
		while (j.hasNext()) {
			CheckBox lime = j.next();
			if (lime.isSelected() == true){
				arrayJacsCodes.add(lime.getText());//TODO FIX so course names can be shown not jacs codes
			}
		}	
		
		//Gender
		if (cbFemale.isSelected() == true){
			arrayIsMale.add("0");
		}
		if (cbMale.isSelected() == true){
			arrayIsMale.add("1");
		}
		
		//Award
		if (cbDistinction.isSelected() == true){
			arrayAward.add("D");
		}
		if (cbMerit.isSelected() == true){
			arrayAward.add("M");
		}
		if (cbPass.isSelected() == true){
			arrayAward.add("P");
		}
		if (cbLowerAward.isSelected() == true){
			arrayAward.add("L");
		}
		
		//X-Axis
		ToggleButton xAxisSelected = (ToggleButton) xAxisRadioGroup.getSelectedToggle();
		if (rbAcademicYear == xAxisSelected) {
			xAxis = "academic_year";
		} else if (rbCourse == xAxisSelected) {
			xAxis = "jacs_code";
		} else if (rbGender == xAxisSelected) {
			xAxis = "is_male";
		} else if (rbAward == xAxisSelected) {
			xAxis = "award";
		} else {
			xAxis = "award";
		}
		
		//Seperates 
		ToggleButton seperatesSelected = (ToggleButton) seperatesRadioGroup.getSelectedToggle();
		if (rbSeperatesSimple == seperatesSelected) {
			seperates = null;
		} else if (rbSeperatesAcademicYear == seperatesSelected) {
			seperates = "academic_year";
		} else if (rbSeperatesCourse == seperatesSelected) {
			seperates = "jacs_code";
		} else if (rbSeperatesGender == seperatesSelected) {
			seperates = "is_male";
		} else if (rbSeperatesAward == seperatesSelected) {
			seperates = "award";
		} else {
			seperates = null;
		}
		
		String[] academicYears = arrayAcademicYears.toArray(new String[0]);
		String[] jacsCodes = arrayJacsCodes.toArray(new String[0]);
		String[] isMale = arrayIsMale.toArray(new String[0]);
		String[] award = arrayAward.toArray(new String[0]);
		
		ArrayList<Pair<String, ArrayList<Pair<String, Number>>>> informationPassthrough = 
				DatabaseController.dataFromGraph(seperates, xAxis, academicYears, jacsCodes, isMale, award);
		
		if (informationPassthrough == null) {
			lblErrorMessage.setText("Do not select all filters for the same topic");
		} else {
			ToggleButton selectedGraphButton = (ToggleButton) graphRadioGroup.getSelectedToggle();
			
			Parent root;
			boolean resourceSet = false;
			final FXMLLoader loader = new FXMLLoader();
			
			if (selectedGraphButton == rbBarChart) {
				loader.setLocation(getClass().getResource("/fxmlScreens/BarGraphScreen.fxml"));
				loader.setController(new BarChartController(informationPassthrough, xAxis));
				resourceSet = true;
			} else if (selectedGraphButton == rbLineChart) {
				loader.setLocation(getClass().getResource("/fxmlScreens/LineGraphScreen.fxml"));
				loader.setController(new LineGraphController(informationPassthrough, xAxis));
				resourceSet = true;
			} else if (selectedGraphButton == rbPieChart) {
				loader.setLocation(getClass().getResource("/fxmlScreens/PieChartScreen.fxml"));
				loader.setController(new PieChartController(informationPassthrough));
				resourceSet = true;
			} else {
				lblErrorMessage.setText("Select a graph type");
			}
			
			if (resourceSet == true){
				try {
					root = (Parent)loader.load();
					
					final Stage graphStage = new Stage();
					graphStage.setTitle("Graph");
					graphStage.setScene(new Scene(root, 500, 400));
					graphStage.show();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	public void helpPressed() {
		Parent root;
		final FXMLLoader loader = new FXMLLoader();
		loader.setController(new HelpController());
		
		try {
			root = FXMLLoader.load(getClass().getResource("/fxmlScreens/HelpScreen.fxml"));
			final Stage helpStage = new Stage();
			helpStage.setTitle("Help Information");
			helpStage.setScene(new Scene(root, 500, 400));
			helpStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void manageSourcesPressed() {
		Parent root;
		final FXMLLoader loader = new FXMLLoader();
		loader.setController(new ManageSourcesController());
		
		try {
			root = FXMLLoader.load(getClass().getResource("/fxmlScreens/ManageSourcesScreen.fxml"));
			final Stage manageSourcesStage = new Stage();
			manageSourcesStage.setTitle("Manage Sources");
			manageSourcesStage.setScene(new Scene(root, 900, 500));
			manageSourcesStage.show();
			lblErrorMessage.getScene().getWindow().hide();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
