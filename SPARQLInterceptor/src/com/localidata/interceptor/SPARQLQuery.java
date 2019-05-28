package com.localidata.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;










import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class SPARQLQuery {

	private static Logger log = Logger.getLogger(SPARQLQuery.class);
	
	
	public static int timeOut = 60000;
	
	public static int Query(String queryString, String sparqlEndpoint) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int rowNumber=-1;
		try
		{
		Query query = QueryFactory.create(queryString);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		
		//QueryEngineHTTP qExe = new QueryEngineHTTP(sparqlEndpoint,query);
		
		//QueryEngineHTTP qExe = QueryExecutionFactory.createServiceRequest(sparqlEndpoint, query);
		
		
		qExe.setTimeout(timeOut);		
		ResultSet results = qExe.execSelect();		
		ResultSetFormatter.out(baos,results, query);
		rowNumber= results.getRowNumber();
		//log.info(baos.toString());
		}
		catch (Exception e)
		{				
			log.error("Query: "+System.getProperty("line.separator")+queryString);
			log.error(e);
		}
		return rowNumber;
		
	}
	
	public static int URIQuery(String uri){
		int rowNumber=-1;
		try {
			String response=Utils.procesaURL(uri,timeOut);		
			//log.info(response);
			JSONParser parser = new JSONParser();
			JSONObject obj = null;
			obj = (JSONObject) parser.parse(response);
			
			if (obj.get("results")!=null)
			{
				JSONObject results=(JSONObject) obj.get("results");
				if (results.get("bindings")!=null)
				{
				JSONArray bindings=(JSONArray) results.get("bindings");
				rowNumber=bindings.size();
				}
			}
			
			
			
		} catch (Exception e) {
			log.error("Error reading URL",e);			
		}
		return rowNumber;
	}
		
	
	
	public static void main(String[] args) {
		
	}
	
}
