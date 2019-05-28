package com.localidata.interceptor;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class TaskMethod {

	private static Logger log = Logger.getLogger(TaskMethod.class);
	private TaskConfig config;
	private ArrayList<ResponseQuery> responseList;
	private int petitionsOK;
	private int timelimit;
	private int petitionsBeforeReport;
	private SendMailSSL emailSender;

	public TaskConfig getConfig() {
		return config;
	}

	public void setConfig(TaskConfig config) {
		this.config = config;
	}

	public ArrayList<ResponseQuery> getResponseList() {
		return responseList;
	}

	public void setResponseList(ArrayList<ResponseQuery> responseList) {
		this.responseList = responseList;
	}

	public int getPetitionsOK() {
		return petitionsOK;
	}

	public void setPetitionsOK(int petitionsOK) {
		this.petitionsOK = petitionsOK;
	}

	public int getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(int timelimit) {
		this.timelimit = timelimit;
	}

	public int getPetitionsBeforeReport() {
		return petitionsBeforeReport;
	}

	public void setPetitionsBeforeReport(int petitionsBeforeReport) {
		this.petitionsBeforeReport = petitionsBeforeReport;
	}

	public SendMailSSL getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(SendMailSSL emailSender) {
		this.emailSender = emailSender;
	}

	public void testQueries() {

		for (Query q : config.getQueryList()) {
			if (q.getActive() == 1) {
				long startTime = System.currentTimeMillis();
				log.info("Throw " + q.getName());
				int size = 0;
				if (q.getType() == 1)
					size = SPARQLQuery.Query(q.getQuery(), config.getEndpoint());
				else
					size = SPARQLQuery.URIQuery(config.getEndpoint() + "?" + q.getQuery());
				long endTime = System.currentTimeMillis();
				double duration = (endTime - startTime);
				duration = duration / 1000.0;
				responseList.add(new ResponseQuery(duration, size, q.getName()));
			}
		}

		ArrayList<ResponseQuery> errors = new ArrayList<ResponseQuery>();
		for (ResponseQuery rq : responseList) {
			if ((rq.getSize() <= 0) || rq.getTime() > timelimit) {
				errors.add(rq);
			}
		}

		if (errors.size() > 0) {
			petitionsOK = 0;
			String bodyMail = "There are errors in the next Query/ies" + System.getProperty("line.separator");
			bodyMail += System.getProperty("line.separator");
			bodyMail += Utils.responseArrayToString(errors);
			// log.info(bodyMail);
			emailSender.send(config.getUsuario(), config.getPassword(), config.getDestinos(), "Some of the queries do not work", bodyMail);
		} else {
			petitionsOK++;
			if (petitionsOK == petitionsBeforeReport + 1) {
				petitionsOK = 0;
				String bodyMail = "There aren't errors after " + petitionsBeforeReport + " times" + System.getProperty("line.separator");
				bodyMail += System.getProperty("line.separator");
				bodyMail += "The last test are theses: " + System.getProperty("line.separator");
				bodyMail += System.getProperty("line.separator");
				bodyMail += Utils.responseArrayToString(responseList);
				// log.info(bodyMail);
				emailSender.send(config.getUsuario(), config.getPassword(), config.getDestinos(), "The queries work fine", bodyMail);
			}

			log.info(Utils.responseArrayToString(responseList));

			log.info(petitionsOK + " tests passed");
		}

		errors.clear();
		responseList.clear();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
