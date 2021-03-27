package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TicketInfoGetter 
{
	private static Logger logger =  Logger.getLogger(TicketInfoGetter.class.getName());
	private TicketInfoGetter() 
	{
	    throw new IllegalStateException("Utility class");
	}
	public static Integer[][] getInfo(String path, String projName, Boolean checkDifference)
	{		
	    Integer j = 0; 
	    Integer i = 0;
	    Integer total = 1;
	    Integer r;
	    Integer c;
	    Integer[][] ticketPerMonthCounter = new Integer[6][12];
	    GitHandler dateModifier = new GitHandler();
	    Integer[] dateVect= {0,0};
	    
	    //Inizializzo i  contatori dei ticket nella matrice, tutti a 0.
	    for(r=0;r<6;r++) 
	    {
	    	for(c=0;c<12;c++)
	    	{
	    		ticketPerMonthCounter[r][c]=0;
	    	}
	    }
	    do 
	    {
	    	j = i + 1000;
	    	String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
	    			+ projName + "%22AND(%22status%22=%22closed%22OR"
	    			+ "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
	    			+ i.toString() + "&maxResults=" + j.toString();
	    	try
	    	{
	    		JSONObject json = readJsonFromUrl(url);
	    		JSONArray issues = json.getJSONArray("issues");
	    		total = json.getInt("total");
	    		for (; i < total && i < j; i++) 
	    		{
					//Preleva dal JSON la resolution date del ticket, trasformala in formato data
					//per poi sommare uno al contatore dei ticket risolti in quel mese
	    			String resDate;
	    			String key;
					resDate = issues.getJSONObject(i%1000).getJSONObject("fields")
							.getString("resolutiondate").substring(0,16);
					key = issues.getJSONObject(i%1000).get("key").toString();
					LocalDateTime dateTime = LocalDateTime.parse(resDate);
	    			dateVect[0]=dateTime.getMonthValue();
	    			dateVect[1]=dateTime.getYear();
	    			//Il metodo che chiamo qui serve a controllare difformità tra JYRA e i commit.
	    			//In caso la data di risoluzione di Gyra differisce da quella riportata nei
	    			//commit, viene scelta la più recente.
	    			if(Boolean.TRUE.equals(checkDifference))
	    				dateVect = dateModifier.checkDateValidity(path,dateVect,key);
					ticketPerMonthCounter[dateVect[1]-2013][dateVect[0]-1] ++;
	    		}
	    	} 
	    	catch (JSONException | IOException e) 
	    	{
	    		logger.log(Level.INFO,"An error has occurred during JSON document analysis");
			} 
	    } 
	    while (i < total);
	    logger.log(Level.INFO,"Ticket counted correctly. Check in folder 'csv' to see the result.");
	    return ticketPerMonthCounter;
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

	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException 
	{
		try (InputStream is = new URL(url).openStream();)
		{
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
			String jsonText = readAll(rd);
			return new JSONObject(jsonText);
		} 
	}
}