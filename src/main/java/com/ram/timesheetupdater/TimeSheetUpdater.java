package com.ram.timesheetupdater;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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
	
	final List<String> holidays = Arrays.asList("2022-03-17","2022-05-03","2022-08-15","2022-08-31","2022-10-05","2022-10-24");
	OrganizationalTimeSheetUpdater orgActivity = new OrganizationalTimeSheetUpdater();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter ddMMyyyyFormater = DateTimeFormatter.ofPattern(("dd-MMM-yy"));
	static final StringBuilder stringEditor = new StringBuilder();
	public static WebDriver setupLogin(String password) throws InterruptedException, IOException {
		FirefoxBinary firefoxBinary = new FirefoxBinary();
		firefoxBinary.addCommandLineOptions("--headless");
		System.setProperty("webdriver.gecko.driver", System.getenv("selenium.firefox.driverpath"));
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
//		WebElement workHrlFld = null;
//		WebElement workDescFld = null;
//		String workDesc = null;
//		List<WebElement> locateElement = null;
		boolean isContinue = updateForDate(driver,today);
		for (int i = 0; i <= 15 && isContinue ; i++) {
			today = today.minusDays(1);
			isContinue = updateForDate(driver,today);
		}
	}

	public default boolean updateForDate(WebDriver driver, LocalDate today) throws InterruptedException {
		String name = today.getDayOfWeek().name();
		System.out.printf("~~~~~~~~~~~Processing %s date~~~~~~~~~~~", today.toString());
		System.out.println();
		if (!(name.equals("SATURDAY") || name.equals("SUNDAY")) && !holidays.contains(today.format(formatter))) {
			String formattedString = "https://myspace.innominds.com/Employee/timesheet?reqdate="
					+ today.format(formatter);
			driver.navigate().to(formattedString);
			TimeUnit.MINUTES.sleep(1);
			List<WebElement> locateElement = this.locateElement(driver);
			String workDesc = "Coding & Development";
			if (locateElement == null) {
				locateElement = orgActivity.locateElement(driver);
				workDesc = "Organizational Activities";
			}
			WebElement workHrlFld = locateElement.get(0);
			WebElement workDescFld = locateElement.get(1);
			if (!isFieldParsabletoInt(workHrlFld.getAttribute("value"))) {
				workHrlFld.sendKeys("9.00");
				workDescFld.sendKeys(workDesc);
				driver.findElement(By.xpath("//*[@id=\"sub\"]")).click();
				System.out.printf("~~~~~~~~Time Sheet Updated Successfully for ' %s ' Date ~~~~~~~~",
						today.toString());
				System.out.println();
				TimeUnit.SECONDS.sleep(40);
				return true;
			} else {
				System.out.printf("~~~~~~~~Time Sheet Already Updated for ' %s ' Date ~~~~~~~~", today.toString());
				System.out.println();
				return false;
			}
		} else {
			System.out.printf("~~~~~~~~~~~~~~~~~~ %s is %s~~~~~~~~~~~~~~~~~~", today.toString(), name);
			System.out.println();
			return true;
		}
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

	public default void updateMissingTimeSheets(WebDriver driver) throws InterruptedException {
		driver.navigate().to("https://myspace.innominds.com/employee/timesheetmonthview");
		TimeUnit.SECONDS.sleep(3);
		WebElement missingDatesElement = findMissingDates(driver,"//div[@id=\"results\"]/p/span[2]");
		if(null!=missingDatesElement) {
			String missingDates = missingDatesElement.getText();
			System.out.printf("TimeSheet was not Updated for %s Dates",missingDates);
			System.out.println();
			String[] split = missingDates.split(",");
			for (String missedDate : split) {
				stringEditor.append(missedDate.trim().replaceAll("\\s+", "-"));
				LocalDate date = LocalDate.parse(stringEditor.delete(2,4).toString(),ddMMyyyyFormater);
				updateForDate(driver,LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth()));
				stringEditor.setLength(0);
			}
		}
	}

	public default WebElement findMissingDates(WebDriver driver, String xPath) {
		try {
			return driver.findElement(By.xpath(xPath));
		} catch (Exception e) {
			System.out.println("~~~~~~There are no Missing dates available to Fill~~~~~~~");
			return null;
		}
	}
}
//WebElement sibling =  workHrlFld.findElement(By.xpath(".."));  // this selects parent element
//sibling.findElement(By.xpath("preceding-sibling::td[1]")).getText() // this selects sibling  element
//System.out.println("Preceeding element text is "+ sibling.findElement(By.xpath("preceding-sibling::td[1]")).getText());
