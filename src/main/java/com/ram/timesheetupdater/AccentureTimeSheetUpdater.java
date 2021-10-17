package com.ram.timesheetupdater;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AccentureTimeSheetUpdater implements TimeSheetUpdater{

	@Override
	public List<WebElement> locateElement(WebDriver driver) throws InterruptedException {
//		WebElement orgTask = driver.findElement(By.xpath("//div[text()='Organization Tasks']"));
//		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orgTask);
//		orgTask.click();	
		try {
			WebElement workHrlFld = driver.findElement(By.xpath("//*[@id=\"hours_1631_55\"]"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", workHrlFld);
			TimeUnit.SECONDS.sleep(5);
			WebElement workDescFld = driver.findElement(By.xpath("//*[@id=\"notes_1631_55\"]"));
			workHrlFld.click();
			return Arrays.asList(workHrlFld,workDescFld);
		} catch (Exception e) {
			System.out.println("Exception occured while locating element :->  "+e.getMessage());
			return null;
		}
	}

}
