package com.ram.timesheetupdater;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
//import java.io.Console;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import java.io.IOException;

public interface TimeSheetUpdater {
	
	final List<String> holidays = Arrays.asList("2021-09-10","2021-10-15","2021-11-01","2021-11-04");
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static WebDriver setupLogin(String password) throws InterruptedException, IOException {
		FirefoxBinary firefoxBinary = new FirefoxBinary();
		firefoxBinary.addCommandLineOptions("--headless");
		System.setProperty("webdriver.gecko.driver", "D:\\Softwares\\Selenium Drivers\\geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(new FirefoxProfile());
		options.setBinary(firefoxBinary);
		options.addPreference("dom.webnotifications.enabled", false);
		WebDriver driver = new FirefoxDriver(options);
		driver.navigate().to("https://myspace.innominds.com/user/login");
		TimeUnit.SECONDS.sleep(4);
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys("rdas");
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
		driver.findElement(By.xpath("//*[@id=\"submit\"]")).click();
		TimeUnit.MINUTES.sleep(2);
		return driver;
	}

	public default void updateTimeSheet(WebDriver driver) throws InterruptedException {
		LocalDate today = LocalDate.now();
		OrganizationalTimeSheetUpdater orgActivity = new OrganizationalTimeSheetUpdater();
		WebElement workHrlFld = null;
		WebElement workDescFld = null;
		String workDesc = null;
		List<WebElement> locateElement = null;
		for (int i = 0; i <= 15 ; i++) {
			today = today.minusDays(i);
			String name = today.getDayOfWeek().name();
			System.out.printf("~~~~~~~~~~~Processing %s date~~~~~~~~~~~", today.toString());
			System.out.println();
			if (!(name.equals("SATURDAY") || name.equals("SUNDAY")) && !holidays.contains(today.format(formatter))) {
				String formattedString = "https://myspace.innominds.com/Employee/timesheet?reqdate="
						+ today.format(formatter);
				driver.navigate().to(formattedString);
				TimeUnit.MINUTES.sleep(1);
				locateElement = this.locateElement(driver);
				workDesc = "Coding & Development";
				if (locateElement == null) {
					locateElement = orgActivity.locateElement(driver);
					workDesc = "Organizational Activities";
				}
				workHrlFld = locateElement.get(0);
				workDescFld = locateElement.get(1);
				if (!isFieldParsabletoInt(workHrlFld.getAttribute("value"))) {
//					WebElement sibling =  workHrlFld.findElement(By.xpath(".."));  // this selects parent element
					//sibling.findElement(By.xpath("preceding-sibling::td[1]")).getText() // this selects sibling  element
//					System.out.println("Preceeding element text is "+ sibling.findElement(By.xpath("preceding-sibling::td[1]")).getText());
					workHrlFld.sendKeys("9.00");
					workDescFld.sendKeys(workDesc);
					driver.findElement(By.xpath("//*[@id=\"sub\"]")).click();
					System.out.printf("~~~~~~~~Time Sheet Updated Successfully for ' %s ' Date ~~~~~~~~",
							today.toString());
					System.out.println();
					TimeUnit.SECONDS.sleep(40);
				} else {
					System.out.printf("~~~~~~~~Time Sheet Already Updated for ' %s ' Date ~~~~~~~~", today.toString());
					System.out.println();
					break;
				}
			} else {
				System.out.printf("~~~~~~~~~~~~~~~~~~ %s is %s~~~~~~~~~~~~~~~~~~", today.toString(), name);
				System.out.println();
			}
		}
//		this.locateElement(driver);
	}

	public default boolean isFieldParsabletoInt(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<WebElement> locateElement(WebDriver driver) throws InterruptedException;
	

}
