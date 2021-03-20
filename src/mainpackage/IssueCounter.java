package mainpackage;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;

import utilities.ReleaseInfoGetter;
import utilities.TicketInfoGetter;

public class IssueCounter 
{
	public static Integer[][] countFixedIssues(String project_name, String csv_path) throws IOException, JSONException
	{
		//ReleaseInfoGetter.getInfo(project_name, csv_path);
		return TicketInfoGetter.getInfo(project_name);
	}
	public static void generateCSV(Integer data[][],String csv_path)
	{
		FileWriter fileWriter = null;
		try 
		{
			fileWriter = null;
		    String outname = csv_path + "ProjectFalconDatas.csv";
		    fileWriter = new FileWriter(outname);
		    fileWriter.append("Month,Fixed tickets");
		    fileWriter.append("\n");
		    String[] months= {"January","February","March","April","May","June","July",
		    		"August","September","October","November","Decembrer"};
		    String[] years= {"2013","2014","2015","2016","2017","2018"};
		    for ( int r = 0; r < 6; r++) 
		    {
		    	for (int c = 0; c < 12 ; c++)
		    	{
		    		fileWriter.append(months[c]+" "+years[r]+";");
		    		fileWriter.append(data[r][c].toString()+"\n");
		    	}
		    }

		} 
		catch (Exception e) 
		{
			System.out.println("Error in csv writer");
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
		        System.out.println("Error while flushing/closing fileWriter !!!");
		        e.printStackTrace();
		    }
		}
		return;
	}
}
