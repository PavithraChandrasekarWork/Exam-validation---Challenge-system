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
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Reporter;

@SuppressWarnings("unused")
public class iTestMethods4 extends Reporter {

	public RemoteWebDriver driver;
	public ChromeDriver cdriver;
	public FirefoxDriver fdriver;
	// public ChromeDriver fdriver;
	public WebDriver pdriver;
	public boolean testOutput = false, optionFive = false;
	public FileWriter fw = null;
	public List<WebElement> LinkList = null;
	public List<WebElement> ActiveLinks = null;
	public int start, AvailableQuestionMasterQP;
	public int startcsm;
	public boolean fdrivermultiple = false;
	public boolean cdrivermultiple = false;
	public boolean mixture;
	public String StringAvailableQuestionMasterQP, StringAvailableQuestionsCS, StringAvailableQuestionsPE;
	public String[] optionText = new String[4];
	public String FullURL;
	int rowCountACS = 0;
	int rowCountCSM = 0;
	int acsOptRowCount = 1;
	int csmOptRowCount = 1;

	public List<WebElement> AvailableQuestionsPE, AvailableQuestionsCS, AvailableQuestionsMQP;
	public String RollNo = null, SubjectLink = null;
	public int QNOint;
	public BufferedWriter bw = null;

	public String sUrl, primaryWindowHandle, sHubUrl, sHubPort;

	public String subjectFromAT;

	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public int rowCount = 0;

	public iTestMethods4() {
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

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	public void openMasterQPFF(String ExcelURL, String SubjectNum) throws InterruptedException {
		// System.setProperty("webdriver.chrome.driver", "./drivers1/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\drivers\\geckodriverNew.exe");
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
		FirefoxProfile profile = new FirefoxProfile();
		File pathBinary = new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		FirefoxBinary firefoxBinary = new FirefoxBinary(pathBinary);
		profile.setPreference("javascript.enabled", true);
		FirefoxOptions fireoptions = new FirefoxOptions();
		fireoptions.setProfile(profile);
		fireoptions.setCapability("marionette", false);
		fireoptions.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");
		fireoptions.setBinary(firefoxBinary);
		fdriver = new FirefoxDriver(fireoptions);
		// fdriver = new ChromeDriver();
		fdriver.manage().timeouts().setScriptTimeout(2, TimeUnit.SECONDS);
		// fdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		fdriver.get(ExcelURL);
		Thread.sleep(4000);
		Select SelectSubjectCode = new Select(fdriver.findElement(By.id("subjectcode")));
		SelectSubjectCode.selectByVisibleText(SubjectNum);
		// SelectSubjectCode.selectByIndex(6);
		Thread.sleep(10000);
		AvailableQuestionsMQP();
	}

	public int totalAttemptedPE = 0;

	public String startPostExamChrome(String AdminURL, String AdminUserName, String AdminPwd, String AdminExamDate,
			String ExamNum, String SubjectNum, String RollNo, String newATurl) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		cdriver = new ChromeDriver();
		Thread.sleep(2000);
		cdriver.manage().window().maximize();

		cdriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		cdriver.get(AdminURL);

		/*
		 * WebElement userName = cdriver.findElementByName("memno");
		 * 
		 * Actions act = new Actions(cdriver); act.sendKeys(userName, AdminUserName);
		 * 
		 * act.sendKeys(Keys.TAB).build().perform();
		 * act.sendKeys(AdminPwd).build().perform();
		 */

		cdriver.findElementByName("memno").sendKeys(AdminUserName);
		cdriver.findElementByName("candpassword").sendKeys(AdminPwd);
		// cdriver.findElementByName("mobileno").sendKeys(mobileNo);

		cdriver.findElementByName("go").click();
		Thread.sleep(1000);

		cdriver.get(newATurl);

		Select date = new Select(cdriver.findElementById("db"));
		date.selectByValue(AdminExamDate);
		Thread.sleep(1000);
		cdriver.findElementByXPath("//span[text()='Reports']").click();
		cdriver.findElementByLinkText("Candidate Response Summary").click();
		Thread.sleep(2000);
		Select Exam = new Select(cdriver.findElementByName("exam_code"));
		Exam.selectByValue(ExamNum);
		Thread.sleep(2000);
		Select Subject = new Select(cdriver.findElementByName("subject_code"));
		Subject.selectByValue(SubjectNum);
		cdriver.findElementById("memno").sendKeys(RollNo);
		cdriver.findElementByXPath("//button[text()='Submit']").click();

		Thread.sleep(1000);
		getQuestionsViewedPE();

		/*
		 * totalAttemptedPE = 0; String attemptedPE = cdriver.
		 * findElementByXPath("(//b[text()='No. of Questions Answered']/../../td)[3]").
		 * getText(); totalAttemptedPE = Integer.parseInt(attemptedPE);
		 * System.out.println("totalAttemptedPE = "+totalAttemptedPE);
		 */

		SubjectLink = cdriver.findElementByXPath("(//td[@class='audit-report-td-2'])[5]").getText();

		SubjectLink = new StringBuffer(SubjectLink).replace(4, SubjectLink.length(), "").toString();
//         System.out.println(SubjectLink);
		subjectFromAT = cdriver.findElementByXPath("(//b[text()='Subject ']/../../td)[3]").getText().trim();
//		 System.out.println(subjectFromAT);
		return SubjectLink;

	}

	public int totalAttemptedCS = 0;

	public void startChallengeSystemFF(String ChallengeURL, String RollNo, String Pass, String ChallengeExamDate)
			throws InterruptedException {

//		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver110.exe");
//		fdriver = new FirefoxDriver();
//		fdriver.manage().window().maximize();
		// System.setProperty("webdriver.chrome.driver", "./drivers1/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\drivers\\geckodriverNew.exe");
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
		FirefoxProfile profile = new FirefoxProfile();
		File pathBinary = new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		FirefoxBinary firefoxBinary = new FirefoxBinary(pathBinary);
		profile.setPreference("javascript.enabled", true);
		FirefoxOptions fireoptions = new FirefoxOptions();
		fireoptions.setProfile(profile);
		fireoptions.setCapability("marionette", false);
		fireoptions.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");
		fireoptions.setBinary(firefoxBinary);
		fdriver = new FirefoxDriver(fireoptions);

		// fdriver = new ChromeDriver();

		// fdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		fdriver.get(ChallengeURL);
		// CHANGE
		Thread.sleep(3000);
		// change done here
		fdriver.manage().window().maximize();
		fdriver.findElementById("Registration_No").sendKeys(RollNo);
		/*
		 * if(Pass.length()!=8) { Pass = 0+Pass; }
		 */
		fdriver.findElementById("Password").sendKeys(Pass);

		WebDriverWait wait = new WebDriverWait(fdriver, 10);

		// wait.until(ExpectedConditions.presenceOfElementLocated(By.id("examdate")));

		Select examDate = new Select(fdriver.findElementById("examdate"));
		examDate.selectByVisibleText(ChallengeExamDate);
//		Thread.sleep(10000);
		// changes done here
		Thread.sleep(5000);

		// fdriver.findElementById("examdate").sendKeys(ChallengeExamDate);

		// Thread.sleep(10000);

		fdriver.findElementByXPath("//input[@class='btn blue_button5 right']").click();
		Thread.sleep(2000);

		// fdriver.findElementByXPath("//button[text()=' View Details ']").click();
		fdriver.findElementByXPath("//button[text()='View response']").click();
		Thread.sleep(1000);

		try {
			Select subject = new Select(fdriver.findElement(By.id("subject_sltd")));
			subject.selectByVisibleText(subjectFromAT);
			Thread.sleep(2000);
		} catch (NoSuchElementException e) {

		}
	}

	public void startChallengeSystemChrome(String ChallengeURL, String RollNo, String Pass, String ChallengeExamDate,
			String SubjectNum) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		cdriver = new ChromeDriver();
		cdriver.manage().window().maximize();
		cdriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		cdriver.get(ChallengeURL);
		cdriver.findElementById("Registration_No").sendKeys(RollNo);
		/*
		 * if(Pass.length()!=8) { Pass = 0+Pass; }
		 */
		cdriver.findElementById("Password").sendKeys(Pass);

		Select examDate = new Select(cdriver.findElementById("examdate"));
		examDate.selectByVisibleText(ChallengeExamDate);

		// cdriver.findElementById("examdate").sendKeys(ChallengeExamDate);

		Thread.sleep(7000);
		cdriver.findElementByXPath("//input[@class='btn blue_button5 right']").click();
		Thread.sleep(2000);
		// cdriver.findElementByXPath("//button[text()=' View Details ']").click();
		cdriver.findElementByXPath("//button[text()='View response']").click();
		Thread.sleep(1000);
		AvailableQuestionsCS();

		/*
		 * if((SubjectNum.equals("203"))||(SubjectNum.equals("204"))) { Select
		 * subjectDrop = new Select(cdriver.findElementById("subject_sltd"));
		 * subjectDrop.selectByIndex(1); Thread.sleep(2000);
		 * 
		 * }
		 */

		try {
			List<WebElement> list = cdriver.findElements(By.id("subject_sltd"));
			if (!(list.size() == 0)) {
				System.out.println("Loop Starts");
				System.out.println(list.size());
				Select subject = new Select(cdriver.findElement(By.name("subject_sltd")));
				subject.selectByValue(SubjectNum);
				Thread.sleep(2000);
			} else {
				WebElement SubjectNo = cdriver
						.findElement(By.xpath("//tr//td[text()='Subject']//following-sibling::td"));
				String subjectName = SubjectNo.getText();
				System.out.println(subjectName);
			}

		} catch (NoSuchElementException e) {

		}

	}

	public void getQuestionsViewedPE() {

		AvailableQuestionsPE = cdriver.findElementsByXPath("//td[@class='greybluetext10']");
		StringAvailableQuestionsPE = Integer.toString(AvailableQuestionsPE.size());

		// System.out.println("AvailableQuestions Size "+ AvailableQuestionsPE.size());

	}

	public void AvailableQuestionsMQP() {

		AvailableQuestionsMQP = fdriver.findElements(By.tagName("tr"));
		AvailableQuestionMasterQP = AvailableQuestionsMQP.size() - 1;

		StringAvailableQuestionMasterQP = Integer.toString(AvailableQuestionMasterQP);

	}

	public void AvailableQuestionsCS() {

		AvailableQuestionsCS = cdriver.findElementsByXPath("((//b[text()='Correct Answer:'])/..)");
		StringAvailableQuestionsCS = Integer.toString(AvailableQuestionsCS.size());

	}

	public void getFirstQIDinCS() {

		/*
		 * String QNO =
		 * fdriver.findElementByXPath("//b[starts-with(text(),'Q. No.')]").getText();
		 * QNO = QNO.replaceAll("[^0-9]", "");
		 */

		String QNO = fdriver.findElementByXPath("//b[starts-with(text(),'QID : ')]").getText();
		QNO = QNO.replaceAll("[^0-9]", "");

		QNOint = Integer.parseInt(QNO);
		QNOint = QNOint - 1;

		System.out.println(QNOint);
	}

	public void createNotepadCSM(String RollNo) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter("F:\\Results\\ChallengeSystemWithMaster\\TestResult_" + RollNo + ".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);

		System.out.println("---------------------------------------------------------------");
		System.out.println("Test Result for the Roll no: " + RollNo);
		System.out.println("---------------------------------------------------------------");

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("Challenge System with Master");
		bw.newLine();
		bw.write("Test Result for the Roll no: " + RollNo);
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
	}

	public void checkNumberOfOptions() {

		optionFive = false;
		int op5count = 0;
		for (int question = 1; question <= 20; question++) {

			String Options = cdriver.findElementByXPath("((//b[text()='Options:'])/..)[" + question + "]").getText();
			Options = new StringBuffer(Options).replace(0, 8, "").toString();

			String op5 = "5) ";

			if (Options.contains(op5)) {
				op5count++;
			}

		}

		if (op5count == 20) {
			optionFive = true;
		}

	}

	public void compareCorrectAnswers(String RollNo, String SubjectNum) throws IOException, InterruptedException {

		/*
		 * if((SubjectNum.equals("203"))||(SubjectNum.equals("204"))) { Select
		 * subjectDrop = new Select(cdriver.findElementById("subject_sltd"));
		 * subjectDrop.selectByIndex(1); Thread.sleep(2000);
		 * 
		 * }
		 */

		// testOutput = true;

		for (start = 1; start <= AvailableQuestionsCS.size(); start++)
		// for(start=41; start<=41; start++)

		{

			/*
			 * // Get QID in Challenge System String QID =
			 * cdriver.findElementByXPath("(//b[starts-with(text(),'Q. No.')])["+start+"]").
			 * getText(); QID = QID.replaceAll("[^0-9]", "");
			 */

			// Get QID in Challenge System
			String QID = cdriver.findElementByXPath("(//b[starts-with(text(),'QID : ')])[" + start + "]").getText();
			QID = QID.replaceAll("[^0-9]", "");

			// Get QID in Master Copy
			String QIDMaster = "";
			try {
				fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[6]").isDisplayed();
				QIDMaster = fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[4]").getText().trim();
			} catch (Exception e) {
				QIDMaster = fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[3]").getText().trim();
			}

			if (QID.equals(QIDMaster)) {

				boolean ImageCheck = true;
				mixture = false;

				// To check if the Question is image based.
				try {
					cdriver.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img").getText();
				} catch (NoSuchElementException e) {
					try {
						cdriver.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/b/img").getText();
					} catch (NoSuchElementException e1) {

						ImageCheck = false;
					}
				}

				String option1 = "";
				String option2 = "";
				String option3 = "";
				String option4 = "", option5 = "";
				String CorrectAnswer = "";

				// If Question is not image based.
				// Get Correct Answer opt
				if (ImageCheck == false) {

					CorrectAnswer = cdriver.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + start + "]")
							.getText();
					CorrectAnswer = new StringBuffer(CorrectAnswer).replace(0, 22, "").toString();

					if (CorrectAnswer.length() != 0) {

						boolean correctAnsEndSpecial;

						correctAnsEndSpecial = false;
						correctAnsEndSpecial = String.valueOf(CorrectAnswer.charAt(CorrectAnswer.length() - 1))
								.matches("\\s");
						if (correctAnsEndSpecial == true) {
							StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswer);
							CorrectAnswer = NewCorrectAnswer.deleteCharAt(CorrectAnswer.length() - 1).toString();
						}
						correctAnsEndSpecial = false;
						correctAnsEndSpecial = String.valueOf(CorrectAnswer.charAt(CorrectAnswer.length() - 1))
								.matches("\\s");
						if (correctAnsEndSpecial == true) {
							StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswer);
							CorrectAnswer = NewCorrectAnswer.deleteCharAt(CorrectAnswer.length() - 1).toString();
						}

						String NoCorrectAnsCS = "orrect Answer ]";
						if (CorrectAnswer.contains(NoCorrectAnsCS)) {
							CorrectAnswer = "NO CORRECT ANSWER";
						}

					} else {
						CorrectAnswer = "Correct Answer is Empty";
					}

					if (testOutput == true) {
						System.out.println("Correct Answer is " + CorrectAnswer);
						System.out.println("CorrectAnswer length is: " + CorrectAnswer.length());
					}

					// System.out.println("Correct Ans length: " + CorrectAnswer.length());

					// Get Options

					String Options = cdriver.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]")
							.getText();
					Options = new StringBuffer(Options).replace(0, 8, "").toString();
					if (testOutput == true) {
						System.out.println("Option Text is " + Options);
					}

					int Op1Start = 0, Op1end = 0, Op2Start = 0, Op2end = 0, Op3Start = 0, Op3end = 0, Op4Start = 0,
							Op4end = 0, Op5Start = 0, Op5end = 0;

					// Getting Option start position
					for (int i = 0; i < Options.length(); i++) {
						if (Op1Start == 0) {
							if (Options.charAt(i) == '1') {

								if (Options.charAt(i + 1) == ')') {
									if (Options.charAt(i + 2) == ' ') {
										// if(Options.charAt(i+3)==' ')
										if (Options.charAt(i - 1) != '(') {
											Op1Start = i + 3;
										}

									}
								}
							}
						}
					}

					for (int i = Op1Start; i < Options.length(); i++) {

						if (Op2Start == 0) {
							if (Options.charAt(i) == '2') {
								if (Options.charAt(i + 1) == ')') {
									if (Options.charAt(i + 2) == ' ') {
										// if(Options.charAt(i+3)==' ')
										if (Options.charAt(i - 1) != '(')

										{
											Op2Start = i + 3;
										}

									}
								}
							}
						}
					}

					for (int i = Op2Start; i < Options.length(); i++) {

						if (Op3Start == 0) {
							if (Options.charAt(i) == '3') {
								if (Options.charAt(i + 1) == ')') {
									if (Options.charAt(i + 2) == ' ') {
										// if(Options.charAt(i+3)==' ')
										if (Options.charAt(i - 1) != '(') {
											Op3Start = i + 3;
										}

									}
								}

							}
						}
					}

					for (int i = Op3Start; i < Options.length(); i++) {

						if (Op4Start == 0) {
							if (Options.charAt(i) == '4') {
								if (Options.charAt(i + 1) == ')') {
									if (Options.charAt(i + 2) == ' ') {
										// if(Options.charAt(i+3)==' ')
										if (Options.charAt(i - 1) != '(') {
											Op4Start = i + 3;

											if (optionFive == false) {
												break;
											}
										}
									}
								}
							}
						}

					}

					for (int i = Op4Start; i < Options.length(); i++) {

						if (optionFive == true) {
							if (Op5Start == 0) {
								if (Options.charAt(i) == '5') {
									if (Options.charAt(i + 1) == ')') {
										if (Options.charAt(i + 2) == ' ') {
											// if(Options.charAt(i+3)==' ')
											if (Options.charAt(i - 1) != '(') {
												Op5Start = i + 3;
												break;
											}
										}
									}
								}
							}
						}
					}

					// Getting Option end position
					if (optionFive == true) {
						Op1end = Op2Start - 4;
						Op2end = Op3Start - 4;
						Op3end = Op4Start - 4;
						Op4end = Op5Start - 4;
						Op5end = Options.length();
					} else {
						Op1end = Op2Start - 4;
						Op2end = Op3Start - 4;
						Op3end = Op4Start - 4;
						Op4end = Options.length();
					}

					// Verifying start and end position of options
					if (testOutput == true) {
						System.out.println(Op1Start);
						System.out.println(Op2Start);
						System.out.println(Op3Start);
						System.out.println(Op4Start);
						if (optionFive == true) {
							System.out.println("Test" + Op5Start);
						}
						System.out.println(Op1end);
						System.out.println(Op2end);
						System.out.println(Op3end);
						System.out.println(Op4end);
						if (optionFive == true) {
							System.out.println("Test" + Op5end);
						}
					}

					// Getting Separate option text
					option1 = Options.substring(Op1Start, Op1end);
					option2 = Options.substring(Op2Start, Op2end);
					option3 = Options.substring(Op3Start, Op3end);
					option4 = Options.substring(Op4Start, Op4end);
					if (optionFive == true) {
						option5 = Options.substring(Op5Start, Op5end);
					}

					// Verifying the Separate Option Text
					if (testOutput == true) {
						System.out.println("Option 1 is " + option1);
						System.out.println("Option 2 is " + option2);
						System.out.println("Option 3 is " + option3);
						System.out.println("Option 4 is " + option4);
						if (optionFive == true) {
							System.out.println("Option 5 is " + option5);
						}
					}

					// For few Questions, the Options has an enter Key at the end which added an
					// extra char at the end. So, deleting that char.
					boolean optionEndSpecial, optionStartEpecial;

					optionStartEpecial = false;
					optionStartEpecial = String.valueOf(option1.charAt(0)).matches("\\s");
					if (optionStartEpecial == true) {
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(0).toString();
					}

					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption1 = new StringBuilder(option1);
						option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption2 = new StringBuilder(option2);
						option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption2 = new StringBuilder(option2);
						option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption2 = new StringBuilder(option2);
						option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption3 = new StringBuilder(option3);
						option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption3 = new StringBuilder(option3);
						option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption3 = new StringBuilder(option3);
						option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option4.charAt(option4.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption4 = new StringBuilder(option4);
						option4 = Newoption4.deleteCharAt(option4.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option4.charAt(option4.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption4 = new StringBuilder(option4);
						option4 = Newoption4.deleteCharAt(option4.length() - 1).toString();
					}
					optionEndSpecial = false;
					optionEndSpecial = String.valueOf(option4.charAt(option4.length() - 1)).matches("\\s");
					if (optionEndSpecial == true) {
						StringBuilder Newoption4 = new StringBuilder(option4);
						option4 = Newoption4.deleteCharAt(option4.length() - 1).toString();
					}

					if (optionFive == true) {
						optionEndSpecial = false;
						optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1)).matches("\\s");
						if (optionEndSpecial == true) {
							StringBuilder Newoption5 = new StringBuilder(option5);
							option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
						}
						optionEndSpecial = false;
						optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1)).matches("\\s");
						if (optionEndSpecial == true) {
							StringBuilder Newoption5 = new StringBuilder(option5);
							option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
						}
						optionEndSpecial = false;
						optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1)).matches("\\s");
						if (optionEndSpecial == true) {
							StringBuilder Newoption5 = new StringBuilder(option5);
							option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
						}
					}

					if (testOutput == true) {
						System.out.println("Option 1 is " + option1);
						System.out.println("Option 2 is " + option2);
						System.out.println("Option 3 is " + option3);
						System.out.println("Option 4 is " + option4);
						if (optionFive == true) {
							System.out.println("Option 5 is " + option5);
						}
					}

				}

				else {
					// If the Question is image based.
					// To get Correct Answer

					mixture = false;

					// String NoCorrectAnsCS = "No Correct Answer ( Benefit to all )";
					String NoCorrectAnsCS = "orrect Answer ]";
					String ImageNotDisplayedInCA = ".jpg[/img]";

					// Check if the Question is only image based or mixture of Image and Text
					try {
						if (optionFive == true) {
							option5 = cdriver
									.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/b/img)[5]")
									.getAttribute("src");
						}
						option4 = cdriver.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/b/img)[4]")
								.getAttribute("src");
						option3 = cdriver.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/b/img)[3]")
								.getAttribute("src");
						option2 = cdriver.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/b/img)[2]")
								.getAttribute("src");
						option1 = cdriver.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/b/img)[1]")
								.getAttribute("src");

						CorrectAnswer = cdriver
								.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + start + "]").getText();

						// System.out.println(CorrectAnswer);

						if (CorrectAnswer.contains(NoCorrectAnsCS)) {
							CorrectAnswer = "NO CORRECT ANSWER";
						} else if (CorrectAnswer.contains(ImageNotDisplayedInCA)) {
							CorrectAnswer = "Image Not Displayed";
						} else if (CorrectAnswer.contains("OMITTED FROM EVALUATION")) {
							CorrectAnswer = "OMITTED FROM EVALUATIONd";
						} else {
							CorrectAnswer = cdriver
									.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + start + "]/b/img")
									.getAttribute("src");
							/*
							 * int slash = 0; for(slash = CorrectAnswer.length()-1; slash>=0; slash--) {
							 * if(CorrectAnswer.charAt(slash)=='/') { break; } } CorrectAnswer = new
							 * StringBuffer(CorrectAnswer).replace(0, slash, "").toString();
							 */
						}
						// System.out.println(CorrectAnswer);
					} catch (NoSuchElementException e) {

						try {
							if (optionFive == true) {
								option5 = cdriver
										.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/img)[5]")
										.getAttribute("src");
							}
							option4 = cdriver
									.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/img)[4]")
									.getAttribute("src");
							int slash = 0;
							for (slash = option4.length() - 1; slash >= 0; slash--) {
								if (option4.charAt(slash) == '/') {
									break;
								}
							}
							option4 = new StringBuffer(option4).replace(0, slash, "").toString();

							option3 = cdriver
									.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/img)[3]")
									.getAttribute("src");
							slash = 0;
							for (slash = option3.length() - 1; slash >= 0; slash--) {
								if (option3.charAt(slash) == '/') {
									break;
								}
							}
							option3 = new StringBuffer(option3).replace(0, slash, "").toString();

							option2 = cdriver
									.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/img)[2]")
									.getAttribute("src");
							slash = 0;
							for (slash = option2.length() - 1; slash >= 0; slash--) {
								if (option2.charAt(slash) == '/') {
									break;
								}
							}
							option2 = new StringBuffer(option2).replace(0, slash, "").toString();

							option1 = cdriver
									.findElementByXPath("(((//b[text()='Options:'])/..)[" + start + "]/img)[1]")
									.getAttribute("src");
							slash = 0;
							for (slash = option1.length() - 1; slash >= 0; slash--) {
								if (option1.charAt(slash) == '/') {
									break;
								}
							}
							option1 = new StringBuffer(option1).replace(0, slash, "").toString();

							// System.out.println("option1 = "+option1);

							CorrectAnswer = cdriver
									.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + start + "]")
									.getText();

							// System.out.println(CorrectAnswer);

							if (CorrectAnswer.contains(NoCorrectAnsCS)) {
								CorrectAnswer = "No Correct Answer ( Benefit to all )";
							} else if (CorrectAnswer.contains(ImageNotDisplayedInCA)) {
								CorrectAnswer = "Image Not Displayed";
							} else if (CorrectAnswer.contains("Considered for Grace marks")) {
								CorrectAnswer = "Considered for Grace marks";
							} else if (CorrectAnswer.contains("Omitted from Evaluation")) {
								CorrectAnswer = "Omitted from Evaluation";
							} else {
								CorrectAnswer = cdriver
										.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + start + "]/img")
										.getAttribute("src");
								slash = 0;
								for (slash = CorrectAnswer.length() - 1; slash >= 0; slash--) {
									if (CorrectAnswer.charAt(slash) == '/') {
										break;
									}
								}
								CorrectAnswer = new StringBuffer(CorrectAnswer).replace(0, slash, "").toString();
							}
							// System.out.println(CorrectAnswer);

							if (testOutput == true) {
								System.out.println("Correct Answer is " + CorrectAnswer);
								System.out.println("CorrectAnswer length is: " + CorrectAnswer.length());
							}

						} catch (NoSuchElementException e1) {

							mixture = true;
						}
					}

					// System.out.println("mixture = "+mixture);

					if (mixture == true) {
						String CorAnsCS = "", CanAnsCS = "", txt = "text", img = "image";

						CorrectAnswer = cdriver
								.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + start + "]").getText();

						CorrectAnswer = new StringBuffer(CorrectAnswer).replace(0, 20, "").toString();

						boolean correctAnsEndSpecial;

						if (CorrectAnswer.length() != 0) {
							correctAnsEndSpecial = false;
							correctAnsEndSpecial = String.valueOf(CorrectAnswer.charAt(CorrectAnswer.length() - 1))
									.matches("\\s");
							if (correctAnsEndSpecial == true) {
								StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswer);
								CorrectAnswer = NewCorrectAnswer.deleteCharAt(CorrectAnswer.length() - 1).toString();
							}

						}

						if (CorrectAnswer.length() != 0) {
							correctAnsEndSpecial = false;
							correctAnsEndSpecial = String.valueOf(CorrectAnswer.charAt(CorrectAnswer.length() - 1))
									.matches("\\s");
							if (correctAnsEndSpecial == true) {
								StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswer);
								CorrectAnswer = NewCorrectAnswer.deleteCharAt(CorrectAnswer.length() - 1).toString();
							}

						}

						if (CorrectAnswer.contains(NoCorrectAnsCS)) {
							CorrectAnswer = "NO CORRECT ANSWER";
						} else if (CorrectAnswer.contains(ImageNotDisplayedInCA)) {
							CorrectAnswer = "Image Not Displayed";
						} else if (CorrectAnswer.contains("Considered for Grace marks")) {
							CorrectAnswer = "Considered for Grace marks";
						} else if (CorrectAnswer.length() >= 1) {
							CorAnsCS = "text";
							// System.out.println(CorrectAnswer);
						} else if (CorrectAnswer.length() == 0) {
							try {
								CorrectAnswer = cdriver
										.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + start + "]/b/img")
										.getAttribute("src");
								int slash = 0;
								for (slash = CorrectAnswer.length() - 1; slash >= 0; slash--) {
									if (CorrectAnswer.charAt(slash) == '/') {
										break;
									}
								}
								CorrectAnswer = new StringBuffer(CorrectAnswer).replace(0, slash, "").toString();
								CorAnsCS = "image";
							} catch (NoSuchElementException e) {

								try {
									CorrectAnswer = cdriver
											.findElementByXPath(
													"((//b[text()='Correct Answer:'])/..)[" + start + "]/img")
											.getAttribute("src");
									int slash = 0;
									for (slash = CorrectAnswer.length() - 1; slash >= 0; slash--) {
										if (CorrectAnswer.charAt(slash) == '/') {
											break;
										}
									}
									CorrectAnswer = new StringBuffer(CorrectAnswer).replace(0, slash, "").toString();
									CorAnsCS = "image";

								} catch (NoSuchElementException e2) {

									CorrectAnswer = "EMPTY";
								}
							}
						}

						if (testOutput == true) {

							System.out.println("CorrectAnswer = " + CorrectAnswer);

						}

						// Get Candidate Answer from Challenge URL
						String CandidateAnswerCS = "";

						try {
							CandidateAnswerCS = cdriver
									.findElementByXPath("(((//b[text()='Candidate Answer:'])/..)[" + start + "])/b/img")
									.getAttribute("src");
							int slash = 0;
							for (slash = CandidateAnswerCS.length() - 1; slash >= 0; slash--) {
								if (CandidateAnswerCS.charAt(slash) == '/') {
									break;
								}
							}
							CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, slash, "").toString();
							CanAnsCS = "image";

						} catch (NoSuchElementException e1) {

							try {
								CandidateAnswerCS = cdriver
										.findElementByXPath(
												"(((//b[text()='Candidate Answer:'])/..)[" + start + "])/img")
										.getAttribute("src");
								int slash = 0;
								for (slash = CandidateAnswerCS.length() - 1; slash >= 0; slash--) {
									if (CandidateAnswerCS.charAt(slash) == '/') {
										break;
									}
								}
								CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, slash, "")
										.toString();
								CanAnsCS = "image";

							} catch (NoSuchElementException e11) {

								CandidateAnswerCS = cdriver
										.findElementByXPath("((//b[text()='Candidate Answer:'])/..)[" + start + "]")
										.getText();
								CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, 18, "").toString();

								// For few Questions, the Candidate answer has an enter Key at the end which
								// added an extra char at the end. So, deleting that char.
								boolean cachalspecial = String
										.valueOf(CandidateAnswerCS.charAt(CandidateAnswerCS.length() - 1))
										.matches("\\s");
								if (cachalspecial == true) {
									StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerCS);
									CandidateAnswerCS = NewCandidateAnswer.deleteCharAt(CandidateAnswerCS.length() - 1)
											.toString();
									// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
								}
								CanAnsCS = "text";
							}
						}

						if (testOutput == true) {

							System.out.println("CandidateAnswerCS = " + CandidateAnswerCS);

						}

						// Get Options for each case of image and text combination
						option1 = "";
						option2 = "";
						option3 = "";
						option4 = "";
						option5 = "";

						{
							int op1count = 0, op2count = 0, op3count = 0, op4count = 0, op5count = 0;
							String op1ti = "", op2ti = "", op3ti = "", op4ti = "", op5ti = "", optxt = "text",
									opim = "image";

							getOptionTextCS();
							option1 = optionText[0];
							option2 = optionText[1];
							option3 = optionText[2];
							option4 = optionText[3];
							if (optionFive == true) {
								option5 = optionText[4];
							}

							{

								// For few Questions, the Options has an enter Key at the end which added an
								// extra char at the end. So, deleting that char.
								boolean optionEndSpecial, optionStartEpecial;

								optionStartEpecial = false;
								optionStartEpecial = String.valueOf(option1.charAt(0)).matches("\\s");
								if (optionStartEpecial == true) {
									StringBuilder Newoption1 = new StringBuilder(option1);
									option1 = Newoption1.deleteCharAt(0).toString();
								}

								if (option1.length() != 0) {
									optionEndSpecial = false;
									optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption1 = new StringBuilder(option1);
										option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
									}
								}
								optionEndSpecial = false;
								if (option1.length() != 0) {
									optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption1 = new StringBuilder(option1);
										option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
									}
								}
								optionEndSpecial = false;
								if (option1.length() != 0) {
									optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption1 = new StringBuilder(option1);
										option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
									}
								}
								optionEndSpecial = false;
								if (option1.length() != 0) {
									optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption1 = new StringBuilder(option1);
										option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
									}
								}
								optionEndSpecial = false;
								if (option1.length() != 0) {
									optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption1 = new StringBuilder(option1);
										option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
									}
								}
								optionEndSpecial = false;
								if (option2.length() != 0) {
									optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption2 = new StringBuilder(option2);
										option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
									}
								}
								optionEndSpecial = false;
								if (option2.length() != 0) {
									optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption2 = new StringBuilder(option2);
										option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
									}
								}
								optionEndSpecial = false;
								if (option2.length() != 0) {
									optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption2 = new StringBuilder(option2);
										option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
									}
								}
								optionEndSpecial = false;

								optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1)).matches("\\s");
								if (optionEndSpecial == true) {
									StringBuilder Newoption3 = new StringBuilder(option3);
									option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

								}

								optionEndSpecial = false;
								if (option4.length() != 0) {
									optionEndSpecial = String.valueOf(option4.charAt(option4.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption4 = new StringBuilder(option4);
										option4 = Newoption4.deleteCharAt(option4.length() - 1).toString();
									}
								}

								if (optionFive == true) {
									optionEndSpecial = false;
									optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption5 = new StringBuilder(option5);
										option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
									}
									optionEndSpecial = false;
									optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption5 = new StringBuilder(option5);
										option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
									}
									optionEndSpecial = false;
									optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption5 = new StringBuilder(option5);
										option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
									}
								}

							}

							if (testOutput == true) {

								System.out.println("option1 length = " + option1.length());
								System.out.println("option2 length = " + option2.length());
								System.out.println("option3 length = " + option3.length());
								System.out.println("option4 length = " + option4.length());

							}

							if (option1.length() == 0) {
								for (int i = 0; i < option1.length(); i++) {
									boolean op1 = String.valueOf(option1.charAt(i)).matches("[A-Za-z0-9]");

									if (op1 == true) {
										op1count++;
									}
								}
								if (op1count == 0) {
									op1ti = "image";
								} else {
									op1ti = "text";
								}
							} else {
								op1ti = "text";
							}

							if (option2.length() == 0) {
								for (int i = 0; i < option2.length(); i++) {
									boolean op2 = String.valueOf(option2.charAt(i)).matches("[A-Za-z0-9]");
									if (op2 == true) {
										op2count++;
									}
								}
								if (op2count == 0) {
									op2ti = "image";
								} else {
									op2ti = "text";
								}
							} else {
								op2ti = "text";
							}

							if (option3.length() == 0) {
								for (int i = 0; i < option3.length(); i++) {
									boolean op3 = String.valueOf(option3.charAt(i)).matches("[A-Za-z0-9]");
									if (op3 == true) {
										op3count++;
									}
								}

								if (op3count == 0) {
									op3ti = "image";
								} else {
									op3ti = "text";
								}
							} else {
								boolean optionEndSpecial = false;
								if (option3.length() != 0) {
									optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption3 = new StringBuilder(option3);
										option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

									}
								}
								optionEndSpecial = false;
								if (option3.length() != 0) {
									optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1))
											.matches("\\s");
									if (optionEndSpecial == true) {
										StringBuilder Newoption3 = new StringBuilder(option3);
										option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

									}
								}

								for (int i = 0; i < option3.length(); i++) {
									boolean op3 = String.valueOf(option3.charAt(i)).matches("[A-Za-z0-9]");
									if (op3 == true) {
										op3count++;
									}
								}

								if (op3count == 0) {
									op3ti = "image";
								} else {
									op3ti = "text";
								}
							}

							if (option4.length() == 0) {
								for (int i = 0; i < option4.length(); i++) {
									boolean op4 = String.valueOf(option4.charAt(i)).matches("[A-Za-z0-9]");
									if (op4 == true) {
										op4count++;
									}
								}
								if (op4count == 0) {
									op4ti = "image";
								} else {
									op4ti = "text";
								}
							} else {
								op4ti = "text";
							}

							if (optionFive == true) {
								for (int i = 0; i < option5.length(); i++) {
									boolean op5 = String.valueOf(option5.charAt(i)).matches("[A-Za-z0-9]");
									if (op5 == true)
										op5count++;
								}
								if (op5count == 0) {
									op5ti = "image";
								} else {
									op5ti = "text";
								}
							}

							if (testOutput == true) {

								System.out.println("op1ti = " + op1ti);
								System.out.println("op2ti = " + op2ti);
								System.out.println("op3ti = " + op3ti);
								System.out.println("op4ti = " + op4ti);

							}

							if ((op1ti.equals(img)) && (op2ti.equals(img)) && (op3ti.equals(img))
									&& (op4ti.equals(txt))) {
								option1 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option2 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								option3 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[3]")
										.getAttribute("src");
								FullURL = option1;
								getImageFileName();
								option1 = FullURL;
								FullURL = option2;
								getImageFileName();
								option2 = FullURL;
								FullURL = option3;
								getImageFileName();
								option3 = FullURL;
							} else if ((op1ti.equals(img)) && (op2ti.equals(txt)) && (op3ti.equals(img))
									&& (op4ti.equals(img))) {
								option1 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option3 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								option4 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[3]")
										.getAttribute("src");
								FullURL = option1;
								getImageFileName();
								option1 = FullURL;
								FullURL = option3;
								getImageFileName();
								option3 = FullURL;
								FullURL = option4;
								getImageFileName();
								option4 = FullURL;

							} else if ((op1ti.equals(img)) && (op2ti.equals(img)) && (op3ti.equals(txt))
									&& (op4ti.equals(img))) {
								option1 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option2 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								option4 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[3]")
										.getAttribute("src");
								FullURL = option1;
								getImageFileName();
								option1 = FullURL;
								FullURL = option2;
								getImageFileName();
								option2 = FullURL;
								FullURL = option4;
								getImageFileName();
								option4 = FullURL;

							} else if ((op1ti.equals(txt)) && (op2ti.equals(img)) && (op3ti.equals(img))
									&& (op4ti.equals(img))) {
								option2 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option3 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								option4 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[3]")
										.getAttribute("src");
								FullURL = option2;
								getImageFileName();
								option2 = FullURL;
								FullURL = option3;
								getImageFileName();
								option3 = FullURL;
								FullURL = option4;
								getImageFileName();
								option4 = FullURL;

							} else if ((op1ti.equals(img)) && (op2ti.equals(img)) && (op3ti.equals(txt))
									&& (op4ti.equals(txt))) {
								option1 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option2 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								FullURL = option1;
								getImageFileName();
								option1 = FullURL;
								FullURL = option2;
								getImageFileName();
								option2 = FullURL;

							} else if ((op1ti.equals(txt)) && (op2ti.equals(img)) && (op3ti.equals(img))
									&& (op4ti.equals(txt))) {
								option2 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option3 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								FullURL = option2;
								getImageFileName();
								option2 = FullURL;
								FullURL = option3;
								getImageFileName();
								option3 = FullURL;

							} else if ((op1ti.equals(txt)) && (op2ti.equals(txt)) && (op3ti.equals(img))
									&& (op4ti.equals(img))) {
								option3 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option4 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								FullURL = option3;
								getImageFileName();
								option3 = FullURL;
								FullURL = option4;
								getImageFileName();
								option4 = FullURL;

							} else if ((op1ti.equals(img)) && (op2ti.equals(txt)) && (op3ti.equals(img))
									&& (op4ti.equals(txt))) {
								option1 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option3 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								FullURL = option1;
								getImageFileName();
								option1 = FullURL;
								FullURL = option3;
								getImageFileName();
								option3 = FullURL;

							} else if ((op1ti.equals(txt)) && (op2ti.equals(img)) && (op3ti.equals(txt))
									&& (op4ti.equals(img))) {
								option2 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option4 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								FullURL = option2;
								getImageFileName();
								option2 = FullURL;
								FullURL = option4;
								getImageFileName();
								option4 = FullURL;

							} else if ((op1ti.equals(img)) && (op2ti.equals(txt)) && (op3ti.equals(txt))
									&& (op4ti.equals(img))) {
								option1 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								option4 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[2]")
										.getAttribute("src");
								FullURL = option1;
								getImageFileName();
								option1 = FullURL;
								FullURL = option4;
								getImageFileName();
								option4 = FullURL;

							} else if ((op1ti.equals(txt)) && (op2ti.equals(txt)) && (op3ti.equals(txt))
									&& (op4ti.equals(img))) {
								option4 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								FullURL = option4;
								getImageFileName();
								option4 = FullURL;

							} else if ((op1ti.equals(txt)) && (op2ti.equals(txt)) && (op3ti.equals(img))
									&& (op4ti.equals(txt))) {
								option3 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								FullURL = option3;
								getImageFileName();
								option3 = FullURL;

							} else if ((op1ti.equals(txt)) && (op2ti.equals(img)) && (op3ti.equals(txt))
									&& (op4ti.equals(txt))) {
								option2 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								FullURL = option2;
								getImageFileName();
								option2 = FullURL;

							} else if ((op1ti.equals(img)) && (op2ti.equals(txt)) && (op3ti.equals(txt))
									&& (op4ti.equals(txt))) {
								option1 = cdriver
										.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]/img[1]")
										.getAttribute("src");
								FullURL = option1;
								getImageFileName();
								option1 = FullURL;
							}

						}

					}

					if (testOutput == true) {
						System.out.println("option1 = " + option1);
						System.out.println("option2 = " + option2);
						System.out.println("option3 = " + option3);
						System.out.println("option4 = " + option4);
					}

					{

						// For few Questions, the Options has an enter Key at the end which added an
						// extra char at the end. So, deleting that char.
						boolean optionEndSpecial, optionStartEpecial;

						optionStartEpecial = false;
						optionStartEpecial = String.valueOf(option1.charAt(0)).matches("\\s");
						if (optionStartEpecial == true) {
							StringBuilder Newoption1 = new StringBuilder(option1);
							option1 = Newoption1.deleteCharAt(0).toString();
						}

						optionEndSpecial = false;
						optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
						if (optionEndSpecial == true) {
							StringBuilder Newoption1 = new StringBuilder(option1);
							option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
						}
						optionEndSpecial = false;
						if (option1.length() != 0) {
							optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption1 = new StringBuilder(option1);
								option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
							}
						}
						optionEndSpecial = false;
						if (option1.length() != 0) {
							optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption1 = new StringBuilder(option1);
								option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
							}
						}
						optionEndSpecial = false;
						if (option1.length() != 0) {
							optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption1 = new StringBuilder(option1);
								option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
							}
						}
						optionEndSpecial = false;
						if (option1.length() != 0) {
							optionEndSpecial = String.valueOf(option1.charAt(option1.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption1 = new StringBuilder(option1);
								option1 = Newoption1.deleteCharAt(option1.length() - 1).toString();
							}
						}
						optionEndSpecial = false;
						if (option2.length() != 0) {
							optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption2 = new StringBuilder(option2);
								option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
							}
						}
						optionEndSpecial = false;
						if (option2.length() != 0) {
							optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption2 = new StringBuilder(option2);
								option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
							}
						}
						optionEndSpecial = false;
						if (option2.length() != 0) {
							optionEndSpecial = String.valueOf(option2.charAt(option2.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption2 = new StringBuilder(option2);
								option2 = Newoption2.deleteCharAt(option2.length() - 1).toString();
							}
						}
						optionEndSpecial = false;

						optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1)).matches("\\s");
						if (optionEndSpecial == true) {
							StringBuilder Newoption3 = new StringBuilder(option3);
							option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

						}
						optionEndSpecial = false;
						optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1)).matches("\\s");
						if (optionEndSpecial == true) {
							StringBuilder Newoption3 = new StringBuilder(option3);
							option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

						}
						optionEndSpecial = false;
						if (option3.length() != 0) {
							optionEndSpecial = String.valueOf(option3.charAt(option3.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption3 = new StringBuilder(option3);
								option3 = Newoption3.deleteCharAt(option3.length() - 1).toString();

							}
						}
						optionEndSpecial = false;
						if (option4.length() != 0) {
							optionEndSpecial = String.valueOf(option4.charAt(option4.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption4 = new StringBuilder(option4);
								option4 = Newoption4.deleteCharAt(option4.length() - 1).toString();
							}
						}
						optionEndSpecial = false;
						if (option4.length() != 0) {
							optionEndSpecial = String.valueOf(option4.charAt(option4.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption4 = new StringBuilder(option4);
								option4 = Newoption4.deleteCharAt(option4.length() - 1).toString();
							}
						}
						optionEndSpecial = false;
						if (option4.length() != 0) {
							optionEndSpecial = String.valueOf(option4.charAt(option4.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption4 = new StringBuilder(option4);
								option4 = Newoption4.deleteCharAt(option4.length() - 1).toString();
							}
						}
						if (optionFive == true) {
							optionEndSpecial = false;
							optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption5 = new StringBuilder(option5);
								option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
							}
							optionEndSpecial = false;
							optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption5 = new StringBuilder(option5);
								option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
							}
							optionEndSpecial = false;
							optionEndSpecial = String.valueOf(option5.charAt(option5.length() - 1)).matches("\\s");
							if (optionEndSpecial == true) {
								StringBuilder Newoption5 = new StringBuilder(option5);
								option5 = Newoption5.deleteCharAt(option5.length() - 1).toString();
							}
						}

					}
				}

				String Benefit = "NO CORRECT ANSWER";
				String EBenefit = "NO CORRECT ANSWER";

				// Get Answer option from Master

//				String ExcelKey = fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[6]").getText();
				String ExcelKey = "";
				try {
					ExcelKey = fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[6]").getText().trim();
				} catch (Exception e) {
					ExcelKey = fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[5]").getText().trim();
				}

				String empty = "";
				if (ExcelKey.equals(empty)) {
					ExcelKey = "EMPTY";
				}

				if (testOutput == true) {
					System.out.println("Excel Key is " + ExcelKey);
				}

				if (testOutput == true) {
					System.out.println("option1 = " + option1);
					System.out.println("option2 = " + option2);
					System.out.println("option3 = " + option3);
					System.out.println("option4 = " + option4);
				}
				// Compare Correct Answer with Options and Master QP

				{

					// if(option4.contains("5)"))
					// {
					// WriteInExcelCSM(RollNo, QID, CorrectAnswer, "Question has 5th option. Pls
					// check", "", "FAIL");
					// }

					// else
					{

						if ((option1.equals(CorrectAnswer)) && (ExcelKey.charAt(1) == 'A')) {
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 1 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "1", ExcelKey, "PASS");

						} else if ((CorrectAnswer.contains(option1)) && (ExcelKey.charAt(1) == 'A')) {
							System.err.println(
									"------------------------------------------------------------------------");
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 1 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);
							System.out.println(
									"Option matched with Contains logic. Please check it manually once to confirm.");
							System.err.println(
									"------------------------------------------------------------------------");

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "1", ExcelKey,
									"Correct Answer Contains Option 1");
						} else if ((option2.equals(CorrectAnswer)) && (ExcelKey.charAt(1) == 'B')) {
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 2 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "2", ExcelKey, "PASS");
						} else if ((CorrectAnswer.contains(option2)) && (ExcelKey.charAt(1) == 'B')) {
							System.err.println(
									"------------------------------------------------------------------------");
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 2 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);
							System.out.println(
									"Option matched with Contains logic. Please check it manually once to confirm.");
							System.err.println(
									"------------------------------------------------------------------------");

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "2", ExcelKey,
									"Correct Answer Contains Option 2");
						} else if ((option3.equals(CorrectAnswer)) && (ExcelKey.charAt(1) == 'C')) {
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 3 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "3", ExcelKey, "PASS");
						} else if ((CorrectAnswer.contains(option3)) && (ExcelKey.charAt(1) == 'C')) {
							System.err.println(
									"------------------------------------------------------------------------");
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 3 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);
							System.out.println(
									"Option matched with Contains logic. Please check it manually once to confirm.");
							System.err.println(
									"------------------------------------------------------------------------");

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "3", ExcelKey,
									"Correct Answer Contains Option 3");
						} else if ((option4.equals(CorrectAnswer)) && (ExcelKey.charAt(1) == 'D')) {
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 4 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "4", ExcelKey, "PASS");
						} else if ((CorrectAnswer.contains(option4)) && (ExcelKey.charAt(1) == 'D')) {
							System.err.println(
									"------------------------------------------------------------------------");
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 4 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);
							System.out.println(
									"Option matched with Contains logic. Please check it manually once to confirm.");
							System.err.println(
									"------------------------------------------------------------------------");

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "4", ExcelKey,
									"Correct Answer Contains Option 4");
						} else if ((option5.equals(CorrectAnswer)) && (ExcelKey.charAt(1) == 'E')) {
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 5 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "5", ExcelKey, "PASS");
						} else if ((CorrectAnswer.contains(option5)) && (ExcelKey.charAt(1) == 'E')) {
							System.err.println(
									"------------------------------------------------------------------------");
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is matched with Option 5 in Challenge System.");
							System.out.println("The Correct Option in Master Key is " + ExcelKey);
							System.out.println(
									"Option matched with Contains logic. Please check it manually once to confirm.");
							System.err.println(
									"------------------------------------------------------------------------");

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "5", ExcelKey,
									"Correct Answer Contains Option 5");
						} else if ((CorrectAnswer.equals(Benefit)) && (ExcelKey.equals(EBenefit))) {
							System.out.println("QID: " + QID);
							System.out.println("The Correct Answer is " + CorrectAnswer);
							System.out.println("The Correct Option in Master Key is " + ExcelKey);

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "", ExcelKey, "PASS");
						} else {
							System.err.println(
									"------------------------------------------------------------------------");
							System.err.println("QID: " + QID);
							System.err.println("The Correct Answer in Challenge System does not match with Master Key");
							System.err.println("The Correct Answer in Challenge System is: " + CorrectAnswer);
							System.err.println("The Correct Option in Master Key is " + ExcelKey);
							System.err.println(
									"------------------------------------------------------------------------");

							WriteInExcelCSM(RollNo, QID, CorrectAnswer, "", ExcelKey, "FAIL");

						}
					}
				}
			} else {
				System.err.println("QID Mismatch");
				System.err.println("Challenge System: " + QID);
				System.err.println("Master Copy: " + QIDMaster);

				WriteInExcelCSM(RollNo, QID, "", "", "", "QID-Mismatch-FAIL");
			}

		}
//		cdriver.quit();
//
//		fdriver.quit();

	}

	public void createNotepadACS(String RollNo) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter("F:\\Results\\AdminWithChallengeSystem\\TestResult_" + RollNo + ".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);

		System.out.println("---------------------------------------------------------------");
		System.out.println("Test Result for the Roll no: " + RollNo);
		System.out.println("AvailableQuestions in Post Exam Link : " + AvailableQuestionsPE.size());
		System.out.println("---------------------------------------------------------------");

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("Admin with Challenge System");
		bw.newLine();
		bw.write("Test Result for the Roll no: " + RollNo);
		bw.newLine();
		bw.write("AvailableQuestions in Post Exam Link : " + AvailableQuestionsPE.size());
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
	}

	public void compareCandidateAnswers(String RollNo, String SubjectNum) throws IOException, InterruptedException {

		int WrongAnswerCount = 0;

		/*
		 * if((SubjectNum.equals("203"))||(SubjectNum.equals("204"))) { Select
		 * subjectDrop = new Select(fdriver.findElementById("subject_sltd"));
		 * subjectDrop.selectByIndex(1); Thread.sleep(2000);
		 * 
		 * }
		 */

		getFirstQIDinCS();

		for (start = 1; start <= AvailableQuestionsPE.size(); start++) {

			String CanAnsCS = "", CorAnsCS = "";

			boolean ImageCheck = true;
			String FullText = cdriver.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]").getText();

			// Get Question Text and check it is not null

			String QuestionText = cdriver.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]/b[1]")
					.getText();

			if (QuestionText.length() < 25) {
				System.err.println("Incomplete Question");
			}

			// To check if the Question is image based.
			try {
				cdriver.findElementByXPath("((//td[@class='greybluetext10'])[" + start + "]//img)");
			} catch (NoSuchElementException e) {

				try {
//					cdriver.findElementByXPath("((//td[@class='greybluetext10'])["+start+"]/b/img)[2]");13/03/2024 we found images only in the question area so we replace the xpath with below one
					cdriver.findElementByXPath(
							"((//td[@class='greybluetext10'])[9]/b)[contains(text(),'Options')]/img");
				} catch (NoSuchElementException e1) {

					ImageCheck = false;
				}
			}

			// To get QID
			boolean loop = true;
			String QID = "";
			int i = 0;

			int tempStart = 0;
			int tempEnd = 0;
			do {

				if (FullText.charAt(i) == '[') {

					tempStart = i;
					loop = false;

				}
				i++;
			} while (loop == true);

			loop = true;
			i = 0;
			do {
				if (FullText.charAt(i) == ']') {
					tempEnd = i;
					loop = false;

				}
				i++;
			} while (loop == true);

			String tempQID = FullText.substring(tempStart + 2, tempEnd - 1);

			/*
			 * do { if (FullText.charAt(i)=='[') { i++;
			 * 
			 * if (FullText.charAt(i)==' ') { i++; QID = QID + FullText.charAt(i); i++; if
			 * (FullText.charAt(i)==' ') { loop = false; } else { QID = QID +
			 * FullText.charAt(i); i++; if (FullText.charAt(i)==' ') { loop = false; } else
			 * { QID = QID + FullText.charAt(i); i++; if (FullText.charAt(i)==' ') { loop =
			 * false; } else { QID = QID + FullText.charAt(i); i++; if
			 * (FullText.charAt(i)==' ') { loop = false; } } } } } } i++;
			 * 
			 * 
			 * }while(loop == true);
			 * 
			 */

			QID = tempQID;

//			System.out.println("expected... 1504" + QID);

			System.out.println(QID);
			String failId = QID;
			int QIDint = Integer.parseInt(QID);
			QIDint = QIDint - QNOint;
			QID = Integer.toString(QIDint);

//			System.out.println("expected... 4" + QID);

//			System.out.println("QID: " + QID);

			// Get Candidate Answer from Challenge URL
			String CandidateAnswerCS = "";

			// handles when QID is not there in CS
			try {
				CandidateAnswerCS = fdriver.findElementByXPath("((//b[text()='Candidate Answer:'])/..)[" + QID + "]")
						.getText();
			} catch (Exception e) {
				System.out.println("QID NOT FOUND : " + failId);
				WriteInExcelACS(RollNo, failId, "", "", "QID NOT FOUND");
				continue;
			}

			try {
				CandidateAnswerCS = fdriver
						.findElementByXPath("(((//b[text()='Candidate Answer:'])/..)[" + QID + "])/b/img")
						.getAttribute("src");
				int slash = 0;
				for (slash = CandidateAnswerCS.length() - 1; slash >= 0; slash--) {
					if (CandidateAnswerCS.charAt(slash) == '/') {
						break;
					}
				}
				CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, slash, "").toString();
				CanAnsCS = "image";

			} catch (NoSuchElementException e1) {

				try {
					CandidateAnswerCS = fdriver
							.findElementByXPath("(((//b[text()='Candidate Answer:'])/..)[" + QID + "])/img")
							.getAttribute("src");
					int slash = 0;
					for (slash = CandidateAnswerCS.length() - 1; slash >= 0; slash--) {
						if (CandidateAnswerCS.charAt(slash) == '/') {
							break;
						}
					}
					CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, slash, "").toString();
					CanAnsCS = "image";

				} catch (NoSuchElementException e11) {

					CandidateAnswerCS = fdriver
							.findElementByXPath("((//b[text()='Candidate Answer:'])/..)[" + QID + "]").getText();
					CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, 24, "").toString();

					// For few Questions, the Candidate answer has an enter Key at the end which
					// added an extra char at the end. So, deleting that char.
					boolean cachalspecial = String.valueOf(CandidateAnswerCS.charAt(CandidateAnswerCS.length() - 1))
							.matches("\\s");
					if (cachalspecial == true) {
						StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerCS);
						CandidateAnswerCS = NewCandidateAnswer.deleteCharAt(CandidateAnswerCS.length() - 1).toString();
						// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
					}
					cachalspecial = String.valueOf(CandidateAnswerCS.charAt(CandidateAnswerCS.length() - 1))
							.matches("\\s");
					if (cachalspecial == true) {
						StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerCS);
						CandidateAnswerCS = NewCandidateAnswer.deleteCharAt(CandidateAnswerCS.length() - 1).toString();
						// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
					}
					CanAnsCS = "text";

				}
			}

			String IncompNotAns = "ANSWERED ]";
			if (CandidateAnswerCS.equals(IncompNotAns)) {
				CandidateAnswerCS = "NOT ANSWERED";
			}
			// System.out.println("Candidate Answer from Challenge System is " +
			// CandidateAnswer);

			// Get Correct Answer from Challenge URL
			String CorrectAnswerCS = "";

			try {
				CorrectAnswerCS = fdriver
						.findElementByXPath("(((//b[text()='Correct Answer:'])/..)[" + QID + "])/b/img")
						.getAttribute("src");
				int slash = 0;
				for (slash = CorrectAnswerCS.length() - 1; slash >= 0; slash--) {
					if (CorrectAnswerCS.charAt(slash) == '/') {
						break;
					}
				}
				CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, slash, "").toString();
				CorAnsCS = "image";

			} catch (NoSuchElementException e11) {

				try {
					CorrectAnswerCS = fdriver
							.findElementByXPath("(((//b[text()='Correct Answer:'])/..)[" + QID + "])/img")
							.getAttribute("src");
					int slash = 0;
					for (slash = CorrectAnswerCS.length() - 1; slash >= 0; slash--) {
						if (CorrectAnswerCS.charAt(slash) == '/') {
							break;
						}
					}
					CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, slash, "").toString();
					CorAnsCS = "image";

				} catch (NoSuchElementException e12) {

					CorrectAnswerCS = fdriver.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + QID + "]")
							.getText();

					CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, 22, "").toString();

					// CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, 24,
					// "").toString();

					// System.out.println("CorrectAnswerCS = "+CorrectAnswerCS);

					// For few Questions, the Candidate answer has an enter Key at the end which
					// added an extra char at the end. So, deleting that char.
					boolean coAnChalspecial = false;
					try {
						coAnChalspecial = String.valueOf(CorrectAnswerCS.charAt(CorrectAnswerCS.length() - 1))
								.matches("\\s");

					} catch (StringIndexOutOfBoundsException IOB) {

					}
					if (coAnChalspecial == true) {
						StringBuilder NewCorrectAnswerCS = new StringBuilder(CorrectAnswerCS);
						CorrectAnswerCS = NewCorrectAnswerCS.deleteCharAt(CorrectAnswerCS.length() - 1).toString();
						// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
					}
					CorAnsCS = "text";

				}
			}

			String NoCorrectBenefit = "No Correct Answer ( Benefit to all )";
			if (CorrectAnswerCS.equals(NoCorrectBenefit)) {
				CorrectAnswerCS = "No Correct Answer ( Benefit to all )";
			}

			// System.out.println(CorrectAnswerCS);

			// To get Correct Answer from Post Exam

			if (ImageCheck == false) {
				String CorrectAnswerPostExam = "";
				int CorrectAnsStart = 0;
				int j = (FullText.length() - 1);
				loop = true;
				boolean correctincorrect = false;
				if (FullText.contains("Omitted Question")) {
					CorrectAnswerPostExam = "Omitted Question";
				} else {
					do {
						// if (FullText.charAt(j)==' ')
						{
							if (FullText.charAt(j) == ':') {
								if (FullText.charAt(j - 1) == ' ') {
									if (FullText.charAt(j - 2) == 'r') {
										if (FullText.charAt(j - 3) == 'e') {
											if (FullText.charAt(j - 4) == 'w') {
												if (FullText.charAt(j - 5) == 's') {

													CorrectAnsStart = j + 1;

													if (FullText.charAt(j + 1) == ' ') {
														CorrectAnsStart = j + 2;
													}

													for (int k = j + 1; k < FullText.length(); k++) {
														CorrectAnswerPostExam = CorrectAnswerPostExam
																+ FullText.charAt(k);
													}
													String CAinCA = "Correct Answer";
													String NoCorAns = "No Correct Answer";

													if (CorrectAnswerPostExam.contains(NoCorAns)) {
														CorrectAnswerPostExam = "No Correct Answer";
													} else if (CorrectAnswerPostExam.contains(CAinCA)) {
														CorrectAnswerPostExam = "EMPTY";
														correctincorrect = true;
													}
													// System.out.println("Correct Answer from Post Exam URL is "+
													// CorrectAnswer);
													loop = false;
												}
											}
										}
									}
								}
							}

						}
						j--;
					} while (loop == true);
				}

				// To get Candidate Answer from Post Exam URL
				String CandidateAnswerPostExam = "";
				String NotAnswered = "NOT ANSWERED";
				int l = 0;
				int CandidateAnsEnd = 0;
				if (FullText.contains(NotAnswered)) {
					CandidateAnswerPostExam = "NOT ANSWERED";
				} else if (FullText.contains("Omitted Question")) {
					CandidateAnswerPostExam = "Omitted Question";
				} else {
					if (correctincorrect == true) {
						CandidateAnsEnd = FullText.length() - 17;
						l = (FullText.length() - 17);
					} else {
						CandidateAnsEnd = CorrectAnsStart - 18;
						l = (CorrectAnsStart - 18);
					}

					loop = true;
					do {
						// if (FullText.charAt(l)==' ')
						{
							if (FullText.charAt(l) == ':') {
								if (FullText.charAt(l - 1) == ' ') {
									if (FullText.charAt(l - 2) == 'r') {
										if (FullText.charAt(l - 3) == 'e') {
											if (FullText.charAt(l - 4) == 'w') {
												if (FullText.charAt(l - 5) == 's') {
													int starting = l + 1;

													if (FullText.charAt(l + 1) == ' ') {
														starting = l + 2;
													}

													for (int k = starting; k < CandidateAnsEnd; k++) {
														CandidateAnswerPostExam = CandidateAnswerPostExam
																+ FullText.charAt(k);
													}
													// System.out.println("Candidate Answer from Post Exam is "+
													// CandidateAnswerPostExam);
													loop = false;
												}
											}
										}
									}
								}
							}
						}
						l--;

					} while (loop == true);
				}

				// System.out.println("CandidateAnswerPostExam"+CandidateAnswerPostExam);

				// For few Questions, the Candidate answer has an enter Key at the end which
				// added an extra char at the end. So, deleting that char.
				boolean caspecial = String.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length() - 1))
						.matches("\\s");
				if (caspecial == true) {
					StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
					CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length() - 1)
							.toString();
					// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
				}

				caspecial = String.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length() - 1))
						.matches("\\s");
				if (caspecial == true) {
					StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
					CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length() - 1)
							.toString();
					// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
				}

				boolean cospecial = String.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length() - 1))
						.matches("\\s");
				if (cospecial == true) {
					StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
					CorrectAnswerPostExam = NewCorrectAnswer.deleteCharAt(CorrectAnswerPostExam.length() - 1)
							.toString();
					// System.out.println("New Correct Answer is "+ CorrectAnswer);
				}

				cospecial = String.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length() - 1))
						.matches("\\s");
				if (cospecial == true) {
					StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
					CorrectAnswerPostExam = NewCorrectAnswer.deleteCharAt(CorrectAnswerPostExam.length() - 1)
							.toString();
					// System.out.println("New Correct Answer is "+ CorrectAnswer);
				}

				// Change to correct QID

				QIDint = Integer.parseInt(QID);
				QIDint = QIDint + QNOint;
				QID = Integer.toString(QIDint);

				// Checking Candidate Answer from PostExam to Challenge URL
				if (CandidateAnswerPostExam.equals(CandidateAnswerCS)) {

					System.out.println("QID: " + QID);
					System.out.println("CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
					System.out.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
					// System.out.println("CorrectAnswer: "+ CorrectAnswer);

					WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "PASS");

				} else {

					System.err.println("---------------------------------------------------------------");
					System.err.println("QID: " + QID);
					System.err.println("CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
					System.err.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
					System.err.println("---------------------------------------------------------------");
					WrongAnswerCount++;

					WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "FAIL");

//					Actions myAction = new Actions(cdriver);
//					WebElement target = cdriver.findElementByXPath("//b[contains(text(), 'Question id [ "+QID+" ]')]/..");
//					Point classname = target.getLocation();
//					int xcordi = classname.getX();			        
//					int ycordi = classname.getY();			        
//					myAction.moveToElement(target, xcordi, ycordi).click().build().perform();
//					FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE) , new File("./reports/images/AdminWithChallenge_"+RollNo+"_"+QID+".jpg"));		

				}

			} else {
				{
					// If the Question is image based.
					// To get Correct Answer
					String CorrectAnswerPostExam = "";
					String CandidateAnswerPostExam = "";
					String NotAnswered = "NOT ANSWERED";
					boolean NotAns = false;
					String NoCorrectAns = "No Correct Answer";

					// To Check if the Candidate answered the image based question or not.
					if (FullText.contains(NotAnswered)) {
						CandidateAnswerPostExam = "NOT ANSWERED";
						NotAns = true;
					} else if (FullText.contains("Omitted Question")) {
						CandidateAnswerPostExam = "Omitted Question";
						NotAns = true;
					}

					// To get Candidate Answer

					boolean mixture = false; // Some Question may contain both images and text in options.
					if (NotAns == false) {

						if (FullText.contains(NoCorrectAns)) {
							CorrectAnswerPostExam = "No Correct Answer";
							try {
								CandidateAnswerPostExam = cdriver
										.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]/img[5]")
										.getAttribute("src");

								int slash = 0;
								for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
									if (CandidateAnswerPostExam.charAt(slash) == '/') {
										break;
									}
								}
								CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam)
										.replace(0, slash, "").toString();

							} catch (NoSuchElementException e) {
								CandidateAnswerPostExam = "Question is mixture of Images and Text. Unable to Extract.";
								mixture = true;
							}
						} else {
							try {

								List<WebElement> allImages = cdriver
										.findElementsByXPath("((//td[@class='greybluetext10'])[" + start + "]/b/img)");

								CorrectAnswerPostExam = cdriver.findElementByXPath("((//td[@class='greybluetext10'])["
										+ start + "]/b/img)[" + allImages.size() + "]").getAttribute("src");
								CandidateAnswerPostExam = cdriver.findElementByXPath("((//td[@class='greybluetext10'])["
										+ start + "]/b/img)[" + (allImages.size() - 1) + "]").getAttribute("src");
								int slash = 0;
								for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
									if (CandidateAnswerPostExam.charAt(slash) == '/') {
										break;
									}
								}
								CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam)
										.replace(0, slash, "").toString();
							}

							catch (NoSuchElementException e) {

								try {

									List<WebElement> allImages = cdriver.findElementsByXPath(
											"((//td[@class='greybluetext10'])[" + start + "]/img)");

									CorrectAnswerPostExam = cdriver
											.findElementByXPath(
													"((//td[@class='greybluetext10'])[" + start + "]/img)[6]")
											.getAttribute("src");
									CandidateAnswerPostExam = cdriver
											.findElementByXPath(
													"((//td[@class='greybluetext10'])[" + start + "]/img)[5]")
											.getAttribute("src");
									int slash = 0;
									for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
										if (CandidateAnswerPostExam.charAt(slash) == '/') {
											break;
										}
									}
									CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam)
											.replace(0, slash, "").toString();
								} catch (NoSuchElementException e1) {

									// CandidateAnswerPostExam = "Question is mixture of Images and Text. Unable to
									// Extract.";
									mixture = true;

								}
							}
						}
					}

					// Change to correct QID

					QIDint = Integer.parseInt(QID);
					QIDint = QIDint + QNOint;
					QID = Integer.toString(QIDint);

					// Mapping Candidate Answers
					if (mixture == false) {
						if (CandidateAnswerPostExam.equals(CandidateAnswerCS)) {

							System.out.println("QID: " + QID);
							System.out
									.println("CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
							System.out.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
							// System.out.println("CandidateAnswer: "+ CandidateAnswer);

							WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "PASS");

						} else {

							System.err.println("---------------------------------------------------------------");
							System.err.println("QID: " + QID);
							System.err
									.println("CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
							System.err.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
							System.err.println("---------------------------------------------------------------");
							WrongAnswerCount++;

							WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "FAIL");

//							Actions myAction = new Actions(cdriver);
//							WebElement target = cdriver.findElementByXPath("//b[contains(text(), 'Question id [ "+QID+" ]')]/..");
//							Point classname = target.getLocation();
//							int xcordi = classname.getX();			        
//							int ycordi = classname.getY();			        
//							myAction.moveToElement(target, xcordi, ycordi).click().build().perform();
//
//							FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE) , new File("./reports/images/AdminWithChallenge_"+QID+".jpg"));	

						}
					} else {
						List<WebElement> NumberOfImages = cdriver
								.findElementsByXPath("(//td[@class='greybluetext10'])[" + start + "]/img");

						String img = "image", txt = "text";
						int slash;

						if (CanAnsCS.equals(img) && CorAnsCS.equals(img)) {

							CorrectAnswerPostExam = cdriver.findElementByXPath(
									"(//td[@class='greybluetext10'])[" + start + "]/img[" + NumberOfImages.size() + "]")
									.getAttribute("src");
							CandidateAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["
									+ start + "]/img[" + (NumberOfImages.size() - 1) + "]").getAttribute("src");

							slash = 0;

							for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
								if (CandidateAnswerPostExam.charAt(slash) == '/') {
									break;
								}
							}
							CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam).replace(0, slash, "")
									.toString();

							slash = 0;
							for (slash = CorrectAnswerPostExam.length() - 1; slash >= 0; slash--) {
								if (CorrectAnswerPostExam.charAt(slash) == '/') {
									break;
								}
							}
							CorrectAnswerPostExam = new StringBuffer(CorrectAnswerPostExam).replace(0, slash, "")
									.toString();
						}

						else if (CanAnsCS.equals(txt) && CorAnsCS.equals(img)) {

							CorrectAnswerPostExam = cdriver.findElementByXPath(
									"(//td[@class='greybluetext10'])[" + start + "]/img[" + NumberOfImages.size() + "]")
									.getAttribute("src");
							slash = 0;
							for (slash = CorrectAnswerPostExam.length() - 1; slash >= 0; slash--) {
								if (CorrectAnswerPostExam.charAt(slash) == '/') {
									break;
								}
							}
							CorrectAnswerPostExam = new StringBuffer(CorrectAnswerPostExam).replace(0, slash, "")
									.toString();

							// To get Candidate Answer from Post Exam URL

							NotAnswered = "NOT ANSWERED";
							int l = 0;
							int CandidateAnsEnd = 0;
							if (FullText.contains(NotAnswered)) {
								CandidateAnswerPostExam = "NOT ANSWERED";
							} else if (FullText.contains("Omitted Question")) {
								CandidateAnswerPostExam = "Omitted Question";
							} else {

								CandidateAnsEnd = FullText.length() - 17;
								l = (FullText.length() - 17);

								loop = true;
								do {
									if (FullText.charAt(l) == ' ') {
										if (FullText.charAt(l - 1) == ':') {
											if (FullText.charAt(l - 2) == ' ') {
												if (FullText.charAt(l - 3) == 'r') {
													if (FullText.charAt(l - 4) == 'e') {
														if (FullText.charAt(l - 5) == 'w') {

															for (int k = l + 1; k < CandidateAnsEnd; k++) {
																CandidateAnswerPostExam = CandidateAnswerPostExam
																		+ FullText.charAt(k);
															}
															// System.out.println("Candidate Answer from Post Exam
															// is "+
															// CandidateAnswerPostExam);
															loop = false;
														}
													}
												}
											}
										}
									}
									l--;

								} while (loop == true);
							}

							// For few Questions, the Candidate answer has an enter Key at the end which
							// added an extra char at the end. So, deleting that char.
							boolean caspecial = String
									.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length() - 1))
									.matches("\\s");
							if (caspecial == true) {
								StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
								CandidateAnswerPostExam = NewCandidateAnswer
										.deleteCharAt(CandidateAnswerPostExam.length() - 1).toString();
								// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
							}

						}

						else if (CanAnsCS.equals(txt) && CorAnsCS.equals(txt))

						{
							// To get Correct Answer from Post Exam

							CorrectAnswerPostExam = "";
							int CorrectAnsStart = 0;
							int j = (FullText.length() - 1);
							loop = true;
							boolean correctincorrect = false;

							if (FullText.contains("Omitted Question")) {
								CorrectAnswerPostExam = "Omitted Question";

							} else {
								do {
									if (FullText.charAt(j) == ' ') {
										if (FullText.charAt(j - 1) == ':') {
											if (FullText.charAt(j - 2) == ' ') {
												if (FullText.charAt(j - 3) == 'r') {
													if (FullText.charAt(j - 4) == 'e') {
														if (FullText.charAt(j - 5) == 'w') {

															CorrectAnsStart = j + 1;
															for (int k = j + 1; k < FullText.length(); k++) {
																CorrectAnswerPostExam = CorrectAnswerPostExam
																		+ FullText.charAt(k);
															}
															String CAinCA = "Correct Answer";
															String NoCorAns = "No Correct Answer";

															if (CorrectAnswerPostExam.equals(NoCorAns)) {
																CorrectAnswerPostExam = "No Correct Answer";
															} else if (CorrectAnswerPostExam.contains(CAinCA)) {
																CorrectAnswerPostExam = "EMPTY";
																correctincorrect = true;
															}
															// System.out.println("Correct Answer from Post Exam URL
															// is
															// "+ CorrectAnswer);
															loop = false;
														}
													}
												}
											}
										}

									}
									j--;
								} while (loop == true);
							}

							// To get Candidate Answer from Post Exam URL
							CandidateAnswerPostExam = "";
							NotAnswered = "NOT ANSWERED";
							int l = 0;
							int CandidateAnsEnd = 0;
							if (FullText.contains(NotAnswered)) {
								CandidateAnswerPostExam = "NOT ANSWERED";
							} else if (FullText.contains("Omitted Question")) {
								CandidateAnswerPostExam = "Omitted Question";

							} else {
								if (correctincorrect == true) {
									CandidateAnsEnd = FullText.length() - 17;
									l = (FullText.length() - 17);
								} else {
									CandidateAnsEnd = CorrectAnsStart - 18;
									l = (CorrectAnsStart - 18);
								}

								loop = true;
								do {
									if (FullText.charAt(l) == ' ') {
										if (FullText.charAt(l - 1) == ':') {
											if (FullText.charAt(l - 2) == ' ') {
												if (FullText.charAt(l - 3) == 'r') {
													if (FullText.charAt(l - 4) == 'e') {
														if (FullText.charAt(l - 5) == 'w') {

															for (int k = l + 1; k < CandidateAnsEnd; k++) {
																CandidateAnswerPostExam = CandidateAnswerPostExam
																		+ FullText.charAt(k);
															}
															// System.out.println("Candidate Answer from Post Exam
															// is "+
															// CandidateAnswerPostExam);
															loop = false;
														}
													}
												}
											}
										}
									}
									l--;

								} while (loop == true);
							}

							// For few Questions, the Candidate answer has an enter Key at the end which
							// added an extra char at the end. So, deleting that char.
							boolean caspecial = String
									.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length() - 1))
									.matches("\\s");
							if (caspecial == true) {
								StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
								CandidateAnswerPostExam = NewCandidateAnswer
										.deleteCharAt(CandidateAnswerPostExam.length() - 1).toString();
								// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
							}

							boolean cospecial = String
									.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length() - 1))
									.matches("\\s");
							if (cospecial == true) {
								StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
								CorrectAnswerPostExam = NewCorrectAnswer
										.deleteCharAt(CorrectAnswerPostExam.length() - 1).toString();
								// System.out.println("New Correct Answer is "+ CorrectAnswer);
							}
						}

						else if (CanAnsCS.equals(img) && CorAnsCS.equals(txt)) {

							// To get Correct Answer from Post Exam

							CorrectAnswerPostExam = "";
							int CorrectAnsStart = 0;
							int j = (FullText.length() - 1);
							loop = true;
							boolean correctincorrect = false;

							if (FullText.contains("Omitted Question")) {
								CorrectAnswerPostExam = "Omitted Question";

							} else {
								do {
									if (FullText.charAt(j) == ' ') {
										if (FullText.charAt(j - 1) == ':') {
											if (FullText.charAt(j - 2) == ' ') {
												if (FullText.charAt(j - 3) == 'r') {
													if (FullText.charAt(j - 4) == 'e') {
														if (FullText.charAt(j - 5) == 'w') {

															CorrectAnsStart = j + 1;
															for (int k = j + 1; k < FullText.length(); k++) {
																CorrectAnswerPostExam = CorrectAnswerPostExam
																		+ FullText.charAt(k);
															}
															String CAinCA = "Correct Answer";
															String NoCorAns = "No Correct Answer";

															if (CorrectAnswerPostExam.equals(NoCorAns)) {
																CorrectAnswerPostExam = "No Correct Answer";
															} else if (CorrectAnswerPostExam.contains(CAinCA)) {
																CorrectAnswerPostExam = "EMPTY";
																correctincorrect = true;
															}
															// System.out.println("Correct Answer from Post Exam URL
															// is
															// "+ CorrectAnswer);
															loop = false;
														}
													}
												}
											}
										}

									}
									j--;
								} while (loop == true);
							}

							boolean cospecial = String
									.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length() - 1))
									.matches("\\s");
							if (cospecial == true) {
								StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
								CorrectAnswerPostExam = NewCorrectAnswer
										.deleteCharAt(CorrectAnswerPostExam.length() - 1).toString();
								// System.out.println("New Correct Answer is "+ CorrectAnswer);
							}

							CandidateAnswerPostExam = cdriver.findElementByXPath(
									"(//td[@class='greybluetext10'])[" + start + "]/img[" + NumberOfImages.size() + "]")
									.getAttribute("src");

							slash = 0;

							for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
								if (CandidateAnswerPostExam.charAt(slash) == '/') {
									break;
								}
							}
							CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam).replace(0, slash, "")
									.toString();
						}

						if (CandidateAnswerPostExam.equals(CandidateAnswerCS)) {

							System.out.println("QID: " + QID);
							System.out
									.println("CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
							System.out.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
							// System.out.println("CandidateAnswer: "+ CandidateAnswer);

							WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "PASS");

						} else {

							System.err.println("---------------------------------------------------------------");
							System.err.println("QID: " + QID);
							System.err
									.println("CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
							System.err.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
							System.err.println("---------------------------------------------------------------");
							WrongAnswerCount++;

							WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "FAIL");

							Actions myAction = new Actions(cdriver);
							WebElement target = cdriver
									.findElementByXPath("//b[contains(text(), 'Question id [ " + QID + " ]')]/..");
							Point classname = target.getLocation();
							int xcordi = classname.getX();
							int ycordi = classname.getY();
							myAction.moveToElement(target, xcordi, ycordi).click().build().perform();

							FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE),
									new File("./reports/images/AdminWithChallenge_" + QID + ".jpg"));

						}
					}
				}
			}
			System.out.println();

		}
//		cdriver.quit();
//
//		fdriver.quit();
	}

	public void compareCandidateAnswerswithTable(String RollNo) throws IOException {

		if (totalAttemptedCS == totalAttemptedPE) {

			int WrongAnswerCount = 0;

			for (start = 1; start <= AvailableQuestionsPE.size(); start++) {

				String CanAnsCS = "", CorAnsCS = "";

				boolean ImageCheck = true;
				String FullText = cdriver.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]")
						.getText();

				// Get Question Text and check it is not null

				String QuestionText = cdriver.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]/b[1]")
						.getText();

				if (QuestionText.length() < 25) {
					System.err.println("Incomplete Question");
				}

				// To check if the Question is image based.
				try {
					cdriver.findElementByXPath("((//td[@class='greybluetext10'])[" + start + "]/img)");
				} catch (NoSuchElementException e) {

					try {
						cdriver.findElementByXPath("((//td[@class='greybluetext10'])[" + start + "]/b/img)[2]");
					} catch (NoSuchElementException e1) {
						ImageCheck = false;
					}
				}

				// System.out.println(FullText);

				// To get QID
				boolean loop = true;
				String QID = "";
				int i = 0;
				do {
					if (FullText.charAt(i) == '[') {
						i++;
						if (FullText.charAt(i) == ' ') {
							i++;
							QID = QID + FullText.charAt(i);
							i++;
							if (FullText.charAt(i) == ' ') {
								loop = false;
							} else {
								QID = QID + FullText.charAt(i);
								i++;
								if (FullText.charAt(i) == ' ') {
									loop = false;
								} else {
									QID = QID + FullText.charAt(i);
									i++;
									if (FullText.charAt(i) == ' ') {
										loop = false;
									} else {
										QID = QID + FullText.charAt(i);
										i++;
										if (FullText.charAt(i) == ' ') {
											loop = false;
										}
									}
								}
							}
						}
					}
					i++;

				} while (loop == true);

				int QIDint = Integer.parseInt(QID);
				QIDint = QIDint - QNOint;
				QID = Integer.toString(QIDint);

				// System.out.println("QID: " + QID);

				// Get Candidate Answer from Challenge URL
				String CandidateAnswerCS = "";

				try {
					CandidateAnswerCS = fdriver
							.findElementByXPath("(((//b[text()='Candidate Answer:'])/..)[" + QID + "])/b/img")
							.getAttribute("src");
					int slash = 0;
					for (slash = CandidateAnswerCS.length() - 1; slash >= 0; slash--) {
						if (CandidateAnswerCS.charAt(slash) == '/') {
							break;
						}
					}
					CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, slash, "").toString();
					CanAnsCS = "image";

				} catch (NoSuchElementException e1) {

					try {
						CandidateAnswerCS = fdriver
								.findElementByXPath("(((//b[text()='Candidate Answer:'])/..)[" + QID + "])/img")
								.getAttribute("src");
						int slash = 0;
						for (slash = CandidateAnswerCS.length() - 1; slash >= 0; slash--) {
							if (CandidateAnswerCS.charAt(slash) == '/') {
								break;
							}
						}
						CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, slash, "").toString();
						CanAnsCS = "image";

					} catch (NoSuchElementException e11) {
						CandidateAnswerCS = fdriver
								.findElementByXPath("((//b[text()='Candidate Answer:'])/..)[" + QID + "]").getText();
						CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, 24, "").toString();

						// For few Questions, the Candidate answer has an enter Key at the end which
						// added an extra char at the end. So, deleting that char.
						boolean cachalspecial = String.valueOf(CandidateAnswerCS.charAt(CandidateAnswerCS.length() - 1))
								.matches("\\s");
						if (cachalspecial == true) {
							StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerCS);
							CandidateAnswerCS = NewCandidateAnswer.deleteCharAt(CandidateAnswerCS.length() - 1)
									.toString();
							// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
						}
						CanAnsCS = "text";
					}
				}

				String IncompNotAns = "ANSWERED ]";
				if (CandidateAnswerCS.equals(IncompNotAns)) {
					CandidateAnswerCS = "NOT ANSWERED";
				}
				// System.out.println("Candidate Answer from Challenge System is " +
				// CandidateAnswer);

				// Get Correct Answer from Challenge URL
				String CorrectAnswerCS = "";

				try {
					CorrectAnswerCS = fdriver
							.findElementByXPath("(((//b[text()='Correct Answer:'])/..)[" + QID + "])/b/img")
							.getAttribute("src");
					int slash = 0;
					for (slash = CorrectAnswerCS.length() - 1; slash >= 0; slash--) {
						if (CorrectAnswerCS.charAt(slash) == '/') {
							break;
						}
					}
					CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, slash, "").toString();
					CorAnsCS = "image";

				} catch (NoSuchElementException e11) {

					try {
						CorrectAnswerCS = fdriver
								.findElementByXPath("(((//b[text()='Correct Answer:'])/..)[" + QID + "])/img")
								.getAttribute("src");
						int slash = 0;
						for (slash = CorrectAnswerCS.length() - 1; slash >= 0; slash--) {
							if (CorrectAnswerCS.charAt(slash) == '/') {
								break;
							}
						}
						CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, slash, "").toString();
						CorAnsCS = "image";

					} catch (NoSuchElementException e12) {

						CorrectAnswerCS = fdriver
								.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + QID + "]").getText();

						CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, 22, "").toString();

						// CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, 24,
						// "").toString();

						// System.out.println("CorrectAnswerCS = "+CorrectAnswerCS);

						// For few Questions, the Candidate answer has an enter Key at the end which
						// added an extra char at the end. So, deleting that char.
						boolean coAnChalspecial = String.valueOf(CorrectAnswerCS.charAt(CorrectAnswerCS.length() - 1))
								.matches("\\s");
						if (coAnChalspecial == true) {
							StringBuilder NewCorrectAnswerCS = new StringBuilder(CorrectAnswerCS);
							CorrectAnswerCS = NewCorrectAnswerCS.deleteCharAt(CorrectAnswerCS.length() - 1).toString();
							// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
						}
						CorAnsCS = "text";
					}
				}

				String NoCorrectBenefit = "No Correct Answer ( Benefit to all )";
				if (CorrectAnswerCS.equals(NoCorrectBenefit)) {
					CorrectAnswerCS = "No Correct Answer ( Benefit to all )";
				}

				// System.out.println(CorrectAnswerCS);

				// To get Correct Answer from Post Exam
				if (ImageCheck == false) {
					String CorrectAnswerPostExam = "";
					int CorrectAnsStart = 0;
					int j = (FullText.length() - 1);
					loop = true;
					boolean correctincorrect = false;

					do {
						if (FullText.charAt(j) == ' ') {
							if (FullText.charAt(j - 1) == ':') {
								if (FullText.charAt(j - 2) == ' ') {
									if (FullText.charAt(j - 3) == 'r') {
										if (FullText.charAt(j - 4) == 'e') {
											if (FullText.charAt(j - 5) == 'w') {

												CorrectAnsStart = j + 1;
												for (int k = j + 1; k < FullText.length(); k++) {
													CorrectAnswerPostExam = CorrectAnswerPostExam + FullText.charAt(k);
												}
												String CAinCA = "Correct Answer";
												String NoCorAns = "No Correct Answer";

												if (CorrectAnswerPostExam.equals(NoCorAns)) {
													CorrectAnswerPostExam = "No Correct Answer";
												} else if (CorrectAnswerPostExam.contains(CAinCA)) {
													CorrectAnswerPostExam = "EMPTY";
													correctincorrect = true;
												}
												// System.out.println("Correct Answer from Post Exam URL is "+
												// CorrectAnswer);
												loop = false;
											}
										}
									}
								}
							}

						}
						j--;
					} while (loop == true);

					// To get Candidate Answer from Post Exam URL
					String CandidateAnswerPostExam = "";
					String NotAnswered = "NOT ANSWERED";
					int l = 0;
					int CandidateAnsEnd = 0;
					if (FullText.contains(NotAnswered)) {
						CandidateAnswerPostExam = "NOT ANSWERED";
					} else {
						if (correctincorrect == true) {
							CandidateAnsEnd = FullText.length() - 17;
							l = (FullText.length() - 17);
						} else {
							CandidateAnsEnd = CorrectAnsStart - 18;
							l = (CorrectAnsStart - 18);
						}

						loop = true;
						do {
							if (FullText.charAt(l) == ' ') {
								if (FullText.charAt(l - 1) == ':') {
									if (FullText.charAt(l - 2) == ' ') {
										if (FullText.charAt(l - 3) == 'r') {
											if (FullText.charAt(l - 4) == 'e') {
												if (FullText.charAt(l - 5) == 'w') {

													for (int k = l + 1; k < CandidateAnsEnd; k++) {
														CandidateAnswerPostExam = CandidateAnswerPostExam
																+ FullText.charAt(k);
													}
													// System.out.println("Candidate Answer from Post Exam is "+
													// CandidateAnswerPostExam);
													loop = false;
												}
											}
										}
									}
								}
							}
							l--;

						} while (loop == true);
					}

					// For few Questions, the Candidate answer has an enter Key at the end which
					// added an extra char at the end. So, deleting that char.
					boolean caspecial = String
							.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length() - 1))
							.matches("\\s");
					if (caspecial == true) {
						StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
						CandidateAnswerPostExam = NewCandidateAnswer.deleteCharAt(CandidateAnswerPostExam.length() - 1)
								.toString();
						// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
					}

					boolean cospecial = String.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length() - 1))
							.matches("\\s");
					if (cospecial == true) {
						StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
						CorrectAnswerPostExam = NewCorrectAnswer.deleteCharAt(CorrectAnswerPostExam.length() - 1)
								.toString();
						// System.out.println("New Correct Answer is "+ CorrectAnswer);
					}

					// Change to correct QID

					QIDint = Integer.parseInt(QID);
					QIDint = QIDint + QNOint;
					QID = Integer.toString(QIDint);

					// Checking Candidate Answer from PostExam to Challenge URL
					if (CandidateAnswerPostExam.equals(CandidateAnswerCS)) {

						System.out.println("QID: " + QID);
						System.out.println("CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
						System.out.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
						// System.out.println("CorrectAnswer: "+ CorrectAnswer);

						WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "PASS");

					} else {

						System.err.println("---------------------------------------------------------------");
						System.err.println("QID: " + QID);
						System.err.println("CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
						System.err.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
						System.err.println("---------------------------------------------------------------");
						WrongAnswerCount++;

						WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "FAIL");

						Actions myAction = new Actions(cdriver);
						WebElement target = cdriver
								.findElementByXPath("//b[contains(text(), 'Question id [ " + QID + " ]')]/..");
						Point classname = target.getLocation();
						int xcordi = classname.getX();
						int ycordi = classname.getY();
						myAction.moveToElement(target, xcordi, ycordi).click().build().perform();
						FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE),
								new File("./reports/images/AdminWithChallenge_" + RollNo + "_" + QID + ".jpg"));

					}

				} else {
					{
						// If the Question is image based.
						// To get Correct Answer
						String CorrectAnswerPostExam = "";
						String CandidateAnswerPostExam = "";
						String NotAnswered = "NOT ANSWERED";
						boolean NotAns = false;
						String NoCorrectAns = "No Correct Answer";

						// To Check if the Candidate answered the image based question or not.
						if (FullText.contains(NotAnswered)) {
							CandidateAnswerPostExam = "NOT ANSWERED";
							NotAns = true;
						}

						// To get Candidate Answer

						boolean mixture = false; // Some Question may contain both images and text in options.
						if (NotAns == false) {

							if (FullText.contains(NoCorrectAns)) {
								CorrectAnswerPostExam = "No Correct Answer";
								try {
									CandidateAnswerPostExam = cdriver
											.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]/img[5]")
											.getAttribute("src");

									int slash = 0;
									for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
										if (CandidateAnswerPostExam.charAt(slash) == '/') {
											break;
										}
									}
									CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam)
											.replace(0, slash, "").toString();

								} catch (NoSuchElementException e) {
									CandidateAnswerPostExam = "Question is mixture of Images and Text. Unable to Extract.";
									mixture = true;
								}
							} else {
								try {

									List<WebElement> allImages = cdriver.findElementsByXPath(
											"((//td[@class='greybluetext10'])[" + start + "]/b/img)");

									CorrectAnswerPostExam = cdriver
											.findElementByXPath("((//td[@class='greybluetext10'])[" + start
													+ "]/b/img)[" + allImages.size() + "]")
											.getAttribute("src");
									CandidateAnswerPostExam = cdriver
											.findElementByXPath("((//td[@class='greybluetext10'])[" + start
													+ "]/b/img)[" + (allImages.size() - 1) + "]")
											.getAttribute("src");
									int slash = 0;
									for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
										if (CandidateAnswerPostExam.charAt(slash) == '/') {
											break;
										}
									}
									CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam)
											.replace(0, slash, "").toString();
								}

								catch (NoSuchElementException e) {

									try {

										List<WebElement> allImages = cdriver.findElementsByXPath(
												"((//td[@class='greybluetext10'])[" + start + "]/img)");

										CorrectAnswerPostExam = cdriver
												.findElementByXPath(
														"((//td[@class='greybluetext10'])[" + start + "]/img)[6]")
												.getAttribute("src");
										CandidateAnswerPostExam = cdriver
												.findElementByXPath(
														"((//td[@class='greybluetext10'])[" + start + "]/img)[5]")
												.getAttribute("src");
										int slash = 0;
										for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
											if (CandidateAnswerPostExam.charAt(slash) == '/') {
												break;
											}
										}
										CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam)
												.replace(0, slash, "").toString();
									} catch (NoSuchElementException e1) {

										// CandidateAnswerPostExam = "Question is mixture of Images and Text. Unable to
										// Extract.";
										mixture = true;

									}
								}
							}
						}

						// Change to correct QID

						QIDint = Integer.parseInt(QID);
						QIDint = QIDint + QNOint;
						QID = Integer.toString(QIDint);

						// Mapping Candidate Answers
						if (mixture == false) {
							if (CandidateAnswerPostExam.equals(CandidateAnswerCS)) {

								System.out.println("QID: " + QID);
								System.out.println(
										"CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
								System.out.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
								// System.out.println("CandidateAnswer: "+ CandidateAnswer);

								WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "PASS");

							} else {

								System.err.println("---------------------------------------------------------------");
								System.err.println("QID: " + QID);
								System.err.println(
										"CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
								System.err.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
								System.err.println("---------------------------------------------------------------");
								WrongAnswerCount++;

								WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "FAIL");

								Actions myAction = new Actions(cdriver);
								WebElement target = cdriver
										.findElementByXPath("//b[contains(text(), 'Question id [ " + QID + " ]')]/..");
								Point classname = target.getLocation();
								int xcordi = classname.getX();
								int ycordi = classname.getY();
								myAction.moveToElement(target, xcordi, ycordi).click().build().perform();

								FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE),
										new File("./reports/images/AdminWithChallenge_" + QID + ".jpg"));

							}
						} else {
							List<WebElement> NumberOfImages = cdriver
									.findElementsByXPath("(//td[@class='greybluetext10'])[" + start + "]/img");

							String img = "image", txt = "text";
							int slash;

							if (CanAnsCS.equals(img) && CorAnsCS.equals(img)) {

								CorrectAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["
										+ start + "]/img[" + NumberOfImages.size() + "]").getAttribute("src");
								CandidateAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["
										+ start + "]/img[" + (NumberOfImages.size() - 1) + "]").getAttribute("src");

								slash = 0;

								for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
									if (CandidateAnswerPostExam.charAt(slash) == '/') {
										break;
									}
								}
								CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam)
										.replace(0, slash, "").toString();

								slash = 0;
								for (slash = CorrectAnswerPostExam.length() - 1; slash >= 0; slash--) {
									if (CorrectAnswerPostExam.charAt(slash) == '/') {
										break;
									}
								}
								CorrectAnswerPostExam = new StringBuffer(CorrectAnswerPostExam).replace(0, slash, "")
										.toString();
							}

							else if (CanAnsCS.equals(txt) && CorAnsCS.equals(img)) {

								CorrectAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["
										+ start + "]/img[" + NumberOfImages.size() + "]").getAttribute("src");
								slash = 0;
								for (slash = CorrectAnswerPostExam.length() - 1; slash >= 0; slash--) {
									if (CorrectAnswerPostExam.charAt(slash) == '/') {
										break;
									}
								}
								CorrectAnswerPostExam = new StringBuffer(CorrectAnswerPostExam).replace(0, slash, "")
										.toString();

								// To get Candidate Answer from Post Exam URL

								NotAnswered = "NOT ANSWERED";
								int l = 0;
								int CandidateAnsEnd = 0;
								if (FullText.contains(NotAnswered)) {
									CandidateAnswerPostExam = "NOT ANSWERED";
								} else {

									CandidateAnsEnd = FullText.length() - 17;
									l = (FullText.length() - 17);

									loop = true;
									do {
										if (FullText.charAt(l) == ' ') {
											if (FullText.charAt(l - 1) == ':') {
												if (FullText.charAt(l - 2) == ' ') {
													if (FullText.charAt(l - 3) == 'r') {
														if (FullText.charAt(l - 4) == 'e') {
															if (FullText.charAt(l - 5) == 'w') {

																for (int k = l + 1; k < CandidateAnsEnd; k++) {
																	CandidateAnswerPostExam = CandidateAnswerPostExam
																			+ FullText.charAt(k);
																}
																// System.out.println("Candidate Answer from Post Exam
																// is "+ CandidateAnswerPostExam);
																loop = false;
															}
														}
													}
												}
											}
										}
										l--;

									} while (loop == true);
								}

								// For few Questions, the Candidate answer has an enter Key at the end which
								// added an extra char at the end. So, deleting that char.
								boolean caspecial = String
										.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length() - 1))
										.matches("\\s");
								if (caspecial == true) {
									StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
									CandidateAnswerPostExam = NewCandidateAnswer
											.deleteCharAt(CandidateAnswerPostExam.length() - 1).toString();
									// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
								}

							}

							else if (CanAnsCS.equals(txt) && CorAnsCS.equals(txt))

							{
								// To get Correct Answer from Post Exam

								CorrectAnswerPostExam = "";
								int CorrectAnsStart = 0;
								int j = (FullText.length() - 1);
								loop = true;
								boolean correctincorrect = false;

								do {
									if (FullText.charAt(j) == ' ') {
										if (FullText.charAt(j - 1) == ':') {
											if (FullText.charAt(j - 2) == ' ') {
												if (FullText.charAt(j - 3) == 'r') {
													if (FullText.charAt(j - 4) == 'e') {
														if (FullText.charAt(j - 5) == 'w') {

															CorrectAnsStart = j + 1;
															for (int k = j + 1; k < FullText.length(); k++) {
																CorrectAnswerPostExam = CorrectAnswerPostExam
																		+ FullText.charAt(k);
															}
															String CAinCA = "Correct Answer";
															String NoCorAns = "No Correct Answer";

															if (CorrectAnswerPostExam.equals(NoCorAns)) {
																CorrectAnswerPostExam = "No Correct Answer";
															} else if (CorrectAnswerPostExam.contains(CAinCA)) {
																CorrectAnswerPostExam = "EMPTY";
																correctincorrect = true;
															}
															// System.out.println("Correct Answer from Post Exam URL is
															// "+ CorrectAnswer);
															loop = false;
														}
													}
												}
											}
										}

									}
									j--;
								} while (loop == true);

								// To get Candidate Answer from Post Exam URL
								CandidateAnswerPostExam = "";
								NotAnswered = "NOT ANSWERED";
								int l = 0;
								int CandidateAnsEnd = 0;
								if (FullText.contains(NotAnswered)) {
									CandidateAnswerPostExam = "NOT ANSWERED";
								} else {
									if (correctincorrect == true) {
										CandidateAnsEnd = FullText.length() - 17;
										l = (FullText.length() - 17);
									} else {
										CandidateAnsEnd = CorrectAnsStart - 18;
										l = (CorrectAnsStart - 18);
									}

									loop = true;
									do {
										if (FullText.charAt(l) == ' ') {
											if (FullText.charAt(l - 1) == ':') {
												if (FullText.charAt(l - 2) == ' ') {
													if (FullText.charAt(l - 3) == 'r') {
														if (FullText.charAt(l - 4) == 'e') {
															if (FullText.charAt(l - 5) == 'w') {

																for (int k = l + 1; k < CandidateAnsEnd; k++) {
																	CandidateAnswerPostExam = CandidateAnswerPostExam
																			+ FullText.charAt(k);
																}
																// System.out.println("Candidate Answer from Post Exam
																// is "+ CandidateAnswerPostExam);
																loop = false;
															}
														}
													}
												}
											}
										}
										l--;

									} while (loop == true);
								}

								// For few Questions, the Candidate answer has an enter Key at the end which
								// added an extra char at the end. So, deleting that char.
								boolean caspecial = String
										.valueOf(CandidateAnswerPostExam.charAt(CandidateAnswerPostExam.length() - 1))
										.matches("\\s");
								if (caspecial == true) {
									StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerPostExam);
									CandidateAnswerPostExam = NewCandidateAnswer
											.deleteCharAt(CandidateAnswerPostExam.length() - 1).toString();
									// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
								}

								boolean cospecial = String
										.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length() - 1))
										.matches("\\s");
								if (cospecial == true) {
									StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
									CorrectAnswerPostExam = NewCorrectAnswer
											.deleteCharAt(CorrectAnswerPostExam.length() - 1).toString();
									// System.out.println("New Correct Answer is "+ CorrectAnswer);
								}
							}

							else if (CanAnsCS.equals(img) && CorAnsCS.equals(txt)) {

								// To get Correct Answer from Post Exam

								CorrectAnswerPostExam = "";
								int CorrectAnsStart = 0;
								int j = (FullText.length() - 1);
								loop = true;
								boolean correctincorrect = false;

								do {
									if (FullText.charAt(j) == ' ') {
										if (FullText.charAt(j - 1) == ':') {
											if (FullText.charAt(j - 2) == ' ') {
												if (FullText.charAt(j - 3) == 'r') {
													if (FullText.charAt(j - 4) == 'e') {
														if (FullText.charAt(j - 5) == 'w') {

															CorrectAnsStart = j + 1;
															for (int k = j + 1; k < FullText.length(); k++) {
																CorrectAnswerPostExam = CorrectAnswerPostExam
																		+ FullText.charAt(k);
															}
															String CAinCA = "Correct Answer";
															String NoCorAns = "No Correct Answer";

															if (CorrectAnswerPostExam.equals(NoCorAns)) {
																CorrectAnswerPostExam = "No Correct Answer";
															} else if (CorrectAnswerPostExam.contains(CAinCA)) {
																CorrectAnswerPostExam = "EMPTY";
																correctincorrect = true;
															}
															// System.out.println("Correct Answer from Post Exam URL is
															// "+ CorrectAnswer);
															loop = false;
														}
													}
												}
											}
										}

									}
									j--;
								} while (loop == true);

								boolean cospecial = String
										.valueOf(CorrectAnswerPostExam.charAt(CorrectAnswerPostExam.length() - 1))
										.matches("\\s");
								if (cospecial == true) {
									StringBuilder NewCorrectAnswer = new StringBuilder(CorrectAnswerPostExam);
									CorrectAnswerPostExam = NewCorrectAnswer
											.deleteCharAt(CorrectAnswerPostExam.length() - 1).toString();
									// System.out.println("New Correct Answer is "+ CorrectAnswer);
								}

								CandidateAnswerPostExam = cdriver.findElementByXPath("(//td[@class='greybluetext10'])["
										+ start + "]/img[" + NumberOfImages.size() + "]").getAttribute("src");

								slash = 0;

								for (slash = CandidateAnswerPostExam.length() - 1; slash >= 0; slash--) {
									if (CandidateAnswerPostExam.charAt(slash) == '/') {
										break;
									}
								}
								CandidateAnswerPostExam = new StringBuffer(CandidateAnswerPostExam)
										.replace(0, slash, "").toString();
							}

							if (CandidateAnswerPostExam.equals(CandidateAnswerCS)) {

								System.out.println("QID: " + QID);
								System.out.println(
										"CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
								System.out.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
								// System.out.println("CandidateAnswer: "+ CandidateAnswer);

								WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "PASS");

							} else {

								System.err.println("---------------------------------------------------------------");
								System.err.println("QID: " + QID);
								System.err.println(
										"CandidateAnswer from Post Exam Report Page: " + CandidateAnswerPostExam);
								System.err.println("CandidateAnswer from Challenge System URL: " + CandidateAnswerCS);
								System.err.println("---------------------------------------------------------------");
								WrongAnswerCount++;

								WriteInExcelACS(RollNo, QID, CandidateAnswerPostExam, CandidateAnswerCS, "FAIL");

								Actions myAction = new Actions(cdriver);
								WebElement target = cdriver
										.findElementByXPath("//b[contains(text(), 'Question id [ " + QID + " ]')]/..");
								Point classname = target.getLocation();
								int xcordi = classname.getX();
								int ycordi = classname.getY();
								myAction.moveToElement(target, xcordi, ycordi).click().build().perform();

								FileUtils.copyFile(cdriver.getScreenshotAs(OutputType.FILE),
										new File("./reports/images/AdminWithChallenge_" + QID + ".jpg"));

							}
						}
					}
				}
				System.out.println();

			}

			cdriver.quit();

			fdriver.quit();

		} else {

			WriteInExcelACS(RollNo, "QuestionCountMismatch", "PE Count: " + totalAttemptedPE,
					"CS Count: " + totalAttemptedCS, "FAIL");

			cdriver.quit();

			fdriver.quit();

		}

	}

	public void createNotepadBLPE(String RollNo) throws IOException {

		try {
			fw = new FileWriter("F:\\Results\\BrokenLinks\\BrokenLink_PostExam_" + RollNo + ".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);

		System.out.println("---------------------------------------------------------------");
		System.out.println("Post Exam");
		System.out.println("Test Result for the Roll no: " + RollNo);
		System.out.println("---------------------------------------------------------------");

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("Post Exam");
		bw.newLine();
		bw.write("Test Result for the Roll no: " + RollNo);
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
	}

	public void createNotepadBLCS(String RollNo) throws IOException {
		try {
			fw = new FileWriter("F:\\Results\\BrokenLinks\\BrokenLink_Challenge_" + RollNo + ".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);

		System.out.println("---------------------------------------------------------------");
		System.out.println("Challenge System");
		System.out.println("Test Result for the Roll no: " + RollNo);
		System.out.println("---------------------------------------------------------------");

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("Challenge System");
		bw.newLine();
		bw.write("Test Result for the Roll no: " + RollNo);
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
	}

	public void createNotepadNumberOfQuestions(String RollNo) throws IOException {

		try {
			fw = new FileWriter("F:\\Results\\BrokenLinks\\NumberOfQuestions_" + RollNo + ".txt");
		} catch (IOException e) {

			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);

		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("Test Result for the Roll no: " + RollNo);
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();

	}

	public void writeNumberOfQuestions() throws IOException {

		fdriver.close();

		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.write("AvailableQuestions in MQP: " + AvailableQuestionMasterQP);
		bw.newLine();
		bw.write("AvailableQuestions in Post Exam: " + AvailableQuestionsPE.size());
		bw.newLine();
		bw.write("AvailableQuestions in Challenge System: " + AvailableQuestionsCS.size());
		bw.newLine();
		bw.write("---------------------------------------------------------------");
		bw.newLine();
		bw.close();

	}

	public void chkBrokenLinkPE() throws IOException {
		LinkList = cdriver.findElementsByTagName("img");
		ActiveLinks = new ArrayList<WebElement>();

		for (int j = 0; j < LinkList.size(); j++) {
			if ((LinkList.get(j).getAttribute("src")) != null) {
				ActiveLinks.add(LinkList.get(j));
			}
		}

		for (int i = 0; i < LinkList.size(); i++) {

			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(LinkList.get(i).getAttribute("src"))
						.openConnection();
				connection.connect();
				String responseMessage = connection.getResponseMessage();
				String OK = "OK";
				connection.disconnect();

				if (responseMessage.equals(OK)) {
					System.out
							.println(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();
					bw.write(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();

				} else {
					System.err
							.println(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					String link = LinkList.get(i).getAttribute("src");
					// String QID =
					// cdriver.findElementByXPath("//img[@src='"+link+"']/../b").getText();
					// QID = QID.replaceAll("[^0-9]", "");
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

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
		cdriver.close();
		bw.close();
	}

	public void chkBrokenLinkCS() throws IOException {
		LinkList = cdriver.findElementsByTagName("img");
		ActiveLinks = new ArrayList<WebElement>();

		for (int j = 0; j < LinkList.size(); j++) {
			if ((LinkList.get(j).getAttribute("src")) != null) {
				ActiveLinks.add(LinkList.get(j));
			}
		}

		for (int i = 0; i < LinkList.size(); i++) {

			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(LinkList.get(i).getAttribute("src"))
						.openConnection();
				connection.connect();
				String responseMessage = connection.getResponseMessage();
				connection.disconnect();
				String OK = "OK";

				if (responseMessage.equals(OK)) {

					System.out
							.println(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();
					bw.write(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					bw.newLine();

				} else {
					System.err
							.println(i + LinkList.get(i).getAttribute("src") + " Response Message: " + responseMessage);
					String link = LinkList.get(i).getAttribute("src");
					// String QID =
					// cdriver.findElementByXPath("//img[@src='"+link+"']/../b").getText();
					// QID = QID.replaceAll("[^0-9]", "");
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

			} catch (MalformedURLException e) {
				e.printStackTrace();

			}

		}

		cdriver.close();
		bw.close();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------

	public void CreateOneExcel() throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("TestResult");

		rowCount = 0;
		WriteInOneExcel("RollNo", "QID", "CandidateAnswerPostExam", "CandidateAnswerCS", "Status1", "CorrectAnswerCS",
				"Option No.", "MasterQP Option", "Status2", "FinalStatus");

	}

	public void WriteInOneExcel(String text1, String text2, String text3, String text4, String text5, String text6,
			String text7, String text8, String text9, String text10) throws FileNotFoundException, IOException {

		Object[][] bookData = { { text1, text2, text3, text4, text5, text6, text7, text8, text9, text10 }, };

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
		try (FileOutputStream outputStream = new FileOutputStream(
				"F:\\Results\\AdminWithChallengeSystem\\TestResult.xlsx")) {
			workbook.write(outputStream);
		}

	}

	public void CreateExcelACS(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("AdminWithChallengeSystem");

		rowCount = 0;// "CandidateAnswerPE","CandidateAnswerCS","QuestionStatus"
		// for answer
		WriteInExcelACS("RollNo", "QID", "CandidateAnswerPostExam", "CandidateAnswerCS", "CandidateAnswerStatus");
		// for question
		WriteInExcelACS("CandidateQuestionPE", "CandidateQuestionCS", "QuestionStatus");
		// for Each option
		WriteInExcelACSOption("PEOption", "CSOption", "OptionStatus", acsOptRowCount, 11);
		// for overall option status
		WriteInExcelACSOptionFinalStatus("Overall Option Status");
	}

	public void CreateExcelTableCS(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("ChallengeSystemTable");

		rowCount = 0;
		WriteInExcelTableCS("RollNo", "Attempted", "NotAttempted", "CorrectAnswer", "WrongAnswer", "Status",
				"AttemptedCS", "NotAttemptedCS", "CorrectAnswerCS", "WrongAnswerCS");
	}

	public void WriteInExcelTableCS(String RollNo, String text1, String text2, String text3, String text4, String text5,
			String text6, String text7, String text8, String text9) throws FileNotFoundException, IOException {

		Object[][] bookData = { { RollNo, text1, text2, text3, text4, text5, text6, text7, text8, text9 }, };

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
		try (FileOutputStream outputStream = new FileOutputStream(
				"F:\\Results\\AdminWithChallengeSystem\\TestResult_TableCS.xlsx")) {
			workbook.write(outputStream);
		}

	}

	public void tableChallengeSystem(String RollNo) throws IOException {

		int WrongAnswerCount = 0;
		int[][] output = new int[4][4];
		int[][] tableCS = new int[4][4];

		AvailableQuestionsCS = fdriver.findElementsByXPath("((//b[text()='Correct Answer:'])/..)");

		for (start = 1; start <= AvailableQuestionsCS.size(); start++) {

			// Get QID from Challenge URL
			String QID = fdriver.findElementByXPath("(//b[starts-with(text(),'QID : ')])[" + start + "]").getText();
			QID = QID.replaceAll("[^0-9]", "");

			// Get Candidate Answer from Challenge URL
			String CanAnsCS = "", CorAnsCS = "";
			String CandidateAnswerCS = "";

			try {
				CandidateAnswerCS = fdriver
						.findElementByXPath("(((//b[text()='Candidate Answer:'])/..)[" + start + "])/b/img")
						.getAttribute("src");
				int slash = 0;
				for (slash = CandidateAnswerCS.length() - 1; slash >= 0; slash--) {
					if (CandidateAnswerCS.charAt(slash) == '/') {
						break;
					}
				}
				CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, slash, "").toString();
				CanAnsCS = "image";

			} catch (NoSuchElementException e1) {

				try {
					CandidateAnswerCS = fdriver
							.findElementByXPath("(((//b[text()='Candidate Answer:'])/..)[" + start + "])/img")
							.getAttribute("src");
					int slash = 0;
					for (slash = CandidateAnswerCS.length() - 1; slash >= 0; slash--) {
						if (CandidateAnswerCS.charAt(slash) == '/') {
							break;
						}
					}
					CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, slash, "").toString();
					CanAnsCS = "image";

				} catch (NoSuchElementException e11) {
					CandidateAnswerCS = fdriver
							.findElementByXPath("((//b[text()='Candidate Answer:'])/..)[" + start + "]").getText();
					CandidateAnswerCS = new StringBuffer(CandidateAnswerCS).replace(0, 17, "").toString().replace(" ",
							"");

					// For few Questions, the Candidate answer has an enter Key at the end which
					// added an extra char at the end. So, deleting that char.
					boolean cachalspecial = String.valueOf(CandidateAnswerCS.charAt(CandidateAnswerCS.length() - 1))
							.matches("\\s");
					if (cachalspecial == true) {
						StringBuilder NewCandidateAnswer = new StringBuilder(CandidateAnswerCS);
						CandidateAnswerCS = NewCandidateAnswer.deleteCharAt(CandidateAnswerCS.length() - 1).toString();
						// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
					}
					CanAnsCS = "text";
				}
			}

			String IncompNotAns = "ANSWERED ]";
			if (CandidateAnswerCS.equals(IncompNotAns)) {
				CandidateAnswerCS = "NOT ANSWERED";
			}
			// System.out.println("Candidate Answer from Challenge System is " +
			// CandidateAnswer);

			// Get Correct Answer from Challenge URL
			String CorrectAnswerCS = "";

			try {
				CorrectAnswerCS = fdriver
						.findElementByXPath("(((//b[text()='Correct Answer:'])/..)[" + start + "])/b/img")
						.getAttribute("src");
				int slash = 0;
				for (slash = CorrectAnswerCS.length() - 1; slash >= 0; slash--) {
					if (CorrectAnswerCS.charAt(slash) == '/') {
						break;
					}
				}
				CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, slash, "").toString();
				CorAnsCS = "image";

			} catch (NoSuchElementException e11) {

				try {
					CorrectAnswerCS = fdriver
							.findElementByXPath("(((//b[text()='Correct Answer:'])/..)[" + start + "])/img")
							.getAttribute("src");
					int slash = 0;
					for (slash = CorrectAnswerCS.length() - 1; slash >= 0; slash--) {
						if (CorrectAnswerCS.charAt(slash) == '/') {
							break;
						}
					}
					CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, slash, "").toString();
					CorAnsCS = "image";

				} catch (NoSuchElementException e12) {

					CorrectAnswerCS = fdriver.findElementByXPath("((//b[text()='Correct Answer:'])/..)[" + start + "]")
							.getText();

					CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, 15, "").toString().replace(" ", "");

					// CorrectAnswerCS = new StringBuffer(CorrectAnswerCS).replace(0, 24,
					// "").toString();

					// System.out.println("CorrectAnswerCS = "+CorrectAnswerCS);

					// For few Questions, the Candidate answer has an enter Key at the end which
					// added an extra char at the end. So, deleting that char.
					boolean coAnChalspecial = String.valueOf(CorrectAnswerCS.charAt(CorrectAnswerCS.length() - 1))
							.matches("\\s");
					if (coAnChalspecial == true) {
						StringBuilder NewCorrectAnswerCS = new StringBuilder(CorrectAnswerCS);
						CorrectAnswerCS = NewCorrectAnswerCS.deleteCharAt(CorrectAnswerCS.length() - 1).toString();
						// System.out.println("New Candidate Answer is "+ CandidateAnswerPostExam);
					}
					CorAnsCS = "text";
				}
			}

			String NoCorrectBenefit = "No Correct Answer ( Benefit to all )";
			String omitted = "d from Evaluation";
			String noCorrect = "RECT ANSWER";

			if (CorrectAnswerCS.equals(NoCorrectBenefit)) {
				CorrectAnswerCS = "No Correct Answer ( Benefit to all )";
			} else if (CorrectAnswerCS.equals(omitted)) {
				CorrectAnswerCS = "Omitted from Evaluation";
			} else if (CorrectAnswerCS.equals(noCorrect)) {
				CorrectAnswerCS = "NO CORRECT ANSWER";
			}

			// System.out.println(CorrectAnswerCS);

			System.out.println("QID = " + QID);
			System.out.println("CandidateAnswerCS = " + CandidateAnswerCS);
			System.out.println("CorrectAnswerCS = " + CorrectAnswerCS);

			int sec = 0;

			if ((start >= 1) && (start <= 25)) {
				sec = 0;
			}
			if ((start >= 26) && (start <= 50)) {
				sec = 1;
			}
			if ((start >= 51) && (start <= 75)) {
				sec = 2;
			}
			if ((start >= 76) && (start <= 100)) {
				sec = 3;
			}

			if ((CorrectAnswerCS.equals(CandidateAnswerCS)) || (CorrectAnswerCS.contains(CandidateAnswerCS))) {
				output[sec][0] = output[sec][0] + 1;
				output[sec][2] = output[sec][2] + 1;
			} else if (CandidateAnswerCS.contains("NOT ANSWERED")) {
				output[sec][1] = output[sec][1] + 1;
			} else if ((!CorrectAnswerCS.equals(CandidateAnswerCS))
					&& (!CorrectAnswerCS.equals("Omitted from Evaluation"))
					&& (!CorrectAnswerCS.equals("NO CORRECT ANSWER"))) {
				output[sec][3] = output[sec][3] + 1;
				output[sec][0] = output[sec][0] + 1;
			} else {

			}

		}

		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j <= 3; j++) {

				String tempString = fdriver.findElementByXPath(
						"((//table[@class='table candidatedetails'])[2]/tbody/tr)[" + (i + 2) + "]/td[" + (j + 2) + "]")
						.getText();
				int tempInt = Integer.parseInt(tempString);
				tableCS[i][j] = tempInt;
			}
		}

		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j <= 3; j++) {
				System.out.print(output[i][j] + " ");
			}
			System.out.println();
		}

		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j <= 3; j++) {
				System.out.print(tableCS[i][j] + " ");
			}
			System.out.println();
		}

		int matchCount = 0;

		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j <= 3; j++) {

				if (output[i][j] == tableCS[i][j]) {
					matchCount++;
				}
			}
		}

		String Status = "";

		int totalAttempted = 0;
		int totalCorrectAndIncorrect = 0;
		int totalNotAttempted = 0;

		totalAttempted = output[0][0] + output[1][0] + output[2][0] + output[3][0];
		totalCorrectAndIncorrect = output[0][2] + output[0][3] + output[1][2] + output[1][3] + output[2][2]
				+ output[2][3] + output[3][2] + output[3][3];
		totalNotAttempted = output[0][1] + output[1][1] + output[2][1] + output[3][1];

		if ((totalAttempted == totalCorrectAndIncorrect)
				&& ((totalAttempted + totalNotAttempted) == AvailableQuestionsCS.size()) && (matchCount == 16)) {
			System.out.println("Matched");
			Status = "PASS";
		} else {
			System.out.println("Not Matched");
			Status = "FAIL";
		}

		fdriver.quit();

		WriteInExcelTableCS(RollNo, Integer.toString(output[0][0]), Integer.toString(output[0][1]),
				Integer.toString(output[0][2]), Integer.toString(output[0][3]), Status, Integer.toString(tableCS[0][0]),
				Integer.toString(tableCS[0][1]), Integer.toString(tableCS[0][2]), Integer.toString(tableCS[0][3]));
		WriteInExcelTableCS(RollNo, Integer.toString(output[1][0]), Integer.toString(output[1][1]),
				Integer.toString(output[1][2]), Integer.toString(output[1][3]), Status, Integer.toString(tableCS[1][0]),
				Integer.toString(tableCS[1][1]), Integer.toString(tableCS[1][2]), Integer.toString(tableCS[1][3]));
		WriteInExcelTableCS(RollNo, Integer.toString(output[2][0]), Integer.toString(output[2][1]),
				Integer.toString(output[2][2]), Integer.toString(output[2][3]), Status, Integer.toString(tableCS[2][0]),
				Integer.toString(tableCS[2][1]), Integer.toString(tableCS[2][2]), Integer.toString(tableCS[2][3]));
		WriteInExcelTableCS(RollNo, Integer.toString(output[3][0]), Integer.toString(output[3][1]),
				Integer.toString(output[3][2]), Integer.toString(output[3][3]), Status, Integer.toString(tableCS[3][0]),
				Integer.toString(tableCS[3][1]), Integer.toString(tableCS[3][2]), Integer.toString(tableCS[3][3]));

	}

	public void CreateExcelCSM(String RollNo) throws FileNotFoundException, IOException {

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("ChallengeSystemWithMaster");

		rowCount = 0;
		WriteInExcelCSM("RollNo", "QID", "CorrectAnswerCS", "Option No.", "MasterQP Option", "CorrectAnswerStatus");
		WriteInExcelCSMQstns("CandidateQuestionCS", "CandidateQuestionMasterQP", "QuestionStatus");
		// for Each Option
		WriteInExcelCSMOptns("CSOption", "MQPOption", "OptionStatus", csmOptRowCount, 11);
		// for overall option status
		WriteInExcelCSMOptionFinalStatus("Overall Option Status");
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
		WriteInFinalExcel("Date", "Subject Code", "Membership No", "Password", "Master QP count",
				"Challenge System count", "Audit trail count", "Status");
	}

	public void WriteInExcelACS(String RollNo, String text1, String text2, String text3, String text4)
			throws FileNotFoundException, IOException {

		Object[][] bookData = { { RollNo, text1, text2, text3, text4 }, };

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
		try (FileOutputStream outputStream = new FileOutputStream(
				"C:\\Results\\AdminWithChallengeSystem\\TestResult_ACS.xlsx")) {
			workbook.write(outputStream);
		}

	}

	public void WriteInExcelBrokenPE(String text1, String text2, String text3, String RollNo)
			throws FileNotFoundException, IOException {

		Object[][] bookData = { { text1, text2, text3 }, };

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
		try (FileOutputStream outputStream = new FileOutputStream(
				"F:\\Results\\BrokenLinks\\TestResult_BrokenLink_PE" + RollNo + ".xlsx")) {
			workbook.write(outputStream);
		}

	}

	public void WriteInExcelBrokenCS(String text1, String text2, String text3, String RollNo)
			throws FileNotFoundException, IOException {

		Object[][] bookData = { { text1, text2, text3 }, };

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
		try (FileOutputStream outputStream = new FileOutputStream(
				"F:\\Results\\BrokenLinks\\TestResult_BrokenLink_CS" + RollNo + ".xlsx")) {
			workbook.write(outputStream);
		}

	}

	public void WriteInFinalExcel(String text1, String text2, String text3, String text4, String text5, String text6,
			String text7, String text8) throws FileNotFoundException, IOException {

		// fdriver.quit();

		Object[][] bookData = { { text1, text2, text3, text4, text5, text6, text7, text8 }, };

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
		try (FileOutputStream outputStream = new FileOutputStream("C:\\Results\\FinalCount\\FinalCount.xlsx")) {
			workbook.write(outputStream);
		}

	}

	public String[] getOptionTextCS() {

		String option1 = "";
		String option2 = "";
		String option3 = "";
		String option4 = "", option5 = "";
		String Options = cdriver.findElementByXPath("((//b[text()='Options:'])/..)[" + start + "]").getText();
		Options = new StringBuffer(Options).replace(0, 8, "").toString();
		if (testOutput == true) {
			System.out.println("Option Text is " + Options);
		}

		int Op1Start = 0, Op1end = 0, Op2Start = 0, Op2end = 0, Op3Start = 0, Op3end = 0, Op4Start = 0, Op4end = 0,
				Op5Start = 0, Op5end = 0;

		// Getting Option start position
		for (int i = 0; i < Options.length(); i++) {
			if (Op1Start == 0) {
				if (Options.charAt(i) == '1') {

					if (Options.charAt(i + 1) == ')') {
						if (Options.charAt(i + 2) == ' ') {
							// if(Options.charAt(i+3)==' ')

							{
								Op1Start = i + 3;
							}

						}
					}
				}
			}

			if (Op2Start == 0) {
				if (Options.charAt(i) == '2') {
					if (Options.charAt(i + 1) == ')') {
						if (Options.charAt(i + 2) == ' ') {
							// if(Options.charAt(i+3)==' ')

							{
								Op2Start = i + 3;
							}

						}
					}
				}
			}

			if (Op3Start == 0) {
				if (Options.charAt(i) == '3') {
					if (Options.charAt(i + 1) == ')') {
						if (Options.charAt(i + 2) == ' ') {
							// if(Options.charAt(i+3)==' ')

							{
								Op3Start = i + 3;
							}

						}
					}

				}
			}

			if (Op4Start == 0) {
				if (Options.charAt(i) == '4') {
					if (Options.charAt(i + 1) == ')') {
						if (Options.charAt(i + 2) == ' ') {
							// if(Options.charAt(i+3)==' ')

							{
								Op4Start = i + 3;

								if (optionFive == false) {
									break;
								}
							}
						}
					}
				}
			}

			if (optionFive == true) {
				if (Op5Start == 0) {
					if (Options.charAt(i) == '5') {
						if (Options.charAt(i + 1) == ')') {
							if (Options.charAt(i + 2) == ' ') {
								// if(Options.charAt(i+3)==' ')

								{
									Op5Start = i + 3;
									break;
								}
							}
						}
					}
				}
			}
		}

		// Getting Option end position
		if (optionFive == true) {
			Op1end = Op2Start - 4;
			Op2end = Op3Start - 4;
			Op3end = Op4Start - 4;
			Op4end = Op5Start - 4;
			Op5end = Options.length();
		} else {
			Op1end = Op2Start - 4;
			Op2end = Op3Start - 4;
			Op3end = Op4Start - 4;
			Op4end = Options.length();
		}

		// Verifying start and end position of options
		if (testOutput == true) {
			System.out.println(Op1Start);
			System.out.println(Op2Start);
			System.out.println(Op3Start);
			System.out.println(Op4Start);
			if (optionFive == true) {
				System.out.println(Op5Start);
			}
			System.out.println(Op1end);
			System.out.println(Op2end);
			System.out.println(Op3end);
			System.out.println(Op4end);
			if (optionFive == true) {
				System.out.println(Op5end);
			}
		}

		// Getting Separate option text
		option1 = Options.substring(Op1Start, Op1end);
		option2 = Options.substring(Op2Start, Op2end);
		option3 = Options.substring(Op3Start, Op3end);
		option4 = Options.substring(Op4Start, Op4end);
		if (optionFive == true) {
			option5 = Options.substring(Op5Start, Op5end);
		}

		// Verifying the Separate Option Text
		if (testOutput == true) {
			System.out.println("Option 1 is " + option1);
			System.out.println("Option 2 is " + option2);
			System.out.println("Option 3 is " + option3);
			System.out.println("Option 4 is " + option4);
			if (optionFive == true) {
				System.out.println("Option 5 is " + option5);
			}
		}

		optionText[0] = option1;
		optionText[1] = option2;
		optionText[2] = option3;
		optionText[3] = option4;
		if (optionFive == true) {
			optionText[4] = option5;
		}

		return optionText;

	}

	public String getImageFileName() {
		int slash = 0;
		for (slash = FullURL.length() - 1; slash >= 0; slash--) {
			if (FullURL.charAt(slash) == '/') {
				break;
			}
		}
		FullURL = new StringBuffer(FullURL).replace(0, slash, "").toString();
		return FullURL;
	}

	public long takeSnap() {
		// TODO Auto-generated method stub
		return 0;
	}

//	------------------ Poovendan & sheik ------------------

	public void compareCSMQuestions() throws FileNotFoundException, IOException {
		for (start = 1; start <= AvailableQuestionsCS.size(); start++) {
			try {
				// Get QID in Challenge System
				String QID = cdriver.findElementByXPath("(//b[starts-with(text(),'QID : ')])[" + start + "]").getText();
				QID = QID.replaceAll("[^0-9]", "");
				// Get QID in Master Copy
				String QIDMaster = "";
				try {
					fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[6]").isDisplayed();
					// with date
					QIDMaster = fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[4]").getText().trim();
				} catch (Exception e) {
					// without date
					QIDMaster = fdriver.findElementByXPath("//tbody/tr[" + (start + 1) + "]/td[3]").getText().trim();
				}
 
//			System.out.println(QIDMaster + "...." + QID);
 
				if (QID.equalsIgnoreCase(QIDMaster)) {
					boolean imageCheck = true;
					try {
						// getting image from CS
						try {
							cdriver.findElementByXPath("//b[text()='QID : " + QID + "   ']//following-sibling::img");
						} catch (NoSuchElementException e) {
							imageCheck = false;
						}
						if (imageCheck == true) {
//							System.out.println("Image found");
							// getting question imgs from CS
							boolean val=cdriver.findElementByXPath("//b[text()='QID : " + QID + "   ']//following-sibling::img").isDisplayed();
							if(val) {
							List<WebElement> imgQsCsEle = new ArrayList<WebElement>();
							int noOfQsImgInCs = 0;
 
							// some images in CS question are inside the tab, so handling here
							try {
								cdriver.findElement(
										By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::img"));
								imgQsCsEle = cdriver.findElements(
										By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::img"));
								noOfQsImgInCs = imgQsCsEle.size();
							} catch (Exception e) {
								cdriver.findElements(
										By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::tab/img"));
								imgQsCsEle = cdriver.findElements(
										By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::tab/img"));
								noOfQsImgInCs = imgQsCsEle.size();
							}
							String QPImgQstns = "", CSImgQstns = "";
							try {
 
								for (int ind = 1; ind <= noOfQsImgInCs; ind++) {
									String QPImgQstn = fdriver.findElementByXPath("(//td[4][text()='" + QID
											+ "']/following-sibling::td)[1]//following-sibling::img[" + ind + "]")
											.getAttribute("src");
 
									String CSImgQstn = cdriver.findElementByXPath(
											"//b[text()='QID : " + QID + "   ']//following-sibling::img[" + ind + "]")
											.getAttribute("src");
									// splitting up img URL and getting only attribute name
									String sp1[] = QPImgQstn.split("/");
									QPImgQstn = sp1[sp1.length - 1];
									String sp2[] = CSImgQstn.split("/");
									CSImgQstn = sp2[sp2.length - 1];
 
									// This code will execute when more than one images in QS
									if (ind > 1) {
										QPImgQstns = QPImgQstns + " & " + QPImgQstn;
										CSImgQstns = CSImgQstns + " & " + CSImgQstn;
									} else {
//										This code will execute when only one images in QS
										QPImgQstns = QPImgQstn;
										CSImgQstns = CSImgQstn;
									}
 
								}
 
								System.out.println("Question ID : " + QID);
								if (QPImgQstns.equalsIgnoreCase(CSImgQstns)) {
									System.out.println("Candidate Question from Challenge System URL: " + CSImgQstns);
									System.out.println("Candidate Question from Master URL: " + QPImgQstns);
									System.out.println("CANDIDATE QUESTIONS MATCHED");
									WriteInExcelCSMQstns(CSImgQstns, QPImgQstns, "PASS");
								} else {
									System.out.println("Candidate Question from Challenge System URL: " + CSImgQstns);
									System.out.println("Candidate Question from Master URL: " + QPImgQstns);
									System.out.println("CANDIDATE QUESTIONS NOT MATCHED");
									WriteInExcelCSMQstns(CSImgQstns, QPImgQstns, "FAIL");
								}
							} catch (Exception e) {
								System.out.println("Exception: " + e);
								WriteInExcelCSMQstns("", "", "FAIL");
							}
						}else {
							WriteInExcelCSMQstns("", "", "FAIL");
						}
						} else {
							try {
								// getting Question text from CS
								String CSQstns_xp = "//b[text()='QID : " + QID + "   ']/..";
								String CSQstnsText = cdriver.findElement(By.xpath(CSQstns_xp)).getText();
								int hypInd = CSQstnsText.indexOf("-");
								CSQstnsText = CSQstnsText.substring(hypInd + 1);
								CSQstnsText = CSQstnsText.replaceAll("\n+", "").trim();
								System.out.println("Question ID : " + QID);
 
								// getting Question text from MasterQP
								String MQP_QstnFtext_xp = "(//td[4][text()='" + QIDMaster
										+ "']/following-sibling::td)[1]";
 
								String escapedPattern = Pattern.quote("**** CASE QN ****");
								String MQP_QstnFtext = fdriver.findElement(By.xpath(MQP_QstnFtext_xp)).getText()
										.replaceAll(escapedPattern, "").replaceAll("\n+", "").trim();
 
								String partialQstn = MQP_QstnFtext;
								if (partialQstn.contains(CSQstnsText)) {
									partialQstn = partialQstn.replace(CSQstnsText, "");
									MQP_QstnFtext = MQP_QstnFtext.replace(partialQstn, "");
									if (CSQstnsText.equalsIgnoreCase(MQP_QstnFtext)) {
										System.out.println(
												"Candidate Question from Challenge System URL: " + CSQstnsText);
										System.out.println("Candidate Question from Master URL: " + MQP_QstnFtext);
										System.out.println("CANDIDATE QUESTIONS MATCHED");
										WriteInExcelCSMQstns(CSQstnsText, MQP_QstnFtext, "PASS");
									} else {
										System.out.println("CANDIDATE QUESTIONS NOT MATCHED");
										WriteInExcelCSMQstns(CSQstnsText, "", "FAIL");
									}
 
								} else {
									System.out.println("CANDIDATE QUESTIONS NOT MATCHED");
									WriteInExcelCSMQstns(CSQstnsText, "", "FAIL");
								}
							} catch (Exception e) {
								System.out.println("Exception is... " + e);
								WriteInExcelCSMQstns("", "", "FAIL");
							}
 
						}
 
						System.out.printf("\n");
 
					} catch (Exception e) {
						System.out.println("Exception found is: " + e);
						WriteInExcelCSMQstns("", "", "FAIL");
					}
				} else {
					System.out.println("Question ID is not matching");
					WriteInExcelCSMQstns("", "", "FAIL");
				}
			} catch (Exception e) {
				System.out.println("Exception found is: " + e);
				WriteInExcelCSMQstns("", "", "FAIL");
			}
		} // for loop
 
	}

	public void compareCSMOptions() throws IOException {
		for (startcsm = 1; startcsm <= AvailableQuestionsCS.size(); startcsm++) {
//		for (startcsm = 57; startcsm <= 61; startcsm++) {
			try {

				// Get QID in Challenge System
				String QID = cdriver.findElementByXPath("(//b[starts-with(text(),'QID : ')])[" + startcsm + "]")
						.getText();
				QID = QID.replaceAll("[^0-9]", "");
				// Get QID in Master Copy
				String QIDMaster = "";
				try {
					fdriver.findElementByXPath("//tbody/tr[" + (startcsm + 1) + "]/td[6]").isDisplayed();
					QIDMaster = fdriver.findElementByXPath("//tbody/tr[" + (startcsm + 1) + "]/td[4]").getText().trim();
				} catch (Exception e) {
					QIDMaster = fdriver.findElementByXPath("//tbody/tr[" + (startcsm + 1) + "]/td[3]").getText().trim();
				}
				if (QID.equalsIgnoreCase(QIDMaster)) {
					boolean imageCheck = true;
					int row = startcsm + 1;
					try {
						// if options is image based
						try {
							cdriver.findElement(By.xpath(
									"//b[text()='QID : " + QID + "   ']/following::tr[1]//following-sibling::img"));
						} catch (Exception e) {
							imageCheck = false;
						}

						if (imageCheck == true) {
							try {
								System.out.println("Image found on " + QID);

								// getting options from MasterQP
								List<WebElement> MQPFullImgEle = fdriver.findElements(By.xpath(
										"//tbody//tr[" + row + "]//following-sibling::td[5]//following-sibling::img"));
//						System.out.println(MQPFullImgEle.size());
								String mqpFullText = fdriver
										.findElement(By.xpath("//tbody//tr[" + row + "]//following-sibling::td[5]"))
										.getText();
//						System.out.println(mqpFullText);

								// getting question imgs from CS
								List<WebElement> imgQsCsEle = new ArrayList<WebElement>();
								int noOfQsImgInCs = 0;

								// some images in CS question are inside the tab, so handling here
								try {
									cdriver.findElement(
											By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::img"));
									imgQsCsEle = cdriver.findElements(
											By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::img"));
									noOfQsImgInCs = imgQsCsEle.size();
								} catch (Exception e) {
									cdriver.findElements(
											By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::tab/img"));
									imgQsCsEle = cdriver.findElements(
											By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::tab/img"));
									noOfQsImgInCs = imgQsCsEle.size();
								}

								// getting option from CS
								List<WebElement> imgOptCSEle = cdriver.findElementsByXPath(
										"//b[text()='QID : " + QID + "   ']/following::tr[1]//img");
								String[] opt = { "A", "B", "C", "D" };
								int[] colCountExcel = { 11, 14, 17, 20 };
								String MQPOptionTxt = "", CSOptionTxt = "";
								int count = 0;
								int imgLocator = 0;
								// getting Question text from CS
								String CSQstns_xp = "//b[text()='QID : " + QID + "   ']/..";
								String CSQstnsText = cdriver.findElement(By.xpath(CSQstns_xp)).getText();
								int hypInd = CSQstnsText.indexOf("-");
								CSQstnsText = CSQstnsText.substring(hypInd + 1).trim();
//						CSQstnsText = CSQstnsText.replaceAll("\n+", "").trim();

								// getting Option text from CS
								String CSOption_xp = "//b[text()='QID : " + QID + "   ']/following::tr[1]";
								String CSOptionText = cdriver.findElement(By.xpath(CSOption_xp)).getText().substring(9)
//								.replaceAll("\n+", "")
										.trim();
								String MQP_QstnFtext_xp = "//tbody//tr[" + row + "]//following-sibling::td[5]";
								String escapedPattern = Pattern.quote("**** CASE QN ****");
								String MQP_QstnFtext = fdriver.findElement(By.xpath(MQP_QstnFtext_xp)).getText()
										.replaceAll(escapedPattern, "")
//								.replaceAll("\n+", "")
										.trim();
								String[] CSOptions = new String[4];
								String MQPOptions[] = new String[4];
								System.out.println("Question ID: " + QID);
								boolean isFifthOption = false;
								if (MQP_QstnFtext.contains(CSQstnsText)) {

									String MQP_OptionTxt = MQP_QstnFtext.replace(CSQstnsText, "").trim();

									// for CS options

									int indACS = CSOptionText.indexOf("1)");
									int indBCS = CSOptionText.indexOf("\n2)");
									int indCCS = CSOptionText.indexOf("\n3)");
									int indDCS = CSOptionText.indexOf("\n4)");
									int indECS = CSOptionText.indexOf("\n5)");
//							System.out.println(indDCS);

									String firstOptCS = CSOptionText.substring(indACS, indBCS).replaceFirst("1\\)", "")
											.trim();
									String secondOptCS = CSOptionText.substring(indBCS, indCCS).replaceFirst("2\\)", "")
											.trim();
									String thirdOptCS = CSOptionText.substring(indCCS, indDCS).replaceFirst("3\\)", "")
											.trim();
									String fourOptCS = CSOptionText.substring(indDCS).replaceFirst("4\\)", "").trim();
									String fifthOptCS = "";
									CSOptions[0] = firstOptCS;
									CSOptions[1] = secondOptCS;
									CSOptions[2] = thirdOptCS;
									CSOptions[3] = fourOptCS;
									// for MQP options

									int indAMQP = MQP_OptionTxt.indexOf("(A)");
									int indBMQP = MQP_OptionTxt.indexOf("\n(B)");
									int indCMQP = MQP_OptionTxt.indexOf("\n(C)");
									int indDMQP = MQP_OptionTxt.indexOf("\n(D)");
									int indEMQP = MQP_OptionTxt.indexOf("\n(E)");

									String firstOptMQP = MQP_OptionTxt.substring(indAMQP, indBMQP)
											.replaceFirst("\\(A\\)", "").trim();
									String secondOptMQP = MQP_OptionTxt.substring(indBMQP, indCMQP)
											.replaceFirst("\\(B\\)", "").trim();
									String thirdOptMQP = MQP_OptionTxt.substring(indCMQP, indDMQP)
											.replaceFirst("\\(C\\)", "").trim();
									String fourOptMQP = MQP_OptionTxt.substring(indDMQP).replaceFirst("\\(D\\)", "")
											.trim();
									String fifthOptMQP = "";
									MQPOptions[0] = firstOptMQP;
									MQPOptions[1] = secondOptMQP;
									MQPOptions[2] = thirdOptMQP;
									MQPOptions[3] = fourOptMQP;

									// handles when 5 option is there
									if (indECS != -1 && indEMQP != -1) {
										isFifthOption = true;
										fifthOptCS = CSOptionText.substring(indDCS).replaceFirst("4\\)", "").trim();
										fifthOptMQP = MQP_OptionTxt.substring(indDMQP).replaceFirst("\\(D\\)", "")
												.trim();
										CSOptions[4] = fifthOptCS;
										MQPOptions[4] = fifthOptMQP;
									}

//									System.out.println(MQPOptions[1] + "..");
								} else {
									System.out.println("Question not matching");
									csmOptRowCount++;
									WriteInExcelCSMOptionFinalStatus("FAIL");
								}

//								System.out.println("NO of img..." + noOfQsImgInCs);

								if (mqpFullText.trim().indexOf("(A)") == 0 || noOfQsImgInCs != 0) {
//							System.out.println("Question has an image");
									// question has an image, so starting from second image in MQP

									int iteration = isFifthOption == true ? 5 : 4;

									if (isFifthOption == true) {
										colCountExcel[4] = 24;
									}

									int mqpImgLocator = noOfQsImgInCs;
									List<String> optImgList = new ArrayList<String>();
									for (int ind = 1; ind <= iteration; ind++) {
										if (CSOptions[ind - 1].equals("") && MQPOptions[ind - 1].equals("")) {
											imgLocator++;
											mqpImgLocator++;
											try {
												String imgSrcMQP = fdriver.findElement(By.xpath("(//tbody//tr[" + row
														+ "]//following-sibling::td[5]//following-sibling::img)["
														+ mqpImgLocator + "]")).getAttribute("src");
												String imgSrcCs = cdriver
														.findElementByXPath("//b[text()='QID : " + QID
																+ "   ']/following::tr[1]//img[" + imgLocator + "]")
														.getAttribute("src");

												// Handling for double Images in same option
												if (optImgList.contains(imgSrcCs)) {
													imgLocator++;
													mqpImgLocator++;
													imgSrcMQP = fdriver.findElement(By.xpath("(//tbody//tr[" + row
															+ "]//following-sibling::td[5]//following-sibling::img)["
															+ mqpImgLocator + "]")).getAttribute("src");
													imgSrcCs = cdriver
															.findElementByXPath("//b[text()='QID : " + QID
																	+ "   ']/following::tr[1]//img[" + imgLocator + "]")
															.getAttribute("src");
													optImgList.add(imgSrcCs);
												} else {
													optImgList.add(imgSrcCs);
												}

												String sp1[] = imgSrcMQP.split("/");
												MQPOptionTxt = sp1[sp1.length - 1];
												// getting image option text in CS
												String sp2[] = imgSrcCs.split("/");
												CSOptionTxt = sp2[sp2.length - 1];

												if (CSOptionTxt.equalsIgnoreCase(MQPOptionTxt)) {
													count++;
													System.out.println("Option " + ind + " from CS :" + CSOptionTxt
															+ " ------ " + "Option " + opt[ind - 1] + " from MQP :"
															+ MQPOptionTxt);
													System.out.println("OPTION " + ind + " MATCHED");
													WriteInExcelCSMOptns(CSOptionTxt, MQPOptionTxt, "PASS",
															csmOptRowCount + 1, colCountExcel[ind - 1]);
												} else {
													System.out.println("Option " + ind + " from CS :" + CSOptionTxt
															+ " ------ " + "Option " + opt[ind - 1] + " from MQP :"
															+ MQPOptionTxt);
													System.out.println("OPTION " + ind + " NOT MATCHED");
													WriteInExcelCSMOptns(CSOptionTxt, MQPOptionTxt, "FAIL",
															csmOptRowCount + 1, colCountExcel[ind - 1]);
												}
											} catch (Exception e) {
												System.out.println("Exception e.." + e);
												WriteInExcelCSMOptns("", "", "FAIL", csmOptRowCount + 1,
														colCountExcel[ind - 1]);
											}
										} else {
											try {
												if (CSOptions[ind - 1].equalsIgnoreCase(MQPOptions[ind - 1])) {
													count++;
													System.out.println("Option " + ind + " from CS :"
															+ CSOptions[ind - 1] + " ------ " + "Option " + opt[ind - 1]
															+ " from MQP :" + MQPOptions[ind - 1]);
													System.out.println("OPTION " + ind + " MATCHED");
													WriteInExcelCSMOptns(CSOptions[ind - 1], MQPOptions[ind - 1],
															"PASS", csmOptRowCount + 1, colCountExcel[ind - 1]);
												} else {
													System.out.println("Option " + ind + " from CS :"
															+ CSOptions[ind - 1] + " ------ " + "Option " + opt[ind - 1]
															+ " from MQP :" + MQPOptions[ind - 1]);
													System.out.println("OPTION " + ind + " NOT MATCHED");
													WriteInExcelCSMOptns(CSOptions[ind - 1], MQPOptions[ind - 1],
															"FAIL", csmOptRowCount + 1, colCountExcel[ind - 1]);
												}
											} catch (Exception e) {
												System.out.println("Exception e.." + e);
												WriteInExcelCSMOptns("", "", "FAIL", csmOptRowCount + 1,
														colCountExcel[ind - 1]);
											}
										}

									}
								} else {
//							System.out.println("Question has no image");
									imgLocator = 0;
									// question does not have an image, so starting from first image in MQP
									int iteration = isFifthOption == true ? 5 : 4;

									if (isFifthOption == true) {
										colCountExcel[4] = 24;
									}
									for (int ind = 1; ind <= iteration; ind++) {
										if (CSOptions[ind - 1].equals("") && MQPOptions[ind - 1].equals("")) {
											imgLocator++;
											try {
												String imgSrcMQP = fdriver.findElement(By.xpath("(//tbody//tr[" + row
														+ "]//following-sibling::td[5]//following-sibling::img)["
														+ imgLocator + "]")).getAttribute("src");
												String imgSrcCs = cdriver
														.findElementByXPath("//b[text()='QID : " + QID
																+ "   ']/following::tr[1]//img[" + imgLocator + "]")
														.getAttribute("src");

												String sp1[] = imgSrcMQP.split("/");
												MQPOptionTxt = sp1[sp1.length - 1];
												// getting image option text in CS
												String sp2[] = imgSrcCs.split("/");
												CSOptionTxt = sp2[sp2.length - 1];
//										
												if (CSOptionTxt.equalsIgnoreCase(MQPOptionTxt)) {
													count++;
													System.out.println("Option " + ind + " from CS :" + CSOptionTxt
															+ " ------ " + "Option " + opt[ind - 1] + " from MQP :"
															+ MQPOptionTxt);
													System.out.println("OPTION " + ind + " MATCHED");
													WriteInExcelCSMOptns(CSOptionTxt, MQPOptionTxt, "PASS",
															csmOptRowCount + 1, colCountExcel[ind - 1]);
												} else {
													System.out.println("Option " + ind + " from CS :" + CSOptionTxt
															+ " ------ " + "Option " + opt[ind - 1] + " from MQP :"
															+ MQPOptionTxt);
													System.out.println("OPTION " + ind + " NOT MATCHED");
													WriteInExcelCSMOptns(CSOptionTxt, MQPOptionTxt, "FAIL",
															csmOptRowCount + 1, colCountExcel[ind - 1]);
												}
											} catch (Exception e) {
												System.out.println("Exception e.." + e);
												WriteInExcelCSMOptns("", "", "FAIL", csmOptRowCount + 1,
														colCountExcel[ind - 1]);
											}
										} else {
											try {
												if (CSOptions[ind - 1].equalsIgnoreCase(MQPOptions[ind - 1])) {
													count++;
													System.out.println("Option " + ind + " from CS :"
															+ CSOptions[ind - 1] + " ------ " + "Option " + opt[ind - 1]
															+ " from MQP :" + MQPOptions[ind - 1]);
													System.out.println("OPTION " + ind + " MATCHED");
													WriteInExcelCSMOptns(CSOptions[ind - 1], MQPOptions[ind - 1],
															"PASS", csmOptRowCount + 1, colCountExcel[ind - 1]);
												} else {
													System.out.println("Option " + ind + " from CS :"
															+ CSOptions[ind - 1] + " ------ " + "Option " + opt[ind - 1]
															+ " from MQP :" + MQPOptions[ind - 1]);
													System.out.println("OPTION " + ind + " NOT MATCHED");
													WriteInExcelCSMOptns(CSOptions[ind - 1], MQPOptions[ind - 1],
															"FAIL", csmOptRowCount + 1, colCountExcel[ind - 1]);
												}
											} catch (Exception e) {
												System.out.println("Exception found is " + e);
												WriteInExcelCSMOptns("", "", "FAIL", csmOptRowCount + 1,
														colCountExcel[ind - 1]);
											}
										}

									}
								}
								if (isFifthOption == true) {
									if (count == 5) {
										System.out.println("All Options from CS is Matched with All Options from MQP");
										WriteInExcelCSMOptionFinalStatus("PASS");
									} else {
										System.out.println(
												"All Options from CS is Not Matched with All Options from MQP");
										WriteInExcelCSMOptionFinalStatus("FAIL");
									}
								} else {
									if (count == 4) {
										System.out.println("All Options from CS is Matched with All Options from MQP");
										WriteInExcelCSMOptionFinalStatus("PASS");
									} else {
										System.out.println(
												"All Options from CS is Not Matched with All Options from MQP");
										WriteInExcelCSMOptionFinalStatus("FAIL");
									}
								}

								csmOptRowCount++;

							} catch (Exception e) {
								System.out.println("Exception found is: " + e);
								csmOptRowCount++;
								WriteInExcelCSMOptionFinalStatus("FAIL");
							}
						} else {
							try {

								// getting Question text from CS
								String CSQstns_xp = "//b[text()='QID : " + QID + "   ']/..";
								String CSQstnsText = cdriver.findElement(By.xpath(CSQstns_xp)).getText();
								int hypInd = CSQstnsText.indexOf("-");
								CSQstnsText = CSQstnsText.substring(hypInd + 1).trim();

								// getting Option text from CS
								String CSOption_xp = "//b[text()='QID : " + QID + "   ']/following::tr[1]";
								String CSOptionText = cdriver.findElement(By.xpath(CSOption_xp)).getText()
//								.replaceAll("\n+", "")
										.trim();

								// handles when table is there in Questions
								int inc = 2;
								while (!CSOptionText.contains("Options:")) {
									CSOption_xp = "//b[text()='QID : " + QID + "   ']/following::tr[" + inc + "]";
									CSOptionText = cdriver.findElement(By.xpath(CSOption_xp)).getText();
									inc++;
								}

								// getting Option text from MasterQP

								String MQP_QstnFtext_xp = "//tbody//tr[" + row + "]//following-sibling::td[5]";
								String MQP_QstnFtext = fdriver.findElement(By.xpath(MQP_QstnFtext_xp)).getText().trim();
								
								// FOR CASE QN
								boolean isCaseQN = false;
								if (MQP_QstnFtext.contains("**** CASE QN ****")) {
									String escapedPattern = Pattern.quote("**** CASE QN ****");
									MQP_QstnFtext = fdriver.findElement(By.xpath(MQP_QstnFtext_xp)).getText()
											.replaceAll(escapedPattern, "").replaceAll("\n+", "").trim();
									CSQstnsText = CSQstnsText.replaceAll("\n+", "").trim();
									isCaseQN = true;
								}


								System.out.println("----------------------------------------------------");

								if (MQP_QstnFtext.contains(CSQstnsText)) {
									String MQP_OptionTxt = MQP_QstnFtext.replace(CSQstnsText, "").trim();
									// for CS options
									int indACS = CSOptionText.indexOf("1)");
									int indBCS = CSOptionText.indexOf("\n2)");
									int indCCS = CSOptionText.indexOf("\n3)");
									int indDCS = CSOptionText.indexOf("\n4)");
									int indECS = CSOptionText.indexOf("\n5)");

//							System.out.println("after index of CS..."+indACS+"..."+indBCS+"..."+indCCS+"..."+indDCS);

									String firstOptCS = CSOptionText.substring(indACS, indBCS).replaceFirst("1\\)", "")
											.trim();
									String secondOptCS = CSOptionText.substring(indBCS, indCCS).replaceFirst("2\\)", "")
											.trim();
									String thirdOptCS = CSOptionText.substring(indCCS, indDCS).replaceFirst("3\\)", "")
											.trim();
									String fourOptCS = CSOptionText.substring(indDCS).replaceFirst("4\\)", "").trim();
									String fifthOptCS = "";

									// for MQP options
									int indAMQP = 0, indBMQP = 0, indCMQP = 0, indDMQP = 0, indEMQP = 0;
									if (isCaseQN == true) {
										indAMQP = MQP_OptionTxt.indexOf("(A) ");
										indBMQP = MQP_OptionTxt.indexOf("(B) ");
										indCMQP = MQP_OptionTxt.indexOf("(C) ");
										indDMQP = MQP_OptionTxt.indexOf("(D) ");
										indEMQP = MQP_OptionTxt.indexOf("(E) ");
									} else {
										indAMQP = MQP_OptionTxt.indexOf("(A)");
										indBMQP = MQP_OptionTxt.indexOf("\n(B)");
										indCMQP = MQP_OptionTxt.indexOf("\n(C)");
										indDMQP = MQP_OptionTxt.indexOf("\n(D)");
										indEMQP = MQP_OptionTxt.indexOf("\n(E)");
									}
//									System.out.println(
//											"Here 2" + indAMQP + "..." + indBMQP + "..." + indCMQP + ".." + indDMQP);
//							System.out.println("after index of MQP");

									String firstOptMQP = MQP_OptionTxt.substring(indAMQP, indBMQP)
											.replaceFirst("\\(A\\)", "").trim();
									String secondOptMQP = MQP_OptionTxt.substring(indBMQP, indCMQP)
											.replaceFirst("\\(B\\)", "").trim();
									String thirdOptMQP = MQP_OptionTxt.substring(indCMQP, indDMQP)
											.replaceFirst("\\(C\\)", "").trim();
									String fourOptMQP = MQP_OptionTxt.substring(indDMQP).replaceFirst("\\(D\\)", "")
											.trim();
									String fifthOptMQP = "";
									System.out.println("Question ID : " + QID);
//									System.out.println("Here 2.1");
									// if
									if (indECS != -1 && indEMQP != -1) {

										boolean opt1 = false, opt2 = false, opt3 = false, opt4 = false, opt5 = false;

										fifthOptCS = CSOptionText.substring(indECS).replaceFirst("5\\)", "").trim();
										fifthOptMQP = MQP_OptionTxt.substring(indEMQP).replaceFirst("\\(E\\)", "")
												.trim();

										for (int ind = 1; ind <= 4; ind++) {
											if (ind == 1) {
												if (firstOptMQP.equalsIgnoreCase(firstOptCS)) {
													opt1 = true;
													System.out.println("Option 1 from CS :" + firstOptCS + " ------ "
															+ "Option A from MQP :" + firstOptMQP);
													System.out.println("OPTION 1 MATCHED");
													WriteInExcelCSMOptns(firstOptCS, firstOptMQP, "PASS",
															csmOptRowCount + 1, 11);
												} else {
													System.out.println("Option 1 from CS :" + firstOptCS + " ------ "
															+ "Option A from MQP :" + firstOptMQP);
													System.out.println("OPTION 1 NOT MATCHED");
													WriteInExcelCSMOptns(firstOptCS, firstOptMQP, "FAIL",
															csmOptRowCount + 1, 11);
												}
											} else if (ind == 2) {
												if (secondOptMQP.equalsIgnoreCase(secondOptCS)) {
													opt2 = true;
													System.out.println("Option 2 from CS :" + secondOptCS + " ------ "
															+ "Option B from MQP :" + secondOptMQP);
													System.out.println("OPTION 2 MATCHED");
													WriteInExcelCSMOptns(secondOptCS, secondOptMQP, "PASS",
															csmOptRowCount + 1, 14);
												} else {
													System.out.println("Option 2 from CS :" + secondOptCS + " ------ "
															+ "Option B from MQP :" + secondOptMQP);
													System.out.println("OPTION 2 NOT MATCHED");
													WriteInExcelCSMOptns(secondOptCS, secondOptMQP, "FAIL",
															csmOptRowCount + 1, 14);
												}
											} else if (ind == 3) {
												if (thirdOptMQP.equalsIgnoreCase(thirdOptCS)) {
													opt3 = true;
													System.out.println("Option 3 from CS :" + thirdOptCS + " ------ "
															+ "Option C from MQP :" + thirdOptMQP);
													System.out.println("OPTION 3 MATCHED");
													WriteInExcelCSMOptns(thirdOptCS, thirdOptMQP, "PASS",
															csmOptRowCount + 1, 17);
												} else {
													System.out.println("Option 3 from CS :" + thirdOptCS + " ------ "
															+ "Option C from MQP :" + thirdOptMQP);
													System.out.println("OPTION 3 NOT MATCHED");
													WriteInExcelCSMOptns(thirdOptCS, thirdOptMQP, "FAIL",
															csmOptRowCount + 1, 17);
												}
											} else if (ind == 4) {
												if (fourOptMQP.equals(fourOptCS)) {
													opt4 = true;
													System.out.println("Option 4 from CS :" + fourOptCS + " ------ "
															+ "Option D from MQP :" + fourOptMQP);
													System.out.println("OPTION 4 MATCHED");
													WriteInExcelCSMOptns(fourOptCS, fourOptMQP, "PASS",
															csmOptRowCount + 1, 20);
												} else {
													System.out.println("Option 4 from CS :" + fourOptCS + " ------ "
															+ "Option D from MQP :" + fourOptMQP);
													System.out.println("OPTION 4 NOT MATCHED");
													WriteInExcelCSMOptns(fourOptCS, fourOptMQP, "FAIL",
															csmOptRowCount + 1, 20);
												}
											} else {
												if (fifthOptMQP.equals(fifthOptCS)) {
													opt5 = true;
													System.out.println("Option 5 from CS :" + fifthOptCS + " ------ "
															+ "Option E from MQP :" + fifthOptMQP);
													System.out.println("OPTION 5 MATCHED");
													WriteInExcelCSMOptns(fifthOptCS, fifthOptMQP, "PASS",
															csmOptRowCount + 1, 23);
												} else {
													System.out.println("Option 5 from CS :" + fifthOptCS + " ------ "
															+ "Option E from MQP :" + fifthOptMQP);
													System.out.println("OPTION 5 MATCHED");
													WriteInExcelCSMOptns(fifthOptCS, fifthOptMQP, "FAIL",
															csmOptRowCount + 1, 23);
												}
											}
										}

										// overall option report

										if (opt1 && opt2 && opt3 && opt4 && opt5) {
											System.out.println(
													"All Options from CS is Matched with All Options from MQP");
											WriteInExcelCSMOptionFinalStatus("PASS");
										} else {
											System.out.println(
													"All Options from CS is Not Matched with All Options from MQP");
											WriteInExcelCSMOptionFinalStatus("FAIL");
										}
									} else {
										boolean opt1 = false, opt2 = false, opt3 = false, opt4 = false;

										for (int ind = 1; ind <= 4; ind++) {
											if (ind == 1) {
												if (firstOptMQP.equalsIgnoreCase(firstOptCS)) {
													opt1 = true;
													System.out.println("Option 1 from CS :" + firstOptCS + " ------ "
															+ "Option A from MQP :" + firstOptMQP);
													System.out.println("OPTION 1 MATCHED");
													WriteInExcelCSMOptns(firstOptCS, firstOptMQP, "PASS",
															csmOptRowCount + 1, 11);
												} else {
													System.out.println("Option 1 from CS :" + firstOptCS + " ------ "
															+ "Option A from MQP :" + firstOptMQP);
													System.out.println("OPTION 1 NOT MATCHED");
													WriteInExcelCSMOptns(firstOptCS, firstOptMQP, "FAIL",
															csmOptRowCount + 1, 11);
												}
											} else if (ind == 2) {
												if (secondOptMQP.equalsIgnoreCase(secondOptCS)) {
													opt2 = true;
													System.out.println("Option 2 from CS :" + secondOptCS + " ------ "
															+ "Option B from MQP :" + secondOptMQP);
													System.out.println("OPTION 2 MATCHED");
													WriteInExcelCSMOptns(secondOptCS, secondOptMQP, "PASS",
															csmOptRowCount + 1, 14);
												} else {
													System.out.println("Option 2 from CS :" + secondOptCS + " ------ "
															+ "Option B from MQP :" + secondOptMQP);
													System.out.println("OPTION 2 NOT MATCHED");
													WriteInExcelCSMOptns(secondOptCS, secondOptMQP, "FAIL",
															csmOptRowCount + 1, 14);
												}
											} else if (ind == 3) {
												if (thirdOptMQP.equalsIgnoreCase(thirdOptCS)) {
													opt3 = true;
													System.out.println("Option 3 from CS :" + thirdOptCS + " ------ "
															+ "Option C from MQP :" + thirdOptMQP);
													System.out.println("OPTION 3 MATCHED");
													WriteInExcelCSMOptns(thirdOptCS, thirdOptMQP, "PASS",
															csmOptRowCount + 1, 17);
												} else {
													System.out.println("Option 3 from CS :" + thirdOptCS + " ------ "
															+ "Option C from MQP :" + thirdOptMQP);
													System.out.println("OPTION 3 NOT MATCHED");
													WriteInExcelCSMOptns(thirdOptCS, thirdOptMQP, "FAIL",
															csmOptRowCount + 1, 17);
												}
											} else {
												if (fourOptMQP.equals(fourOptCS)) {
													opt4 = true;
													System.out.println("Option 4 from CS :" + fourOptCS + " ------ "
															+ "Option C from MQP :" + fourOptMQP);
													System.out.println("OPTION 4 MATCHED");
													WriteInExcelCSMOptns(fourOptCS, fourOptMQP, "PASS",
															csmOptRowCount + 1, 20);
												} else {
													System.out.println("Option 4 from CS :" + fourOptCS + " ------ "
															+ "Option C from MQP :" + fourOptMQP);
													System.out.println("OPTION 4 NOT MATCHED");
													WriteInExcelCSMOptns(fourOptCS, fourOptMQP, "FAIL",
															csmOptRowCount + 1, 20);
												}
											}
										}

										// overall option report

										if (opt1 && opt2 && opt3 && opt4) {
											System.out.println(
													"All Options from CS is Matched with All Options from MQP");
											WriteInExcelCSMOptionFinalStatus("PASS");
										} else {
											System.out.println(
													"All Options from CS is Not Matched with All Options from MQP");
											WriteInExcelCSMOptionFinalStatus("FAIL");
										}
									}

									csmOptRowCount++;

								} else {
									System.out.println("QUESTIONS MISMATCH");
									csmOptRowCount++;
									WriteInExcelCSMOptionFinalStatus("FAIL");
								}
							} catch (Exception e) {
								System.out.println("Exception found is: " + e);
								csmOptRowCount++;
								WriteInExcelCSMOptionFinalStatus("FAIL");
							}
						}
						System.out.printf("\n");
//
					} catch (Exception e) {
						System.out.println("Exception found is: " + e);
						csmOptRowCount++;
						WriteInExcelCSMOptionFinalStatus("FAIL");
					}
				} else {
					csmOptRowCount++;
					WriteInExcelCSMOptionFinalStatus("FAIL");
					System.out.println("Question ID is not matching");
				}
			} catch (Exception e) {
				System.out.println("Exception found is: " + e);
				csmOptRowCount++;
				WriteInExcelCSMOptionFinalStatus("FAIL");
			}
		} // for loop

		saveExcelFile("C:\\Results\\ChallengeSystemWithMaster\\TestResult_CSM.xlsx");

		cdriver.quit();

		fdriver.quit();
	}

	// int rowCountCSMFinalOpt = 0;
	/*
	 * public void WriteInExcelCSMOptionFinalStatus(String status,int rowcount1,int
	 * columncount1) throws FileNotFoundException, IOException {
	 * 
	 * Object[][] bookData = { { status } }; for (Object[] aBook : bookData) { //Row
	 * row = sheet.getRow(++rowCountCSMFinalOpt);
	 * System.out.println("Row Count :"+rowcount1); Row row =
	 * sheet.getRow(rowcount1);
	 * 
	 * if (row == null) { row = sheet.createRow(rowcount1); // Create a new row if
	 * it doesn't exist }
	 * 
	 * for (Object field : aBook) { //Cell cell = row.createCell(++columnCount1);
	 * Cell cell = row.createCell(columncount1); if (field instanceof String) {
	 * cell.setCellValue((String) field); System.out.println(":"+field.toString());
	 * } else if (field instanceof Integer) { cell.setCellValue((Integer) field); }
	 * 
	 * }
	 * 
	 * } /* if(start==2) { try (FileOutputStream outputStream = new
	 * FileOutputStream(
	 * "C:\\Results\\AdminWithChallengeSystem\\TestResult_ACS.xlsx")) {
	 * workbook.write(outputStream); } }else { System.out.println("else "+start); }
	 * 
	 * }
	 */

	public void saveExcelFile(String filePath) throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
			workbook.write(outputStream);
		}
	}
	/*
	 * public void WriteInExcelCSMOptionFinalStatus(String status) throws
	 * FileNotFoundException, IOException {
	 * 
	 * Object[][] bookData = { { status } };
	 * 
	 * for (Object[] aBook : bookData) { Row row =
	 * sheet.getRow(++rowCountCSMFinalOpt);
	 * 
	 * int columnCount = 23;
	 * 
	 * for (Object field : aBook) { Cell cell = row.createCell(++columnCount); if
	 * (field instanceof String) { cell.setCellValue((String) field); } else if
	 * (field instanceof Integer) { cell.setCellValue((Integer) field); } }
	 * 
	 * } try (FileOutputStream outputStream = new FileOutputStream(
	 * "C:\\Results\\AdminWithChallengeSystem\\TestResult_ACS.xlsx")) {
	 * workbook.write(outputStream); }
	 * 
	 * }
	 */

	public void compareACSQuestions() throws FileNotFoundException, IOException {
		for (start = 1; start <= AvailableQuestionsPE.size(); start++) {
//		for (start = 33; start <= 33; start++) {
			try {

				String canditateQstnCS = "", canditateOptCS = "";

				boolean imageCheck = true;

				String FullText = cdriver.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]")
						.getText();

				// To get QID in PE
				boolean loop = true;
				String QID = "";
				int i = 0;

				int tempStart = 0;
				int tempEnd = 0;
				do {

					if (FullText.charAt(i) == '[') {

						tempStart = i;
						loop = false;

					}
					i++;
				} while (loop == true);

				loop = true;
				i = 0;
				do {
					if (FullText.charAt(i) == ']') {
						tempEnd = i;
						loop = false;

					}
					i++;
				} while (loop == true);

				String tempQID = FullText.substring(tempStart + 2, tempEnd - 1);

				QID = tempQID;

				// To check if the Question is image based.
				boolean val1 = false, val2 = false;
				try {
					cdriver.findElementByXPath("((//td[@class='greybluetext10'])[" + start + "]//img)");
					fdriver.findElementByXPath("//b[text()='QID : " + QID + "   ']//following-sibling::img");
				} catch (NoSuchElementException e) {
					imageCheck = false;
				}

				if (imageCheck == true) {

					val1 = cdriver.findElementByXPath("((//td[@class='greybluetext10'])[" + start + "]//img)")
							.isDisplayed();
					val2 = fdriver.findElementByXPath("//b[text()='QID : " + QID + "   ']//following-sibling::img")
							.isDisplayed();

					if (val1 && val2) {

						// getting question imgs from CS
						List<WebElement> imgQsCsEle = new ArrayList<WebElement>();
						int noOfQsImgInCs = 0;

//					boolean isMixture=false;
//					String csQsTxt="",peQsTxt="";
//					csQsTxt= fdriver.findElement(By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling")).getText();
//					peQsTxt=cdriver
//							.findElementByXPath(
//									"((//td[@class='greybluetext10'])[" + start + "])").getText();
//					if(csQsTxt!="") {
//						isMixture=true;
//					}

						// some images in CS question are inside the tab, so handling here
						try {
							fdriver.findElement(By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::img"));
							imgQsCsEle = fdriver.findElements(
									By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::img"));
							noOfQsImgInCs = imgQsCsEle.size();
						} catch (Exception e) {
							fdriver.findElements(
									By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::tab/img"));
							imgQsCsEle = fdriver.findElements(
									By.xpath("//b[text()='QID : " + QID + "   ']//following-sibling::tab/img"));
							noOfQsImgInCs = imgQsCsEle.size();
						}

						try {
							String PEImgQstns = "", CSImgQstns = "";
							// handles when multiple images is there in Questions and printing all the image
							System.out.println(noOfQsImgInCs);
							for (int ind = 1; ind <= noOfQsImgInCs; ind++) {
								String PEImgQstn = cdriver
										.findElementByXPath(
												"((//td[@class='greybluetext10'])[" + start + "]//img)[" + ind + "]")
										.getAttribute("src");
								String CSImgQstn = fdriver.findElementByXPath(
										"//b[text()='QID : " + QID + "   ']//following-sibling::img[" + ind + "]")
										.getAttribute("src");
								String sp1[] = PEImgQstn.split("/");
								PEImgQstn = sp1[sp1.length - 1];

								String sp2[] = CSImgQstn.split("/");
								CSImgQstn = sp2[sp2.length - 1];
								// This code will execute when more than one images in QS
								if (ind > 1) {
									PEImgQstns = PEImgQstns + " & " + PEImgQstn;
									CSImgQstns = CSImgQstns + " & " + CSImgQstn;
								} else {
									// This code will execute when only one images in QS
									PEImgQstns = PEImgQstn;
									CSImgQstns = CSImgQstn;
								}
							}

//						if(isMixture) {
//							CSImgQstns=CSImgQstns+" & "+csQsTxt;
//						}

							// splitting up img URL and getting only attribute name

							System.out.println("QID: " + QID);
							if (PEImgQstns.equalsIgnoreCase(CSImgQstns)) {
								System.out.println("Candidate Question from Audit Page: " + PEImgQstns);
								System.out.println("Candidate Question from Challenge System URL: " + CSImgQstns);
								System.out.println("CANDIDATE QUESTIONS MATCHED");
								WriteInExcelACS(PEImgQstns, CSImgQstns, "PASS");
							} else {
								System.out.println("Candidate Question from Audit Page: " + PEImgQstns);
								System.out.println("Candidate Question from Challenge System URL: " + CSImgQstns);
								System.out.println("CANDIDATE QUESTIONS NOT MATCHED");
								WriteInExcelACS(PEImgQstns, CSImgQstns, "FAIL");
							}
						} catch (Exception e) {
							System.out.println("Exception e..." + e);
							WriteInExcelACS("", "", "FAIL");
						}
					} else {
						WriteInExcelACS("", "", "FAIL");
					}

				} else {

					// Get Question Text in PE and check it is not null
					try {
						String PEQstnText = cdriver
								.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]/b[1]").getText();

						if (PEQstnText.length() < 25) {
							System.err.println("Incomplete Question");
						}

						// to get candidate quesion from Challenge URL
						String candidateQstnCS = "";
						String CSQstns_xp = "//b[text()='QID : " + QID + "   ']/..";
						try {
							WebElement CSQstnEle = fdriver.findElement(By.xpath(CSQstns_xp));
							String csQstnText = CSQstnEle.getText();

							// need to compare PE question & CS question

							// PE(only question text)
							StringBuffer sbr = new StringBuffer(PEQstnText);
							String str = sbr.toString();
							int startTemp = str.indexOf("'");
							int endTemp = 0;
							String finalPEQstnTxt = "";
							try {
								endTemp = str.indexOf("' [Marks");
								finalPEQstnTxt = str.substring(startTemp + 1, endTemp).trim();
							} catch (Exception e) {
								// for some question [marks] will not be there
								endTemp = str.indexOf("Options :");
								finalPEQstnTxt = str.substring(startTemp + 1, endTemp).trim();
							}

							// CS(only question text)
							StringBuffer sbr2 = new StringBuffer(csQstnText);
							String str2 = sbr2.toString();
							int hypInd = str2.indexOf("-");
							String finalCSQstnTxt = str2.substring(hypInd + 1).trim();

							if (finalPEQstnTxt.equalsIgnoreCase(finalCSQstnTxt)) {
								System.out.println("Question ID : " + QID);
								System.out.println("Candidate Question from Audit URL: " + finalPEQstnTxt);
								System.out.println("Candidate Question from Challenge URL: " + finalCSQstnTxt);
								System.out.println("CANDIDATE QUESTIONS MATCHED");

								WriteInExcelACS(finalPEQstnTxt, finalCSQstnTxt, "PASS");
							} else {
								// In PE sometimes text (Question :) will occur in between so replacing it with
								// null to match the length
								finalPEQstnTxt = finalPEQstnTxt.replace("Question : ", "").trim();
								if (finalPEQstnTxt.equalsIgnoreCase(finalCSQstnTxt)) {
									System.out.println("QID: " + QID);
									System.out.println("Candidate Question from Audit Page: " + finalPEQstnTxt);
									System.out
											.println("Candidate Question from Challenge System URL: " + finalCSQstnTxt);
									System.out.println("CANDIDATE QUESTIONS MATCHED");
									WriteInExcelACS(finalPEQstnTxt, finalCSQstnTxt, "PASS");
								} else {
									System.out.println("QID: " + QID);
									System.out.println("Candidate Question from Audit Page: " + finalPEQstnTxt);
									System.out
											.println("Candidate Question from Challenge System URL: " + finalCSQstnTxt);
									System.out.println("CANDIDATE QUESTIONS NOT MATCHED");
									WriteInExcelACS(finalPEQstnTxt, finalCSQstnTxt, "FAIL");
								}

							}
							System.out.printf("\n");
						}

						catch (Exception e) {
							System.out.println("Exception found is :" + e);
							WriteInExcelACS("", "", "FAIL");
						}
					} catch (Exception e) {
						// handle when QID is not there in CS
						System.out.println("Exception found is :" + e);
						System.out.println("QID NOT FOUND");
						WriteInExcelACS("", "", "QID NOT FOUND");
					}
				}
			} catch (Exception e) {
				System.out.println("Exception found is :" + e);
				WriteInExcelACS("", "", "FAIL");
			}

		} // for loop

	}// method

	public void compareACSOptions() throws FileNotFoundException, IOException {

		for (start = 1; start <= AvailableQuestionsPE.size(); start++) {
//		for (start = 33; start <= 33; start++) {

			try {

				boolean imageCheck = true;
				// Getting full text including questions, options, answers in PE
				String FullText = cdriver.findElementByXPath("(//td[@class='greybluetext10'])[" + start + "]")
						.getText();

				// To get QID in PE
				boolean loop = true;
				String QID = "";
				int i = 0;

				int tempStart = 0;
				int tempEnd = 0;
				do {

					if (FullText.charAt(i) == '[') {

						tempStart = i;
						loop = false;

					}
					i++;
				} while (loop == true);

				loop = true;
				i = 0;
				do {
					if (FullText.charAt(i) == ']') {
						tempEnd = i;
						loop = false;

					}
					i++;
				} while (loop == true);

				String tempQID = FullText.substring(tempStart + 2, tempEnd - 1);

				QID = tempQID;

				// to check the option is image based
				try {
					fdriver.findElementByXPath("//b[text()='QID : " + QID + "   ']/following::tr[1]//img");
				} catch (NoSuchElementException e) {
					imageCheck = false;
				}
				String[] opt = { "A", "B", "C", "D" };
				if (imageCheck == true) {
					System.out.println("Coming inside..." + QID);
					boolean val = fdriver.findElementByXPath("//b[text()='QID : " + QID + "   ']/following::tr[1]//img")
							.isDisplayed();
					System.out.println("IsDISPLAYED>>>" + val);
					if (val == true) {

						System.out.println("Option contains IMAGE... " + QID);

						List<WebElement> imgCSEle = fdriver
								.findElementsByXPath("//b[text()='QID : " + QID + "   ']/following::tr[1]//img");

						// getting the no of images in CS
						int noOfImgCS = imgCSEle.size();

						String PEOptionTxt = "", CSOptionTxt = "";
						int count = 0;

						if (noOfImgCS >= 1)
						// Option has both image and text and if option has only image
						{
							try {
								// getting the option text in PE
								String splitOpt_Ans = FullText.split("Options :")[1];
								String splitOptOnly = "";
								boolean isOmitted = false;

								// handling for the omitted questions or there is no candidate answer
								if (!splitOpt_Ans.contains("Candidate Answer :")
										|| splitOpt_Ans.contains("Omitted Question")) {
									isOmitted = true;
								}

								if (isOmitted == true) {
									splitOptOnly = splitOpt_Ans.replace("Omitted Question", "");
								} else {
									splitOptOnly = splitOpt_Ans.split("Candidate Answer :")[0];
								}

								String PEOptText = splitOptOnly.trim();

								// to get candidate option from Challenge URL
								String CSOptn_xp = "//b[text()='QID : " + QID + "   ']/following::tr[1]";

								String csOptText = fdriver.findElement(By.xpath(CSOptn_xp)).getText().substring(8)
										.trim();

								// for PE options

								int indAPE = PEOptText.indexOf("(A)");
								int indBPE = PEOptText.indexOf("\n(B)");
								int indCPE = PEOptText.indexOf("\n(C)");
								int indDPE = PEOptText.indexOf("\n(D)");
								int indEPE = PEOptText.indexOf("\n(E)");

								String firstOptPE = PEOptText.substring(indAPE, indBPE).replaceFirst("\\(A\\)", "")
										.trim();
								String secondOptPE = PEOptText.substring(indBPE, indCPE).replaceFirst("\\(B\\)", "")
										.trim();
								String thirdOptPE = PEOptText.substring(indCPE, indDPE).replaceFirst("\\(C\\)", "")
										.trim();
								String fourOptPE = PEOptText.substring(indDPE).replaceFirst("\\(D\\)", "").trim();
								String fifthOptPE = "";

								// for CS options

								int indACS = csOptText.indexOf("1)");
								int indBCS = csOptText.indexOf("\n2)");
								int indCCS = csOptText.indexOf("\n3)");
								int indDCS = csOptText.indexOf("\n4)");
								int indECS = PEOptText.indexOf("\n5)");

								String firstOptCS = csOptText.substring(indACS, indBCS).replaceFirst("1\\)", "").trim();
								String secondOptCS = csOptText.substring(indBCS, indCCS).replaceFirst("2\\)", "")
										.trim();
								String thirdOptCS = csOptText.substring(indCCS, indDCS).replaceFirst("3\\)", "").trim();
								String fourOptCS = csOptText.substring(indDCS).replaceFirst("4\\)", "").trim();
								String fifthOptCS = "";

//						System.out.println(CSOptions);

								System.out.println("QID: " + QID);

								int imgLocator = 0;

								List<String> optImgList = new ArrayList<String>();

								// handles when 5 option is there
								if (indEPE != -1 && indECS != -1) {
									int[] colCountExcel = { 11, 14, 17, 20, 23 };

									fifthOptPE = PEOptText.substring(indEPE).replaceFirst("\\(E\\)", "").trim();
									fifthOptCS = csOptText.substring(indECS).replaceFirst("5\\)", "").trim();

									boolean opt1 = false, opt2 = false, opt3 = false, opt4 = false, opt5 = false;
									String[] PEOptions = { firstOptPE, secondOptPE, thirdOptPE, fourOptPE, fifthOptPE };
									String[] CSOptions = { firstOptCS, secondOptCS, thirdOptCS, fourOptCS, fifthOptCS };

									for (int ind = 1; ind <= 5; ind++) {
										// if option has image
										if (CSOptions[ind - 1].equals("") && PEOptions[ind - 1].equals("")) {

											imgLocator++;

											try {
												String imgSrcCS = fdriver
														.findElementByXPath("//b[text()='QID : " + QID
																+ "   ']/following::tr[1]//img[" + imgLocator + "]")
														.getAttribute("src");

												String imgSrcPE = cdriver

														.findElementByXPath("(//td[@class='greybluetext10'])[" + start
																+ "]//b[text()='Options : ']/following-sibling::img["
																+ imgLocator + "]")
														.getAttribute("src");
												// Handling for double Images in same option
												if (optImgList.contains(imgSrcPE)) {
													imgLocator++;
													imgSrcCS = fdriver
															.findElementByXPath("//b[text()='QID : " + QID
																	+ "   ']/following::tr[1]//img[" + imgLocator + "]")
															.getAttribute("src");
													imgSrcPE = cdriver

															.findElementByXPath("(//td[@class='greybluetext10'])["
																	+ start
																	+ "]//b[text()='Options : ']/following-sibling::img["
																	+ imgLocator + "]")
															.getAttribute("src");
													optImgList.add(imgSrcPE);
												} else {
													optImgList.add(imgSrcPE);
												}
												// getting image option text in PE
												String sp1[] = imgSrcPE.split("/");
												PEOptionTxt = sp1[sp1.length - 1];

												// getting image option text in CS
												String sp2[] = imgSrcCS.split("/");
												CSOptionTxt = sp2[sp2.length - 1];

												if (CSOptionTxt.equalsIgnoreCase(PEOptionTxt)) {
													count++;
													System.out.println("Option " + ind + " from PE :" + PEOptionTxt
															+ " ------ " + "Option " + opt[ind - 1] + " from CS :"
															+ CSOptionTxt);
													System.out.println("OPTION " + ind + " MATCHED");
													WriteInExcelACSOption(PEOptionTxt, CSOptionTxt, "PASS",
															acsOptRowCount + 1, colCountExcel[ind - 1]);
												} else {
													System.out.println("Option " + ind + " from PE :" + PEOptionTxt
															+ " ------ " + "Option " + opt[ind - 1] + " from CS :"
															+ CSOptionTxt);
													System.out.println("OPTION " + ind + " NOT MATCHED");
													WriteInExcelACSOption(PEOptionTxt, CSOptionTxt, "FAIL",
															acsOptRowCount + 1, colCountExcel[ind - 1]);
												}

											} catch (Exception e) {
												System.out.println("Exception e " + e);
												WriteInExcelACSOption("", "", "FAIL", acsOptRowCount + 1,
														colCountExcel[ind - 1]);
											}

										} else {
											// if option is text
											try {
												if (CSOptions[ind - 1].equalsIgnoreCase(PEOptions[ind - 1])) {
													count++;
													System.out.println("Option " + ind + " from PE :"
															+ PEOptions[ind - 1] + " ------ " + "Option " + opt[ind - 1]
															+ " from CS :" + CSOptions[ind - 1]);
													System.out.println("OPTION " + ind + " MATCHED");
													WriteInExcelACSOption(PEOptions[ind - 1], CSOptions[ind - 1],
															"PASS", acsOptRowCount + 1, colCountExcel[ind - 1]);
												} else {
													System.out.println("Option " + ind + " from PE :"
															+ PEOptions[ind - 1] + " ------ " + "Option " + opt[ind - 1]
															+ " from CS :" + CSOptions[ind - 1]);
													System.out.println("OPTION " + ind + " NOT MATCHED");
													WriteInExcelACSOption(PEOptions[ind - 1], CSOptions[ind - 1],
															"FAIL", acsOptRowCount + 1, colCountExcel[ind - 1]);
												}
											} catch (Exception e) {
												System.out.println("Exception is " + e);
												WriteInExcelACSOption("", "", "FAIL", acsOptRowCount + 1,
														colCountExcel[ind - 1]);
											}
										}
									}
									// overall option report

									if (count == 5) {
										System.out.println("All Options from PE is Matched with All Options from CS");
										WriteInExcelACSOptionFinalStatus("PASS");

									} else {
										System.out
												.println("All Options from PE is Not Matched with All Options from CS");
										WriteInExcelACSOptionFinalStatus("FAIL");
									}

								} else {
									int[] colCountExcel = { 11, 14, 17, 20 };
									// handles when 4 option is there

									String[] PEOptions = { firstOptPE, secondOptPE, thirdOptPE, fourOptPE };
									String[] CSOptions = { firstOptCS, secondOptCS, thirdOptCS, fourOptCS };

									for (int ind = 1; ind <= 4; ind++) {
										// if option has image
										if (CSOptions[ind - 1].equals("") && PEOptions[ind - 1].equals("")) {

											imgLocator++;

											try {
												String imgSrcCS = fdriver
														.findElementByXPath("//b[text()='QID : " + QID
																+ "   ']/following::tr[1]//img[" + imgLocator + "]")
														.getAttribute("src");

												String imgSrcPE = cdriver

														.findElementByXPath("(//td[@class='greybluetext10'])[" + start
																+ "]//b[text()='Options : ']/following-sibling::img["
																+ imgLocator + "]")
														.getAttribute("src");

												// Handling for double Images in same option

												if (optImgList.contains(imgSrcPE)) {

													imgLocator++;

													imgSrcCS = fdriver
															.findElementByXPath("//b[text()='QID : " + QID
																	+ "   ']/following::tr[1]//img[" + imgLocator + "]")
															.getAttribute("src");
													imgSrcPE = cdriver

															.findElementByXPath("(//td[@class='greybluetext10'])["
																	+ start
																	+ "]//b[text()='Options : ']/following-sibling::img["
																	+ imgLocator + "]")
															.getAttribute("src");

//												
													optImgList.add(imgSrcPE);
												} else {
													optImgList.add(imgSrcPE);
												}

												// getting image option text in PE
												String sp1[] = imgSrcPE.split("/");
												PEOptionTxt = sp1[sp1.length - 1];

												// getting image option text in CS
												String sp2[] = imgSrcCS.split("/");
												CSOptionTxt = sp2[sp2.length - 1];

												if (CSOptionTxt.equalsIgnoreCase(PEOptionTxt)) {
													count++;
													System.out.println("Option " + ind + " from PE :" + PEOptionTxt
															+ " ------ " + "Option " + opt[ind - 1] + " from CS :"
															+ CSOptionTxt);
													System.out.println("OPTION " + ind + " MATCHED");
													WriteInExcelACSOption(PEOptionTxt, CSOptionTxt, "PASS",
															acsOptRowCount + 1, colCountExcel[ind - 1]);
												} else {
													System.out.println("Option " + ind + " from PE :" + PEOptionTxt
															+ " ------ " + "Option " + opt[ind - 1] + " from CS :"
															+ CSOptionTxt);
													System.out.println("OPTION " + ind + " NOT MATCHED");
													WriteInExcelACSOption(PEOptionTxt, CSOptionTxt, "FAIL",
															acsOptRowCount + 1, colCountExcel[ind - 1]);
												}

											} catch (Exception e) {
												System.out.println("Exception e " + e);
												WriteInExcelACSOption("", "", "FAIL", acsOptRowCount + 1,
														colCountExcel[ind - 1]);
											}

										} else {
											// if option is text
											try {
												if (CSOptions[ind - 1].equalsIgnoreCase(PEOptions[ind - 1])) {
													count++;
													System.out.println("Option " + ind + " from PE :"
															+ PEOptions[ind - 1] + " ------ " + "Option " + opt[ind - 1]
															+ " from CS :" + CSOptions[ind - 1]);
													System.out.println("OPTION " + ind + " MATCHED");
													WriteInExcelACSOption(PEOptions[ind - 1], CSOptions[ind - 1],
															"PASS", acsOptRowCount + 1, colCountExcel[ind - 1]);
												} else {
													System.out.println("Option " + ind + " from PE :"
															+ PEOptions[ind - 1] + " ------ " + "Option " + opt[ind - 1]
															+ " from CS :" + CSOptions[ind - 1]);
													System.out.println("OPTION " + ind + " NOT MATCHED");
													WriteInExcelACSOption(PEOptions[ind - 1], CSOptions[ind - 1],
															"FAIL", acsOptRowCount + 1, colCountExcel[ind - 1]);
												}
											} catch (Exception e) {
												System.out.println("Exception is " + e);
												WriteInExcelACSOption("", "", "FAIL", acsOptRowCount + 1,
														colCountExcel[ind - 1]);
											}
										}
									}
									// overall option report

									if (count == 4) {
										System.out.println("All Options from PE is Matched with All Options from CS");
										WriteInExcelACSOptionFinalStatus("PASS");

									} else {
										System.out
												.println("All Options from PE is Not Matched with All Options from CS");
										WriteInExcelACSOptionFinalStatus("FAIL");
									}

								}

							} catch (Exception e) {
								System.out.println("Exception is " + e);
							}

						}

						acsOptRowCount++;
						System.out.printf("\n");
					} else {
						WriteInExcelACSOptionFinalStatus("FAIL");
						acsOptRowCount++;
						System.out.printf("\n");
					}

				} else {

					// Getting Full text including questions, options, answers in PE

					try {

						String splitOpt_Ans = FullText.split("Options :")[1];
						String splitOptOnly = "";
						boolean isOmitted = false;

						// handling for the omitted questions or there is no candidate answer
						if (!splitOpt_Ans.contains("Candidate Answer :") || splitOpt_Ans.contains("Omitted Question")) {
							isOmitted = true;
						}

						if (isOmitted == true) {
							splitOptOnly = splitOpt_Ans.replace("Omitted Question", "");
						} else {
							splitOptOnly = splitOpt_Ans.split("Candidate Answer :")[0];
						}

						String PEOptText = splitOptOnly.trim();

						// to get candidate option from Challenge URL
						String CSOptn_xp = "//b[text()='QID : " + QID + "   ']/following::tr[1]";

						String csOptText = fdriver.findElement(By.xpath(CSOptn_xp)).getText();

						// handles when table is there in Questions
						int inc = 2;
						while (!csOptText.contains("Options:")) {
							CSOptn_xp = "//b[text()='QID : " + QID + "   ']/following::tr[" + inc + "]";
							csOptText = fdriver.findElement(By.xpath(CSOptn_xp)).getText();
							inc++;
						}

						System.out.println("QID : " + QID);

						// for PE options

						int indAPE = PEOptText.indexOf("(A)");
						int indBPE = PEOptText.indexOf("\n(B)");
						int indCPE = PEOptText.indexOf("\n(C)");
						int indDPE = PEOptText.indexOf("\n(D)");
						int indEPE = PEOptText.indexOf("\n(E)");

						String firstOptPE = PEOptText.substring(indAPE, indBPE).replaceFirst("\\(A\\)", "").trim();
						String secondOptPE = PEOptText.substring(indBPE, indCPE).replaceFirst("\\(B\\)", "").trim();
						String thirdOptPE = PEOptText.substring(indCPE, indDPE).replaceFirst("\\(C\\)", "").trim();
						String fourOptPE = PEOptText.substring(indDPE).replaceFirst("\\(D\\)", "").trim();
						String fifthOptPE = "";

//				System.out.println(firstOptPE + "..." + secondOptPE + "..." + thirdOptPE + "....." + fourOptPE);

						// for CS options

						int indACS = csOptText.indexOf("1)");
						int indBCS = csOptText.indexOf("\n2)");
						int indCCS = csOptText.indexOf("\n3)");
						int indDCS = csOptText.indexOf("\n4)");
						int indECS = csOptText.indexOf("\n5)");

						System.out.println(indEPE + "..." + indECS);

//				System.out.println(csOptText);
//				System.out.println(indACS+"...."+indBCS+"....."+indCCS+"...."+indDCS);
						String firstOptCS = csOptText.substring(indACS, indBCS).replaceFirst("1\\)", "").trim();
						String secondOptCS = csOptText.substring(indBCS, indCCS).replaceFirst("2\\)", "").trim();
						String thirdOptCS = csOptText.substring(indCCS, indDCS).replaceFirst("3\\)", "").trim();
						String fourOptCS = csOptText.substring(indDCS).replaceFirst("4\\)", "").trim();
						String fifthOptCS = "";

//						System.out.println(firstOptCS + "..." + secondOptCS + "..." + thirdOptCS + "....." + fourOptCS);

						if (indEPE != -1 && indECS != -1) {
							fifthOptPE = PEOptText.substring(indEPE).replaceFirst("\\(E\\)", "").trim();
							fifthOptCS = csOptText.substring(indECS).replaceFirst("5\\)", "").trim();

							boolean opt1 = false, opt2 = false, opt3 = false, opt4 = false, opt5 = false;

							for (int ind = 1; ind <= 5; ind++) {
								if (ind == 1) {
									if (firstOptPE.equalsIgnoreCase(firstOptCS)) {
										opt1 = true;
										System.out.println("Option 1 from PE :" + firstOptPE + " ------ "
												+ "Option A from CS :" + firstOptCS);
										System.out.println("OPTION 1 MATCHED");
										WriteInExcelACSOption(firstOptPE, firstOptCS, "PASS", acsOptRowCount + 1, 11);
									} else {
										System.out.println("Option A from PE :" + firstOptPE + " ------ "
												+ "Option 1 from CS :" + firstOptCS);
										System.out.println("OPTION 1 NOT MATCHED");
										WriteInExcelACSOption(firstOptPE, firstOptCS, "FAIL", acsOptRowCount + 1, 11);
									}
								} else if (ind == 2) {
									if (secondOptPE.equalsIgnoreCase(secondOptCS)) {
										opt2 = true;
										System.out.println("Option B from PE :" + secondOptPE + " ------ "
												+ "Option 2 from CS :" + secondOptCS);
										System.out.println("OPTION 2 MATCHED");
										WriteInExcelACSOption(secondOptPE, secondOptCS, "PASS", acsOptRowCount + 1, 14);
									} else {
										System.out.println("Option B from PE :" + secondOptPE + " ------ "
												+ "Option 2 from CS :" + secondOptCS);
										System.out.println("OPTION 2 NOT MATCHED");
										WriteInExcelACSOption(secondOptPE, secondOptCS, "FAIL", acsOptRowCount + 1, 14);
									}
								} else if (ind == 3) {
									if (thirdOptPE.equalsIgnoreCase(thirdOptCS)) {
										opt3 = true;
										System.out.println("Option C from PE :" + thirdOptPE + " ------ "
												+ "Option 3 from CS :" + thirdOptCS);
										System.out.println("OPTION 3 MATCHED");
										WriteInExcelACSOption(thirdOptPE, thirdOptCS, "PASS", acsOptRowCount + 1, 17);
									} else {
										System.out.println("Option C from PE :" + thirdOptPE + " ------ "
												+ "Option 3 from CS :" + thirdOptCS);
										System.out.println("OPTION 3 NOT MATCHED");
										WriteInExcelACSOption(thirdOptPE, thirdOptCS, "FAIL", acsOptRowCount + 1, 17);
									}
								} else if (ind == 4) {
									if (fourOptPE.equals(fourOptCS)) {
										opt4 = true;
										System.out.println("Option D from PE :" + fourOptPE + " ------ "
												+ "Option 4 from CS :" + fourOptCS);
										System.out.println("OPTION 4 MATCHED");
										WriteInExcelACSOption(fourOptPE, fourOptCS, "PASS", acsOptRowCount + 1, 20);
									} else {
										System.out.println("Option D from PE :" + fourOptPE + " ------ "
												+ "Option 4 from CS :" + fourOptCS);
										System.out.println("OPTION 4 NOT MATCHED");
										WriteInExcelACSOption(fourOptPE, fourOptCS, "FAIL", acsOptRowCount + 1, 20);
									}
								} else {
									if (fifthOptPE.equals(fifthOptCS)) {
										opt5 = true;
										System.out.println("Option E from PE :" + fifthOptPE + " ------ "
												+ "Option 5 from CS :" + fifthOptCS);
										System.out.println("OPTION 5 MATCHED");
										WriteInExcelACSOption(fifthOptPE, fifthOptCS, "PASS", acsOptRowCount + 1, 23);
									} else {
										System.out.println("Option E from PE :" + fifthOptPE + " ------ "
												+ "Option 5 from CS :" + fifthOptCS);
										System.out.println("OPTION 5 MATCHED");
										WriteInExcelACSOption(fifthOptPE, fifthOptCS, "FAIL", acsOptRowCount + 1, 23);
									}
								}
							}

							// overall option report

							if (opt1 && opt2 && opt3 && opt4 && opt5) {
								System.out.println("All Options from PE is Matched with All Options from CS");
								WriteInExcelACSOptionFinalStatus("PASS");

							} else {
								System.out.println("All Options from PE is Not Matched with All Options from CS");
								WriteInExcelACSOptionFinalStatus("FAIL");
							}
						} else {

							boolean opt1 = false, opt2 = false, opt3 = false, opt4 = false;

							for (int ind = 1; ind <= 4; ind++) {
								if (ind == 1) {
									if (firstOptPE.equalsIgnoreCase(firstOptCS)) {
										opt1 = true;
										System.out.println("Option 1 from PE :" + firstOptPE + " ------ "
												+ "Option A from CS :" + firstOptCS);
										System.out.println("OPTION 1 MATCHED");
										WriteInExcelACSOption(firstOptPE, firstOptCS, "PASS", acsOptRowCount + 1, 11);
									} else {
										System.out.println("Option A from PE :" + firstOptPE + " ------ "
												+ "Option 1 from CS :" + firstOptCS);
										System.out.println("OPTION 1 NOT MATCHED");
										WriteInExcelACSOption(firstOptPE, firstOptCS, "FAIL", acsOptRowCount + 1, 11);
									}
								} else if (ind == 2) {
									if (secondOptPE.equalsIgnoreCase(secondOptCS)) {
										opt2 = true;
										System.out.println("Option B from PE :" + secondOptPE + " ------ "
												+ "Option 2 from CS :" + secondOptCS);
										System.out.println("OPTION 2 MATCHED");
										WriteInExcelACSOption(secondOptPE, secondOptCS, "PASS", acsOptRowCount + 1, 14);
									} else {
										System.out.println("Option B from PE :" + secondOptPE + " ------ "
												+ "Option 2 from CS :" + secondOptCS);
										System.out.println("OPTION 2 NOT MATCHED");
										WriteInExcelACSOption(secondOptPE, secondOptCS, "FAIL", acsOptRowCount + 1, 14);
									}
								} else if (ind == 3) {
									if (thirdOptPE.equalsIgnoreCase(thirdOptCS)) {
										opt3 = true;
										System.out.println("Option C from PE :" + thirdOptPE + " ------ "
												+ "Option 3 from CS :" + thirdOptCS);
										System.out.println("OPTION 3 MATCHED");
										WriteInExcelACSOption(thirdOptPE, thirdOptCS, "PASS", acsOptRowCount + 1, 17);
									} else {
										System.out.println("Option C from PE :" + thirdOptPE + " ------ "
												+ "Option 3 from CS :" + thirdOptCS);
										System.out.println("OPTION 3 NOT MATCHED");
										WriteInExcelACSOption(thirdOptPE, thirdOptCS, "FAIL", acsOptRowCount + 1, 17);
									}
								} else {
									if (fourOptPE.equals(fourOptCS)) {
										opt4 = true;
										System.out.println("Option D from PE :" + fourOptPE + " ------ "
												+ "Option 4 from CS :" + fourOptCS);
										System.out.println("OPTION 4 MATCHED");
										WriteInExcelACSOption(fourOptPE, fourOptCS, "PASS", acsOptRowCount + 1, 20);
									} else {
										System.out.println("Option D from PE :" + fourOptPE + " ------ "
												+ "Option 4 from CS :" + fourOptCS);
										System.out.println("OPTION 4 NOT MATCHED");
										WriteInExcelACSOption(fourOptPE, fourOptCS, "FAIL", acsOptRowCount + 1, 20);
									}
								}
							}

							// overall option report

							if (opt1 && opt2 && opt3 && opt4) {
								System.out.println("All Options from PE is Matched with All Options from CS");
								WriteInExcelACSOptionFinalStatus("PASS");

							} else {
								System.out.println("All Options from PE is Not Matched with All Options from CS");
								WriteInExcelACSOptionFinalStatus("FAIL");
							}
						}

						acsOptRowCount++;

						System.out.printf("\n");

					} catch (Exception e) {
						System.out.println("Exception found is " + e);
						// handles when QID is not there
						System.out.println("QID NOT FOUND");
						WriteInExcelACSOptionFinalStatus("FAIL");
						acsOptRowCount++;
					}
				}
			} catch (Exception e) {
				System.out.println("Exception found is " + e);
				WriteInExcelACSOptionFinalStatus("FAIL");
				acsOptRowCount++;
			}
		} // for loop

		cdriver.quit();

		fdriver.quit();

	} // method

	public void WriteInExcelACS(String text5, String text6, String text7) throws FileNotFoundException, IOException {

		Object[][] bookData = { { text5, text6, text7 }, };

		for (Object[] aBook : bookData) {
			Row row = sheet.getRow(++rowCountACS);

			int columnCount = 6;

			for (Object field : aBook) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}

		}
		try (FileOutputStream outputStream = new FileOutputStream(
				"C:\\Results\\AdminWithChallengeSystem\\TestResult_ACS.xlsx")) {
			workbook.write(outputStream);
		}

	}

	int rowCountACSOpt = 0;

	public void WriteInExcelACSOption(String PEOption, String CSOption, String status, int RowCount, int ColCount)
			throws FileNotFoundException, IOException {
		Object[][] bookData = { { PEOption, CSOption, status } };

		for (Object[] aBook : bookData) {
			Row row = sheet.getRow(RowCount);

			int columnCount1 = ColCount;

			for (Object field : aBook) {

				Cell cell = row.createCell(++columnCount1);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}

		}
		try (FileOutputStream outputStream = new FileOutputStream(
				"C:\\Results\\AdminWithChallengeSystem\\TestResult_ACS.xlsx")) {
			workbook.write(outputStream);
		}

	}

	int rowCountACSFinalOpt = 0;

	public void WriteInExcelACSOptionFinalStatus(String status) throws FileNotFoundException, IOException {

		Object[][] bookData = { { status } };

		for (Object[] aBook : bookData) {
			Row row = sheet.getRow(++rowCountACSFinalOpt);

			int columnCount = 26;

			for (Object field : aBook) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}

		}
		try (FileOutputStream outputStream = new FileOutputStream(
				"C:\\Results\\AdminWithChallengeSystem\\TestResult_ACS.xlsx")) {
			workbook.write(outputStream);
		}

	}

	public void WriteInExcelCSM(String RollNo, String text1, String text2, String text3, String text4, String text5)
			throws FileNotFoundException, IOException {

		Object[][] bookData = { { RollNo, text1, text2, text3, text4, text5 }, };

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
		try (FileOutputStream outputStream = new FileOutputStream(
				"C:\\Results\\ChallengeSystemWithMaster\\TestResult_CSM.xlsx")) {
			workbook.write(outputStream);
		}

	}

	int rowCountCSMQstn = 0;

	public void WriteInExcelCSMQstns(String text7, String text8, String text9)
			throws FileNotFoundException, IOException {

		Object[][] bookData = { { text7, text8, text9 } };

		for (Object[] aBook : bookData) {
			Row row = sheet.getRow(++rowCountCSMQstn);

			int columnCount = 7;

			for (Object field : aBook) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}

		}
		try (FileOutputStream outputStream = new FileOutputStream(
				"C:\\Results\\ChallengeSystemWithMaster\\TestResult_CSM.xlsx")) {
			workbook.write(outputStream);
		}

	}

	int rowCountCSMOptn = 0;

	public void WriteInExcelCSMOptns(String CSOption, String MQPOption, String status, int RowCount, int ColCount)
			throws FileNotFoundException, IOException {

		Object[][] bookData = { { CSOption, MQPOption, status } };

		for (Object[] aBook : bookData) {
			Row row = sheet.getRow(RowCount);

			int columnCount = ColCount;

			for (Object field : aBook) {

				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}

		}
		try (FileOutputStream outputStream = new FileOutputStream(
				"C:\\Results\\ChallengeSystemWithMaster\\TestResult_CSM.xlsx")) {
			workbook.write(outputStream);
		}

	}

	int rowCountCSMFinalOpt1 = 0;

	public void WriteInExcelCSMOptionFinalStatus(String status) throws FileNotFoundException, IOException {

		Object[][] bookData = { { status } };

		for (Object[] aBook : bookData) {
			Row row = sheet.getRow(++rowCountCSMFinalOpt1);

			int columnCount = 26;

			for (Object field : aBook) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}

		}
		try (FileOutputStream outputStream = new FileOutputStream(
				"C:\\Results\\ChallengeSystemWithMaster\\TestResult_CSM.xlsx")) {
			workbook.write(outputStream);
		}

	}
}
