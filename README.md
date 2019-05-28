SPARQL Interceptor
=================

This is a simple Web-based tool where you can test and monitor SPARQL queries over a set of SPARQL endpoints. The tool executes a set of queries periodically over a SPARQL endpoint. All these parameters (queries, SPARQL endpoint, periodicity of evaluations) can be configured, as explained below.

If any query fails (that is, it provides a timeout or zero results), it will send an e-mail, using a Google account to the e-mail account that is also specified in the configuration file. This may be improved in the feature to look for specific results.

When the whole process of testing queries is OK for all queries, a counter increases. When the counter is equal to a value (that can be also configured) the tool sends an e-mail with a report of the last execution time.

The tool is developed in Java, as a Java Application and as a Web Application (without a graphic enviroment).

The queries.json file is a deprecated version of the unit test file, as it was initially used when Jenkins was not used for this purpose. It is maintained in the repository for provenance reasons.


#JUnit Test of SPARQL queries

We added a new funcionality, with junit tests (mvn test) you can execute queries and test the number of rows that the query return match with a specific number.

If you want to add a query, you must create a file inside the "queries" directory with this format:


\# Description: A description of the query  
\# Result: number of results  
Query line 1  
Query line 2  
....  
Query line n  


## Example

```
# Description: Venues with no cell associated to them. It should be zero.
# Result: 0
SELECT ?venue (COUNT(DISTINCT ?cell) AS ?numberCells)
WHERE
{
  ?venue a dul:Place .
  ?venue locationOnt:cell ?cell

} GROUP BY ?venue HAVING (COUNT(DISTINCT ?cell) = 0)
```

## Be carefull with Jena
Jena execute these queries, you must test your queries in Jena first, maybe you put a 0 but Jena return -1, or the query is ok in Virtuoso but fails here.


# Deploy on Jenkins

Here I describe the steps to desploy this project without use any source control.

Usually the Jenkins home is:  /var/lib/jenkins 

When you are on Jenkins, you must add a new Item:

	- Add the name of the project
	- Select Maven project and click the "ok" button
	
Then you must do this steps:

    - Build the Project at least once, (it will fail), but Jenkins will create the structure jenkins/workspace/PROJECTNAME/
    - Copy the project files to jenkins/workspace/PROJECTNAME/
    - Build again and configure appropriately
    
Back to the Jenkins project configuration:

	In Build Triggers:
	
		- Check Build periodically and write in the Schedule a cron expression to configure it. For example: "H/30 * * * *"
	
	In Build:
	
		- Root POM: must be "pom.xml"
		- Goals and options: I write "clean package test"

	And that´s enought.
	
	With this configuration, Jenkins will run the test every 30 minutes.


#Configuration

This tool has three configuration files:

##log4j.properties

The log configuration, you can see this [tutorial](http://www.tutorialspoint.com/log4j/log4j_configuration.htm) to learn how to configure it.

##conf.properties

This file has the following attributes to configure:

####time

The periodicity of every query execution cycle.

Example: 3600000

####emails

List of email address of the people that you want to send an e-mail for errors or reports.

Example: account1@gmail.com, account2@hotmail.com, account3@yahoo.com

####user

The Google account used to send e-mails.

Example: account@gmail.com

####password

The Google account password.

Example: PASSWORD

####endpoint

The SPARQL Endpoint where the queries will be executed.

Example: http://dev.3cixty.com/sparql

####timeout

The max-time for a  timeout error, expressed in milliseconds.

Example: 60000

####timelimit

The max-time for an alert via e-mail, in seconds.

Example: 30

####petitionsBeforeReport

The number of OK petitions before sending a report with the last execution time.

Example: 24

##queries.json

Here you configure all the queries that you want to test. The file is an array that contains all the queries.

Each query has four attributes:

####type

The type can be 1 or 2.

The type 1 uses "the JENA parser" and only works when the query is standard. If you use fucntions like "bif:intersect” from Virtuoso you cannot use this type.

The type 2 uses the URL to send the query. You must begin with que "query" parameter, and the output format must be "json".

####active

Active can be 0 or 1. The query is only executed if active is set to 1.

####name

The name of the query

####query

That is the query.

If the type is 1 you must write the query as follows:

    select distinct ?Concept where {[] a ?Concept} LIMIT 100

If the type is 2 you must write it as follows:

    query=select+distinct+%3FConcept+where+{[]+a+%3FConcept}+LIMIT+100&format=json&timeout=0&debug=on

Be carefull with the double quote, here it must be a single quote.

#Running


##JAR

You can run the jar with this command:

    java -jar SPARQLInterceptor.jar

You can stop pressing CONTROL + C.

If you are on LINUX you can add the " &" at the end of the command to run it in background:

    java -jar SPARQLInterceptor.jar &

You can stop it with a kill command:

    kill -9 pid

But you need to know the PID of the process. With this command you can obtain the pids of all Java processes

    jps

##WAR

You can run SPARQL Interceptor deploying the war on servlet container.

Where the webapp is deployed you can edit the configuration files inside the WEB-INF folder.
