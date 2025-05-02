package iTestMethods;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import utils.ExcelDataProvider;

@SuppressWarnings("unused")
public class ProjectMethods extends iTestMethods2{
	
	public String browserName;
	public String dataSheetName;

	@BeforeSuite
	public void beforeSuite() throws FileNotFoundException, IOException{		
		startResult();
	}
	
	@BeforeTest
	public void beforeTest(){
	}
	
	@BeforeMethod
	public void beforeMethod(){
		test = startTestCase(testCaseName, testDescription);
		test.assignCategory(category);
		test.assignAuthor(authors);
		//startApp(browserName);
		
	}
		
	@AfterSuite
	public void afterSuite(){
		endResult();
	}

	@AfterTest
	public void afterTest(){
	
	}
	
	@AfterMethod
	public void afterMethod(){
		endTestcase();
		//cdriver.quit();
		//fdriver.quit();	
		//closeAllBrowsers();		
	}
	
	@DataProvider(name="fetchData")
	public  Object[][] getData(){
		return ExcelDataProvider.getData(dataSheetName);		
	}	
	
}
