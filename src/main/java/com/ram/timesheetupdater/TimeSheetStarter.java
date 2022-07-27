package com.ram.timesheetupdater;

import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;

import com.rameswar.EncryptionUtils.EncryptionUtil;

public class TimeSheetStarter {
	private static TimeSheetUpdater timeSheetUpdaterService = null;
	static {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(System.getenv("timesheet.proppath").trim()));
			String property = props.getProperty("timesheet.class");
			Class<?> forName = Class.forName(property);
			timeSheetUpdaterService = (TimeSheetUpdater) forName.newInstance();
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		WebDriver driver = null;
		if (args.length == 1) {
			String password = EncryptionUtil.decrypt(System.getenv("timesheet.key").trim(),System.getenv("timesheet.hash").trim());
			driver = TimeSheetUpdater.setupLogin(password);
		} else {
			Console console = System.console();
			if (console == null) {
				System.out.println("Couldn't get Console instance");
				System.exit(0);
			}
			console.flush();
			console.flush();
			String password = new String(console.readPassword("Enter your password: "));
			//new Scanner(System.in).nextLine()
					//new String(console.readPassword("Enter your password: "));
			driver = TimeSheetUpdater.setupLogin(password);
			console.flush();
		}
		timeSheetUpdaterService.updateTimeSheet(driver);
		timeSheetUpdaterService.updateMissingTimeSheets(driver);
	}
	
	public static void maintest(String[] args) throws MalformedURLException, IOException, InterruptedException {
		for (int i = 1; i < 11; i++) {
			BufferedImage image = null;
			try {

				URL url = new URL("Any Url");
				// read the url
				image = ImageIO.read(url);

				String ds = "Any Path" + i + ".jpg";
				// for jpg
				ImageIO.write(image, "jpg", new File(ds));

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
}
