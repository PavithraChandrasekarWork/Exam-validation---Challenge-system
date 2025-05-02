package iTestcases;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import iTestMethods.ProjectMethods;

public class CompleteComparisonCSM extends ProjectMethods{

	@BeforeClass
	public void setData() throws FileNotFoundException, IOException {
		dataSheetName = "TC_iTestData";
		testCaseName = "ChallengeSystemWithMasterQP";
		testDescription = "To Compare the entire Question and Option text";
		category= "Smoke";
		authors	="Kamalesh";		
		//CreateExcelCSMcc(RollNo);// Create Excel file to write Output
	}	

	@Test(dataProvider="fetchData")
	public void completeComparisonCSM(String AdminURL, String AdminUserName, String AdminPwd, String ChallengeURL, String AdminExamDate, 
			String ExamNum, String SubjectNum, String RollNo, String Pass, String ChallengeExamDate, String ExcelURL) throws InterruptedException, IOException {
		
		
		startPostExamChrome(AdminURL, AdminUserName, AdminPwd, AdminExamDate, ExamNum, SubjectNum, RollNo); // Opening Post Exam Report Page in Chrome Browser.
		
		cdriver.quit(); @SuppressWarnings("unused")
		ChromeDriver cdriver;
				
		startChallengeSystemFF(ChallengeURL, RollNo, Pass, ChallengeExamDate); // Opening Challenge System URL in FireFox Browser.		
		
		//openMasterQPChrome(ExcelURL, SubjectNum);  //Opening Master QP in Chrome Browser.		
		
		//completeCompareCSM(RollNo); //Compares Complete Text from Challenge System with Master QP.

	}

}
