package utilities;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.util.FS;

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
						System.out.println("Checking differences in the resolution date between Jyra and commit for ticket: "+key);
						//Sostituisci la data solo se ne trovi una più recente.
						if ((date[1] < y) || (date[1]==y && date[0] < m))
						{
							System.out.println("DIFFERENCE FOUND: Ticket "+key+" 's resolution date has "
									+ "been changed from the one reported on Jyra ("+date[0]+"/"+date[1]+
									") to a more recent one reported in a newer commit ("+m+"/"+y+").");
		    			
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
			System.out.println("Error during comparison between Jyra and commits.");
			e.printStackTrace();
		}
		return date;
	}
	
	//Questo metodo serve a creare la repository locale di Falcon (qualora non esistesse ancora) 
	//nella cartella di lavoro per controllare eventuali difformità sulle date dei commit e quelle di Jyra.
	public static void cloneRepository(String local_path, String url)
	{
		try 
		{		
			if (!RepositoryCache.FileKey.isGitRepository(new File(local_path+"/.git"), FS.DETECTED)) 
			{
				System.out.println("Creating a Falcon local repository...");
				Git.cloneRepository().setURI(url).setDirectory(new File(local_path)).call();
				System.out.println("Copy done successfully.");
			} 
		} 
		catch (GitAPIException e) 
		{
			System.out.println("Error in Git services.");
			e.printStackTrace();
		} 
	}
}