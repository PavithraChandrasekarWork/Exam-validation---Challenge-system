package iTestcases;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Test {


	public static void main(String arg[]) throws IOException, InterruptedException, AWTException, ParseException{



/*		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		driver.get("https://www.proxysite.com/");
		driver.findElement(By.xpath("//input[@placeholder='Enter Url']")).sendKeys("Tamilrockers.net");
		driver.findElement(By.xpath("//input[@placeholder='Enter Url']/following::button")).click();
		driver.findElement(By.linkText("Forums")).click();
		driver.findElement(By.xpath("//a[contains(@title, 'Tamil Dubbed Movies')]")).click();
		List<WebElement> Avatar = null;
		int page = 1;
		do 
		{
		
		Avatar = driver.findElements(By.xpath("//a[contains(@title, 'Avatar')]"));
		
		if(Avatar.size()==0)
		{
			driver.findElement(By.xpath("//a[contains(@title, 'Next page')]")).click();
			System.out.println("Not found in Page "+page);
			page++;
		}
		else
		{
			System.out.println("Found in Page "+ page);
		}
		}
		while (Avatar.size()==0);
*/
		
		
		
		
		
		String day = "04";
		
		if(day.charAt(0)=='0')
		{
			day = day.substring(1);
		}
		
		System.out.println(day);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
