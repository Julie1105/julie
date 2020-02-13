package com.ctrip.web;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class HotelDetails {
	private WebDriver driver;
	private HotelDetails test;

	@BeforeTest
	public void beforeTest() throws IOException {
		System.out.println("Open the ctrip website");
		test = new HotelDetails();
		Runtime.getRuntime().exec("taskkill /F /im " + "chromedriver.exe");
		Runtime.getRuntime().exec("taskkill /F /im " + "chrome.exe");
		System.setProperty("webdriver.chrome.driver", "C:\\Julie\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.get("https://www.ctrip.com/");
		driver.manage().window().maximize();
		driver.findElement(By.id("oversera_mask")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterTest
	public void afterTest() {
		System.out.println("Execution completed, close the broswer");
		driver.quit();
	}

	@Test
	public void test() throws Exception {
		System.out.println("Seach the hotels with keywords");
		searchWithKeywords();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(
				"Find the hotel with the lowest price in the top five search results and click 'View details'");
		getLowestHotel();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Verify that the total price of the default item is correct on the default page");
		compareToTotalPrice();

	}

	private boolean isElementLoaded(WebElement element, WebDriver driver) throws Exception {
		WebDriverWait tempWait = new WebDriverWait(driver, 10);

		try {
			tempWait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void loginToSystem(WebDriver driver) throws Exception {
		HotelDetails
		test = new HotelDetails();
		if (test.isElementLoaded(driver.findElement(By.cssSelector("li#nav-bar-set-login a")), driver)) {
			driver.findElement(By.cssSelector("li#nav-bar-set-login a")).click();
			driver.findElement(By.id("nloginname")).sendKeys("18061515431");
			driver.findElement(By.id("npwd")).sendKeys("passw0rd");
			if (test.isElementLoaded(driver.findElement(By.className("cpt-drop-btn")), driver)) {
				test.mouseHoverDragandDrop(driver, driver.findElement(By.className("cpt-drop-btn")));
			}
			driver.findElement(By.id("nsubmit")).click();
			if (test.isElementLoaded(driver.findElement(By.className("cpt-drop-btn")), driver)) {
				test.mouseHoverDragandDrop(driver, driver.findElement(By.className("cpt-drop-btn")));
				driver.findElement(By.id("nsubmit")).click();
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void mouseHoverDragandDrop(WebDriver driver, WebElement fromDestination) {
		Actions actions = new Actions(driver);
		actions.moveToElement(fromDestination).clickAndHold(fromDestination);
		actions.dragAndDropBy(fromDestination, 305, 0).perform();
	}

	private void searchWithKeywords() throws Exception {
		if (test.isElementLoaded(driver.findElement(By.className("website_pop_close")), driver)) {
			driver.findElement(By.className("website_pop_close")).click();
		}
		// login
		test.loginToSystem(driver);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (test.isElementLoaded(driver.findElement(By.className("website_pop_close")), driver)) {
			driver.findElement(By.className("fl_wrap_close")).click();
		}

		// search hotels
		driver.findElement(By.id("HD_CityName")).sendKeys("xm");
		driver.findElement(By.id("HD_CityName")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("HD_CheckIn")).clear();
		driver.findElement(By.id("HD_CheckIn")).sendKeys("2020-03-03");
		driver.findElement(By.id("HD_CheckOut")).clear();
		driver.findElement(By.id("HD_CheckOut")).sendKeys("2020-03-05");
		driver.findElement(By.id("J_roomCountList")).click();
		driver.findElement(By.cssSelector("#J_roomCountList > option:nth-child(2)")).click();
		driver.findElement(By.id("J_RoomGuestInfoTxt")).click();
		driver.findElement(By.cssSelector("span#J_AdultCount span.number_plus")).click();
		driver.findElement(By.cssSelector("span#J_AdultCount span.number_plus")).click();
		driver.findElement(By.cssSelector("span#J_ChildCount span.number_plus")).click();
		driver.findElement(By.id("J_childageVal0")).click();
		driver.findElement(By.cssSelector("#J_childageVal0 > option:nth-child(4)")).click();
		driver.findElement(By.cssSelector("#J_RoomGuestInfoBtnOK")).click();
		driver.findElement(By.cssSelector("#searchHotelLevelSelect > option:nth-child(4)")).click();
		driver.findElement(By.cssSelector("#HD_TxtKeyword")).sendKeys("鼓浪屿");
		driver.findElement(By.cssSelector("#HD_TxtKeyword")).sendKeys(Keys.ENTER);
	}

	private void getLowestHotel() throws Exception {
		((JavascriptExecutor) driver).executeScript("document.documentElement.scrollTop=50");
		if (test.isElementLoaded(driver.findElement(By.id("J_sortBox")), driver)) {
			List<WebElement> hotels = driver.findElements(By.cssSelector("li.hotel_price_icon"));
			List<String> prices = new ArrayList<>();
			int minIndex = 0;
			if (hotels.size() >= 5) {
				for (int i = 0; i < 5; i++) {
					prices.add(hotels.get(i).findElement(By.cssSelector("span.J_price_lowList")).getText());
				}
				if (prices.size() == 5) {
					int min = Integer.parseInt(prices.get(0));
					for (int i = 1; i < 5; i++) {
						if (Integer.parseInt(prices.get(i)) < min) {
							min = Integer.parseInt(prices.get(i));
							minIndex = i;
						}
					}
					WebElement lookForDetails = hotels.get(minIndex).findElement(By.cssSelector("a.btn_buy"));
					String currentHandle = driver.getWindowHandle();
					lookForDetails.click();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Set<String> handles = driver.getWindowHandles();
					for (String s : handles) {
						if (s.equals(currentHandle))
							continue;
						else
							driver.switchTo().window(s);
					}
				}
			}
		}
	}

	private void compareToTotalPrice() throws Exception {
		((JavascriptExecutor) driver).executeScript("document.documentElement.scrollTop=160");
		if (test.isElementLoaded(driver.findElement(By.className("btns_base22_main")), driver)) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.findElement(By.className("btns_base22_main")).click();

			((JavascriptExecutor) driver).executeScript("document.documentElement.scrollTop=150");

			List<WebElement> prices = driver.findElements(By.cssSelector("span.orange"));
			String priceDay1 = prices.get(0).getText();
			String priceDay2 = prices.get(1).getText();
			int priceInt = Integer.parseInt(priceDay1) + Integer.parseInt(priceDay2);

			List<WebElement> priceDetail = driver.findElements(By.cssSelector("span.cost_detail_quota"));
			List<String> discountDetail = new ArrayList<>();
			int discountSum = 0;
			for (int i = 1; i < priceDetail.size(); i++) {
				discountDetail.add(priceDetail.get(i).getText());
				int discountInt = Integer.parseInt(discountDetail.get(i - 1).split(" ")[1]);
				discountSum += discountInt;
			}
			String totalPrice = driver.findElement(By.className("htotal_price_num")).getText();
			int totalPriceInt = Integer.parseInt(totalPrice);
			System.out.println("The total price in the order is: " + totalPriceInt);
			int expectedTotal = priceInt * 2 + discountSum;
			System.out.println(
					"The expected price is: " + expectedTotal + " (2 Rooms Cost - Discount + Cash Pledag if have)");
			assertEquals(totalPriceInt, expectedTotal);
		}
	}
}
