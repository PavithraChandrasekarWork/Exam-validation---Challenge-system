package iTestcases;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import iTestMethods.ProjectMethods;

public class BrokenLinkCheck extends ProjectMethods{

	@BeforeClass
	public void setData() throws FileNotFoundException, IOException {
		dataSheetName = "TC_iTestData";
		testCaseName = "ImageCheckAdminAndChallengeSystem";
		testDescription = "To Check the Images in Admin and Challenge System Pages";
		category= "Smoke";
		authors	="Kamalesh";
		CreateFinalExcel();
		
	}


	@Test(dataProvider="fetchData")
	public void adminLogin(String AdminURL, String AdminUserName, String AdminPwd, String ChallengeURL, String AdminExamDate, 
			String ExamNum, String SubjectNum, String RollNo, String Pass, String ChallengeExamDate, String ExcelURL) throws InterruptedException, IOException {



		startPostExamChrome(AdminURL, AdminUserName, AdminPwd, AdminExamDate, ExamNum, SubjectNum, RollNo); // Opening Post Exam Report Page in Chrome Browser

		createNotepadBLPE(RollNo); // Create Notepad file to write Output.		

		chkBrokenLinkPE(); // Check for the Broken Links inPost Exam Page. 

		startChallengeSystemChrome(ChallengeURL, RollNo, Pass, ChallengeExamDate); // Opening Challenge System URL in Chrome Browser.		

		createNotepadBLCS(RollNo); // Create Notepad file to write Output.
		
		chkBrokenLinkCS(); //Checks for Broken Links.		

		openMasterQPFF(ExcelURL, SubjectNum);  //Opening Master QP in Firefox Browser.			
		
		WriteInFinalExcel(ChallengeExamDate, SubjectNum, RollNo, Pass, StringAvailableQuestionMasterQP, StringAvailableQuestionsCS, StringAvailableQuestionsPE, "status");

	}
	
}
