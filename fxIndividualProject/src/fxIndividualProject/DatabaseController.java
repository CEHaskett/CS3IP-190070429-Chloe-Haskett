package fxIndividualProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.sqlite.SQLiteDataSource;

import javafx.util.Pair;

public class DatabaseController {

	private static SQLiteDataSource ds;
	
	public DatabaseController() {}
	
	public static void initialize() {
		ds = null;
        try {
            ds = new SQLiteDataSource();
            ds.setUrl("jdbc:sqlite:preparing_data.db");
		} catch(Exception e) {
	    	// TODO Block of code to handle errors
			e.printStackTrace();
	    }
	}
	
	public static ArrayList<Pair<String, ArrayList<Pair<String, Number>>>> dataFromGraph(String seperates, String xAxis, String[] academicYears, String[] jacsCodes, String[] isMale, String[] award) {
		ResultSet results = null;
		String query;
		ArrayList<Pair<String, ArrayList<Pair<String, Number>>>> toReturn = new ArrayList<Pair<String, ArrayList<Pair<String, Number>>>>();
		int xAxisNumber;
		int seperatesNumber;
		String xAxisOrder;
		String seperatesOrder;
		
		if (xAxis == "academic_year") {
			xAxisNumber = 1;
			xAxisOrder = "r.academic_year";
		} else if (xAxis == "jacs_code") {
			xAxisNumber = 2;
			xAxisOrder = "j.course_title";
		} else if (xAxis == "is_male") {
			xAxisNumber = 3;
			xAxisOrder = "g.gender";
		} else if (xAxis == "award") {
			xAxisNumber = 4;
			xAxisOrder = "a.sort_order";
		} else {
			xAxisNumber = 1;
			xAxisOrder = "r.academic_year";
		}
		
		if (seperates == "academic_year") {
			seperatesNumber = 1;
			seperatesOrder = "r.academic_year";
		} else if (seperates == "jacs_code") {
			seperatesNumber = 2;
			seperatesOrder = "j.course_title";
		} else if (seperates == "is_male") {
			seperatesNumber = 3;
			seperatesOrder = "g.gender";
		} else if (seperates == "award") {
			seperatesNumber = 4;
			seperatesOrder = "a.sort_order";
		} else {
			seperatesNumber = 1;
			seperatesOrder = null;
		}
		
		
		try ( Connection conn = ds.getConnection() ) { 
			Statement stmt = conn.createStatement();
			if (seperates != null) {
				query = "SELECT r.academic_year, j.course_title, g.gender, a.award_name, r.number_of_students "
						+ "FROM record r "
						+ "INNER JOIN source s ON s.source_id = r.source_id "
						+ "INNER JOIN gender_lookup g ON g.code = r.is_male "
						+ "INNER JOIN award_lookup a ON a.code = r.award "
						+ "INNER JOIN jacs_lookup j "
							+ "ON (j.code_jacs = r.jacs_code AND j.edition_jacs = s.edition_jacs) "
						+ "WHERE r.academic_year NOT IN ('" + String.join("','", academicYears) + "') AND "
							+ "r.jacs_code NOT IN ('" + String.join("','",  jacsCodes) + "') AND "
							+ "r.is_male NOT IN (" + String.join(",", isMale) + ") AND "
							+ "r.award NOT IN ('" + String.join("','", award) + "') "
						+ "ORDER BY " + seperatesOrder + ", " + xAxisOrder + ";";
				
				results = stmt.executeQuery(query);
				
				while (results.isAfterLast() == false) {
					ArrayList<Pair<String, Number>> addingArray = new ArrayList<Pair<String, Number>>();
					addingArray.add(new Pair<String, Number>(results.getString(xAxisNumber) , results.getInt(5)));
					String series = results.getString(seperatesNumber);
					
					while (results.next() != false && series.equals(results.getString(seperatesNumber))) {
						addingArray.add(new Pair<String, Number>(results.getString(xAxisNumber) , results.getInt(5)));
					}
					toReturn.add(new Pair<String, ArrayList<Pair<String, Number>>> (series, addingArray));
				}
				
			} else {
				query = "SELECT r.academic_year, j.course_title, g.gender, a.award_name, r.number_of_students "
						+ "FROM record r "
						+ "INNER JOIN source s ON s.source_id = r.source_id "
						+ "INNER JOIN gender_lookup g ON g.code = r.is_male "
						+ "INNER JOIN award_lookup a ON a.code = r.award "
						+ "INNER JOIN jacs_lookup j "
							+ "ON (j.code_jacs = r.jacs_code AND j.edition_jacs = s.edition_jacs) "
						+ "WHERE r.academic_year NOT IN ('" + String.join("','", academicYears) + "') AND "
							+ "r.jacs_code NOT IN ('" + String.join("','",  jacsCodes) + "') AND "
							+ "r.is_male NOT IN (" + String.join(",", isMale) + ") AND "
							+ "r.award NOT IN ('" + String.join("','", award) + "') "
						+ "ORDER BY " + xAxisOrder + ";";
				
				results = stmt.executeQuery(query);
				
				ArrayList<Pair<String, Number>> addingArray = new ArrayList<Pair<String, Number>>();
				while (results.next()) {
					addingArray.add(new Pair<String, Number>(results.getString(xAxisNumber) , results.getInt(5)));
				}
				toReturn.add(new Pair<String, ArrayList<Pair<String, Number>>> ("series", addingArray));
			}
			
        } catch ( SQLException e ) {
        	// TODO Block of code to handle errors
            e.printStackTrace();
            return null;
        }
		return toReturn;
	}
	
	public static Pair<ArrayList<String>, ArrayList<Pair<String, ArrayList<String>>>> courseAcademicYear () {
		ResultSet academicYearsList;
		ArrayList<String> academicYears = new ArrayList<String>();
		ResultSet jacsLookupList;
		ArrayList<Pair<String, ArrayList<String>>> courseNameAndCodes = new ArrayList<Pair<String,  ArrayList<String>>>();
		
		try ( Connection conn = ds.getConnection() ) { 
			Statement stmt = conn.createStatement();
			
			String query = "SELECT DISTINCT academic_year FROM record";
			academicYearsList = stmt.executeQuery(query);
			while (academicYearsList.next()) {
				academicYears.add(academicYearsList.getString("academic_year"));
			}
			
			query = "SELECT DISTINCT jl.course_title, jl.code_jacs "
					+ "FROM jacs_lookup jl "
					+ "INNER JOIN record r ON r.jacs_code = jl.code_jacs "
					+ "ORDER BY jl.course_title";
			jacsLookupList = stmt.executeQuery(query);
			
			String courseTitle;
			ArrayList<String> jacsCode = new ArrayList<String>();
			
			while (jacsLookupList.isAfterLast() == false){
				courseTitle = jacsLookupList.getString("course_title");
				jacsCode.add(jacsLookupList.getString("code_jacs"));
				
				jacsLookupList.next();
				
				while (courseTitle.equals(jacsLookupList.getString("course_title"))) {
					jacsCode.add(jacsLookupList.getString("code_jacs"));
					jacsLookupList.next();
				}
				courseNameAndCodes.add(new Pair<String, ArrayList<String>>(courseTitle, jacsCode));
				jacsCode.clear();	
			} 	
		} catch ( SQLException e ) {
        	// TODO Block of code to handle errors
            e.printStackTrace();
            return null;
        }
		
		Pair<ArrayList<String>, ArrayList<Pair<String, ArrayList<String>>>> toReturn = new Pair<ArrayList<String>, ArrayList<Pair<String, ArrayList<String>>>>(academicYears, courseNameAndCodes);
		return toReturn;
	}
	
	public static ArrayList<Pair<Integer, String>> recordSourceList(){
		ArrayList<Pair<Integer, String>> listOfSources = new ArrayList<Pair<Integer, String>>();
		ResultSet dateSourceDBReturn;
		
		try ( Connection conn = ds.getConnection() ) { 
			Statement stmt = conn.createStatement();
			String query = "SELECT source_id, time_added, source_address, description FROM source";
			dateSourceDBReturn = stmt.executeQuery(query);
			
			while (dateSourceDBReturn.next()) {
				listOfSources.add(new Pair<Integer, String>(dateSourceDBReturn.getInt(1), (dateSourceDBReturn.getString(2) + " " + dateSourceDBReturn.getString(3) + "\n" + dateSourceDBReturn.getString(4))));
			}	
        } catch ( SQLException e ) {
        	// TODO Block of code to handle errors
            e.printStackTrace();
        }
		return listOfSources;
	}
	
	/*
	public static boolean deleteAllRecords() { //CURRENTLY UNUSED
		try ( Connection conn = ds.getConnection() ) { 
			Statement stmt = conn.createStatement();
			String query = "DELETE FROM record;"; //clear other table
			stmt.executeUpdate(query);
			
        } catch ( SQLException e ) {
        	// TODO Block of code to handle errors
            e.printStackTrace();
            return false;
        }
		return true;
	}
	*/
	
	public static boolean deleteSelectedRecord(int sourceID) {
		try ( Connection conn = ds.getConnection() ) { 
			Statement stmt = conn.createStatement();
			String query = "DELETE FROM record WHERE source_id = " + sourceID; 
			stmt.executeUpdate(query);
			query = "DELETE FROM source WHERE source_id = " + sourceID; 
			stmt.executeUpdate(query);
			
        } catch ( SQLException e ) {
        	// TODO Block of code to handle errors
            e.printStackTrace();
            return false;
        }
		return true;
	}
	
	public static Triplet addCSVFile (String csvFileAddress, String jacsEdition, String sourceDescription) { 
		
		ArrayList<String> valuesList = new ArrayList<String>();
		ResultSet source;
		int sourceID;
		String timeAddressDecription;
		
		//TODO add proper checking the address if for real csv file that is structured correctly
		//or just continue to use the fact it came from filechooser
		
		try (Connection conn = ds.getConnection(); 
				Statement stmt = conn.createStatement();
				Scanner scanner = new Scanner(new File(csvFileAddress))) {
		
			String query = "INSERT INTO source (source_address, edition_jacs, description)"
					+ "VALUES ('" + csvFileAddress + "', '" + jacsEdition + "', '" + sourceDescription + "')"
							+ "RETURNING *;";
			source = stmt.executeQuery(query);
			
			timeAddressDecription = source.getString(2) + " " + source.getString(3) + "\n" + source.getString(5);
			sourceID = source.getInt(1);
			
			scanner.nextLine(); //handles column labels, TODO write proper handling rather than hard coding
			scanner.nextLine();
			
			while (scanner.hasNextLine()) {
				Scanner rowScanner = new Scanner(scanner.nextLine());
		        rowScanner.useDelimiter(",");
		        while (rowScanner.hasNext()) {
		            valuesList.add(rowScanner.next());
		        }
		        
		        query = "INSERT INTO record (source_id, academic_year, jacs_code, is_male, award, number_of_students) "
		        		+ "VALUES "
		        		+ "('" + sourceID + "', '" + valuesList.get(0) + "', '"+ valuesList.get(2) + "', 1, 'D', " + valuesList.get(5) + " ), "
		        		+ "('" + sourceID + "', '" + valuesList.get(0) + "', '"+ valuesList.get(2) + "', 1, 'M', " + valuesList.get(6) + " ), "
		        		+ "('" + sourceID + "', '"+ valuesList.get(0) + "', '"+ valuesList.get(2) + "', 1, 'P', " + valuesList.get(7) + " ), "
		        		+ "('" + sourceID + "', '"+ valuesList.get(0) + "', '"+ valuesList.get(2) + "', 1, 'L', " + valuesList.get(8) + " ), "
	    				+ "('" + sourceID + "', '"+ valuesList.get(0) + "', '"+ valuesList.get(2) + "', 0, 'D', " + valuesList.get(9) + " ), "
		        		+ "('" + sourceID + "', '"+ valuesList.get(0) + "', '"+ valuesList.get(2) + "', 0, 'M', " + valuesList.get(10) + " ), "
		        		+ "('" + sourceID + "', '"+ valuesList.get(0) + "', '"+ valuesList.get(2) + "', 0, 'P', " + valuesList.get(11) + " ), "
		        		+ "('" + sourceID + "', '"+ valuesList.get(0) + "', '"+ valuesList.get(2) + "', 0, 'L', " + valuesList.get(12) + " );";
		        
		        stmt.executeUpdate(query);
		        valuesList.clear();
		    }
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Triplet(false, 0, null);
		} catch(Exception e) {
	    	  // TODO Block of code to handle errors
			e.printStackTrace();
			return new Triplet(false, 0, null);
	    }		
		return new Triplet(true, sourceID, timeAddressDecription);
	}
}
