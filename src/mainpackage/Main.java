package mainpackage;

import java.io.IOException;
import org.json.JSONException;

public class Main {
	public static void main(String[] args) throws IOException, JSONException
	{
		String project="FALCON";
		String csv_path="/home/matteo/eclipse-workspace/ISW2-Deliverable1/csv/";
		Integer data[][]=IssueCounter.countFixedIssues(project,csv_path);
		System.out.println(data);
		IssueCounter.generateCSV(data,csv_path);
	}

}
