package mainpackage;

import java.io.FileWriter;
import java.io.IOException;
import utilities.TicketInfoGetter;

public class IssueCounter 
{
	//Metodo che restituisce il CSV da cui poi realizzare il process control chart.
	public static void generateCSV(String projectName,String csvPath)
	{
	    String[] months= {"January","February","March","April","May","June","July",
	    		"August","September","October","November","Decembrer"};
	    String[] years= {"2013","2014","2015","2016","2017","2018"};
		
		//Ottieni una matrice contenente i fixed tickets per mese
	    Integer data[][]=TicketInfoGetter.getInfo(projectName);
		FileWriter fileWriter = null;
		try 
		{
			fileWriter = null;
		    String outname = csvPath +"Project"+projectName+"TicketDatasNEWCHECK.csv";
		    fileWriter = new FileWriter(outname);
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
			System.out.println("Error in CSV writer.");
		    e.printStackTrace();
		} 
		finally 
		{
		    try 
		    {
		        fileWriter.flush();
		        fileWriter.close();
		    } 
		    catch (IOException e) 
		    {
		        System.out.println("Error while flushing/closing fileWriter.");
		        e.printStackTrace();
		    }
		}
	}
}