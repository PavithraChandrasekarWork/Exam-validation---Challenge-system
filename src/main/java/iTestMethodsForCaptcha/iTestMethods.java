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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
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
public class iTestMethods extends Reporter{


	public RemoteWebDriver driver;
	public ChromeDriver cdriver;
	public FirefoxDriver fdriver;
	public WebDriver pdriver;
	public boolean testOutput = false, optionFive = false;
	public FileWriter fw = null;
	public List<WebElement> LinkList=null;
	public List<WebElement> ActiveLinks=null;
	public int start, AvailableQuestionMasterQP;
	public boolean fdrivermultiple = false;
	public boolean cdrivermultiple = false;
	public boolean mixture;
	public String StringAvailableQuestionMasterQP, StringAvailableQuestionsCS, StringAvailableQuestionsPE;

	public List<WebElement> AvailableQuestionsPE, AvailableQuestionsCS, AvailableQuestionsMQP;
	public String RollNo = null, SubjectLink = null;
	public int QNOint;
	public BufferedWriter bw = null;

	public String sUrl,primaryWindowHandle,sHubUrl,sHubPort;


	public  XSSFWorkbook workbook;
	public XSSFSheet sheet;	
	public int rowCount = 0;


	public iTestMethods() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./src/main/resources/config.properties")));
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			sUrl = prop.getProperty("URL");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	//---------------------------------------------------------------------------------------------------------------------------------------------------

	public void openMasterQPFF(String ExcelURL, String SubjectNum) throws InterruptedException {

		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver_32bit.exe");
		fdriver = new FirefoxDriver();		
		fdriver.manage().window().maximize();
		fdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		fdriver.get(ExcelURL);
		Select SelectSubjectCode = new Select (fdriver.findElementById("subjectcode"));
		SelectSubjectCode.selectByVisibleText(SubjectNum);


		AvailableQuestionsMQP();


	}	


	public void getAvailableQuestionsCS() {
		AvailableQuestionsCS = cdriver.findElementsByXPath("((//b[text()='Correct Answer:'])/..)");

		if (testOutput == true)
		{
			System.out.println("AvailableQuestions: "+AvailableQuestionsCS.size());
		}
	}



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

		SubjectLink = new StringBuffer(SubjectLink).replace(10, SubjectLink.length(), "").toString();



		return SubjectLink;	

	}


	public void startChallengeSystemFF(String ChallengeURL, String RollNo, String Pass, String ChallengeExamDate) throws InterruptedException {

		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver_32bit.exe");
		fdriver = new FirefoxDriver();
		fdriver.manage().window().maximize();
		fdriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		fdriver.get(ChallengeURL);
		fdriver.findElementById("txtregno").sendKeys(RollNo);
		if(Pass.length()!=8)
		{
			Pass = 0+Pass;
		}		
		fdriver.findElementById("txtpass").sendKeys(Pass);
		Select examDate = new Select(fdriver.findElementById("exam_date"));
		examDate.selectByVisibleText(ChallengeExamDate);
		//Thread.sleep(10000);
		fdriver.findElementByXPath("//input[@class='button']").click();
		Thread.sleep(2000);		
		try {
			fdriver.findElementByXPath("//input[@value='logout']");
		} catch (NoSuchElementException e) {
			fdriver.findElementByXPath("//a[contains(text(), '"+SubjectLink+"')]").click();
			Thread.sleep(2000);
			Set<String> allwindow = fdriver.getWindowHandles();
			List<String> windowlist = new ArrayList<String>();
			windowlist.addAll(allwindow);
			String focus = windowlist.get(1);
			fdriver.switchTo().window(focus);
			fdrivermultiple = true;
		}	

		getFirstQIDinCS();

	}


	public void startChallengeSystemChrome(String ChallengeURL, String RollNo, String Pass, String ChallengeExamDate) throws InterruptedException {

		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver_32bit.exe");
		cdriver = new ChromeDriver();	
		cdriver.manage().window().maximize();
		cdriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		cdriver.get(ChallengeURL);
		cdriver.findElementById("txtregno").sendKeys(RollNo);
		if(Pass.length()!=8)
		{
			Pass = 0+Pass;
		}	
		cdriver.findElementById("txtpass").sendKeys(Pass);
		Select examDate = new Select(cdriver.findElementById("exam_date"));
		examDate.selectByVisibleText(ChallengeExamDate);
		//Thread.sleep(10000);
		cdriver.findElementByXPath("//input[@class='button']").click();
		Thread.sleep(2000);		
		try {
			cdriver.findElementByXPath("//input[@value='logout']");
		} catch (NoSuchElementException e) {
			cdriver.findElementByXPath("//a[contains(text(), '"+SubjectLink+"')]").click();
			Thread.sleep(6000);		
			Set<String> allwindow = cdriver.getWindowHandles();
			List<String> windowlist = new ArrayList<String>();
			windowlist.addAll(allwindow);
			String focus = windowlist.get(1);
			cdriver.switchTo().window(focus);
			cdrivermultiple = true;
		}
		AvailableQuestionsCS();
		getAvailableQuestionsCS();
	}


	public void getQuestionsViewedPE() {

		AvailableQuestionsPE = cdriver.findElementsByXPath("//td[@class='greybluetext10']");
		StringAvailableQuestionsPE = Integer.toString(AvailableQuestionsPE.size());

		//System.out.println(AvailableQuestions.size());

	}


	public void AvailableQuestionsMQP() {

		AvailableQuestionsMQP = fdriver.findElements(By.tagName("tr"));
		AvailableQuestionMasterQP = AvailableQuestionsMQP.size()-1;

		StringAvailableQuestionMasterQP = Integer.toString(AvailableQuestionMasterQP);


	}


	public void AvailableQuestionsCS() {

		AvailableQuestionsCS = cdriver.findElementsByXPath("((//b[text()='Correct Answer:'])/..)");		
		StringAvailableQuestionsCS = Integer.toString(AvailableQuestionsCS.size());

	}









	public void getFirstQIDinCS() {
		String QNO = fdriver.findElementByXPath("//b[starts-with(text(),'QID : ')]").getText();
		QNO = QNO.replaceAll("[^0-9]", "");
		QNOint =  Integer.parseInt(QNO);
		QNOint = QNOint - 1;
	}



	public void createNotepadCSM(String RollNo) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter("e:\\Results\\ChallengeSystemWithMaster\\TestResult_"+RollNo+".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw =new BufferedWriter(fw);



		System.out.println("---------------------------------------------------------------");
		System.out.println("Test Result for the Roll no: "+ RollNo);
		System.out.println("---------------------------------------------------------------");

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("Challenge System with Master");
		bw.newLine();
		bw.write("Test Result for the Roll no: "+ RollNo);
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
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


	public void compareCorrectAnswers(String RollNo) throws IOException {

		//testOutput = true;

		for(start=1; start<=AvailableQuestionsCS.size(); start++)		
		{

			// Get QID in Challenge System
			String QID = cdriver.findElementByXPath("(//b[starts-with(text(),'QID : ')])["+start+"]").getText();
			QID = QID.replaceAll("[^0-9]", "");



			// Get QID in Master Copy
			String QIDMaster = fdriver.findElementByXPath("//tbody/tr["+(start+1)+"]/td[3]").getText();	



			if (QID.equals(QIDMaster))
			{

				boolean ImageCheck = true;
				mixture=false;


				//To check if the Question is image based.
				try {
					cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img").getText();
				} catch (NoSuchElementException e) {
					ImageCheck = false;				
				}




				// If Question is not image based.
				// Get Correct Answer	


				String option1 ="";String option2 ="";String option3 ="";String option4 ="", option5 ="";
				String CorrectAnswer = "";

				if(ImageCheck==false)
				{

					CorrectAnswer = cdriver.findElementByXPath("((//b[text()='Correct Answer:'])/..)["+start+"]").getText();
					CorrectAnswer = new StringBuffer(CorrectAnswer).replace(0, 16, "").toString();
					if (testOutput == true)
					{
						System.out.println("Correct Answer is " + CorrectAnswer);
						System.out.println("CorrectAnswer length is: "+ CorrectAnswer.length());						
					}

					if(CorrectAnswer.length()!=0)
					{				

						boolean correctAnsEndSpecial;

						correctAnsEndSpecial = false;
						correctAnsEndSpecial = String.valueOf(CorrectAnswer.charAt(CorrectAnswer.length()-1)).matches("\\s");
						if (correctAnsEndSpecial==true) 
						{
							StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswer);
							CorrectAnswer = NewCorrectAnswer.deleteCharAt(CorrectAnswer.length()-1).toString();						
						}
						correctAnsEndSpecial = false;
						correctAnsEndSpecial = String.valueOf(CorrectAnswer.charAt(CorrectAnswer.length()-1)).matches("\\s");
						if (correctAnsEndSpecial==true) 
						{
							StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswer);
							CorrectAnswer = NewCorrectAnswer.deleteCharAt(CorrectAnswer.length()-1).toString();						
						}
					}

					if(CorrectAnswer.length()==0)
					{
						CorrectAnswer = "Correct Answer is Empty";
					}



					//System.out.println("Correct Ans length: " + CorrectAnswer.length());


					//Get Options

					String Options = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]").getText();
					Options = new StringBuffer(Options).replace(0, 8, "").toString();
					if (testOutput == true)
					{
						System.out.println("Option Text is " + Options);	
					}



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

					//Verifying start and end position of options
					if (testOutput == true)
					{
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


					//Verifying the Separate Option Text
					if (testOutput == true)
					{
						System.out.println("Option 1 is "+option1);
						System.out.println("Option 2 is "+option2);
						System.out.println("Option 3 is "+option3);
						System.out.println("Option 4 is "+option4);
						if (optionFive==true)
						{
							System.out.println("Option 5 is "+option5);
						}					
					}

					// For few Questions, the Options has an enter Key at the end which added an extra char at the end. So, deleting that char.
					boolean optionEndSpecial;

					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option1.charAt(option1.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(option1.length()-1).toString();						
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option1.charAt(option1.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(option1.length()-1).toString();						
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option1.charAt(option1.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(option1.length()-1).toString();						
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option2.charAt(option2.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption2 = new StringBuilder(option2);
						option2 = Newoption2.deleteCharAt(option2.length()-1).toString();						
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option2.charAt(option2.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption2 = new StringBuilder(option2);
						option2 = Newoption2.deleteCharAt(option2.length()-1).toString();						
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option2.charAt(option2.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption2 = new StringBuilder(option2);
						option2 = Newoption2.deleteCharAt(option2.length()-1).toString();						
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option3.charAt(option3.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption3 = new StringBuilder(option3);
						option3 = Newoption3.deleteCharAt(option3.length()-1).toString();

					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option3.charAt(option3.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption3 = new StringBuilder(option3);
						option3 = Newoption3.deleteCharAt(option3.length()-1).toString();

					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option3.charAt(option3.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption3 = new StringBuilder(option3);
						option3 = Newoption3.deleteCharAt(option3.length()-1).toString();

					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option4.charAt(option4.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption4 = new StringBuilder(option4);
						option4 = Newoption4.deleteCharAt(option4.length()-1).toString();						
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option4.charAt(option4.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption4 = new StringBuilder(option4);
						option4 = Newoption4.deleteCharAt(option4.length()-1).toString();						
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option4.charAt(option4.length()-1)).matches("\\s");
					if (optionEndSpecial==true) 
					{
						StringBuilder Newoption4 = new StringBuilder(option4);
						option4 = Newoption4.deleteCharAt(option4.length()-1).toString();						
					}

					if (optionFive==true)
					{
						optionEndSpecial = false;
						optionEndSpecial = String.valueOf(option5.charAt(option5.length()-1)).matches("\\s");
						if (optionEndSpecial==true) 
						{
							StringBuilder Newoption5 = new StringBuilder(option5);
							option5 = Newoption5.deleteCharAt(option5.length()-1).toString();						
						}
						optionEndSpecial = false;
						optionEndSpecial = String.valueOf(option5.charAt(option5.length()-1)).matches("\\s");
						if (optionEndSpecial==true) 
						{
							StringBuilder Newoption5 = new StringBuilder(option5);
							option5 = Newoption5.deleteCharAt(option5.length()-1).toString();						
						}
						optionEndSpecial = false;
						optionEndSpecial = String.valueOf(option5.charAt(option5.length()-1)).matches("\\s");
						if (optionEndSpecial==true) 
						{
							StringBuilder Newoption5 = new StringBuilder(option5);
							option5 = Newoption5.deleteCharAt(option5.length()-1).toString();						
						}
					}


					if (testOutput == true)
					{
						System.out.println("Option 1 is "+option1);
						System.out.println("Option 2 is "+option2);
						System.out.println("Option 3 is "+option3);
						System.out.println("Option 4 is "+option4);
						if (optionFive==true)
						{
							System.out.println("Option 5 is "+option5);
						}					
					}


				}

				else
				{
					//If the Question is image based.
					// To get Correct Answer		
					mixture = false;	

					String NoCorrectAnsCS = "No Correct Answer ( Benefit to all )"; 
					String ImageNotDisplayedInCA = ".jpg[/img]";

					//Check if the Question is only image based or mixture of Image and Text
					try {
						if (optionFive==true)
						{
							option5 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[5]").getAttribute("src");
						}
						option4 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[4]").getAttribute("src");
						option3 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[3]").getAttribute("src");
						option2 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[2]").getAttribute("src");
						option1 = cdriver.findElementByXPath("((//b[text()='Options:'])/..)["+start+"]/img[1]").getAttribute("src");

						CorrectAnswer = cdriver.findElementByXPath("((//b[text()='Correct Answer:'])/..)["+start+"]").getText(); 

						if (CorrectAnswer.contains(NoCorrectAnsCS))
						{							
							CorrectAnswer = "No Correct Answer ( Benefit to all )";
						}
						else if (CorrectAnswer.contains(ImageNotDisplayedInCA))
						{							
							CorrectAnswer = "Image Not Displayed";
						}						
						else
						{
							CorrectAnswer = cdriver.findElementByXPath("((//b[text()='Correct Answer:'])/..)["+start+"]/img").getAttribute("src");
						}
					} catch (NoSuchElementException e) {

						mixture = true;
					}					


					if (mixture==true)
					{
						bw.write("---------------------------------------------------------------");
						bw.newLine();
						bw.write("QID: " + QID);	
						bw.newLine();
						bw.write("Question has both Images and Text. Please test it manually.");
						bw.newLine();
						bw.write("---------------------------------------------------------------");
						bw.newLine();

						System.err.println("---------------------------------------------------------------");
						System.err.println("QID: " + QID);
						System.err.println("Question has both Images and Text. Please test it manually.");
						System.err.println("---------------------------------------------------------------");

						WriteInExcelCSM(RollNo, QID, "Mixture of Image and Text", "Mixture of Image and Text", "Mixture of Image and Text", "Manual");


					}


				}






				String Benefit = "No Correct Answer ( Benefit to all )";
				String EBenefit = "NO CORRECT ANSWER";



				// Get Answer option from Master

				String ExcelKey = fdriver.findElementByXPath("//tbody/tr["+(start+1)+"]/td[5]").getText();	

				String empty = "";					
				if (ExcelKey.equals(empty))
				{
					ExcelKey = "EMPTY";
				}

				if (testOutput == true)
				{
					System.out.println("Excel Key is "+ExcelKey);
				}



				//Compare Correct ANswer with Options and Master QP
				if (mixture==false)
				{

					if((option1.equals(CorrectAnswer))&&(ExcelKey.charAt(1)=='A'))
					{
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 1 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 1 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "1", ExcelKey, "PASS");

					}
					else if((CorrectAnswer.contains(option1))&&(ExcelKey.charAt(1)=='A'))
					{
						System.err.println("------------------------------------------------------------------------");
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 1 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);
						System.out.println("Option matched with Contains logic. Please check it manually once to confirm.");
						System.err.println("------------------------------------------------------------------------");

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 1 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "1", ExcelKey, "Correct Answer Contains Option 1");
					}
					else if((option2.equals(CorrectAnswer))&&(ExcelKey.charAt(1)=='B'))
					{
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 2 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 2 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "2", ExcelKey, "PASS");
					}
					else if((CorrectAnswer.contains(option2))&&(ExcelKey.charAt(1)=='B'))
					{
						System.err.println("------------------------------------------------------------------------");
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 2 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);
						System.out.println("Option matched with Contains logic. Please check it manually once to confirm.");
						System.err.println("------------------------------------------------------------------------");

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 2 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "2", ExcelKey, "Correct Answer Contains Option 2");
					}
					else if((option3.equals(CorrectAnswer))&&(ExcelKey.charAt(1)=='C'))
					{
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 3 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 3 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "3", ExcelKey, "PASS");
					}
					else if((CorrectAnswer.contains(option3))&&(ExcelKey.charAt(1)=='C'))
					{
						System.err.println("------------------------------------------------------------------------");
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 3 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);
						System.out.println("Option matched with Contains logic. Please check it manually once to confirm.");
						System.err.println("------------------------------------------------------------------------");

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 3 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "3", ExcelKey, "Correct Answer Contains Option 3");
					}
					else if((option4.equals(CorrectAnswer))&&(ExcelKey.charAt(1)=='D'))
					{
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 4 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 4 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();						

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "4", ExcelKey, "PASS");
					}	
					else if((CorrectAnswer.contains(option4))&&(ExcelKey.charAt(1)=='D'))
					{
						System.err.println("------------------------------------------------------------------------");
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 4 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);
						System.out.println("Option matched with Contains logic. Please check it manually once to confirm.");
						System.err.println("------------------------------------------------------------------------");

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 4 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "4", ExcelKey, "Correct Answer Contains Option 4");
					}	
					else if((option5.equals(CorrectAnswer))&&(ExcelKey.charAt(1)=='E'))
					{
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 5 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 5 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "5", ExcelKey, "PASS");
					}
					else if((CorrectAnswer.contains(option5))&&(ExcelKey.charAt(1)=='E'))
					{
						System.err.println("------------------------------------------------------------------------");
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is matched with Option 5 in Challenge System.");
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);
						System.out.println("Option matched with Contains logic. Please check it manually once to confirm.");
						System.err.println("------------------------------------------------------------------------");

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is matched with Option 5 in Challenge System.");
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "5", ExcelKey, "Correct Answer Contains Option 5");
					}
					else if((CorrectAnswer.equals(Benefit))&&(ExcelKey.equals(EBenefit)))
					{
						System.out.println("QID: "+QID);
						System.out.println("The Correct Answer is "+CorrectAnswer);
						System.out.println("The Correct Option in Master Key is "+ ExcelKey);

						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer is "+CorrectAnswer);
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "", ExcelKey, "PASS");
					}
					else
					{
						System.err.println("------------------------------------------------------------------------");
						System.err.println("QID: "+QID);
						System.err.println("The Correct Answer in Challenge System does not match with Master Key");
						System.err.println("The Correct Answer in Challenge System is: "+CorrectAnswer);
						System.err.println("The Correct Option in Master Key is "+ ExcelKey);
						System.err.println("------------------------------------------------------------------------");

						bw.write("------------------------------------------------------------------------");
						bw.newLine();
						bw.write("QID: "+QID);
						bw.newLine();
						bw.write("The Correct Answer in Challenge System does not match with Master Key");
						bw.newLine();
						bw.write("The Correct Answer in Challenge System is: "+CorrectAnswer);
						bw.newLine();
						bw.write("The Correct Option in Master Key is "+ ExcelKey);
						bw.newLine();
						bw.write("------------------------------------------------------------------------");
						bw.newLine();

						WriteInExcelCSM(RollNo, QID, CorrectAnswer, "", ExcelKey, "FAIL");
					}	
				}
			}
			else
			{
				System.err.println("QID Mismatch");
				System.err.println("Challenge System: "+QID);				
				System.err.println("Master Copy: "+ QIDMaster);				
				bw.write("QID Mismatch");
				bw.newLine();
				bw.write("Challenge System: "+QID);
				bw.newLine();
				bw.write(QID.length());
				bw.newLine();
				bw.write("Master Copy: "+ QIDMaster);
				bw.newLine();
				bw.write(QIDMaster.length());
				bw.newLine();	


				WriteInExcelCSM(RollNo, QID, "", "", "", "QID-Mismatch-FAIL");
			}

		}
		bw.close();
		cdriver.quit();
		if (cdrivermultiple==true)
		{
			cdriver.quit();
		}		
		fdriver.quit();

	}


	public void createNotepadACS(String RollNo) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter("e:\\Results\\AdminWithChallengeSystem\\TestResult_"+RollNo+".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);


		System.out.println("---------------------------------------------------------------");
		System.out.println("Test Result for the Roll no: "+ RollNo);
		System.out.println("AvailableQuestions in Post Exam Link : " + AvailableQuestionsPE.size());
		System.out.println("---------------------------------------------------------------");

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();		
		bw.write("Admin with Challenge System");
		bw.newLine();
		bw.write("Test Result for the Roll no: "+ RollNo);
		bw.newLine();	
		bw.write("AvailableQuestions in Post Exam Link : " + AvailableQuestionsPE.size());
		bw.newLine();	
		bw.write("---------------------------------------------------------------");
		bw.newLine();
	}


	public void compareCandidateAnswers(String RollNo) throws IOException{

		int WrongAnswerCount = 0;

		for(start=1; start<=AvailableQuestionsPE.size(); start++)		
		{			

			boolean ImageCheck = true;
			String FullText = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]").getText();


			// Get Question Text and check it is not null

			String QuestionText = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/b[1]").getText();

			if (QuestionText.length()<25)
			{
				System.err.println("Incomplete Question");
			}


			//To check if the Question is image based.
			try {
				cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[1]");
			} catch (NoSuchElementException e) {
				ImageCheck = false;				
			}			

			//System.out.println(FullText);

			//To get QID
			boolean loop = true;
			String QID = "";
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




			int QIDint = Integer.parseInt(QID);			
			QIDint = QIDint - QNOint;
			QID = Integer.toString(QIDint);


			//System.out.println("QID: " + QID);



			// Get Candidate Answer from Challenge URL		
			String CandidateAnswer = "";

			try {
				CandidateAnswer = fdriver.findElementByXPath("(((//b[text()='Candidate Answer:'])/..)["+QID+"])/img").getAttribute("src");
				int slash = 0;
				for(slash = CandidateAnswer.length()-1; slash>=0; slash--)
				{
					if(CandidateAnswer.charAt(slash)=='/')
					{
						break;
					}
				}
				CandidateAnswer = new StringBuffer(CandidateAnswer).replace(0, slash, "").toString();	

			} catch (NoSuchElementException e1) {
				CandidateAnswer = fdriver.findElementByXPath("((//b[text()='Candidate Answer:'])/..)["+QID+"]").getText();
				CandidateAnswer = new StringBuffer(CandidateAnswer).replace(0, 18, "").toString();	


				// For few Questions, the Candidate answer has an enter Key at the end which added an extra char at the end. So, deleting that char.
				
				
				boolean cachalspecial = String.valueOf(CandidateAnswer.charAt(CandidateAnswer.length()-1)).matches("\\s");
				if (cachalspecial==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswer);
					CandidateAnswer = NewCandidateAnswer.deleteCharAt(CandidateAnswer.length()-1).toString();
					//System.out.println("New Candidate Answer is "+ CandidateAnswer);
				}
			}


			String IncompNotAns = "[ NOT ANSWERED ]";
			if (CandidateAnswer.equals(IncompNotAns))
			{
				CandidateAnswer = "NOT ANSWERED";
			}
			//System.out.println("Candidate Answer from Challenge System is " + CandidateAnswer);



			// To get Correct Answer
			if(ImageCheck==false)
			{			
				String CorrectAnswer = "";
				int CorrectAnsStart = 0;
				int j=(FullText.length()-1);			
				loop = true;
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
												CorrectAnswer = CorrectAnswer + FullText.charAt(k);
											}
											String CAinCA = "Correct Answer";
											String NoCorAns = "No Correct Answer";
											
											if(CorrectAnswer.equals(NoCorAns))
											{
												CorrectAnswer = "No Correct Answer";
											}											
											else if(CorrectAnswer.contains(CAinCA))
											{
												CorrectAnswer = "EMPTY";
												correctincorrect = true;
											}
											System.out.println("Correct Answer from Post Exam URL is "+ CorrectAnswer);
											loop = false;						
										}
									}
								}
							}
						}

					}j--;
				}while(loop==true);


				// To get Candidate Answer from Post Exam URL
				String CandidateAnswerPostExam = "";
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
				
				System.out.println("CandidateAnswerPostExam"+CandidateAnswerPostExam);
				
				
				boolean caspecial = String.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length()-1)).matches("\\s");
				if (caspecial==true) 
				{
					StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
					CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length()-1).toString();
					//System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
				}

				boolean cospecial = String.valueOf(CorrectAnswer.charAt(CorrectAnswer.length()-1)).matches("\\s");
				if (cospecial==true) 
				{
					StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswer);
					CorrectAnswer = NewCorrectAnswer.deleteCharAt(CorrectAnswer.length()-1).toString();
					//System.out.println("New Correct Answer is "+ CorrectAnswer);
				}



				// Change to correct QID

				QIDint = Integer.parseInt(QID);
				QIDint = QIDint + QNOint;
				QID = Integer.toString(QIDint);



				//Checking Candidate Answer from PostExam to Challenge URL
				if (CandidateAnswerPostExam.equals(CandidateAnswer))
				{

					bw.write("QID: " + QID);
					bw.newLine();
					bw.write("CandidateAnswer from Post Exam Report Page: "+ CandidateAnswerPostExam);
					bw.newLine();
					bw.write("CandidateAnswer from Challenge System URL: "+ CandidateAnswer);
					bw.newLine();


					System.out.println("QID: " + QID);				
					System.out.println("CandidateAnswer from Post Exam Report Page: "+ CandidateAnswerPostExam);
					System.out.println("CandidateAnswer from Challenge System URL: "+ CandidateAnswer);
					//System.out.println("CorrectAnswer: "+ CorrectAnswer);


					WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswer, "PASS");	

				}
				else
				{
					bw.write("---------------------------------------------------------------");
					bw.newLine();
					bw.write("QID: " + QID);	
					bw.newLine();
					bw.write("CandidateAnswer from Post Exam Report Page: "+ CandidateAnswerPostExam);
					bw.newLine();
					bw.write("CandidateAnswer from Challenge System URL: "+ CandidateAnswer);
					bw.newLine();
					bw.write("---------------------------------------------------------------");
					bw.newLine();

					System.err.println("---------------------------------------------------------------");
					System.err.println("QID: " + QID);
					System.err.println("CandidateAnswer from Post Exam Report Page: "+ CandidateAnswerPostExam);
					System.err.println("CandidateAnswer from Challenge System URL: "+ CandidateAnswer);
					System.err.println("---------------------------------------------------------------");
					WrongAnswerCount++;


					WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswer, "FAIL");

					Actions myAction = new Actions(cdriver);
					WebElement target = cdriver.findElementByXPath("//b[contains(text(), 'Question id [ "+QID+" ]')]/..");
					Point classname = target.getLocation();
					int xcordi = classname.getX();			        
					int ycordi = classname.getY();			        
					myAction.moveToElement(target, xcordi, ycordi).click().build().perform();
					FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE) , new File("./reports/images/AdminWithChallenge_"+RollNo+"_"+QID+".jpg"));		

				}

			}
			else
			{
				{	
					//If the Question is image based.
					// To get Correct Answer		
					String CorrectAnswer = "";
					String CandidateAnswerPostExam = "";
					String NotAnswered = "NOT ANSWERED";
					boolean NotAns = false;
					String NoCorrectAns = "No Correct Answer";

					//To Check if the Candidate answered the image based question or not.
					if (FullText.contains(NotAnswered))
					{						
						CandidateAnswerPostExam = "NOT ANSWERED";
						NotAns = true;
					}


					// To get Candidate Answer

					boolean mixture = false; // Some Question may contain both images and text in options.
					if (NotAns==false)
					{


						if (FullText.contains(NoCorrectAns))
						{
							CorrectAnswer = "No Correct Answer";
							try {
								CandidateAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[5]").getAttribute("src");

								int slash = 0;
								for(slash = CandidateAnswerPostExam.length()-1; slash>=0; slash--)
								{
									if(CandidateAnswerPostExam.charAt(slash)=='/')
									{
										break;
									}
								}
								CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam).replace(0, slash, "").toString();

							} catch (NoSuchElementException e) {
								CandidateAnswerPostExam = "Question is mixture of Images and Text. Unable to Extract.";
								mixture = true;
							}
						}
						else
						{
							try {

								CorrectAnswer = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[6]").getAttribute("src");
								CandidateAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["+start+"]/img[5]").getAttribute("src");
								int slash = 0;
								for(slash = CandidateAnswerPostExam.length()-1; slash>=0; slash--)
								{
									if(CandidateAnswerPostExam.charAt(slash)=='/')
									{
										break;
									}
								}
								CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam).replace(0, slash, "").toString();
							}

							catch (NoSuchElementException e) {

								CandidateAnswerPostExam = "Question is mixture of Images and Text. Unable to Extract.";
								mixture = true;

							}	
						} 
					}





					// Change to correct QID

					QIDint = Integer.parseInt(QID);
					QIDint = QIDint + QNOint;
					QID = Integer.toString(QIDint);




					//Mapping Candidate Answers
					if (mixture==false)
					{
						if (CandidateAnswerPostExam.equals(CandidateAnswer))
						{
							bw.write("QID: " + QID);	
							bw.newLine();
							bw.write("CandidateAnswer from Post Exam Report Page: "+ CandidateAnswerPostExam);
							bw.newLine();
							bw.write("CandidateAnswer from Challenge System URL: "+ CandidateAnswer);
							bw.newLine();

							System.out.println("QID: " + QID);				
							System.out.println("CandidateAnswer from Post Exam Report Page: "+ CandidateAnswerPostExam);
							System.out.println("CandidateAnswer from Challenge System URL: "+ CandidateAnswer);
							//System.out.println("CandidateAnswer: "+ CandidateAnswer);


							WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswer, "PASS");

						}
						else
						{
							bw.write("---------------------------------------------------------------");
							bw.newLine();
							bw.write("QID: " + QID);	
							bw.newLine();
							bw.write("CandidateAnswer from Post Exam Report Page: "+ CandidateAnswerPostExam);
							bw.newLine();
							bw.write("CandidateAnswer from Challenge System URL: "+ CandidateAnswer);
							bw.newLine();
							bw.write("---------------------------------------------------------------");
							bw.newLine();

							System.err.println("---------------------------------------------------------------");
							System.err.println("QID: " + QID);
							System.err.println("CandidateAnswer from Post Exam Report Page: "+ CandidateAnswerPostExam);
							System.err.println("CandidateAnswer from Challenge System URL: "+ CandidateAnswer);
							System.err.println("---------------------------------------------------------------");
							WrongAnswerCount++;

							WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswer, "FAIL");



							Actions myAction = new Actions(cdriver);
							WebElement target = cdriver.findElementByXPath("//b[contains(text(), 'Question id [ "+QID+" ]')]/..");
							Point classname = target.getLocation();
							int xcordi = classname.getX();			        
							int ycordi = classname.getY();			        
							myAction.moveToElement(target, xcordi, ycordi).click().build().perform();

							FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE) , new File("./reports/images/AdminWithChallenge_"+QID+".jpg"));	


						}
					}
					else
					{
						bw.write("---------------------------------------------------------------");
						bw.newLine();
						bw.write("QID: " + QID);	
						bw.newLine();
						bw.write("Question has both Images and Text. Please test it manually.");
						bw.newLine();
						bw.write("---------------------------------------------------------------");
						bw.newLine();

						System.err.println("---------------------------------------------------------------");
						System.err.println("QID: " + QID);
						System.err.println("Question has both Images and Text. Please test it manually.");
						System.err.println("---------------------------------------------------------------");


						WriteInExcelACS(RollNo, QID, "Mixture of Image and Text", "Mixture of Image and Text", "Manual");



						Actions myAction = new Actions(cdriver);
						WebElement target = cdriver.findElementByXPath("//b[contains(text(), 'Question id [ "+QID+" ]')]/..");
						Point classname = target.getLocation();
						int xcordi = classname.getX();			        
						int ycordi = classname.getY();			        
						myAction.moveToElement(target, xcordi, ycordi).click().build().perform();

						FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE) , new File("./reports/images/AdminWithChallenge_"+QID+".jpg"));	

					}

				}
			}
			System.out.println();

		}
		bw.close();	
		cdriver.close();
		cdriver.quit();
		fdriver.close();
		if (fdrivermultiple==true)
		{
			fdriver.quit();
		}
	}

	public void createNotepadBLPE(String RollNo) throws IOException {

		try {
			fw = new FileWriter("e:\\Results\\BrokenLinks\\BrokenLink_PostExam_"+RollNo+".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw =new BufferedWriter(fw);





		System.out.println("---------------------------------------------------------------");
		System.out.println("Post Exam");
		System.out.println("Test Result for the Roll no: "+ RollNo);
		System.out.println("---------------------------------------------------------------");

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("Post Exam");
		bw.newLine();
		bw.write("Test Result for the Roll no: "+ RollNo);
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
	}


	public void createNotepadBLCS(String RollNo) throws IOException {		
		try {
			fw = new FileWriter("e:\\Results\\BrokenLinks\\BrokenLink_Challenge_"+RollNo+".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw =new BufferedWriter(fw);



		System.out.println("---------------------------------------------------------------");
		System.out.println("Challenge System");
		System.out.println("Test Result for the Roll no: "+ RollNo);
		System.out.println("---------------------------------------------------------------");

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("Challenge System");
		bw.newLine();
		bw.write("Test Result for the Roll no: "+ RollNo);
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
	}



	public void createNotepadNumberOfQuestions(String RollNo) throws IOException {

		try {
			fw = new FileWriter("e:\\Results\\BrokenLinks\\NumberOfQuestions_"+RollNo+".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw =new BufferedWriter(fw);		

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();		
		bw.write("Test Result for the Roll no: "+ RollNo);
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();



	}


	public void writeNumberOfQuestions() throws IOException {

		fdriver.close();

		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("AvailableQuestions in MQP: "+ AvailableQuestionMasterQP);
		bw.newLine();
		bw.write("AvailableQuestions in Post Exam: "+ AvailableQuestionsPE.size());
		bw.newLine();
		bw.write("AvailableQuestions in Challenge System: "+ AvailableQuestionsCS.size());
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();		
		bw.close();

	}






	public void chkBrokenLinkPE() throws IOException {
		LinkList = cdriver.findElementsByTagName("img");
		ActiveLinks = new ArrayList<WebElement>();



		for(int j=0; j<LinkList.size(); j++)
		{
			if ((LinkList.get(j).getAttribute("src"))!= null)
			{
				ActiveLinks.add(LinkList.get(j));
			}
		}


		for(int i=1; i<LinkList.size(); i++)
		{


			try {
				HttpURLConnection connection = (HttpURLConnection)new URL(LinkList.get(i).getAttribute("src")).openConnection();
				connection.connect();
				String responseMessage = connection.getResponseMessage();
				String OK = "OK";
				connection.disconnect();

				if(responseMessage.equals(OK))
				{													
					System.out.println(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();
					bw.write(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();			

				}
				else
				{
					System.err.println(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					String link = LinkList.get(i).getAttribute("src");
					//String QID = cdriver.findElementByXPath("//img[@src='"+link+"']/../b").getText();
					//QID = QID.replaceAll("[^0-9]", "");
					System.err.println("Check the Images for the QID: ");


					bw.newLine();
					bw.write("----------------------------------------------------------------------------------");
					bw.newLine();
					bw.write("Check the Images for the QID: ");
					bw.newLine();
					bw.write(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();
					bw.write("----------------------------------------------------------------------------------");
					bw.newLine();					

				}				

			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}	
		cdriver.close();
		bw.close();
	}


	public void chkBrokenLinkCS() throws IOException {	
		LinkList = cdriver.findElementsByTagName("img");
		ActiveLinks = new ArrayList<WebElement>();



		for(int j=0; j<LinkList.size(); j++)
		{
			if ((LinkList.get(j).getAttribute("src"))!= null)
			{
				ActiveLinks.add(LinkList.get(j));
			}
		}


		for(int i=0; i<LinkList.size(); i++)
		{


			try {
				HttpURLConnection connection = (HttpURLConnection)new URL(LinkList.get(i).getAttribute("src")).openConnection();
				connection.connect();
				String responseMessage = connection.getResponseMessage();
				connection.disconnect();
				String OK = "OK";

				if(responseMessage.equals(OK))
				{					

					System.out.println(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();
					bw.write(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();				

				}
				else
				{
					System.err.println(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					String link = LinkList.get(i).getAttribute("src");
					//String QID = cdriver.findElementByXPath("//img[@src='"+link+"']/../b").getText();
					//QID = QID.replaceAll("[^0-9]", "");
					System.err.println("Check the Images for the QID: ");


					bw.newLine();
					bw.write("----------------------------------------------------------------------------------");
					bw.newLine();
					bw.write("Check the Images for the QID: ");
					bw.newLine();
					bw.write(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();
					bw.write("----------------------------------------------------------------------------------");
					bw.newLine();				

				}	

			}
			catch (MalformedURLException e) {
				e.printStackTrace();

			}

		}	

		cdriver.close();
		bw.close();		
	}











	//------------------------------------------------------------------------------------------------------------------------------------

	public void CreateOneExcel() throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("TestResult");	

		rowCount = 0;
		WriteInOneExcel("RollNo", "QID", "CandidateAnswerPostExam", "CandidateAnswerCS", "Status1", "CorrectAnswerCS", "Option No.", "MasterQP Option", "Status2", "FinalStatus" );

	}	


	public void WriteInOneExcel(String text1, String text2, String text3, String text4, String text5,
			String text6, String text7, String text8, String text9, String text10) throws FileNotFoundException, IOException {



		Object[][] bookData = {
				{text1, text2, text3, text4, text5, text6, text7, text8, text9, text10},              
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
		try (FileOutputStream outputStream = new FileOutputStream("E:\\Results\\AdminWithChallengeSystem\\TestResult.xlsx")) {
			workbook.write(outputStream);
		}

	}













	public void CreateExcelACS(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("AdminWithChallengeSystem");	

		rowCount = 0;
		WriteInExcelACS("RollNo", "QID", "CandidateAnswerPostExam", "CandidateAnswerCS", "Status");
	}


	public void CreateExcelCSM(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("ChallengeSystemWithMaster");	

		rowCount = 0;
		WriteInExcelCSM("RollNo", "QID", "CorrectAnswerCS", "Option No.", "MasterQP Option", "Status");
	}

	public void CreateBrokenExcelPE(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("BrokenLinksPE");	

		rowCount = 0;
		WriteInExcelBrokenPE("QID", "URL", "Response", RollNo);
	}


	public void CreateBrokenExcelCS(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("BrokenLinksCS");	

		rowCount = 0;
		WriteInExcelBrokenPE("QID", "URL", "Response", RollNo);
	}


	public void CreateFinalExcel() throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("AutomationOutput");	

		rowCount = 0;
		WriteInFinalExcel("Date", "Subject Code", "Membership No", "Password", "Master QP count", "Challenge System count", "Audit trail count", "Status");
	}


	public void WriteInExcelACS(String RollNo, String text1, String text2, String text3, String text4) throws FileNotFoundException, IOException {



		Object[][] bookData = {
				{RollNo, text1, text2, text3, text4},              
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
		try (FileOutputStream outputStream = new FileOutputStream("E:\\Results\\AdminWithChallengeSystem\\TestResult_ACS.xlsx")) {
			workbook.write(outputStream);
		}


	}


	public void WriteInExcelBrokenPE(String text1, String text2, String text3, String RollNo) throws FileNotFoundException, IOException {



		Object[][] bookData = {
				{text1, text2, text3},              
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
		try (FileOutputStream outputStream = new FileOutputStream("E:\\Results\\BrokenLinks\\TestResult_BrokenLink_PE"+RollNo+".xlsx")) {
			workbook.write(outputStream);
		}


	}


	public void WriteInExcelBrokenCS(String text1, String text2, String text3, String RollNo) throws FileNotFoundException, IOException {



		Object[][] bookData = {
				{text1, text2, text3},              
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
		try (FileOutputStream outputStream = new FileOutputStream("E:\\Results\\BrokenLinks\\TestResult_BrokenLink_CS"+RollNo+".xlsx")) {
			workbook.write(outputStream);
		}


	}



	public void WriteInFinalExcel(String text1, String text2, String text3, String text4, String text5, String text6, String text7, String text8) throws FileNotFoundException, IOException {



		Object[][] bookData = {
				{text1, text2, text3, text4, text5, text6, text7, text8},              
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
		try (FileOutputStream outputStream = new FileOutputStream("E:\\Results\\BrokenLinks\\FinalCount.xlsx")) {
			workbook.write(outputStream);
		}


	}



	public void WriteInExcelCSM(String RollNo, String text1, String text2, String text3, String text4, String text5) throws FileNotFoundException, IOException {



		Object[][] bookData = {
				{RollNo, text1, text2, text3, text4, text5},              
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
		try (FileOutputStream outputStream = new FileOutputStream("E:\\Results\\ChallengeSystemWithMaster\\TestResult_CSM.xlsx")) {
			workbook.write(outputStream);
		}


	}




	public long takeSnap() {
		// TODO Auto-generated method stub
		return 0;
	}

}
