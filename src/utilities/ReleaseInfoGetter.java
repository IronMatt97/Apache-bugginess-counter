package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class ReleaseInfoGetter 
{
	private static HashMap<LocalDateTime, String> releaseNames;
	private static HashMap<LocalDateTime, String> releaseID;
	private static ArrayList<LocalDateTime> releases;
	private static Integer numVersions;
	
	public static void getInfo(String projName, String path) throws IOException, JSONException 
	{
		releases = new ArrayList<LocalDateTime>();
        releaseNames = new HashMap<LocalDateTime, String>();
        releaseID = new HashMap<LocalDateTime, String> ();
		Integer i;
		String url = "https://issues.apache.org/jira/rest/api/2/project/" + projName;
		JSONObject json = readJsonFromUrl(url);
		JSONArray versions = json.getJSONArray("versions");

		for (i = 0; i < versions.length(); i++ ) 
		{
			String name = "";
		    String id = "";
		    if(versions.getJSONObject(i).has("releaseDate")) 
		    {
		    	if (versions.getJSONObject(i).has("name"))
		    		name = versions.getJSONObject(i).get("name").toString();
		        if (versions.getJSONObject(i).has("id"))
		            id = versions.getJSONObject(i).get("id").toString();
		        addRelease(versions.getJSONObject(i).get("releaseDate").toString(),name,id);
		    }
		}
		
		Collections.sort(releases, new Comparator<LocalDateTime>()
		{
			//@Override
			public int compare(LocalDateTime o1, LocalDateTime o2) 
			{
				return o1.compareTo(o2);
		    }
		});
		         
		if (releases.size() < 6)
			return;
		FileWriter fileWriter = null;
		try 
		{
			fileWriter = null;
		    String outname = path + projName + "VersionInfo.csv";
		    fileWriter = new FileWriter(outname);
		    fileWriter.append("Index,Version ID,Version Name,Date");
		    fileWriter.append("\n");
		    numVersions = releases.size();
		    for ( i = 0; i < releases.size(); i++) 
		    {
	        	Integer index = i + 1;
	            fileWriter.append(index.toString());
	            fileWriter.append(",");
		        fileWriter.append(releaseID.get(releases.get(i)));
		        fileWriter.append(",");
		        fileWriter.append(releaseNames.get(releases.get(i)));
		        fileWriter.append(",");
		        fileWriter.append(releases.get(i).toString());
		        System.out.println(releases.get(i).toString());
		        /*
		         * Qui prendo l'informazione relativa alla data di versione
		         */
		        fileWriter.append("\n");
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
	
	private static void addRelease(String strDate, String name, String id) 
	{
		LocalDate date = LocalDate.parse(strDate);
		LocalDateTime dateTime = date.atStartOfDay();
		if (!releases.contains(dateTime))
			releases.add(dateTime);
		releaseNames.put(dateTime, name);
		releaseID.put(dateTime, id);
		return;
	}

	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException 
	{
		InputStream is = new URL(url).openStream();
	    try 
	    {
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	        String jsonText = readAll(rd);
	        JSONObject json = new JSONObject(jsonText);
	        return json;
	    } 
	    finally 
	    {
	        is.close();
	    }
	}
	   
	private static String readAll(Reader rd) throws IOException 
	{
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) 
		{
			sb.append((char) cp);
		}
		   	return sb.toString();
	}
}