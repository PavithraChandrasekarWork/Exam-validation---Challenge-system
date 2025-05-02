package iTestMethodsForCaptcha;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Reporter;

@SuppressWarnings("unused")
public class iTestMethods3 extends Reporter{


	public RemoteWebDriver driver;
	public ChromeDriver cdriver;
	public FirefoxDriver fdriver;
	public WebDriver pdriver;
	public boolean optionFive = false, ImageCheck;
	public List<WebElement> LinkList=null;
	public List<WebElement> ActiveLinks=null;
	public int start, AvailableQuestionMasterQP, QIDint, candidateAnsStartAT;
	public boolean fdrivermultiple = false;
	public boolean cdrivermultiple = false;
	public boolean mixture, qimage;
	public String StringAvailableQuestionMasterQP, StringAvailableQuestionsCS, StringAvailableQuestionsPE;
	public String[] optionText = new String[5];
	public String[] CorCandAT = new String[2];
	public String[] CorCandCS = new String[2];
	public String ImgName, FullURL, FullTextAT, FullTextMQP, QID="", CanAnsCS, CorAnsCS, QIDCS, QIDMaster, ccCorOptCS, ccCorOptMQP;


	public String ccQID, ccQuestionTextAT, ccQuestionTextCS, ccQuestionTextMQP, ccOption1AT, ccOption1CS, ccOption1MQP, ccOption2AT, ccOption2CS, ccOption2MQP, ccOption3AT, 
	ccOption3CS, ccOption3MQP, ccOption4AT, ccOption4CS, ccOption4MQP, ccCanAnsAT, ccCanAnsCS, ccCorAnsAT, ccCorAnsCS, ccStatus;

	public List<WebElement> AvailableQuestionsPE, AvailableQuestionsCS, AvailableQuestionsMQP;
	public String RollNo = null, SubjectLink = null;
	public int QNOint;
	public BufferedWriter bw = null;

	//public String sUrl,primaryWindowHandle,sHubUrl,sHubPort;

	public  XSSFWorkbook workbook;
	public XSSFSheet sheet;	
	public int rowCount = 0;


	//---------------------------------------------------------------------------------------------------------------------------------------------------


	public String startPostExamChrome(String AdminURL, String AdminUserName, String AdminPwd, String AdminExamDate, String ExamNum, String SubjectNum, String RollNo) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		cdriver = new ChromeDriver();	
		cdriver.manage().window().maximize();
		cdriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		cdriver.get(AdminURL);

		cdriver.findElementByName("memno").sendKeys(AdminUserName);
		cdriver.findElementByName("candpassword").sendKeys(AdminPwd);
		cdriver.findElementByName("go").click();
		Thread.sleep(2000);
		cdriver.switchTo().alert().accept();
		Select date = new Select(cdriver.findElementById("db"));
		date.selectByValue(AdminExamDate);
		cdriver.findElementByLinkText("Candidate Response Summary").click();
		Select Exam = new Select(cdriver.findElementByName("exam_code"));
		Exam.selectByValue(ExamNum);
		Select Subject = new Select(cdriver.findElementByName("subject_code"));
		Subject.selectByValue(SubjectNum);
		cdriver.findElementById("memno").sendKeys(RollNo);
		cdriver.findElementByXPath("//button[text()='Submit']").click();

		getQuestionsViewedPE();

		SubjectLink = cdriver.findElementByXPath("(//td[@class='audit-report-td-2'])[5]").getText();
		SubjectLink = new StringBuffer(SubjectLink).replace(5, SubjectLink.length(), "").toString();
		return SubjectLink;	
	}


	public void startChallengeSystemFF(String ChallengeURL, String RollNo, String Pass, String ChallengeExamDate) throws InterruptedException {

		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver110.exe");
		fdriver = new FirefoxDriver();
		fdriver.manage().window().maximize();
		//fdriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		fdriver.get(ChallengeURL);
		fdriver.findElementById("Registration_No").sendKeys(RollNo);
		if(Pass.length()!=8)
		{
			Pass = 0+Pass;
		}		
		fdriver.findElementById("Password").sendKeys(Pass);
		Select examDate = new Select(fdriver.findElementById("examdate"));
		examDate.selectByVisibleText(ChallengeExamDate);
		Thread.sleep(10000);

		fdriver.findElementByXPath("//input[@class='btn blue_button5 right']").click();
		Thread.sleep(2000);

		fdriver.findElementByXPath("//button[text()='View response']").click();
		Thread.sleep(2000);

		getFirstQIDinCS();	
		AvailableQuestionsCS();		
		checkNumberOfOptionsFF(); // Checks the number of option as four or five.

	}


	public void AvailableQuestionsCS() {

		AvailableQuestionsCS = fdriver.findElementsByXPath("((//b[text()='Correct Answer:'])/..)");		
		StringAvailableQuestionsCS = Integer.toString(AvailableQuestionsCS.size());

	}

	public void getQuestionsViewedPE() {

		AvailableQuestionsPE = cdriver.findElementsByXPath("//td[@class='greybluetext10']");
		StringAvailableQuestionsPE = Integer.toString(AvailableQuestionsPE.size());
	}


	public void getFirstQIDinCS() {
		String QNO = fdriver.findElementByXPath("//b[starts-with(text(),'Q. No.')]").getText();
		QNO = QNO.replaceAll("[^0-9]", "");
		QNOint =  Integer.parseInt(QNO);
		QNOint = QNOint - 1;
	}



	public void checkNumberOfOptions() {

		optionFive = false;
		int op5count = 0;
		for(int question =1; question<=20; question++)
		{

			String Options = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+question+"]").getText();
			Options = new StringBuffer(Options).replace(0, 8, "").toString();

			String op5 = "5)  ";

			if (Options.contains(op5))
			{
				op5count++;
			}		

		}

		if(op5count!=0)
		{
			optionFive = true;
		}

	}



	public void checkNumberOfOptionsFF() {

		optionFive = false;
		int op5count = 0;
		for(int question =1; question<=20; question++)
		{

			String Options = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+question+"]").getText();
			Options = new StringBuffer(Options).replace(0, 8, "").toString();

			String op5 = "5)  ";

			if (Options.contains(op5))
			{
				op5count++;
			}		

		}

		if(op5count!=0)
		{
			optionFive = true;
		}

	}


	public String getFullTextAT(int start) {		
		FullTextAT = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]").getText();		
		return FullTextAT;		
	}


	public String getFullTextMQP(int start) {		
		FullTextMQP = cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]").getText();		
		return FullTextMQP;		
	}


	public String getQID(String FullText) {		

		//To get QID
		QID = "";
		boolean loop = true;		
		int i=0;
		do
		{
			if (FullText.charAt(i)=='[')
			{
				i++;
				if (FullText.charAt(i)==' ')
				{
					i++;
					QID = QID + FullText.charAt(i);
					i++;
					if (FullText.charAt(i)==' ')
					{
						loop = false;
					}
					else
					{
						QID = QID + FullText.charAt(i);
						i++;
						if (FullText.charAt(i)==' ')
						{
							loop = false;
						}
						else
						{
							QID = QID + FullText.charAt(i);								
							i++;
							if (FullText.charAt(i)==' ')
							{
								loop = false;
							}
							else
							{
								QID = QID + FullText.charAt(i);								
								i++;
								if (FullText.charAt(i)==' ')
								{
									loop = false;
								}
							}
						}
					}
				}
			}
			i++;

		}while(loop == true);		

		/*QIDint = Integer.parseInt(QID);			
		QIDint = QIDint - QNOint;
		QID = Integer.toString(QIDint);*/

		return QID;		
	}


	public String ConvertoToCorrectQID(String QID, int QNOint) {	

		QIDint = Integer.parseInt(QID);
		QIDint = QIDint + QNOint;
		QID = Integer.toString(QIDint);		
		ccQID = QID;	

		return ccQID;		
	}

	public String getQuestionTextAT(int start) {		

		try {
			ccQuestionTextAT = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/b/img").getAttribute("src");
			FullURL = ccQuestionTextAT;
			getImageFileName(FullURL);
			ccQuestionTextAT = ImgName;	
		}			
		catch (NoSuchElementException e) {

			try {
				ccQuestionTextAT = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/b/b/img").getAttribute("src");
				FullURL = ccQuestionTextAT;
				getImageFileName(FullURL);
				ccQuestionTextAT = ImgName;			
			}

			catch (NoSuchElementException e1) {

				ccQuestionTextAT = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/b").getText();

				String QuestionTextFront = start+". Question id [ "+QID+" ]   '";
				ccQuestionTextAT = new StringBuffer(ccQuestionTextAT).replace(0, QuestionTextFront.length()-1, "").toString();

				int trimStart = 0;
				for(int i=ccQuestionTextAT.length()-1; i>=0; i--)
				{

					if(ccQuestionTextAT.charAt(i)=='M')
					{
						if(ccQuestionTextAT.charAt(i-1)=='[')
						{
							trimStart = i-3;
							break;
						}
					}			
				}
				ccQuestionTextAT = new StringBuffer(ccQuestionTextAT).replace(trimStart, ccQuestionTextAT.length(), "").toString();

				boolean xtraSpace;
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextAT.charAt(ccQuestionTextAT.length()-1)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextAT);
					ccQuestionTextAT = NewCandidateAnswer.deleteCharAt(ccQuestionTextAT.length()-1).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextAT.charAt(ccQuestionTextAT.length()-1)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextAT);
					ccQuestionTextAT = NewCandidateAnswer.deleteCharAt(ccQuestionTextAT.length()-1).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextAT.charAt(ccQuestionTextAT.length()-1)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextAT);
					ccQuestionTextAT = NewCandidateAnswer.deleteCharAt(ccQuestionTextAT.length()-1).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextAT.charAt(0)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextAT);
					ccQuestionTextAT = NewCandidateAnswer.deleteCharAt(0).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextAT.charAt(0)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextAT);
					ccQuestionTextAT = NewCandidateAnswer.deleteCharAt(0).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextAT.charAt(0)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextAT);
					ccQuestionTextAT = NewCandidateAnswer.deleteCharAt(0).toString();				
				}
			}
		}

		return ccQuestionTextAT;	
	}


	public String getQuestionTextMQP(String FullTextMQP) {		

		if (qimage==false)
		{
			ccQuestionTextMQP = FullTextMQP;
			int trim = 0;
			for(int i=0; i<=FullTextMQP.length()-1; i++)
			{

				if(FullTextMQP.charAt(i)=='(')
				{
					if(FullTextMQP.charAt(i+1)=='A')

					{
						if(FullTextMQP.charAt(i+2)==')')
						{

							if((FullTextMQP.charAt(i+3)==' ')||(String.valueOf(FullTextMQP.charAt(i+3)).matches("\\s")))
							{
								trim = i;
							}
						}
					}
				}			
			}

			ccQuestionTextMQP = new StringBuffer(ccQuestionTextMQP).replace(trim, ccQuestionTextMQP.length(), "").toString();		



			boolean xtraSpace;
			xtraSpace = false;
			xtraSpace = String.valueOf(ccQuestionTextMQP.charAt(ccQuestionTextMQP.length()-1)).matches("\\s");
			if (xtraSpace==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextMQP);
				ccQuestionTextMQP = NewCandidateAnswer.deleteCharAt(ccQuestionTextMQP.length()-1).toString();				
			}
			xtraSpace = false;
			xtraSpace = String.valueOf(ccQuestionTextMQP.charAt(ccQuestionTextMQP.length()-1)).matches("\\s");
			if (xtraSpace==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextMQP);
				ccQuestionTextMQP = NewCandidateAnswer.deleteCharAt(ccQuestionTextMQP.length()-1).toString();			
			}
			xtraSpace = false;
			xtraSpace = String.valueOf(ccQuestionTextMQP.charAt(ccQuestionTextMQP.length()-1)).matches("\\s");
			if (xtraSpace==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextMQP);
				ccQuestionTextMQP = NewCandidateAnswer.deleteCharAt(ccQuestionTextMQP.length()-1).toString();			
			}
			xtraSpace = false;
			xtraSpace = String.valueOf(ccQuestionTextMQP.charAt(ccQuestionTextMQP.length()-1)).matches("\\s");
			if (xtraSpace==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextMQP);
				ccQuestionTextMQP = NewCandidateAnswer.deleteCharAt(ccQuestionTextMQP.length()-1).toString();			
			}
			xtraSpace = false;
			xtraSpace = String.valueOf(ccQuestionTextMQP.charAt(ccQuestionTextMQP.length()-1)).matches("\\s");
			if (xtraSpace==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextMQP);
				ccQuestionTextMQP = NewCandidateAnswer.deleteCharAt(ccQuestionTextMQP.length()-1).toString();			
			}
			xtraSpace = false;
			xtraSpace = String.valueOf(ccQuestionTextMQP.charAt(0)).matches("\\s");
			if (xtraSpace==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextMQP);
				ccQuestionTextMQP = NewCandidateAnswer.deleteCharAt(0).toString();				
			}
			xtraSpace = false;
			xtraSpace = String.valueOf(ccQuestionTextMQP.charAt(0)).matches("\\s");
			if (xtraSpace==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextMQP);
				ccQuestionTextMQP = NewCandidateAnswer.deleteCharAt(0).toString();				
			}
			xtraSpace = false;
			xtraSpace = String.valueOf(ccQuestionTextMQP.charAt(0)).matches("\\s");
			if (xtraSpace==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextMQP);
				ccQuestionTextMQP = NewCandidateAnswer.deleteCharAt(0).toString();				
			}
		}
		else
		{
			try {
				ccQuestionTextMQP = cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img").getAttribute("src");				
				getImageFileName(ccQuestionTextMQP);
				ccQuestionTextMQP = ImgName;
			} catch (NoSuchElementException e) {
				ccQuestionTextMQP = cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/b/img").getAttribute("src");				
				getImageFileName(ccQuestionTextMQP);
				ccQuestionTextMQP = ImgName;
			}
		}

		return ccQuestionTextMQP;
	}	






	public String getQuestionTextCS(String QID) {		

		try {

			ccQuestionTextCS = fdriver.findElementByXPath("//b[text()='Q. No. : "+QID+"   ']/../b/img").getAttribute("src"); // Uses /.. coz in few cases the question is not under this x path it comes in following::p
			FullURL = ccQuestionTextCS;
			getImageFileName();
			ccQuestionTextCS = FullURL;
			qimage = true;
		} catch (NoSuchElementException e) {		

			try {

				ccQuestionTextCS = fdriver.findElementByXPath("//b[text()='Q. No. : "+QID+"   ']/../b/b/img").getAttribute("src"); // Uses /.. coz in few cases the question is not under this x path it comes in following::p
				FullURL = ccQuestionTextCS;
				getImageFileName();
				ccQuestionTextCS = FullURL;
				qimage = true;
			}

			catch (NoSuchElementException e1) {
				
				try {

					ccQuestionTextCS = fdriver.findElementByXPath("//b[text()='Q. No. : "+QID+"   ']/../img").getAttribute("src"); // Uses /.. coz in few cases the question is not under this x path it comes in following::p
					FullURL = ccQuestionTextCS;
					getImageFileName();
					ccQuestionTextCS = FullURL;
					qimage = true;
				}
				
				catch (NoSuchElementException e11) {

				ccQuestionTextCS = fdriver.findElementByXPath("//b[text()='Q. No. : "+QID+"   ']/..").getText();
				String QuestionTextFront = "Q. No. : "+QID+" - ";		
				ccQuestionTextCS = new StringBuffer(ccQuestionTextCS).replace(0, QuestionTextFront.length(), "").toString();


				boolean xtraSpace;
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextCS.charAt(ccQuestionTextCS.length()-1)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextCS);
					ccQuestionTextCS = NewCandidateAnswer.deleteCharAt(ccQuestionTextCS.length()-1).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextCS.charAt(ccQuestionTextCS.length()-1)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextCS);
					ccQuestionTextCS = NewCandidateAnswer.deleteCharAt(ccQuestionTextCS.length()-1).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextCS.charAt(ccQuestionTextCS.length()-1)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextCS);
					ccQuestionTextCS = NewCandidateAnswer.deleteCharAt(ccQuestionTextCS.length()-1).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextCS.charAt(0)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextCS);
					ccQuestionTextCS = NewCandidateAnswer.deleteCharAt(0).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextCS.charAt(0)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextCS);
					ccQuestionTextCS = NewCandidateAnswer.deleteCharAt(0).toString();				
				}
				xtraSpace = false;
				xtraSpace = String.valueOf(ccQuestionTextCS.charAt(0)).matches("\\s");
				if (xtraSpace==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(ccQuestionTextCS);
					ccQuestionTextCS = NewCandidateAnswer.deleteCharAt(0).toString();				
				}


			}
		}
		}


		return ccQuestionTextCS;	
	}	


	public String[] getCorCanAnsAT(String FullText) {



		try {
			cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img");
			ImageCheck = true;
		} catch (NoSuchElementException e) {
			ImageCheck = false;

		}


		String CorrectAnswerPostExam = "";
		String CandidateAnswerPostExam = "";
		// To get Correct Answer from Post Exam
		if(ImageCheck==false)
		{			

			int CorrectAnsStart = 0;
			int j=(FullText.length()-1);			
			boolean loop = true;
			boolean correctincorrect = false;

			do
			{
				if (FullText.charAt(j)==' ')
				{
					if (FullText.charAt(j-1)==':')
					{
						if (FullText.charAt(j-2)==' ')
						{
							if (FullText.charAt(j-3)=='r')
							{
								if (FullText.charAt(j-4)=='e')
								{
									if (FullText.charAt(j-5)=='w')
									{

										CorrectAnsStart = j+1;
										for(int k=j+1; k<FullText.length(); k++)
										{
											CorrectAnswerPostExam = CorrectAnswerPostExam + FullText.charAt(k);
										}
										String CAinCA = "Correct Answer";
										String NoCorAns = "No Correct Answer";

										if(CorrectAnswerPostExam.equals(NoCorAns))
										{
											CorrectAnswerPostExam = "No Correct Answer";
										}											
										else if(CorrectAnswerPostExam.contains(CAinCA))
										{
											CorrectAnswerPostExam = "EMPTY";
											correctincorrect = true;
										}										

										//System.out.println("Correct Answer from Post Exam URL is "+ CorrectAnswer);
										loop = false;						
									}
								}
							}
						}
					}

				}j--;
			} while(loop==true);

			// To get Candidate Answer from Post Exam URL

			String NotAnswered = "NOT ANSWERED";
			int l = 0;
			int CandidateAnsEnd = 0;
			if (FullText.contains(NotAnswered))
			{
				CandidateAnswerPostExam = "NOT ANSWERED";
				candidateAnsStartAT = CorrectAnsStart-30;
			}				
			else
			{
				if (correctincorrect==true)
				{
					CandidateAnsEnd = FullText.length()-17;
					l=(FullText.length()-17);
				}
				else
				{
					CandidateAnsEnd = CorrectAnsStart-18;
					l=(CorrectAnsStart-18);
				}	


				loop = true;
				do
				{		
					if (FullText.charAt(l)==' ')
					{
						if (FullText.charAt(l-1)==':')
						{
							if (FullText.charAt(l-2)==' ')
							{
								if (FullText.charAt(l-3)=='r')
								{
									if (FullText.charAt(l-4)=='e')
									{
										if (FullText.charAt(l-5)=='w')
										{
											candidateAnsStartAT = l+1;
											for(int k=l+1; k<CandidateAnsEnd; k++)
											{
												CandidateAnswerPostExam = CandidateAnswerPostExam + FullText.charAt(k);
											}
											//System.out.println("Candidate Answer from Post Exam is "+ CandidateAnswerPostExam);
											loop = false;	
										}
									}
								}
							}
						}
					}l--;

				}while(loop==true);
			}

			// For few Questions, the Candidate answer has an enter Key at the end which added an extra char at the end. So, deleting that char.
			boolean caspecial;
			caspecial = false;
			caspecial = String.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length()-1)).matches("\\s");
			if (caspecial==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
				CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length()-1).toString();
				//System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
			}
			caspecial = false;
			caspecial = String.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length()-1)).matches("\\s");
			if (caspecial==true) 
			{
				StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
				CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length()-1).toString();
				//System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
			}
			boolean cospecial;
			cospecial = false;
			cospecial = String.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length()-1)).matches("\\s");
			if (cospecial==true) 
			{
				StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
				CorrectAnswerPostExam = NewCorrectAnswer.deleteCharAt(CorrectAnswerPostExam.length()-1).toString();
				//System.out.println("New Correct Answer is "+ CorrectAnswer);
			}
			cospecial = false;
			cospecial = String.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length()-1)).matches("\\s");
			if (cospecial==true) 
			{
				StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
				CorrectAnswerPostExam = NewCorrectAnswer.deleteCharAt(CorrectAnswerPostExam.length()-1).toString();
				//System.out.println("New Correct Answer is "+ CorrectAnswer);
			}
		}



		if(ImageCheck==true)		
		{
			mixture = false;
			List<WebElement> numberOfImageList = cdriver.findElementsByXPath("(//td[@class='greybluetext10'])["+start+"]/img");

			String NotAnsweredAT = "NOT ANSWERED";
			if(FullText.contains(NotAnsweredAT))
			{
				if(numberOfImageList.size()<5)
				{
					mixture = true;
				}			
			}
			else 
			{
				if(numberOfImageList.size()<6)
				{
					mixture = true;
				}	
			}


			if(mixture==false)
			{

				if(FullText.contains(NotAnsweredAT))
				{
					CandidateAnswerPostExam = "NOT ANSWERED";
					CorrectAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[5]").getAttribute("src");
					FullURL = CorrectAnswerPostExam;
					getImageFileName(FullURL);
					CorrectAnswerPostExam = ImgName;

				}
				else
				{
					CandidateAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[5]").getAttribute("src");
					FullURL = CandidateAnswerPostExam;
					getImageFileName(FullURL);
					CandidateAnswerPostExam = ImgName;
					CorrectAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[6]").getAttribute("src");
					FullURL = CorrectAnswerPostExam;
					getImageFileName(FullURL);
					CorrectAnswerPostExam = ImgName;
				}
			}

			if(mixture==true)
			{

				List<WebElement> NumberOfImages = cdriver.findElementsByXPath("(//td[@class='greybluetext10'])["+start+"]/img");

				String img = "image", txt = "text";
				int slash;

				if(CanAnsCS.equals(img)&&CorAnsCS.equals(img))
				{

					CorrectAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img["+NumberOfImages.size()+"]").getAttribute("src");
					CandidateAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img["+(NumberOfImages.size()-1)+"]").getAttribute("src");

					slash = 0;

					for(slash = CandidateAnswerPostExam.length()-1; slash>=0; slash--)
					{
						if(CandidateAnswerPostExam.charAt(slash)=='/')
						{
							break;
						}
					}
					CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam).replace(0, slash, "").toString();

					slash = 0;
					for(slash = CorrectAnswerPostExam.length()-1; slash>=0; slash--)
					{
						if(CorrectAnswerPostExam.charAt(slash)=='/')
						{
							break;
						}
					}
					CorrectAnswerPostExam = new StringBuffer(CorrectAnswerPostExam).replace(0, slash, "").toString();	
				}

				else if(CanAnsCS.equals(txt)&&CorAnsCS.equals(img))
				{

					CorrectAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img["+NumberOfImages.size()+"]").getAttribute("src");
					slash = 0;
					for(slash = CorrectAnswerPostExam.length()-1; slash>=0; slash--)
					{
						if(CorrectAnswerPostExam.charAt(slash)=='/')
						{
							break;
						}
					}
					CorrectAnswerPostExam = new StringBuffer(CorrectAnswerPostExam).replace(0, slash, "").toString();	

					// To get Candidate Answer from Post Exam URL

					String NotAnswered = "NOT ANSWERED";
					int l = 0;
					int CandidateAnsEnd = 0;
					if (FullText.contains(NotAnswered))
					{
						CandidateAnswerPostExam = "NOT ANSWERED";
					}				
					else
					{								

						CandidateAnsEnd = FullText.length()-17;
						l=(FullText.length()-17);


						boolean loop = true;
						do
						{		
							if (FullText.charAt(l)==' ')
							{
								if (FullText.charAt(l-1)==':')
								{
									if (FullText.charAt(l-2)==' ')
									{
										if (FullText.charAt(l-3)=='r')
										{
											if (FullText.charAt(l-4)=='e')
											{
												if (FullText.charAt(l-5)=='w')
												{

													for(int k=l+1; k<CandidateAnsEnd; k++)
													{
														CandidateAnswerPostExam = CandidateAnswerPostExam + FullText.charAt(k);
													}
													//System.out.println("Candidate Answer from Post Exam is "+ CandidateAnswerPostExam);
													loop = false;	
												}
											}
										}
									}
								}
							}l--;

						}while(loop==true);
					}

					// For few Questions, the Candidate answer has an enter Key at the end which added an extra char at the end. So, deleting that char.
					boolean caspecial = String.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length()-1)).matches("\\s");
					if (caspecial==true) 
					{
						StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
						CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length()-1).toString();
						//System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
					}

				}


				else if(CanAnsCS.equals(txt)&&CorAnsCS.equals(txt))

				{
					// To get Correct Answer from Post Exam							

					CorrectAnswerPostExam = "";
					int CorrectAnsStart = 0;
					int j=(FullText.length()-1);			
					Boolean loop = true;
					boolean correctincorrect = false;

					do
					{
						if (FullText.charAt(j)==' ')
						{
							if (FullText.charAt(j-1)==':')
							{
								if (FullText.charAt(j-2)==' ')
								{
									if (FullText.charAt(j-3)=='r')
									{
										if (FullText.charAt(j-4)=='e')
										{
											if (FullText.charAt(j-5)=='w')
											{

												CorrectAnsStart = j+1;
												for(int k=j+1; k<FullText.length(); k++)
												{
													CorrectAnswerPostExam = CorrectAnswerPostExam + FullText.charAt(k);
												}
												String CAinCA = "Correct Answer";
												String NoCorAns = "No Correct Answer";

												if(CorrectAnswerPostExam.equals(NoCorAns))
												{
													CorrectAnswerPostExam = "No Correct Answer";
												}											
												else if(CorrectAnswerPostExam.contains(CAinCA))
												{
													CorrectAnswerPostExam = "EMPTY";
													correctincorrect = true;
												}
												//System.out.println("Correct Answer from Post Exam URL is "+ CorrectAnswer);
												loop = false;						
											}
										}
									}
								}
							}

						}j--;
					}while(loop==true);


					// To get Candidate Answer from Post Exam URL
					CandidateAnswerPostExam = "";
					String NotAnswered = "NOT ANSWERED";
					int l = 0;
					int CandidateAnsEnd = 0;
					if (FullText.contains(NotAnswered))
					{
						CandidateAnswerPostExam = "NOT ANSWERED";
					}				
					else
					{
						if (correctincorrect==true)
						{
							CandidateAnsEnd = FullText.length()-17;
							l=(FullText.length()-17);
						}
						else
						{
							CandidateAnsEnd = CorrectAnsStart-18;
							l=(CorrectAnsStart-18);
						}	


						loop = true;
						do
						{		
							if (FullText.charAt(l)==' ')
							{
								if (FullText.charAt(l-1)==':')
								{
									if (FullText.charAt(l-2)==' ')
									{
										if (FullText.charAt(l-3)=='r')
										{
											if (FullText.charAt(l-4)=='e')
											{
												if (FullText.charAt(l-5)=='w')
												{

													for(int k=l+1; k<CandidateAnsEnd; k++)
													{
														CandidateAnswerPostExam = CandidateAnswerPostExam + FullText.charAt(k);
													}
													//System.out.println("Candidate Answer from Post Exam is "+ CandidateAnswerPostExam);
													loop = false;	
												}
											}
										}
									}
								}
							}l--;

						}while(loop==true);
					}

					// For few Questions, the Candidate answer has an enter Key at the end which added an extra char at the end. So, deleting that char.
					boolean caspecial; 
					caspecial = String.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length()-1)).matches("\\s");
					if (caspecial==true) 
					{
						StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
						CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length()-1).toString();
					}
					caspecial = false;
					caspecial = String.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length()-1)).matches("\\s");
					if (caspecial==true) 
					{
						StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
						CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length()-1).toString();
					}
					boolean cospecial;
					cospecial = String.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length()-1)).matches("\\s");
					if (cospecial==true) 
					{
						StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
						CorrectAnswerPostExam = NewCorrectAnswer.deleteCharAt(CorrectAnswerPostExam.length()-1).toString();						
					}
					cospecial = false;
					cospecial = String.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length()-1)).matches("\\s");
					if (cospecial==true) 
					{
						StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
						CorrectAnswerPostExam = NewCorrectAnswer.deleteCharAt(CorrectAnswerPostExam.length()-1).toString();						
					}
				}

				else if(CanAnsCS.equals(img)&&CorAnsCS.equals(txt))
				{

					// To get Correct Answer from Post Exam							

					CorrectAnswerPostExam = "";
					int CorrectAnsStart = 0;
					int j=(FullText.length()-1);			
					boolean loop = true;
					boolean correctincorrect = false;

					do
					{
						if (FullText.charAt(j)==' ')
						{
							if (FullText.charAt(j-1)==':')
							{
								if (FullText.charAt(j-2)==' ')
								{
									if (FullText.charAt(j-3)=='r')
									{
										if (FullText.charAt(j-4)=='e')
										{
											if (FullText.charAt(j-5)=='w')
											{

												CorrectAnsStart = j+1;
												for(int k=j+1; k<FullText.length(); k++)
												{
													CorrectAnswerPostExam = CorrectAnswerPostExam + FullText.charAt(k);
												}
												String CAinCA = "Correct Answer";
												String NoCorAns = "No Correct Answer";

												if(CorrectAnswerPostExam.equals(NoCorAns))
												{
													CorrectAnswerPostExam = "No Correct Answer";
												}											
												else if(CorrectAnswerPostExam.contains(CAinCA))
												{
													CorrectAnswerPostExam = "EMPTY";
													correctincorrect = true;
												}
												//System.out.println("Correct Answer from Post Exam URL is "+ CorrectAnswer);
												loop = false;						
											}
										}
									}
								}
							}

						}j--;
					}while(loop==true);

					boolean cospecial = String.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length()-1)).matches("\\s");
					if (cospecial==true) 
					{
						StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
						CorrectAnswerPostExam = NewCorrectAnswer.deleteCharAt(CorrectAnswerPostExam.length()-1).toString();
						//System.out.println("New Correct Answer is "+ CorrectAnswer);
					}


					CandidateAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img["+NumberOfImages.size()+"]").getAttribute("src");

					slash = 0;

					for(slash = CandidateAnswerPostExam.length()-1; slash>=0; slash--)
					{
						if(CandidateAnswerPostExam.charAt(slash)=='/')
						{
							break;
						}
					}
					CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam).replace(0, slash, "").toString();							
				}

			}

		}

		CorCandAT[0] = CorrectAnswerPostExam;
		CorCandAT[1] = CandidateAnswerPostExam;
		return CorCandAT;
	}


	public String[] getCorCanAnsCS(String QID) {

		ccCorAnsCS = "";
		ccCanAnsCS = "";
		String CanAnsCS = "", CorAnsCS = "";

		QIDint = Integer.parseInt(QID);			
		QIDint = QIDint - QNOint;
		String QID4CS = Integer.toString(QIDint);


		try {
			ccCorAnsCS = fdriver.findElementByXPath("((//b[text()='Correct Answer:'])/..)["+QID4CS+"]/img").getAttribute("src");
			int slash = 0;
			for(slash = ccCorAnsCS.length()-1; slash>=0; slash--)
			{
				if(ccCorAnsCS.charAt(slash)=='/')
				{
					break;
				}
			}
			ccCorAnsCS = new StringBuffer(ccCorAnsCS).replace(0, slash, "").toString();
			CorAnsCS = "image";

		} catch (NoSuchElementException e) {

			ccCorAnsCS = fdriver.findElementByXPath("((//b[text()='Correct Answer:'])/..)["+QID4CS+"]").getText();
			ccCorAnsCS = new StringBuffer(ccCorAnsCS).replace(0, 22, "").toString();
			CorAnsCS = "text";

			if(ccCorAnsCS.length()!=0)
			{				

				boolean correctAnsEndSpecial;

				correctAnsEndSpecial = false;
				correctAnsEndSpecial = String.valueOf(ccCorAnsCS.charAt(ccCorAnsCS.length()-1)).matches("\\s");
				if (correctAnsEndSpecial==true) 
				{
					StringBuilder NewCorrectAnswer = new StringBuilder(ccCorAnsCS);
					ccCorAnsCS = NewCorrectAnswer.deleteCharAt(ccCorAnsCS.length()-1).toString();						
				}
				correctAnsEndSpecial = false;
				correctAnsEndSpecial = String.valueOf(ccCorAnsCS.charAt(ccCorAnsCS.length()-1)).matches("\\s");
				if (correctAnsEndSpecial==true) 
				{
					StringBuilder NewCorrectAnswer = new StringBuilder(ccCorAnsCS);
					ccCorAnsCS = NewCorrectAnswer.deleteCharAt(ccCorAnsCS.length()-1).toString();						
				}
			} 
			else					
			{
				ccCorAnsCS = "Correct Answer is Empty";
			}

		}

		try {
			ccCanAnsCS = fdriver.findElementByXPath("((//b[text()='Candidate Answer:'])/..)["+QID4CS+"]/img").getAttribute("src");
			int slash = 0;
			for(slash = ccCanAnsCS.length()-1; slash>=0; slash--)
			{
				if(ccCanAnsCS.charAt(slash)=='/')
				{
					break;
				}
			}
			ccCanAnsCS = new StringBuffer(ccCanAnsCS).replace(0, slash, "").toString();
			CorAnsCS = "image";

		} catch (NoSuchElementException e) {

			ccCanAnsCS = fdriver.findElementByXPath("((//b[text()='Candidate Answer:'])/..)["+QID4CS+"]").getText();
			ccCanAnsCS = new StringBuffer(ccCanAnsCS).replace(0, 24, "").toString();
			CorAnsCS = "text";

			if(ccCanAnsCS.length()!=0)
			{				

				boolean correctAnsEndSpecial;

				correctAnsEndSpecial = false;
				correctAnsEndSpecial = String.valueOf(ccCanAnsCS.charAt(ccCanAnsCS.length()-1)).matches("\\s");
				if (correctAnsEndSpecial==true) 
				{
					StringBuilder NewCorrectAnswer = new StringBuilder(ccCanAnsCS);
					ccCanAnsCS = NewCorrectAnswer.deleteCharAt(ccCanAnsCS.length()-1).toString();						
				}
				correctAnsEndSpecial = false;
				correctAnsEndSpecial = String.valueOf(ccCanAnsCS.charAt(ccCanAnsCS.length()-1)).matches("\\s");
				if (correctAnsEndSpecial==true) 
				{
					StringBuilder NewCorrectAnswer = new StringBuilder(ccCanAnsCS);
					ccCanAnsCS = NewCorrectAnswer.deleteCharAt(ccCanAnsCS.length()-1).toString();						
				}
			} 
			else					
			{
				ccCanAnsCS = "Correct Answer is Empty";
			}

			String NA = "ANSWERED ]";
			if (ccCanAnsCS.equals(NA))
			{
				ccCanAnsCS = "NOT ANSWERED";
			}


		}
		CorCandCS[0] = ccCorAnsCS;	
		CorCandCS[1] = ccCanAnsCS;	
		return CorCandCS;		

	}






	public void CreateExcelACScc(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("AdminWithChallengeSystem");	

		rowCount = 0;
		WriteInExcelACScc("RollNo", "QID", "QuestionTextAT", "QuestionTextCS", "Option1AT", "Option1CS", "Option2AT", "Option2CS", 
				"Option3AT", "Option3CS", "Option4AT", "Option4CS", "CanAnsAT", "CanAnsCS", "CorAnsAT", "CorAnsCS", "Status");
	}


	public void CreateExcelCSMcc(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("ChallengeSystemWithMasterQP");	

		rowCount = 0;
		WriteInExcelCSMcc("RollNo", "QID", "QuestionTextMQP", "QuestionTextCS", "Option1MQP", "Option1CS", "Option2MQP", "Option2CS", 
				"Option3MQP", "Option3CS", "Option4MQP", "Option4CS", "CorAnsCS", "CorOptCS", "CorOptMQP", "Status");
	}



	public void WriteInExcelACScc(String text1, String text2, String text3, String text4, String text5, String text6, String text7, String text8, String text9, String text10, 
			String text11, String text12, String text13, String text14, String text15, String text16, String text17) throws FileNotFoundException, IOException {



		Object[][] bookData = {
				{text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12, text13, text14, text15, text16, text17},              
		};



		for (Object[] aBook : bookData) {
			Row row = sheet.createRow(++rowCount);

			int columnCount = 0;

			for (Object field : aBook) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}

		}  
		try (FileOutputStream outputStream = new FileOutputStream("F:\\Results\\AdminWithChallengeSystem\\TestResult_ACScc.xlsx")) {
			workbook.write(outputStream);
		}


	}


	public void WriteInExcelCSMcc(String text1, String text2, String text3, String text4, String text5, String text6, String text7, String text8, String text9, String text10, 
			String text11, String text12, String text13, String text14, String text15, String text16) throws FileNotFoundException, IOException {



		Object[][] bookData = {
				{text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12, text13, text14, text15, text16},              
		};



		for (Object[] aBook : bookData) {
			Row row = sheet.createRow(++rowCount);

			int columnCount = 0;

			for (Object field : aBook) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}

		}  
		try (FileOutputStream outputStream = new FileOutputStream("F:\\Results\\ChallengeSystemWithMaster\\TestResult_CSMcc.xlsx")) {
			workbook.write(outputStream);
		}


	}








	public String[] getOptionsAT(String Options) {

		try {
			cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img");
			ImageCheck = true;
		} catch (NoSuchElementException e) {
			ImageCheck = false;

		}

		String option1 ="";String option2 ="";String option3 ="";String option4 ="", option5 ="";

		if (ImageCheck==false)
		{
			int Op1Start=0, Op1end=0, Op2Start=0, Op2end=0, Op3Start=0, Op3end=0, Op4Start=0, Op4end=0, Op5Start=0, Op5end=0; 

			//Getting Option start position	
			for(int i=0; i<Options.length(); i++)
			{
				if(Op1Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='A')
						{
							if(Options.charAt(i+2)==')')
							{
								if((Options.charAt(i+3)==' ')||(String.valueOf(Options.charAt(i+3)).matches("\\s")))

								{
									Op1Start = i+4;								
								}

							}
						}
					}}

				if(Op2Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='B')
						{
							if(Options.charAt(i+2)==')')
							{													
								if(Options.charAt(i+3)==' ')

								{
									Op2Start = i+4;
								}

							}
						}								
					}
				}

				if(Op3Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='C')
						{
							if(Options.charAt(i+2)==')')
							{
								if(Options.charAt(i+3)==' ')

								{
									Op3Start = i+4;
								}

							}
						}								


					}
				}

				if(Op4Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='D')
						{
							if(Options.charAt(i+2)==')')
							{
								if(Options.charAt(i+3)==' ')												

								{
									Op4Start = i+4;	

									if (optionFive==false)
									{
										break;
									}
								}
							}
						}
					}
				}

				if (optionFive==true)
				{
					if(Op5Start==0)
					{
						if(Options.charAt(i)=='(')
						{
							if(Options.charAt(i+1)=='E')
							{
								if(Options.charAt(i+2)==')')
								{
									if(Options.charAt(i+3)==' ')												

									{
										Op5Start = i+4;
										break;
									}
								}
							}
						}
					}
				}
			}

			if (Op1Start==0)
			{

			}


			//Getting Option end position
			if (optionFive==true)
			{
				Op1end=Op2Start-4;
				Op2end=Op3Start-4;
				Op3end=Op4Start-4;
				Op4end=Op5Start-4;				
				Op5end=Options.length();
			}
			else
			{
				Op1end=Op2Start-4;
				Op2end=Op3Start-4;
				Op3end=Op4Start-4;
				Op4end=candidateAnsStartAT-19;
			}

			

			//Getting Separate option text
			option1=Options.substring(Op1Start, Op1end);
			option2=Options.substring(Op2Start, Op2end);
			option3=Options.substring(Op3Start, Op3end);
			option4=Options.substring(Op4Start, Op4end);
			if (optionFive==true)
			{
				option5=Options.substring(Op5Start, Op5end);
			}

		}
		if (ImageCheck==true)
		{			
			mixture = false;
			List<WebElement> numberOfImageList = cdriver.findElementsByXPath("(//td[@class='greybluetext10'])["+start+"]/img");

			String NotAnsweredAT = "NOT ANSWERED";
			if(ccCanAnsAT.equals(NotAnsweredAT))
			{
				if(numberOfImageList.size()<5)
				{
					mixture = true;
				}			
			}
			else 
			{
				if(numberOfImageList.size()<6)
				{
					mixture = true;
				}	
			}

			if(mixture==false)
			{
				option1=cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[1]").getAttribute("src");
				getImageFileName(option1); option1 = ImgName;

				option2=cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[2]").getAttribute("src");
				getImageFileName(option2); option2 = ImgName;

				option3=cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[3]").getAttribute("src");
				getImageFileName(option3); option3 = ImgName;

				option4=cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[4]").getAttribute("src");
				getImageFileName(option4); option4 = ImgName;
			}
			else
			{
				// Get Options for each case of image and text combination
				option1 ="";option2 ="";option3 ="";option4 ="";option5 ="";					

				{	
					int op1count=0, op2count=0, op3count=0, op4count=0, op5count=0;
					String op1ti, op2ti, op3ti, op4ti, op5ti, optxt = "text", opim = "image", img = "image", txt = "text";

					getOptionTextAT(Options);
					option1 = optionText[0];
					option2 = optionText[1];
					option3 = optionText[2];
					option4 = optionText[3];
					if (optionFive==true)
					{
						option5 = optionText[4];
					}


					for(int i=0; i<option1.length(); i++)
					{
						boolean op1 = String.valueOf(option1.charAt(i)).matches("[A-Za-z0-9]");
						if (op1==true) op1count++;
					}
					if (op1count==0)
					{
						op1ti = "image";
					}
					else
					{
						op1ti = "text";
					}

					for(int i=0; i<option2.length(); i++)
					{
						boolean op2 = String.valueOf(option2.charAt(i)).matches("[A-Za-z0-9]");
						if (op2==true) op2count++;
					}
					if (op2count==0)
					{
						op2ti = "image";
					}
					else
					{
						op2ti = "text";
					}

					for(int i=0; i<option3.length(); i++)
					{
						boolean op3 = String.valueOf(option3.charAt(i)).matches("[A-Za-z0-9]");
						if (op3==true) op3count++;
					}
					if (op3count==0)
					{
						op3ti = "image";
					}
					else
					{
						op3ti = "text";
					}

					for(int i=0; i<option4.length(); i++)
					{
						boolean op4 = String.valueOf(option4.charAt(i)).matches("[A-Za-z0-9]");
						if (op4==true) op4count++;
					}
					if (op4count==0)
					{
						op4ti = "image";
					}
					else
					{
						op4ti = "text";
					}

					if (optionFive==true)
					{
						for(int i=0; i<option5.length(); i++)
						{
							boolean op5 = String.valueOf(option5.charAt(i)).matches("[A-Za-z0-9]");
							if (op5==true) op5count++;
						}
						if (op5count==0)
						{
							op5ti = "image";
						}
						else
						{
							op5ti = "text";
						}
					}

					//System.out.println(op1ti);
					//System.out.println(op2ti);
					//System.out.println(op3ti);
					//System.out.println(op4ti);					



					if((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
					{
						option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[3]").getAttribute("src");
						FullURL = option1; getImageFileName(); option1 = FullURL;
						FullURL = option2; getImageFileName(); option2 = FullURL;
						FullURL = option3; getImageFileName(); option3 = FullURL;
					}
					else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(img)))
					{
						option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[3]").getAttribute("src");
						FullURL = option1; getImageFileName(); option1 = FullURL;
						FullURL = option3; getImageFileName(); option3 = FullURL;
						FullURL = option4; getImageFileName(); option4 = FullURL;

					}else if ((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
					{
						option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[3]").getAttribute("src");
						FullURL = option1; getImageFileName(); option1 = FullURL;
						FullURL = option2; getImageFileName(); option2 = FullURL;
						FullURL = option4; getImageFileName(); option4 = FullURL;

					}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(img)))
					{
						option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[3]").getAttribute("src");
						FullURL = option2; getImageFileName(); option2 = FullURL;
						FullURL = option3; getImageFileName(); option3 = FullURL;
						FullURL = option4; getImageFileName(); option4 = FullURL;

					}else if ((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
					{
						option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						FullURL = option1; getImageFileName(); option1 = FullURL;
						FullURL = option2; getImageFileName(); option2 = FullURL;

					}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
					{
						option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						FullURL = option2; getImageFileName(); option2 = FullURL;
						FullURL = option3; getImageFileName(); option3 = FullURL;

					}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(img)))
					{
						option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						FullURL = option3; getImageFileName(); option3 = FullURL;
						FullURL = option4; getImageFileName(); option4 = FullURL;

					}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
					{
						option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						FullURL = option1; getImageFileName(); option1 = FullURL;
						FullURL = option3; getImageFileName(); option3 = FullURL;								

					}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
					{
						option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");

					}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
					{
						option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");								
						option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						FullURL = option2; getImageFileName(); option2 = FullURL;
						FullURL = option4; getImageFileName(); option4 = FullURL;

					}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
					{
						option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");
						FullURL = option4; getImageFileName(); option4 = FullURL;

					}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
					{
						option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");
						FullURL = option3; getImageFileName(); option3 = FullURL;

					}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
					{
						option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");
						FullURL = option2; getImageFileName(); option2 = FullURL;

					}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
					{
						option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");	
						FullURL = option1; getImageFileName(); option1 = FullURL;
					}						

				}
			}
		}

		if((option1.length()!=0)&&(option2.length()!=0)&&(option3.length()!=0)&&(option4.length()!=0)&&(option4.length()!=0))
		{

			boolean optionEndSpecial;			
			int stop;
			String specialString = "";
			if (optionFive==true)
			{
				stop= 4;
			} 
			else 
			{
				stop = 5;
			}
			for (int i = 1; i <= 4; i++) {
				switch (i)
				{
				case 1: specialString = option1; break;
				case 2: specialString = option2; break;
				case 3: specialString = option3; break;
				case 4: specialString = option4; break;
				case 5: specialString = option5; break;
				}

				for (int j = 1; j <= 5; j++) {
					optionEndSpecial = false;				
					optionEndSpecial = String.valueOf(specialString.charAt(specialString.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption = new StringBuilder(specialString);
						specialString = Newoption.deleteCharAt(specialString.length() - 1).toString();
						switch (i)
						{
						case 1: option1 = specialString; 
						break;
						case 2: option2 = specialString; 
						break;
						case 3: option3 = specialString; 
						break;
						case 4: option4 = specialString; 
						break;
						case 5: option5 = specialString; 
						break;
						}						
					}			
				}
			}
		}

		optionText[0]=option1;
		optionText[1]=option2;
		optionText[2]=option3;
		optionText[3]=option4;
		if (optionFive==true)
		{
			optionText[4]=option5;
		}

		return optionText; 		
	}



	public String[] getOptionsMQP(String Options) {



		String option1 ="";String option2 ="";String option3 ="";String option4 ="", option5 ="";

		if (ImageCheck==false)
		{
			int Op1Start=0, Op1end=0, Op2Start=0, Op2end=0, Op3Start=0, Op3end=0, Op4Start=0, Op4end=0, Op5Start=0, Op5end=0; 

			//Getting Option start position	
			for(int i=0; i<Options.length(); i++)
			{
				if(Op1Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='A')
						{
							if(Options.charAt(i+2)==')')
							{
								if((Options.charAt(i+3)==' ')||(String.valueOf(Options.charAt(i+3)).matches("\\s")))

								{
									Op1Start = i+4;								
								}

							}
						}
					}}

				if(Op2Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='B')
						{
							if(Options.charAt(i+2)==')')
							{													
								if(Options.charAt(i+3)==' ')

								{
									Op2Start = i+4;
								}

							}
						}								
					}
				}

				if(Op3Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='C')
						{
							if(Options.charAt(i+2)==')')
							{
								if(Options.charAt(i+3)==' ')

								{
									Op3Start = i+4;
								}

							}
						}								


					}
				}

				if(Op4Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='D')
						{
							if(Options.charAt(i+2)==')')
							{
								if(Options.charAt(i+3)==' ')												

								{
									Op4Start = i+4;	

									if (optionFive==false)
									{
										break;
									}
								}
							}
						}
					}
				}

				if (optionFive==true)
				{
					if(Op5Start==0)
					{
						if(Options.charAt(i)=='(')
						{
							if(Options.charAt(i+1)=='E')
							{
								if(Options.charAt(i+2)==')')
								{
									if(Options.charAt(i+3)==' ')												

									{
										Op5Start = i+4;
										break;
									}
								}
							}
						}
					}
				}
			}




			//Getting Option end position
			if (optionFive==true)
			{
				Op1end=Op2Start-4;
				Op2end=Op3Start-4;
				Op3end=Op4Start-4;
				Op4end=Op5Start-4;				
				Op5end=Options.length();
			}
			else
			{
				Op1end=Op2Start-4;
				Op2end=Op3Start-4;
				Op3end=Op4Start-4;
				Op4end=Options.length();
			}



			//Getting Separate option text
			option1=Options.substring(Op1Start, Op1end);
			option2=Options.substring(Op2Start, Op2end);
			option3=Options.substring(Op3Start, Op3end);
			option4=Options.substring(Op4Start, Op4end);
			if (optionFive==true)
			{
				option5=Options.substring(Op5Start, Op5end);
			}

		}
		if (ImageCheck==true)
		{			
			mixture = false;
			List<WebElement> numberOfImageList = cdriver.findElementsByXPath("//tr["+(start+1)+"]/td[4]/img");


			if(qimage==true)
			{
				if(numberOfImageList.size()<5)
				{
					mixture = true;
				}			
			}
			else 
			{
				if(numberOfImageList.size()<4)
				{
					mixture = true;
				}	
			}

			if((mixture==false)&&(qimage==false))
			{
				option1=cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img[1]").getAttribute("src");
				getImageFileName(option1); option1 = ImgName;

				option2=cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img[2]").getAttribute("src");
				getImageFileName(option2); option2 = ImgName;

				option3=cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img[3]").getAttribute("src");
				getImageFileName(option3); option3 = ImgName;

				option4=cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img[4]").getAttribute("src");
				getImageFileName(option4); option4 = ImgName;
			}
			else if((mixture==false)&&(qimage==true))
			{
				option1=cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img[2]").getAttribute("src");
				getImageFileName(option1); option1 = ImgName;

				option2=cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img[3]").getAttribute("src");
				getImageFileName(option2); option2 = ImgName;

				option3=cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img[4]").getAttribute("src");
				getImageFileName(option3); option3 = ImgName;

				option4=cdriver.findElementByXPath("//tr["+(start+1)+"]/td[4]/img[5]").getAttribute("src");
				getImageFileName(option4); option4 = ImgName;
			}		
			else if (mixture==true)
			{
				// Get Options for each case of image and text combination
				option1 ="";option2 ="";option3 ="";option4 ="";option5 ="";					

				{	
					int op1count=0, op2count=0, op3count=0, op4count=0, op5count=0;
					String op1ti, op2ti, op3ti, op4ti, op5ti, optxt = "text", opim = "image", img = "image", txt = "text";

					getOptionTextMQP(Options);
					option1 = optionText[0];
					option2 = optionText[1];
					option3 = optionText[2];
					option4 = optionText[3];
					if (optionFive==true)
					{
						option5 = optionText[4];
					}


					for(int i=0; i<option1.length(); i++)
					{
						boolean op1 = String.valueOf(option1.charAt(i)).matches("[A-Za-z0-9]");
						if (op1==true) op1count++;
					}
					if (op1count==0)
					{
						op1ti = "image";
					}
					else
					{
						op1ti = "text";
					}

					for(int i=0; i<option2.length(); i++)
					{
						boolean op2 = String.valueOf(option2.charAt(i)).matches("[A-Za-z0-9]");
						if (op2==true) op2count++;
					}
					if (op2count==0)
					{
						op2ti = "image";
					}
					else
					{
						op2ti = "text";
					}

					for(int i=0; i<option3.length(); i++)
					{
						boolean op3 = String.valueOf(option3.charAt(i)).matches("[A-Za-z0-9]");
						if (op3==true) op3count++;
					}
					if (op3count==0)
					{
						op3ti = "image";
					}
					else
					{
						op3ti = "text";
					}

					for(int i=0; i<option4.length(); i++)
					{
						boolean op4 = String.valueOf(option4.charAt(i)).matches("[A-Za-z0-9]");
						if (op4==true) op4count++;
					}
					if (op4count==0)
					{
						op4ti = "image";
					}
					else
					{
						op4ti = "text";
					}

					if (optionFive==true)
					{
						for(int i=0; i<option5.length(); i++)
						{
							boolean op5 = String.valueOf(option5.charAt(i)).matches("[A-Za-z0-9]");
							if (op5==true) op5count++;
						}
						if (op5count==0)
						{
							op5ti = "image";
						}
						else
						{
							op5ti = "text";
						}
					}

					//System.out.println(op1ti);
					//System.out.println(op2ti);
					//System.out.println(op3ti);
					//System.out.println(op4ti);					


					if(qimage==false)
					{

						if((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;
						}
						else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(img)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(img)))
						{
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option2; getImageFileName(); option2 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
						{
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(img)))
						{
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option3; getImageFileName(); option3 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;								

						}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
						{
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");

						}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
						{
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
						{
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");
							FullURL = option3; getImageFileName(); option3 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
						{
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");
							FullURL = option2; getImageFileName(); option2 = FullURL;

						}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");	
							FullURL = option1; getImageFileName(); option1 = FullURL;
						}
					}
					if(qimage==true)
					{


						if((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[4]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;
						}
						else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(img)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[4]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[4]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(img)))
						{
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[4]").getAttribute("src");
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[1]").getAttribute("src");								
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option2; getImageFileName(); option2 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
						{
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(img)))
						{
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							FullURL = option3; getImageFileName(); option3 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							FullURL = option1; getImageFileName(); option1 = FullURL;
							FullURL = option3; getImageFileName(); option3 = FullURL;								

						}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
						{
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");

						}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");								
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[3]").getAttribute("src");
							FullURL = option2; getImageFileName(); option2 = FullURL;
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
						{
							option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option4; getImageFileName(); option4 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
						{
							option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option3; getImageFileName(); option3 = FullURL;

						}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
						{
							option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");
							FullURL = option2; getImageFileName(); option2 = FullURL;

						}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
						{
							option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+(start+1)+"]/img[2]").getAttribute("src");	
							FullURL = option1; getImageFileName(); option1 = FullURL;
						}

					}

				}
			}
		}

		if((option1.length()!=0)&&(option2.length()!=0)&&(option3.length()!=0)&&(option4.length()!=0)&&(option4.length()!=0))
		{

			boolean optionEndSpecial;			
			int stop;
			String specialString = "";
			if (optionFive==true)
			{
				stop= 4;
			} 
			else 
			{
				stop = 5;
			}
			for (int i = 1; i <= 4; i++) {
				switch (i)
				{
				case 1: specialString = option1; break;
				case 2: specialString = option2; break;
				case 3: specialString = option3; break;
				case 4: specialString = option4; break;
				case 5: specialString = option5; break;
				}

				for (int j = 1; j <= 6; j++) {
					optionEndSpecial = false;				
					optionEndSpecial = String.valueOf(specialString.charAt(specialString.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption = new StringBuilder(specialString);
						specialString = Newoption.deleteCharAt(specialString.length() - 1).toString();
						switch (i)
						{
						case 1: option1 = specialString; 
						break;
						case 2: option2 = specialString; 
						break;
						case 3: option3 = specialString; 
						break;
						case 4: option4 = specialString; 
						break;
						case 5: option5 = specialString; 
						break;
						}						
					}			
				}
			}
		}

		optionText[0]=option1;
		optionText[1]=option2;
		optionText[2]=option3;
		optionText[3]=option4;
		if (optionFive==true)
		{
			optionText[4]=option5;
		}

		return optionText; 		
	}



	public String[] getOptionsCS(String QID) {


		QIDint = Integer.parseInt(QID);			
		QIDint = QIDint - QNOint;
		String QID4CS = Integer.toString(QIDint);

		optionText[0]=""; optionText[1]=""; optionText[2]=""; optionText[3]="";

		//To check if the Question is image based.
		try {
			fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img");
			ImageCheck = true;
		} catch (NoSuchElementException e) {
			ImageCheck = false;				
		}


		String Options = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]").getText();
		String option1 ="";String option2 ="";String option3 ="";String option4 ="", option5 ="";
		Options = new StringBuffer(Options).replace(0, 8, "").toString();

		if (ImageCheck==false)
		{
			int Op1Start=0, Op1end=0, Op2Start=0, Op2end=0, Op3Start=0, Op3end=0, Op4Start=0, Op4end=0, Op5Start=0, Op5end=0; 

			//Getting Option start position	
			for(int i=0; i<Options.length(); i++)
			{
				if(Op1Start==0)
				{
					if(Options.charAt(i)=='1')
					{
						if(Options.charAt(i+1)==')')
						{
							if(Options.charAt(i+2)==' ')
							{


								{
									Op1Start = i+3;								
								}

							}
						}
					}}

				if(Op2Start==0)
				{
					if(Options.charAt(i)=='2')
					{
						if(Options.charAt(i+1)==')')
						{
							if(Options.charAt(i+2)==' ')
							{													


								{
									Op2Start = i+3;
								}

							}
						}								
					}
				}

				if(Op3Start==0)
				{
					if(Options.charAt(i)=='3')
					{
						if(Options.charAt(i+1)==')')
						{
							if(Options.charAt(i+2)==' ')
							{


								{
									Op3Start = i+3;
								}

							}
						}								


					}
				}

				if(Op4Start==0)
				{
					if(Options.charAt(i)=='4')
					{
						if(Options.charAt(i+1)==')')
						{
							if(Options.charAt(i+2)==' ')
							{


								{
									Op4Start = i+3;	

									if (optionFive==false)
									{
										break;
									}
								}
							}
						}
					}
				}

				if (optionFive==true)
				{
					if(Op5Start==0)
					{
						if(Options.charAt(i)=='5')
						{
							if(Options.charAt(i+1)==')')
							{
								if(Options.charAt(i+2)==' ')
								{


									{
										Op5Start = i+3;
										break;
									}
								}
							}
						}
					}
				}
			}		


			//Getting Option end position
			if (optionFive==true)
			{
				Op1end=Op2Start-4;
				Op2end=Op3Start-4;
				Op3end=Op4Start-4;
				Op4end=Op5Start-4;				
				Op5end=Options.length();
			}
			else
			{
				Op1end=Op2Start-4;
				Op2end=Op3Start-4;
				Op3end=Op4Start-4;
				Op4end=Options.length();
			}

			//Verifying start and end position of options

			/*{
			System.out.println(Op1Start);
			System.out.println(Op2Start);
			System.out.println(Op3Start);
			System.out.println(Op4Start);	
			if (optionFive==true)
			{
				System.out.println(Op5Start);
			}
			System.out.println(Op1end);
			System.out.println(Op2end);
			System.out.println(Op3end);
			System.out.println(Op4end);
			if (optionFive==true)
			{
				System.out.println(Op5end);
			}
		}*/

			//Getting Separate option text
			option1=Options.substring(Op1Start, Op1end);
			option2=Options.substring(Op2Start, Op2end);
			option3=Options.substring(Op3Start, Op3end);
			option4=Options.substring(Op4Start, Op4end);
			if (optionFive==true)
			{
				option5=Options.substring(Op5Start, Op5end);
			}

		}
		if (ImageCheck==true)
		{			
			mixture = false;
			List<WebElement> numberOfImageList = fdriver.findElementsByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img");				

			if(numberOfImageList.size()<4)
			{
				mixture = true;
			}			


			if(mixture==false)				
			{
				option1=fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");
				getImageFileName(option1); option1 = ImgName;

				option2=fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
				getImageFileName(option2); option2 = ImgName;

				option3=fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[3]").getAttribute("src");
				getImageFileName(option3); option3 = ImgName;

				option4=fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[4]").getAttribute("src");
				getImageFileName(option4); option4 = ImgName;
			}
			else
			{

				int op1count=0, op2count=0, op3count=0, op4count=0, op5count=0;
				String op1ti, op2ti, op3ti, op4ti, op5ti, optxt = "text", opim = "image", img = "image", txt = "text";

				getOptionTextCS();
				option1 = optionText[0];
				option2 = optionText[1];
				option3 = optionText[2];
				option4 = optionText[3];
				if (optionFive==true)
				{
					option5 = optionText[4];
				}


				for(int i=0; i<option1.length(); i++)
				{
					boolean op1 = String.valueOf(option1.charAt(i)).matches("[A-Za-z0-9]");
					if (op1==true) op1count++;
				}
				if (op1count==0)
				{
					op1ti = "image";
				}
				else
				{
					op1ti = "text";
				}

				for(int i=0; i<option2.length(); i++)
				{
					boolean op2 = String.valueOf(option2.charAt(i)).matches("[A-Za-z0-9]");
					if (op2==true) op2count++;
				}
				if (op2count==0)
				{
					op2ti = "image";
				}
				else
				{
					op2ti = "text";
				}

				for(int i=0; i<option3.length(); i++)
				{
					boolean op3 = String.valueOf(option3.charAt(i)).matches("[A-Za-z0-9]");
					if (op3==true) op3count++;
				}
				if (op3count==0)
				{
					op3ti = "image";
				}
				else
				{
					op3ti = "text";
				}

				for(int i=0; i<option4.length(); i++)
				{
					boolean op4 = String.valueOf(option4.charAt(i)).matches("[A-Za-z0-9]");
					if (op4==true) op4count++;
				}
				if (op4count==0)
				{
					op4ti = "image";
				}
				else
				{
					op4ti = "text";
				}

				if (optionFive==true)
				{
					for(int i=0; i<option5.length(); i++)
					{
						boolean op5 = String.valueOf(option5.charAt(i)).matches("[A-Za-z0-9]");
						if (op5==true) op5count++;
					}
					if (op5count==0)
					{
						op5ti = "image";
					}
					else
					{
						op5ti = "text";
					}
				}

				//System.out.println(op1ti);
				//System.out.println(op2ti);
				//System.out.println(op3ti);
				//System.out.println(op4ti);					



				if((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
				{
					option1 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option2 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					option3 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[3]").getAttribute("src");
					FullURL = option1; getImageFileName(); option1 = FullURL;
					FullURL = option2; getImageFileName(); option2 = FullURL;
					FullURL = option3; getImageFileName(); option3 = FullURL;
				}
				else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(img)))
				{
					option1 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option3 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					option4 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[3]").getAttribute("src");
					FullURL = option1; getImageFileName(); option1 = FullURL;
					FullURL = option3; getImageFileName(); option3 = FullURL;
					FullURL = option4; getImageFileName(); option4 = FullURL;

				}else if ((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
				{
					option1 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option2 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					option4 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[3]").getAttribute("src");
					FullURL = option1; getImageFileName(); option1 = FullURL;
					FullURL = option2; getImageFileName(); option2 = FullURL;
					FullURL = option4; getImageFileName(); option4 = FullURL;

				}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(img)))
				{
					option2 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option3 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					option4 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[3]").getAttribute("src");
					FullURL = option2; getImageFileName(); option2 = FullURL;
					FullURL = option3; getImageFileName(); option3 = FullURL;
					FullURL = option4; getImageFileName(); option4 = FullURL;

				}else if ((op1ti.equals(img))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
				{
					option1 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option2 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					FullURL = option1; getImageFileName(); option1 = FullURL;
					FullURL = option2; getImageFileName(); option2 = FullURL;

				}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
				{
					option2 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option3 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					FullURL = option2; getImageFileName(); option2 = FullURL;
					FullURL = option3; getImageFileName(); option3 = FullURL;

				}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(img)))
				{
					option3 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option4 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					FullURL = option3; getImageFileName(); option3 = FullURL;
					FullURL = option4; getImageFileName(); option4 = FullURL;

				}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
				{
					option1 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option3 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					FullURL = option1; getImageFileName(); option1 = FullURL;
					FullURL = option3; getImageFileName(); option3 = FullURL;								

				}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
				{
					option2 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option4 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");

				}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
				{
					option1 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");								
					option4 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[2]").getAttribute("src");
					FullURL = option2; getImageFileName(); option2 = FullURL;
					FullURL = option4; getImageFileName(); option4 = FullURL;

				}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(img)))
				{
					option4 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");
					FullURL = option4; getImageFileName(); option4 = FullURL;

				}else if ((op1ti.equals(txt))&&(op2ti.equals(txt))&&(op3ti.equals(img))&&(op4ti.equals(txt)))
				{
					option3 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");
					FullURL = option3; getImageFileName(); option3 = FullURL;

				}else if ((op1ti.equals(txt))&&(op2ti.equals(img))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
				{
					option2 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");
					FullURL = option2; getImageFileName(); option2 = FullURL;

				}else if ((op1ti.equals(img))&&(op2ti.equals(txt))&&(op3ti.equals(txt))&&(op4ti.equals(txt)))
				{
					option1 = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+QID4CS+"]/img[1]").getAttribute("src");	
					FullURL = option1; getImageFileName(); option1 = FullURL;
				}						


			}



		}



		if((option1.length()!=0)&&(option2.length()!=0)&&(option3.length()!=0)&&(option4.length()!=0)&&(option4.length()!=0))
		{

			boolean optionEndSpecial, optionStartSpecial;			
			int stop;
			String specialString = "";
			if (optionFive==true)
			{
				stop= 4;
			} 
			else 
			{
				stop = 5;
			}
			for (int i = 1; i <= 4; i++) {
				switch (i)
				{
				case 1: specialString = option1; break;
				case 2: specialString = option2; break;
				case 3: specialString = option3; break;
				case 4: specialString = option4; break;
				case 5: specialString = option5; break;
				}

				for (int j = 1; j <= 6; j++) {
					optionEndSpecial = false;
					optionStartSpecial = false;
					optionEndSpecial = String.valueOf(specialString.charAt(specialString.length() - 1)).matches("\\s");
					optionStartSpecial = String.valueOf(specialString.charAt(0)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption = new StringBuilder(specialString);
						specialString = Newoption.deleteCharAt(specialString.length() - 1).toString();
						switch (i)
						{
						case 1: option1 = specialString; 
						break;
						case 2: option2 = specialString; 
						break;
						case 3: option3 = specialString; 
						break;
						case 4: option4 = specialString; 
						break;
						case 5: option5 = specialString; 
						break;
						}						
					}
					if (optionStartSpecial == true) {
						StringBuilder Newoption = new StringBuilder(specialString);
						specialString = Newoption.deleteCharAt(0).toString();
						switch (i)
						{
						case 1: option1 = specialString; 
						break;
						case 2: option2 = specialString; 
						break;
						case 3: option3 = specialString; 
						break;
						case 4: option4 = specialString; 
						break;
						case 5: option5 = specialString; 
						break;
						}						
					}	
				}
			}
		}

		optionText[0]=option1;
		optionText[1]=option2;
		optionText[2]=option3;
		optionText[3]=option4;
		if (optionFive==true)
		{
			optionText[4]=option5;
		}

		return optionText; 		
	}



	public String getImageFileName(String FullURL) {
		int slash = 0;
		for(slash = FullURL.length()-1; slash>=0; slash--)
		{
			if(FullURL.charAt(slash)=='/')
			{
				break;
			}
		}
		ImgName = new StringBuffer(FullURL).replace(0, slash, "").toString();
		return ImgName;
	}	



	public String[] getOptionTextAT(String OptionTxt) {


		String option1 ="";String option2 ="";String option3 ="";String option4 ="", option5 ="";		
		String Options = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]").getText();
		Options = new StringBuffer(Options).replace(0, 8, "").toString();



		int Op1Start=0, Op1end=0, Op2Start=0, Op2end=0, Op3Start=0, Op3end=0, Op4Start=0, Op4end=0, Op5Start=0, Op5end=0; 

		//Getting Option start position	
		for(int i=0; i<Options.length(); i++)
		{
			if(Op1Start==0)
			{
				if(Options.charAt(i)=='(')
				{
					if(Options.charAt(i+1)=='A')
					{
						if(Options.charAt(i+2)==')')
						{
							if((Options.charAt(i+3)==' ')||(String.valueOf(Options.charAt(i+3)).matches("\\s")))

							{
								Op1Start = i+4;								
							}

						}
					}
				}}

			if(Op2Start==0)
			{
				if(Options.charAt(i)=='(')
				{
					if(Options.charAt(i+1)=='B')
					{
						if(Options.charAt(i+2)==')')
						{													
							if(Options.charAt(i+3)==' ')

							{
								Op2Start = i+4;
							}

						}
					}								
				}
			}

			if(Op3Start==0)
			{
				if(Options.charAt(i)=='(')
				{
					if(Options.charAt(i+1)=='C')
					{
						if(Options.charAt(i+2)==')')
						{
							if(Options.charAt(i+3)==' ')

							{
								Op3Start = i+4;
							}

						}
					}								


				}
			}

			if(Op4Start==0)
			{
				if(Options.charAt(i)=='(')
				{
					if(Options.charAt(i+1)=='D')
					{
						if(Options.charAt(i+2)==')')
						{
							if(Options.charAt(i+3)==' ')												

							{
								Op4Start = i+4;	

								if (optionFive==false)
								{
									break;
								}
							}
						}
					}
				}
			}

			if (optionFive==true)
			{
				if(Op5Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='E')
						{
							if(Options.charAt(i+2)==')')
							{
								if(Options.charAt(i+3)==' ')												

								{
									Op5Start = i+4;
									break;
								}
							}
						}
					}
				}
			}
		}		


		//Getting Option end position
		if (optionFive==true)
		{
			Op1end=Op2Start-4;
			Op2end=Op3Start-4;
			Op3end=Op4Start-4;
			Op4end=Op5Start-4;				
			Op5end=Options.length();
		}
		else
		{
			Op1end=Op2Start-4;
			Op2end=Op3Start-4;
			Op3end=Op4Start-4;
			Op4end=Options.length();
		}



		//Getting Separate option text
		option1=Options.substring(Op1Start, Op1end);
		option2=Options.substring(Op2Start, Op2end);
		option3=Options.substring(Op3Start, Op3end);
		option4=Options.substring(Op4Start, Op4end);
		if (optionFive==true)
		{
			option5=Options.substring(Op5Start, Op5end);
		}


		optionText[0]=option1;
		optionText[1]=option2;
		optionText[2]=option3;
		optionText[3]=option4;
		if (optionFive==true)
		{
			optionText[4]=option5;
		}	

		return optionText;

	}


	public String[] getOptionTextMQP(String Options) {


		String option1 ="";String option2 ="";String option3 ="";String option4 ="", option5 ="";	



		int Op1Start=0, Op1end=0, Op2Start=0, Op2end=0, Op3Start=0, Op3end=0, Op4Start=0, Op4end=0, Op5Start=0, Op5end=0; 

		//Getting Option start position	
		for(int i=0; i<Options.length(); i++)
		{
			if(Op1Start==0)
			{
				if(Options.charAt(i)=='(')
				{
					if(Options.charAt(i+1)=='A')
					{
						if(Options.charAt(i+2)==')')
						{
							if((Options.charAt(i+3)==' ')||(String.valueOf(Options.charAt(i+3)).matches("\\s")))

							{
								Op1Start = i+4;								
							}

						}
					}
				}}

			if(Op2Start==0)
			{
				if(Options.charAt(i)=='(')
				{
					if(Options.charAt(i+1)=='B')
					{
						if(Options.charAt(i+2)==')')
						{													
							if(Options.charAt(i+3)==' ')

							{
								Op2Start = i+4;
							}

						}
					}								
				}
			}

			if(Op3Start==0)
			{
				if(Options.charAt(i)=='(')
				{
					if(Options.charAt(i+1)=='C')
					{
						if(Options.charAt(i+2)==')')
						{
							if(Options.charAt(i+3)==' ')

							{
								Op3Start = i+4;
							}

						}
					}								


				}
			}

			if(Op4Start==0)
			{
				if(Options.charAt(i)=='(')
				{
					if(Options.charAt(i+1)=='D')
					{
						if(Options.charAt(i+2)==')')
						{
							if(Options.charAt(i+3)==' ')												

							{
								Op4Start = i+4;	

								if (optionFive==false)
								{
									break;
								}
							}
						}
					}
				}
			}

			if (optionFive==true)
			{
				if(Op5Start==0)
				{
					if(Options.charAt(i)=='(')
					{
						if(Options.charAt(i+1)=='E')
						{
							if(Options.charAt(i+2)==')')
							{
								if(Options.charAt(i+3)==' ')												

								{
									Op5Start = i+4;
									break;
								}
							}
						}
					}
				}
			}
		}		


		//Getting Option end position
		if (optionFive==true)
		{
			Op1end=Op2Start-4;
			Op2end=Op3Start-4;
			Op3end=Op4Start-4;
			Op4end=Op5Start-4;				
			Op5end=Options.length();
		}
		else
		{
			Op1end=Op2Start-4;
			Op2end=Op3Start-4;
			Op3end=Op4Start-4;
			Op4end=Options.length();
		}



		//Getting Separate option text
		option1=Options.substring(Op1Start, Op1end);
		option2=Options.substring(Op2Start, Op2end);
		option3=Options.substring(Op3Start, Op3end);
		option4=Options.substring(Op4Start, Op4end);
		if (optionFive==true)
		{
			option5=Options.substring(Op5Start, Op5end);
		}


		optionText[0]=option1;
		optionText[1]=option2;
		optionText[2]=option3;
		optionText[3]=option4;
		if (optionFive==true)
		{
			optionText[4]=option5;
		}	

		return optionText;

	}


	public String[] getOptionTextCS() {



		String option1 ="";String option2 ="";String option3 ="";String option4 ="", option5 ="";		
		String Options = fdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]").getText();
		Options = new StringBuffer(Options).replace(0, 8, "").toString();




		int Op1Start=0, Op1end=0, Op2Start=0, Op2end=0, Op3Start=0, Op3end=0, Op4Start=0, Op4end=0, Op5Start=0, Op5end=0; 

		//Getting Option start position	
		for(int i=0; i<Options.length(); i++)
		{
			if(Op1Start==0)
			{
				if(Options.charAt(i)=='1')
				{

					if(Options.charAt(i+1)==')')
					{
						if(Options.charAt(i+2)==' ')
						{
							if(Options.charAt(i+3)==' ')

							{
								Op1Start = i+4;
							}

						}
					}
				}}

			if(Op2Start==0)
			{
				if(Options.charAt(i)=='2')
				{
					if(Options.charAt(i+1)==')')
					{
						if(Options.charAt(i+2)==' ')
						{													
							if(Options.charAt(i+3)==' ')

							{
								Op2Start = i+4;
							}

						}
					}								
				}
			}

			if(Op3Start==0)
			{
				if(Options.charAt(i)=='3')
				{
					if(Options.charAt(i+1)==')')
					{
						if(Options.charAt(i+2)==' ')
						{
							if(Options.charAt(i+3)==' ')

							{
								Op3Start = i+4;
							}

						}
					}								


				}
			}

			if(Op4Start==0)
			{
				if(Options.charAt(i)=='4')
				{
					if(Options.charAt(i+1)==')')
					{
						if(Options.charAt(i+2)==' ')
						{
							if(Options.charAt(i+3)==' ')												

							{
								Op4Start = i+4;	

								if (optionFive==true)
								{
									break;
								}
							}
						}
					}
				}
			}

			if (optionFive==true)
			{
				if(Op5Start==0)
				{
					if(Options.charAt(i)=='5')
					{
						if(Options.charAt(i+1)==')')
						{
							if(Options.charAt(i+2)==' ')
							{
								if(Options.charAt(i+3)==' ')												

								{
									Op5Start = i+4;
									break;
								}
							}
						}
					}
				}
			}
		}		


		//Getting Option end position
		if (optionFive==true)
		{
			Op1end=Op2Start-4;
			Op2end=Op3Start-4;
			Op3end=Op4Start-4;
			Op4end=Op5Start-4;				
			Op5end=Options.length();
		}
		else
		{
			Op1end=Op2Start-4;
			Op2end=Op3Start-4;
			Op3end=Op4Start-4;
			Op4end=Options.length();
		}



		//Getting Separate option text
		option1=Options.substring(Op1Start, Op1end);
		option2=Options.substring(Op2Start, Op2end);
		option3=Options.substring(Op3Start, Op3end);
		option4=Options.substring(Op4Start, Op4end);
		if (optionFive==true)
		{
			option5=Options.substring(Op5Start, Op5end);
		}


		optionText[0]=option1;
		optionText[1]=option2;
		optionText[2]=option3;
		optionText[3]=option4;
		if (optionFive==true)
		{
			optionText[4]=option5;
		}	

		return optionText; 




	}



	public String getImageFileName() {
		int slash = 0;
		for(slash = FullURL.length()-1; slash>=0; slash--)
		{
			if(FullURL.charAt(slash)=='/')
			{
				break;
			}
		}
		FullURL = new StringBuffer(FullURL).replace(0, slash, "").toString();
		return FullURL;
	}





	public void openMasterQPChrome(String ExcelURL, String SubjectNum) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		cdriver = new ChromeDriver();	
		cdriver.manage().window().maximize();
		cdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		cdriver.get(ExcelURL);
		Select SelectSubjectCode = new Select (cdriver.findElementById("subjectcode"));
		SelectSubjectCode.selectByVisibleText(SubjectNum);

		AvailableQuestionsMQP = cdriver.findElements(By.tagName("tr"));
		AvailableQuestionMasterQP = AvailableQuestionsMQP.size()-1;

		StringAvailableQuestionMasterQP = Integer.toString(AvailableQuestionMasterQP);


	}	


	public void getCorOptCS() {

		ccCorOptCS = "";
		String Benefit = "No Correct Answer ( Benefit to all )";		

		if(ccCorAnsCS.equals(ccOption1CS))
		{
			ccCorOptCS = "A";
		}
		else if(ccCorAnsCS.contains(ccOption1CS))
		{
			ccCorOptCS = "Passed with Contains condition. May be more then one correct answer. Pls check and Confirm.";
		}
		else if(ccCorAnsCS.equals(ccOption2CS))
		{
			ccCorOptCS = "B";
		}
		else if(ccCorAnsCS.contains(ccOption2CS))
		{
			ccCorOptCS = "Passed with Contains condition. May be more then one correct answer. Pls check and Confirm.";
		}
		else if(ccCorAnsCS.equals(ccOption3CS))
		{
			ccCorOptCS = "C";
		}
		else if(ccCorAnsCS.contains(ccOption3CS))
		{
			ccCorOptCS = "Passed with Contains condition. May be more then one correct answer. Pls check and Confirm.";
		}
		else if(ccCorAnsCS.equals(ccOption4CS))
		{
			ccCorOptCS = "D";
		}
		else if(ccCorAnsCS.contains(ccOption4CS))
		{
			ccCorOptCS = "Passed with Contains condition. May be more then one correct answer. Pls check and Confirm.";
		}
		else if(ccCorAnsCS.equals(Benefit))
		{
			ccCorOptCS = "NO CORRECT ANSWER";
		}	

	}

	public void getCorOptMQP() {

		ccCorOptMQP = "";
		String ExcelKey = cdriver.findElementByXPath("//tbody/tr["+(start+1)+"]/td[5]").getText();	
		
		

		String empty = "";					
		if (ExcelKey.equals(empty))
		{
			ccCorOptMQP = "EMPTY";
		}

		if(ExcelKey.contains("&"))
		{
			ccCorOptMQP = ExcelKey;		
		}
		else
		{
			ccCorOptMQP = ccCorOptMQP + ExcelKey.charAt(1);
		}

	}




	public long takeSnap() {

		return 0;
	}



	//----------------------------------------------------------------------------------------------------------------------------




	public void completeCompareACS(String RollNo) throws IOException{		

		System.out.println("Test Results for "+RollNo+":");
		System.out.println();
		for(start=1; start<=AvailableQuestionsPE.size(); start++)		
		{			

			getFullTextAT(start);			

			getQID(FullTextAT);
			ccQID = QID;

			getQuestionTextAT(start);
			getQuestionTextCS(QID);			

			getCorCanAnsCS(QID);
			ccCorAnsCS = CorCandCS[0];
			ccCanAnsCS = CorCandCS[1];

			getCorCanAnsAT(FullTextAT);
			ccCorAnsAT = CorCandAT[0];
			ccCanAnsAT = CorCandAT[1];

			getOptionsAT(FullTextAT);			
			ccOption1AT = optionText[0];
			ccOption2AT = optionText[1];
			ccOption3AT = optionText[2];
			ccOption4AT = optionText[3];

			getOptionsCS(QID);			
			ccOption1CS = optionText[0];
			ccOption2CS = optionText[1];
			ccOption3CS = optionText[2];
			ccOption4CS = optionText[3];			


			//ConvertoToCorrectQID(QID, QNOint);

			String FinalStatus="";
			int equalCount = 0;

			String[] arrayAT = new String[7];
			String[] arrayCS = new String[7];

			arrayAT[0]=ccQuestionTextAT;
			arrayAT[1]=ccOption1AT;
			arrayAT[2]=ccOption2AT;
			arrayAT[3]=ccOption3AT;
			arrayAT[4]=ccOption4AT;
			arrayAT[5]=ccCanAnsAT;
			arrayAT[6]=ccCorAnsAT;

			arrayCS[0]=ccQuestionTextCS;
			arrayCS[1]=ccOption1CS;
			arrayCS[2]=ccOption2CS;
			arrayCS[3]=ccOption3CS;
			arrayCS[4]=ccOption4CS;
			arrayCS[5]=ccCanAnsCS;
			arrayCS[6]=ccCorAnsCS;			

			for(int i=0; i<=6; i++)
			{
				if(arrayAT[i].equals(arrayCS[i]))
				{
					equalCount++;
				}
			}

			if(equalCount==7)
			{
				FinalStatus = "PASS";
			}
			else
			{
				FinalStatus = "FAIL";
			}			

			System.out.println(QID+": "+FinalStatus);
			WriteInExcelACScc(RollNo, ccQID, ccQuestionTextAT, ccQuestionTextCS, ccOption1AT, ccOption1CS, ccOption2AT, ccOption2CS, 
					ccOption3AT, ccOption3CS, ccOption4AT, ccOption4CS, ccCanAnsAT, ccCanAnsCS, ccCorAnsAT, ccCorAnsCS, FinalStatus);

		}cdriver.quit(); fdriver.quit();

	}



	public void completeCompareCSM(String RollNo) throws IOException{		


		System.out.println("Test Results for "+RollNo+":");
		System.out.println();
		for(start=1; start<=AvailableQuestionsCS.size(); start++)		
		{
			qimage = false; mixture = false; ImageCheck = false;

			getQIDCS(start);
			getQIDMQP(start);

			if(QIDCS.equals(QIDMaster))
			{
				ccQID = QIDCS;
				QID = QIDCS;
			}


			getFullTextMQP(start);	


			getQuestionTextCS(QID);		
			getQuestionTextMQP(FullTextMQP);

			getCorCanAnsCS(QID);
			ccCorAnsCS = CorCandCS[0];
			ccCanAnsCS = CorCandCS[1];


			getOptionsCS(QID);			
			ccOption1CS = optionText[0];
			ccOption2CS = optionText[1];
			ccOption3CS = optionText[2];
			ccOption4CS = optionText[3];

			getCorOptCS();

			getOptionsMQP(FullTextMQP);			
			ccOption1MQP = optionText[0];
			ccOption2MQP = optionText[1];
			ccOption3MQP = optionText[2];
			ccOption4MQP = optionText[3];

			getCorOptMQP();

			String FinalStatus="";
			int equalCount = 0;

			String[] arrayAT = new String[6];
			String[] arrayCS = new String[6];

			arrayAT[0]=ccQuestionTextMQP;
			arrayAT[1]=ccOption1MQP;
			arrayAT[2]=ccOption2MQP;
			arrayAT[3]=ccOption3MQP;
			arrayAT[4]=ccOption4MQP;
			arrayAT[5]=ccCorOptMQP;


			arrayCS[0]=ccQuestionTextCS;
			arrayCS[1]=ccOption1CS;
			arrayCS[2]=ccOption2CS;
			arrayCS[3]=ccOption3CS;
			arrayCS[4]=ccOption4CS;
			arrayCS[5]=ccCorOptCS;


			for(int i=0; i<=5; i++)
			{
				if(arrayAT[i].equals(arrayCS[i]))
				{
					equalCount++;
				}
			}

			if(equalCount==6)
			{
				FinalStatus = "PASS";
			}
			else
			{
				FinalStatus = "FAIL";
			}			

			System.out.println(QID+": "+FinalStatus);
			WriteInExcelCSMcc(RollNo, ccQID, ccQuestionTextMQP, ccQuestionTextCS, ccOption1MQP, ccOption1CS, ccOption2MQP, ccOption2CS, 
					ccOption3MQP, ccOption3CS, ccOption4MQP, ccOption4CS, ccCorAnsCS, ccCorOptCS, ccCorOptMQP, FinalStatus);

		}cdriver.quit(); fdriver.quit();

	}



	public String getQIDCS(int start) {
		QIDCS = fdriver.findElementByXPath("(//b[starts-with(text(),'Q. No.')])["+start+"]").getText();
		return QIDCS = QIDCS.replaceAll("[^0-9]", "");
	}


	public String getQIDMQP(int start) {
		QIDMaster = cdriver.findElementByXPath("//tbody/tr["+(start+1)+"]/td[3]").getText();
		return QIDMaster;
	}


}
