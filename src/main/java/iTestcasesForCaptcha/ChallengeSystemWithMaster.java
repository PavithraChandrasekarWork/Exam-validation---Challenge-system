package iTestcasesForCaptcha;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import iTestMethodsForCaptcha.ProjectMethods;

public class ChallengeSystemWithMaster extends ProjectMethods{
	
	@BeforeClass
	public void setData() throws FileNotFoundException, IOException {
		dataSheetName = "TC_iTestData";
		testCaseName = "ChallengeSystemWithMaster";
		testDescription = "To Check the Question from Challenge System with Master QP";
		category= "Smoke";
		authors	="Kamalesh";
		CreateExcelCSM(RollNo); // Create Excel file to write Output
	}	


	@Test(dataProvider="fetchData")
	public void challengeSystemWithMaster(String AdminURL, String AdminUserName, String AdminPwd, String ChallengeURL, String AdminExamDate, 
			String ExamNum, String SubjectNum, String RollNo, String Pass, String ChallengeExamDate,String ExcelURL, String newATurl) throws InterruptedException, IOException {

		
				
		openMasterQPFF(ExcelURL, SubjectNum);  //Opening Master QP in Firefox Browser.
				
		startChallengeSystemChrome(ChallengeURL, RollNo, Pass, ChallengeExamDate, SubjectNum); // Opening Challenge System URL in Chrome Browser.
						
		checkNumberOfOptions();// Checks the number of option as four or five.

	    compareCorrectAnswers(RollNo, SubjectNum); // Compares Correct Answer from Challenge System with Master QP.	
		
		compareCSMQuestions();
		
		compareCSMOptions();
		
	}
}
