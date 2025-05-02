package testcases;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wdMethods.ProjectMethods;

public class AdminWithChallengeSystem extends ProjectMethods{

	@BeforeClass
	public void setData() {
		dataSheetName = "TC_iTestData";
		testCaseName = "AdminWithChallengeSystem";
		testDescription = "To Check the Question from Admin with Challenge System";
		category= "Smoke";
		authors	="Kamalesh";
	}	

	@Test(dataProvider="fetchData")
	public void adminWithChallenge(String AdminURL, String AdminUserName, String AdminPwd, String ChallengeURL, String AdminExamDate, 
			String ExamNum, String SubjectNum, String RollNo, String Pass, String ChallengeExamDate, String ExcelURL) throws InterruptedException, IOException {

		
		//startPostExamChrome(AdminURL, AdminUserName, AdminPwd, AdminExamDate, ExamNum, SubjectNum, RollNo); // Opening Post Exam Report Page in Chrome Browser.
		
		//startChallengeSystemFF(ChallengeURL, RollNo, Pass, ChallengeExamDate); // Opening Challenge System URL in FireFox Browser.

		//getQuestionsViewedPE(); ////Get No. of Questions viewed in Post Exam Report.
		
		//getFirstQIDinCS();	// get the first QID in Challenge URL.		
		
		//createNotepadACS(RollNo); // Create Notepad file to write Output.

		//compareCandidateAnswers(); //Compares Candidate Answer from Post Exam with Challenge System.

	}

}
