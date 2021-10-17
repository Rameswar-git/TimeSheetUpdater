package com.ram.timesheetupdater;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

public class OrganizationalTimeSheetUpdater implements TimeSheetUpdater {
	@Override
	public List<WebElement> locateElement(WebDriver driver) throws InterruptedException {
		WebElement orgTask = driver.findElement(By.xpath("//div[text()='Organization Tasks']"));
//		WebDriverWait wait0 = new WebDriverWait(driver, 10); //here, wait time is 20 seconds
//		wait0.until(ExpectedConditions.visibilityOf(orgTask));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orgTask);
//		Actions act=new Actions(driver);
//		act.moveToElement(orgTask).click().perform();
//		orgTask.click();	
		TimeUnit.SECONDS.sleep(1);
//		TimeUnit.MILLISECONDS.sleep(40);
//		((JavascriptExecutor) driver).executeScript("arguments[0].click();", workHrlFld);
//		TimeUnit.SECONDS.sleep(5);
		WebElement workDescFld = driver.findElement(By.xpath("//*[@id=\"notes_0_399\"]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", workDescFld);
//		Actions act2=new Actions(driver);
//		act2.moveToElement(workDescFld).click().perform();
//		WebDriverWait wait2 = new WebDriverWait(driver, 10); //here, wait time is 20 seconds
//		wait2.until(ExpectedConditions.visibilityOf(workDescFld));
		TimeUnit.SECONDS.sleep(1);
		WebElement workHrlFld = driver.findElement(By.xpath("//*[@id=\"hours_0_399\"]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", workHrlFld);
		workHrlFld.click();
//		WebDriverWait wait = new WebDriverWait(driver, 10); //here, wait time is 20 seconds
//		wait.until(ExpectedConditions.visibilityOf(workHrlFld)); //this will wait for elememt to be visible for 20 seconds
		
//		Actions act3=new Actions(driver);
//		act3.moveToElement(workHrlFld).click().perform();
		TimeUnit.SECONDS.sleep(1);
		return Arrays.asList(workHrlFld,workDescFld);
	}

}

// //*[@id="hours_1631_55"]    //*[@id="notes_1631_55"]

//WebElement ntt1 = driver.findElement(By.xpath("//*[@id=\"hours_1520_55\"]"));
//WebElement ntt2 = driver.findElement(By.xpath("//*[@id=\"notes_1520_55\"]"));
//if(null!= ntt1 && null!=ntt2) {
//	ntt1.sendKeys("9.00");
//	ntt2.sendKeys("Coding & Development ");
//}else {
//
//}
