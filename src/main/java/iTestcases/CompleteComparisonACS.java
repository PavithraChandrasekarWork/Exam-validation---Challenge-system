package iTestcases;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import iTestMethods.ProjectMethods;

public class CompleteComparisonACS extends ProjectMethods{

	@BeforeClass
	public void setData() throws FileNotFoundException, IOException {
		dataSheetName = "TC_iTestData";
		testCaseName = "AdminWithChallengeSystem";
		testDescription = "To Compare the entire Question and Option text";
		category= "Smoke";
		authors	="Kamalesh";		
		//CreateExcelACScc(RollNo);// Create Excel file to write Output
	}	

	@Test(dataProvider="fetchData")
	public void completeComparisonACS(String AdminURL, String AdminUserName, String AdminPwd, String ChallengeURL, String AdminExamDate, 
			String ExamNum, String SubjectNum, String RollNo, String Pass, String ChallengeExamDate, String ExcelURL) throws InterruptedException, IOException {

		
		startPostExamChrome(AdminURL, AdminUserName, AdminPwd, AdminExamDate, ExamNum, SubjectNum, RollNo); // Opening Post Exam Report Page in Chrome Browser.
		
		startChallengeSystemFF(ChallengeURL, RollNo, Pass, ChallengeExamDate); // Opening Challenge System URL in FireFox Browser.
		
		//completeCompareACS(RollNo); //Compares Candidate Answer from Post Exam with Challenge System.

	}

}
