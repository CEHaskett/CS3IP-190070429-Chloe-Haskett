package fxIndividualProject;

public class Triplet {
	private boolean success;
	private int sourceID;
	private String description; 
	
	public Triplet(boolean passedSuccess, int passedSourceID, String passedDescription) {
		this.success = passedSuccess;
		this.sourceID = passedSourceID;
		this.description = passedDescription;
	}

	public boolean getSuccess() {return success;}
	public Integer getSourceID() {return sourceID;}
	public String getDescription() {return description;}
}
