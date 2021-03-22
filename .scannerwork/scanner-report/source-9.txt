package mainpackage;

public class Main {
	public static void main(String[] args)
	{
		String project_name="FALCON";
		String csv_path="/home/matteo/eclipse-workspace/ISW2-Deliverable1/csv/";
		
		IssueCounter.generateCSV(project_name,csv_path);
	}

}
