package com.ram.timesheetupdater;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DeloitteTimeSheetUpdater implements TimeSheetUpdater {

	@Override
	public List<WebElement> locateElement(WebDriver driver) throws InterruptedException {
		try {
			WebElement workHrlFld = driver.findElement(By.xpath("//*[@id=\"hours_1525_56\"]"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", workHrlFld);
			TimeUnit.SECONDS.sleep(5);
			WebElement workDescFld = driver.findElement(By.xpath("//*[@id=\"notes_1525_56\"]"));
			workHrlFld.click();
			return Arrays.asList(workHrlFld,workDescFld);
		} catch (Exception e) {
			System.out.println("Exception occured while locating element :->  "+e.getMessage());
			return null;
		}
	}

}
