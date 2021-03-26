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
		// Se la variabile checkDifference viene impostata a true, verranno controllate le differenze 
		// fra i ticket presenti su Jyra ed ogni singolo commit del progetto Falcon. 
		// Mettere true o false quindi genera due csv differenti, uno che considera 
		// questa differenza ed uno che invece considera solo i ticket di Jyra direttamente.
		Boolean checkDifference = true;
		
		GitHandler.cloneRepository(gitLocalRepositoryDest,gitHubRepositoryURL);
		IssueCounter.generateCSV(projectName,csvPath,checkDifference);
	}
}