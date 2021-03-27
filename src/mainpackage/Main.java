package mainpackage;

import utilities.GitHandler;

public class Main 
{
	public static void main(String[] args)
	{
		String user="matteo";
		String locPath="/home/"+user;
		String workspace="/eclipse-workspace/ISW2-Deliverable1/";
		String projectName="FALCON";
		String csvPath= locPath+workspace+"csv";
		String gitLocalRepositoryDest = locPath+workspace+"/FALCON_PROJECT";
		String gitHubRepositoryURL ="https://github.com/apache/falcon.git";
		
		// Se la variabile checkDifference viene impostata a true, verranno controllate le differenze 
		// fra i ticket presenti su Jyra ed ogni singolo commit del progetto Falcon. 
		// Mettere true o false quindi genera due csv differenti, uno che considera 
		// questa differenza ed uno che invece considera solo i ticket di Jyra direttamente.
		Boolean checkDifference = false;
		//Crea una copia della repository a meno che non esista già
		GitHandler.cloneRepository(gitLocalRepositoryDest,gitHubRepositoryURL);
		//Genera il csv
		IssueCounter.generateCSV(projectName,csvPath,gitLocalRepositoryDest,checkDifference);
	}
}