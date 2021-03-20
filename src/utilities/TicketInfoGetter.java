package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TicketInfoGetter 
{
	public static Integer[][] getInfo(String projName) throws IOException, JSONException
	{
		
	    Integer j = 0, i = 0, total = 1;
	    Integer monthcounter[][] = new Integer[6][12];
	    for(int l=0;l<6;l++) 
	    {
	    	for(int f=0;f<12;f++)
	    	{
	    		monthcounter[l][f]=0;
	    	}
	    }
	    do 
	    {
	    	j = i + 1000;
	    	String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
	    			+ projName + "%22AND(%22status%22=%22closed%22OR"
	    			+ "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
	    			+ i.toString() + "&maxResults=" + j.toString();
	    	JSONObject json = readJsonFromUrl(url);
	    	JSONArray issues = json.getJSONArray("issues");
	    	total = json.getInt("total");
	    	for (; i < total && i < j; i++) 
	    	{
	    		String key = issues.getJSONObject(i%1000).get("key").toString();
	    		String resDate = issues.getJSONObject(i%1000).getJSONObject("fields").getString("resolutiondate");
	    		String date = resDate.substring(0,16);
	    		LocalDateTime dateTime = LocalDateTime.parse(date);
	    		monthcounter[dateTime.getYear()-2013][dateTime.getMonthValue()-1] ++;
	    		//System.out.println(key + "   " + dateTime.getYear());
	    		/*
	    		 * QUI OTTENGO L'INFO
	    		 */
	    	}  
	    } 
	    while (i < total);
	    //Stampo la matrice dei ticket per mese
	    System.out.println("\t\t\t\t\t   MATRICE DEI FIXED ISSUES AL MESE");
	    System.out.println("\t\tGen\tFeb\tMar\tApr\tMag\tGiu\tLug\tAgo\tSet\tOtt\tNov\tDic");
	    int k=0;
	    for(int a=0;a<6;a++) 
	    {
	    	System.out.print("ANNO "+(k+2013)+"\t");
	    	for(int b=0;b<12;b++)
	    	{
	    		System.out.print(monthcounter[a][b]+"\t");
	    	}
	    	System.out.println("");
	    	k++;
	    }
	    
	    return monthcounter;
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

	private static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException 
	{
		InputStream is = new URL(url).openStream();
		try 
		{
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} 
		finally 
		{
			is.close();
		}
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
}