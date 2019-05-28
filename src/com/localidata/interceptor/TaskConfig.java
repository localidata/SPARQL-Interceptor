package com.localidata.interceptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TaskConfig {

	private static Logger log = Logger.getLogger(TaskConfig.class);

	private int time;
	private ArrayList<Query> queryList;

	private String usuario;
	private String password;
	private String destinos;

	private String endpoint;
	private String timelimit;
	private String timeout;

	
	private String petitionsBeforeReport;
	
	
	
	
	public String getPetitionsBeforeReport() {
		return petitionsBeforeReport;
	}

	public void setPetitionsBeforeReport(String petitionsBeforeReport) {
		this.petitionsBeforeReport = petitionsBeforeReport;
	}

	public String getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(String timelimit) {
		this.timelimit = timelimit;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public ArrayList<Query> getQueryList() {
		return queryList;
	}

	public void setQueryList(ArrayList<Query> queryList) {
		this.queryList = queryList;
	}



	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDestinos() {
		return destinos;
	}

	public void setDestinos(String destinos) {
		this.destinos = destinos;
	}

	public TaskConfig(ServletContextEvent evt) {

		try {
				Properties p = null;
				JSONObject o = null;
			if (evt==null){
				p = Utils.leerProperties("conf.properties");
				o = Utils.leerJSONFile("queries.json");
			}else{			
				 p = Utils.leerProperties(evt.getServletContext().getRealPath("/") + "/WEB-INF/conf.properties");
				 o = Utils.leerJSONFile(evt.getServletContext().getRealPath("/") + "/WEB-INF/queries.json");
			}

			time = Integer.parseInt(p.getProperty("time"));

			usuario = p.getProperty("user");
			password = p.getProperty("password");
			destinos = p.getProperty("emails");
			endpoint = p.getProperty("endpoint");			
			timelimit=p.getProperty("timelimit");
			timeout=p.getProperty("timeout");
			
			petitionsBeforeReport=p.getProperty("petitionsBeforeReport");
			

			JSONArray queries = (JSONArray) o.get("queries");

			queryList = new ArrayList<Query>();

			for (int i = 0; i < queries.size(); i++) {
				JSONObject queryTemp = (JSONObject) queries.get(i);
				Query q = new Query();
				if (queryTemp != null) {

					if (queryTemp.get("query") != null) {
						q.setQuery((String) queryTemp.get("query"));
					}

					if (queryTemp.get("name") != null) {
						q.setName((String) queryTemp.get("name"));
					}
					
					if (queryTemp.get("type") != null) {
						q.setType(((Long) queryTemp.get("type")).longValue());
					}
					
					if (queryTemp.get("active") != null) {
						q.setActive(((Long) queryTemp.get("active")).longValue());
					}


					queryList.add(q);
				}
			}

			log.info("Config Loaded");

		} catch (Exception e) {
			log.error("Error loading configuration", e);
		}
	}

}
