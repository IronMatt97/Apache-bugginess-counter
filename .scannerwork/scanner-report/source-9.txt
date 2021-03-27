package mainpackage;

import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.TicketInfoGetter;

public class IssueCounter 
{
	private static Logger logger = Logger.getLogger(IssueCounter.class.getName());
	private IssueCounter() 
	{
	    throw new IllegalStateException("Utility class");
	}
	
	//Metodo che restituisce il CSV da cui poi realizzare il process control chart.
	public static void generateCSV(String projectName,String csvPath, Boolean checkDifference)
	{
	    String[] months= {"January","February","March","April","May","June","July",
	    		"August","September","October","November","December"};
	    String[] years= {"2013","2014","2015","2016","2017","2018"};
	    
	    String outname;
	    if (Boolean.TRUE.equals(checkDifference))
	    	outname = csvPath +"Project"+projectName+"ComparedTicketDatas.csv";
	    else
	    	outname = csvPath +"Project"+projectName+"JyraTicketDatas.csv";
		
		//Ottieni una matrice contenente i fixed tickets per mese
	    Integer[][] data=TicketInfoGetter.getInfo(projectName,checkDifference);
	    
		try (FileWriter fileWriter = new FileWriter(outname)) 
		{	
		    //Scrivo il file CSV, riportando i mesi ed il numero di ticket risolti
		    fileWriter.append("Months;Fixed tickets\n");
		    for ( int r = 0; r < 6; r++) 
		    {
		    	for (int c = 0; c < 12 ; c++)
		    	{
		    		fileWriter.append(months[c]+" "+years[r]+";"+data[r][c].toString()+"\n");
		    	}
		    }
		} 
		catch (Exception e) 
		{
			logger.log(Level.SEVERE,"CSV writer encountered a problem.");
		    e.printStackTrace();
		} 
	}
}