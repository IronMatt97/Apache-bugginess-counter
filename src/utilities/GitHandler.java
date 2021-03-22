package utilities;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class GitHandler 
{
	//Questo metodo controlla la validità della data riportata su Jyra. Qualora differisse
	//da quella riportata nei commit, viene selezionata la più recente.
	public Integer[] checkDateValidity(Integer[] date, String key) 
	{
		File gitDir = new File("/home/matteo/eclipse-workspace/ISW2-Deliverable1/FALCON_PROJECT/.git");

		RepositoryBuilder builder = new RepositoryBuilder();
		Repository repository;
		try {
				repository = builder.setGitDir(gitDir).readEnvironment().findGitDir().build();
				Git git = new Git(repository);
				RevWalk walk = new RevWalk(repository);
				RevCommit commit = null;

				Iterable<RevCommit> logs = git.log().call();
				Iterator<RevCommit> i = logs.iterator();

				while (i.hasNext()) 
				{
					commit = walk.parseCommit( i.next() );
					if(commit.getShortMessage().contains(key+" "))
					{
						//Ottengo il mese e l'anno dei commit con il ticket cercato
						Integer m = commit.getAuthorIdent().getWhen().getMonth()+1;
						Integer y = commit.getAuthorIdent().getWhen().getYear()+1900;
						System.out.println("Controllo difformità del ticket: "+key);
						if ((date[1] < y) || (date[1]==y && date[0] < m))
						{
							System.out.println("DIFFORMITA' RISCONTRATA: E' stata cambiata la data del ticket "
									+key+" da quella riportata su Gyra ("+date[0]+"/"+date[1]+
									") a quella più recente riportata nell'ultimo commit relativo ("
									+m+"/"+y+").");
		    			
							date[0]=m;
							date[1]=y;
						}
					}
				}
				git.close();
				walk.close();
		} 
		catch (IOException | GitAPIException e) 
		{
			e.printStackTrace();
		}
		return date;
	}
	
	//Questo metodo serve a creare la repository di Falcon nella cartella di lavoro per
	//controllare eventuali difformità sulle date dei commit e quelle di Gyra.
	public static void cloneRepository(String local_path, String url)
	{
		try 
		{
				System.out.println("Creazione di una copia locale della repository di Falcon...");
				Git.cloneRepository().setURI(url).setDirectory(new File(local_path)).call();
				System.out.println("Copia creata con successo.");
		} 
		catch (GitAPIException e) 
		{
			e.printStackTrace();
		} 
	}
}