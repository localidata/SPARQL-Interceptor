package tests;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.localidata.interceptor.Utils;
import com.localidata.queries.ListOfQueries;
import com.localidata.queries.QueryBean;



@RunWith(Parameterized.class)
public class GenericTest {

	private QueryBean bean;

	@Before
	public void method() {
		// set up method	
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		// load the files as you want from the directory
		// System.out.println("Parameters");
		Utils.logConfLoad("log4j.properties");

		ListOfQueries list = new ListOfQueries();
		
		return Utils.generateData(list);
	}

	

	public GenericTest(QueryBean input) {
		// constructor
		this.bean = input;
	}

	@Test
	public void test() {
		// test
		Utils.realTest(bean);
	}	

	@After
	public void tearDown() {

	}
}