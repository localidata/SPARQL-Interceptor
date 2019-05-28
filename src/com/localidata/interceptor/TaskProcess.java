package com.localidata.interceptor;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class TaskProcess {
	
	private static Logger log = Logger.getLogger(TaskProcess.class);
	
	private static Timer timer;
	private static TimerTask timerTask;	
	private static int timelimit=30;	
	private TaskConfig config;	
	private ArrayList<ResponseQuery> responseList;	
	private SendMailSSL emailSender = new SendMailSSL();
	private int petitionsOK=0;
	private int petitionsBeforeReport=100;
	
	private TaskMethod method;

	public TaskProcess()
	{
			responseList=new ArrayList <ResponseQuery>();
			config=new TaskConfig(null);
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
			
	}
	
	public void init()
	{			
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
	


	
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");		
		TaskProcess t=new TaskProcess();
		t.init();
	}

}
