package iTestcasesForCaptcha;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import iTestMethodsForCaptcha.ProjectMethods;

public class SIDownload extends ProjectMethods{

	@BeforeClass
	public void setData() throws FileNotFoundException, IOException {
		dataSheetName = "TC_iTestData_2";
		testCaseName = "AdminWithChallengeSystem";
		testDescription = "To Check the Candidate Answer from Admin with Challenge System";
		category= "Smoke";
		authors	="Kamalesh";		
		//CreateExcelACS(RollNo);// Create Excel file to write Output		
	}
	

	@Test(dataProvider="fetchData")
	public void adminWithChallenge(int SNO, String UserName, String Result) throws InterruptedException, IOException {

		
		
		
		
		
						
		
	}
 
}
