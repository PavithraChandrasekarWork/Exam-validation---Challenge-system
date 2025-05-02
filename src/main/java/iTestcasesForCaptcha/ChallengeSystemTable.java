package iTestcasesForCaptcha;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import iTestMethodsForCaptcha.ProjectMethods;

public class ChallengeSystemTable extends ProjectMethods{

	@BeforeClass
	public void setData() throws FileNotFoundException, IOException {
		dataSheetName = "TC_iTestData";
		testCaseName = "ChallengeSystemTable";
		testDescription = "To Check the Candidate Answer and Correct answer in Challenge System and compare the numbers with table";
		category= "Smoke";
		authors	="Kamalesh";		
		CreateExcelTableCS(RollNo);// Create Excel file to write Output
	}	

	@Test(dataProvider="fetchData")
	public void adminWithChallenge(String AdminURL, String AdminUserName, String AdminPwd, String ChallengeURL, String AdminExamDate, 
			String ExamNum, String SubjectNum, String RollNo, String Pass, String ChallengeExamDate, String ExcelURL, String newATurl) throws InterruptedException, IOException {

				
		startChallengeSystemFF(ChallengeURL, RollNo, Pass, ChallengeExamDate); // Opening Challenge System URL in FireFox Browser.
				
		tableChallengeSystem(RollNo); //Compares Candidate Answer from Post Exam with Challenge System.

	}

}
