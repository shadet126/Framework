package customLibrary;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

import com.google.common.base.Function;
import com.sun.jmx.snmp.Timestamp;

/***
 * This class will provide basic selenium functionalities using helper methods.
 * These methods should work in any environment and different browsers
 * 
 * @author Xadat
 *
 */
public class UtilityLibrary {
	private WebDriver driver;
	public boolean isDemoMode = false;

	/***
	 * This method used for any checkbox web-elements. if user wants to check
	 * the box, pass isCheck=true else pass isCheck=false.
	 * 
	 * @param by
	 * @param isCheck
	 */

	public void selectCheckBox(By by, boolean isCheck) {
		WebElement checkBoxElem = driver.findElement(by);
		highlightElement(checkBoxElem);
		boolean checkboxState = checkBoxElem.isSelected();
		if (isCheck == true)// user wants to check box
		{
			if (checkboxState == true) // by default checked
			{
				/* do nothing */ } else // by default un-checked
			{
				checkBoxElem.click();
			}
		} else // user wants to un-check the box
		{
			if (checkboxState == true) {
				checkBoxElem.click();
			} else {
				/* do nothing */}
		}

	}

	public WebDriver startBrowser(String browserType) {
		if (browserType.contains("chrome")) {
			driver = startChromeBrowser();
		} else if (browserType.contains("firefox")) {
			driver = startFireFoxBrowser();
		} else if (browserType.contains("ie")) {
			driver = startIEBrowser();
		} else {
			System.out.println("you chose: '" + browserType + "'");
			System.out.println("we currently support ' chrome firefox & ie ' browser.");
			System.out.println("please contact our automation team.");
		}

		return driver;
	}

	/***
	 * This method starts chrome browser
	 * 
	 * @return driver
	 */
	private WebDriver startChromeBrowser() {
		try {
			System.setProperty("webdriver.chrome.driver", "D://Seleniumjars//Driver//chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			options.addArguments("start-maximized");
			options.addArguments("--enable-automation");
			options.addArguments("test-type=browser");
			options.addArguments("disable-infobars");
			driver = new ChromeDriver(options);
			setDefaultWaitConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	/***
	 * This method starts Firefox browser
	 * 
	 * @return driver
	 */
	private WebDriver startFireFoxBrowser() {
		try {
			System.setProperty("webdriver.gecko.driver", "D://Seleniumjars//Driver//geckodriver.exe");
			driver = new FirefoxDriver();
			setDefaultWaitConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return driver;
	}

	/***
	 * This method starts IE browser
	 * 
	 * @return driver
	 */

	private WebDriver startIEBrowser() {
		try {
			System.setProperty("webdriver.ie.driver", "D://Seleniumjars//Driver//IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			setDefaultWaitConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	private void setDefaultWaitConfiguration() {
		try {

			//driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * This method enters text to the text field web-element
	 * 
	 * @param by
	 * @param inputString
	 */
	public void enterTextField(By by, String inputString) {
		try {
			WebElement textFieldElem = driver.findElement(by);
			highlightElement(textFieldElem);
			textFieldElem.clear();
			textFieldElem.sendKeys(inputString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * This method selects dropdown web-elements
	 * 
	 * @param by
	 * @param optionValue
	 */

	public void selectDropDown(By by, String optionValue) {
		try {
			WebElement dropdownElem = driver.findElement(by);
			highlightElement(dropdownElem);
			Select dropdownSelect = new Select(dropdownElem);
			dropdownSelect.selectByVisibleText(optionValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickButton(By by) {
		try {
			WebElement btnElem = driver.findElement(by);
			highlightElement(btnElem);
			btnElem.click();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void clickButton(WebElement element) {
		try {

			highlightElement(element);
			element.click();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/***
	 * This is customWait method using Thread.sleep
	 * 
	 * @param inSeconds
	 */
	public void customWait(int inSeconds) {
		try {
			Thread.sleep(inSeconds * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * This is a dynamic wait. User can use it when switch browsers. When
	 * webpage synchronization happens
	 * 
	 * @param by
	 * @return
	 */
	public WebElement fluentWait(final By by) {
		WebElement targetElem = null;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(3, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			targetElem = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {

					return driver.findElement(by);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		highlightElement(targetElem);
		return targetElem;
	}

	/***
	  * This method captures the screenshot of the browser
	  * @param screenshotFileName
	  * @param filepathToSave
	  * @return full path of the image name and location
	  */
		public String captureScreenShot(String screenshotFileName,String filepathToSave){
			String finalImage = null;
			try{
			String tempName = screenshotFileName + filepathToSave + getCurrentTime() + ".png";
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(tempName));
			finalImage = tempName;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return finalImage;
		}

	/***
	 * This method calculates current time of
	 * 
	 * @return returns value of the current time
	 */
	public String getCurrentTime() {
		String finalTimeStamp = null;
		try {
			Date date = new Date();
			String tempTime = (new Timestamp(date.getTime()).toString());
			tempTime.replaceAll(":", "_").replaceAll("-", "_").replaceAll(".", "_");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalTimeStamp;

	}

	public void highlightElement(By by) {
		if(isDemoMode == true){
		try {
			WebElement temp = driver.findElement(by);
			WrapsDriver wrappedElement = (WrapsDriver) temp;
			JavascriptExecutor js = (JavascriptExecutor) wrappedElement.getWrappedDriver();
			customWait(1);
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", temp,
					"color: red; boarder:2px solid yellow;");
			customWait(1);
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", temp, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

	}

	public void highlightElement(WebElement element) {
		if(isDemoMode == true){
		try {
			for (int i = 0; i < 4; i++) {

				WrapsDriver wrappedElement = (WrapsDriver) element;
				JavascriptExecutor js = (JavascriptExecutor) wrappedElement.getWrappedDriver();
				customWait(1);
				js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
						"color: red; boarder:2px solid yellow;");
				customWait(1);
				js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

	}
	/***
	 * This method switch to iframe content
	 * @param by
	 */
	public void switchToIframe(By by){
		try{
		WebElement iframe = driver.findElement(by);
		driver.switchTo().frame(iframe);
	}catch (Exception e) {
		e.printStackTrace();
	}

}
	/***
	 * This method switch to default content
	 */
   public void switchToDefault(){
	   try{
	   driver.switchTo().defaultContent();
   }catch (Exception e) {
		e.printStackTrace();
	}

}
   /***
    * This method move mouse to the given WebElement
    * @param toElement
    */
   public void moveMouseToElement(WebElement toElement){
	   try{
	   Actions action = new Actions(driver);
	   action.moveToElement(toElement).build().perform();
   }catch (Exception e) {
		e.printStackTrace();
	}

}
   /***
    * this method move mouse pointer to first element then to second element
    * @param firstElem
    * @param secondElem
    */
   public void moveMouseToElement(WebElement firstElem, WebElement secondElem){
	   try{
	   Actions action = new Actions(driver);
	   action.moveToElement(firstElem).build().perform();
	   customWait(2);
	   action.moveToElement(secondElem).build().perform();
	   customWait(2);
   }catch (Exception e) {
		e.printStackTrace();
	}

}
   /***
    * This method clicks located link
    * @param by
    */
   public void clickLink(By by){
	   try{
	   WebElement link = driver.findElement(by);
	   highlightElement(link);
	   link.click();
   }catch (Exception e) {
		e.printStackTrace();
	}

}
   public String verifyPageTitle(){
	   String pageTitle = null;
	   try{
	    pageTitle = driver.getTitle();
		System.out.println("Website Tile " + pageTitle);
	   }catch (Exception e) {
			e.printStackTrace();
		}
   
		
		return pageTitle;
   }
   /***
    * This method clicks on hidden element
    * @param by
    */
   public void clickOnHiddenElemnent(By by){
	   try{
	   JavascriptExecutor js = (JavascriptExecutor) driver;
	   js.executeScript("arguments[0].click();", driver.findElement(by));
	   }catch (Exception e) {
			e.printStackTrace();
		}
  
	   
   }
   public void clickOnHiddenElemnent(WebElement element){
	   try{
	   JavascriptExecutor js = (JavascriptExecutor) driver;
	   js.executeScript("arguments[0].click();", element);
	   }catch (Exception e) {
			e.printStackTrace();
		}
  
	   
	   
   }
 
   
   
   
   
   
   
   
   
   
   
   
   
   
   
	   
   }
   
   
   
   
   
   


