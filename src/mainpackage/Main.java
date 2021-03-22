package mainpackage;

public class Main 
{
	public static void main(String[] args)
	{
		String project_name="FALCON";
		String csv_path="/home/matteo/eclipse-workspace/ISW2-Deliverable1/csv/";
		
		//Codice per clonare la repo di GitHub in locale qualora dovesse servire
		//String gitLocalRepositoryDest = "/home/matteo/eclipse-workspace/ISW2-Deliverable1/FALCON_PROJECT";
		//String gitHubRepositoryURL ="https://github.com/apache/falcon.git";
		//GitHandler.cloneRepository(gitLocalRepositoryDest,gitHubRepositoryURL);
		
		IssueCounter.generateCSV(project_name,csv_path);
	}
}
