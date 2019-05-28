package com.localidata.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class Task implements ServletContextListener {

	private static Logger log = Logger.getLogger(Task.class);
	
	private static Timer timer;
	private static TimerTask timerTask;	
	private static int timelimit=30;
	
	private TaskConfig config;
	
	private ArrayList<ResponseQuery> responseList;
	
	private SendMailSSL emailSender = new SendMailSSL();

	private int petitionsOK=0;
	private int petitionsBeforeReport=100;
	
	private TaskMethod method;


	private void initLogs(ServletContextEvent evt) {

		String log4jLocation = evt.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "log4j.properties";

		ServletContext sc = evt.getServletContext();

		File ficheroLogProperties = new File(log4jLocation);
		if (ficheroLogProperties.exists()) {
			System.out.println("Initializing log4j with: " + log4jLocation);
			PropertyConfigurator.configure(log4jLocation);
			
			responseList=new ArrayList <ResponseQuery>(); 
			
		} else {
			System.err.println("*** " + log4jLocation + " file not found, so initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		}

	}

	

	public void contextInitialized(final ServletContextEvent evt) {

		initLogs(evt);
		config=new TaskConfig(evt);
		SPARQLQuery.timeOut=Integer.parseInt(config.getTimeout());
		timelimit=Integer.parseInt(config.getTimelimit().trim());
		petitionsBeforeReport=Integer.parseInt(config.getPetitionsBeforeReport().trim());
		
		method=new TaskMethod();
		method.setConfig(config);
		method.setEmailSender(emailSender);
		method.setPetitionsBeforeReport(petitionsBeforeReport);
		method.setPetitionsOK(petitionsOK);
		method.setResponseList(responseList);
		method.setTimelimit(timelimit);
		
		timerTask = new TimerTask() {
			public void run() {
				log.info("Init process");
					method.testQueries();
				log.info("End proccess");
			}
		};

		
		
		timer = new Timer();
		// timer.schedule(this, 0, 10*60*1000); //  10 minutes
		timer.schedule(timerTask, 0, config.getTime());

	}

	

	public void contextDestroyed(ServletContextEvent evt) {
		timer.cancel();
	}

	

	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		log.info("Ready to rock");
		
	
		
		
	}
	



}