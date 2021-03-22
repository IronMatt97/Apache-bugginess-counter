package mainpackage;

import utilities.GitHandler;

public class Main 
{
	public static void main(String[] args)
	{
		String projectName="FALCON";
		String csvPath="/home/matteo/eclipse-workspace/ISW2-Deliverable1/csv/";
		String gitLocalRepositoryDest = "/home/matteo/eclipse-workspace/ISW2-Deliverable1/FALCON_PROJECT";
		String gitHubRepositoryURL ="https://github.com/apache/falcon.git";
		GitHandler.cloneRepository(gitLocalRepositoryDest,gitHubRepositoryURL);
		IssueCounter.generateCSV(projectName,csvPath);
	}
}
