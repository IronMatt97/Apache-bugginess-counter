package utilities;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
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
	private static Logger logger =  Logger.getLogger(GitHandler.class.getName());
	//Questo metodo controlla la validità della data riportata su Jyra. Qualora differisse
	//da quella riportata nei commit, viene selezionata la più recente.
	public Integer[] checkDateValidity(String path, Integer[] date, String key) 
	{
		File gitDir = new File(path+"/.git");

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
						Integer m = obtainMonthNumber(commit.getAuthorIdent().getWhen().toString().substring(4,7));
						Integer y;
						if(commit.getAuthorIdent().getWhen().toString().substring(20,24).equals("CEST"))
							y = Integer.parseInt(commit.getAuthorIdent().getWhen().toString().substring(25,29));
						else
							y = Integer.parseInt(commit.getAuthorIdent().getWhen().toString().substring(24,28));
						
						logger.log(Level.INFO,"Checking differences in the resolution date between Jyra and commit for ticket: {0}",key);
						//Sostituisci la data solo se ne trovi una più recente.
						if ((date[1] < y) || (date[1].equals(y) && date[0] < m))
						{
							logger.log(Level.INFO,"DIFFERENCE FOUND: Ticket {0}'s resolution date has "
									+ "been changed from the one reported on Jyra to a more recent "
									+ "one reported in a newer commit.",key);
		    			
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
			logger.log(Level.INFO,"Error during comparison between Jyra and commits.");
		}
		return date;
	}
	
	//Questo metodo serve a creare la repository locale di Falcon (qualora non esistesse ancora) 
	//nella cartella di lavoro per controllare eventuali difformità sulle date dei commit e quelle di Jyra.
	public static void cloneRepository(String localPath, String url)
	{
		try 
		{		
			if (!RepositoryCache.FileKey.isGitRepository(new File(localPath+"/.git"), FS.DETECTED)) 
			{
				logger.log(Level.INFO,"Creating a Falcon local repository...");
				Git.cloneRepository().setURI(url).setDirectory(new File(localPath)).call();
				logger.log(Level.INFO,"Copy done successfully.");
			} 
		} 
		catch (GitAPIException e) 
		{
			logger.log(Level.INFO,"Error in Git services.");
		} 
	}
	private Integer obtainMonthNumber(String s)
	{
		String[] months= {"January","February","March","April","May","June","July",
	    		"August","September","October","November","December"};
		for (int i = 0; i<12; i++)
		{
			if(s.equals(months[i].substring(0,3)))
			{
				return i+1;
			}
		}
		
		logger.log(Level.INFO,"Something went wrong reading months.");
		return -1;
		
	}
}