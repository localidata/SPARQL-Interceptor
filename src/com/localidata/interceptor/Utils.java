package com.localidata.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.localidata.queries.ListOfQueries;
import com.localidata.queries.QueryBean;



public class Utils {
	
	private static Logger log = Logger.getLogger(Utils.class);

	public static Properties leerProperties(String ruta) throws FileNotFoundException, IOException {
		Properties propiedades = new Properties();
		propiedades.load(new FileInputStream(ruta));
		return propiedades;
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject leerJSONFile(String file) throws IOException, ParseException {
		FileInputStream fis = new FileInputStream(file);

		BufferedReader rd = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String jsonText = readAll(rd);

		JSONParser parser = new JSONParser();
		JSONObject obj = null;

		obj = (JSONObject) parser.parse(jsonText);

		return obj;

	}
	
	public static String procesaURL(String urlCadena, int timeout) throws IOException,MalformedURLException  {		
		
		URL url = new URL(urlCadena);	      

    HttpURLConnection huc = (HttpURLConnection) url.openConnection();
    HttpURLConnection.setFollowRedirects(false);
    huc.setConnectTimeout(timeout);
    huc.setRequestMethod("GET");
    huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
    huc.connect();
    InputStream input = huc.getInputStream();
    
    
		
		StringBuffer contenidoURL=new StringBuffer("");		
			      
	      
	      BufferedReader in = null;
	      //Volcamos lo recibido al buffer
	      in = new BufferedReader(new InputStreamReader(huc.getInputStream())); 
	      // Transformamos el contenido del buffer a texto
	      String inputLine;		      	      
	      if (in!=null){	    	  
	    	  
	    		  while ((inputLine = in.readLine()) != null)
			      {	  
	    			  contenidoURL.append(inputLine+"\n");
			      }	
	    	  		    
		      in.close();	
	      }
		return contenidoURL.toString();
	}
	
	public static String responseArrayToString(ArrayList<ResponseQuery> errors) {
		String body="";
		for (ResponseQuery rq:errors)
		{
			body+= rq.getName()+":"+System.getProperty("line.separator");
			body+= "\tNumber of results: "+rq.getSize()+System.getProperty("line.separator");
			body+= "\tExecution time: "+rq.getTime()+ " seconds"+System.getProperty("line.separator");
			body+= System.getProperty("line.separator");
		}
		return body;
	}
	
	
	public static String getQueryFromGitHub(String url) {

		StringBuffer sb = new StringBuffer();
		HttpURLConnection httpConnection =null;
		try {
			URL targetUrl = new URL(url);
			
			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");

			
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));

			String output;

			while ((output = responseBuffer.readLine()) != null) {
				sb.append(output + System.getProperty("line.separator"));				
			}

			
			

		} catch (MalformedURLException e) {
			log.error("Error with the URI: " + url );
			log.error(e);

		} catch (IOException e) {
			log.error("IOError: " + url);
			log.error(e);
		}finally{
			httpConnection.disconnect();
		}

		return sb.toString();

	}

	
	public static boolean logConfLoad(String path) {
		File logProperties = new File(path);
		if (!logProperties.exists()) {
			System.out.println("log4j.properties not found. Please generate a log4j.properties.");
			return false;
		} else {
			PropertyConfigurator.configure(path);
			return true;
		}
	}
	
	public static String readFile(String file) throws IOException {
		FileInputStream fis = new FileInputStream(file);

		BufferedReader rd = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		String jsonText = readAll(rd);

		return jsonText;

	}
	
	public static void realTest(QueryBean bean) {
		if (bean.getCompareType().equals(QueryBean.EQUAL)) {
			if (bean.getExpectedResult() != bean.getResultSize()) {
				writeData(bean);
			}
			assertEquals(bean.getExpectedResult(), bean.getResultSize());
		}else if (bean.getCompareType().equals(QueryBean.GREATER)) {
			if (bean.getResultSize() <= bean.getExpectedResult()) {
				writeData(bean);
			}
			assertTrue( bean.getResultSize() > bean.getExpectedResult());
		}else if (bean.getCompareType().equals(QueryBean.LESS)) {
			if (bean.getResultSize() >= bean.getExpectedResult()) {
				writeData(bean);
			}
			assertTrue( bean.getResultSize() < bean.getExpectedResult());
		}
	}
	
	public static void writeData(QueryBean bean) {
		System.out.println(bean.getDescription());
		System.out.println("");
		System.out.println("Http Query:");
		System.out.println("");
		System.out.println(bean.getHttpQuery());
		System.out.println("");
		System.out.println("Query:");
		System.out.println("");
		System.out.println(bean.getQuery());
	}
	
	public static Collection<Object[]> generateData(ListOfQueries list) {
		Collection<Object[]> data = new ArrayList<Object[]>();
		for (QueryBean bean : list.getQueryList()) {
			Object[] arg1 = new Object[] { bean };
			data.add(arg1);
		}
		return data;
	}
	
	public static boolean checkURL(String url) {
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				return false;
			}			
		} catch (Exception e) {
			//log.error("Error testing URL",e);
			return false;
		}
		return true;		
}
	
	public static String watch(String url) {	
	
	StopWatch watch = new StopWatch();
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	String r=dateFormat.format(new Date());
	
	boolean ok=false;

	try {
		watch.start();
		ok=checkURL(url);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		watch.stop();
	}

	if (ok)
		r+=","+watch.getTime();
	else
		r+=",0";		
	
	return r;
	}
	
	
	public static void main(String[] args) {
		String json="http://jsonpdataproxy.appspot.com/?url=http://sandbox-ckan.localidata.com/dataset/e5b37d9a-f551-4375-872e-a9fad5be6c8b/resource/dcae2eab-7ea5-4966-850b-7695979e7c69/download/recurso.csv";		
		System.out.println(watch(json));
	}

}
