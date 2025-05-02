package testcases;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wdMethods.ProjectMethods;

public class ChallengeSystemWithMaster extends ProjectMethods{
	
	@BeforeClass
	public void setData() {
		dataSheetName = "TC_iTestData";
		testCaseName = "ChallengeSystemWithMaster";
		testDescription = "To Check the Question from Challenge System with Master QP";
		category= "Smoke";
		authors	="Kamalesh";		
	}

	

	@Test(dataProvider="fetchData")
	public void challengeSystemWithMaster(String AdminURL, String AdminUserName, String AdminPwd, String ChallengeURL, String AdminExamDate, 
			String ExamNum, String SubjectNum, String RollNo, String Pass, String ChallengeExamDate, String ExcelURL) throws InterruptedException, IOException {

		
		//startPostExamChrome(AdminURL, AdminUserName, AdminPwd, AdminExamDate, ExamNum, SubjectNum, RollNo); // Opening Post Exam Report Page in Chrome Browser
		
		cdriver.close();
		
		//startChallengeSystemChrome(ChallengeURL, RollNo, Pass, ChallengeExamDate); // Opening Challenge System URL in Chrome Browser.
		
		//openMasterQPFF(ExcelURL, SubjectNum);  //Opening Master QP in Firefox Browser.		
		
		//getAvailableQuestionsCS(); //Get No. of Available Questions in Challenge System.
		
		//createNotepadCSM(RollNo); // Create Notepad file to write Output
		
		//checkNumberOfOptions();// Checks the number of option as four or five.

		//compareCorrectAnswers(); // Compares Correct Answer from Challenge System with Master QP.		
		
	}
}
