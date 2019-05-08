package com.brems.e2e;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import io.github.bonigarcia.seljup.SeleniumExtension;

@ExtendWith(SeleniumExtension.class)
public class TestE2EFront extends ElastestBaseTest {

	final static Logger log = getLogger(lookup().lookupClass());

	@Test
	public void checkShowAdminPage() {
		// Login
		this.goToPage("login");
		this.loginUser("carlosv", "passc");

		// Go to admin page
		this.goToPage("admin/");

		// Checking if the user see admin page
		waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("errorMessage")), "Error, the user see admin page", 5);
		WebElement errorMessage = driver.findElement(By.className("errorMessage"));
		assertThat(errorMessage.getText().toLowerCase()).contains("error:");
		logger.info("Correct, the user don't see admin page");
		sleep(2000);

		// Logout
		this.goToPage();
		this.logout();
	}

	public void goToPage() {
		String url = sutUrl;

		this.driver.get(url);
	}

	public void goToPage(String page) {
		String url = sutUrl;

		this.driver.get(url + page);
	}

	public void waitUntil(ExpectedCondition<WebElement> expectedCondition, String errorMessage, int seconds) {
		WebDriverWait waiter = new WebDriverWait(driver, seconds);

		try {
			waiter.until(expectedCondition);
		} catch (org.openqa.selenium.TimeoutException timeout) {
			log.error(errorMessage);
			throw new org.openqa.selenium.TimeoutException(
					"\"" + errorMessage + "\" (checked with condition) > " + timeout.getMessage());
		}
	}

	public void loginUser(String name, String pass) {
		// Wait show form login
		waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("content")), "No login page", 5);
		WebElement loginPageContent = driver.findElement(By.className("content"));

		// Load form
		WebElement userField = loginPageContent.findElement(By.id("username"));
		WebElement passField = loginPageContent.findElement(By.id("password"));

		// Write credentials
		userField.sendKeys(name);
		passField.sendKeys(pass);

		loginPageContent.findElement(By.id("password")).sendKeys(Keys.ENTER);

		waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("userdropmenu")), "Login failed", 5);

		WebElement menuBarUser = driver.findElement(By.className("userdropmenu"));
		WebElement user = menuBarUser.findElement(By.tagName("a"));
		assertThat(user.getText()).isEqualToIgnoringCase(name);

		log.info("Loggin successful, user {}", name);
	}

	public void logout() {
		String name = "";

		waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("userdropmenu")), "Login failed", 5);

		// Logout
		WebElement menuBarUser = driver.findElement(By.className("userdropmenu"));
		List<WebElement> buttonsUser = menuBarUser.findElements(By.tagName("a"));
		name = buttonsUser.get(0).getText().toLowerCase();
		buttonsUser.get(0).click();
		sleep(2000);
		buttonsUser.get(2).click();

		// Check logout
		waitUntil(ExpectedConditions.visibilityOfElementLocated(By.className("userDropdownMenu")), "logout failed", 5);
		menuBarUser = driver.findElement(By.className("userDropdownMenu"));
		assertThat(menuBarUser.getText().toLowerCase()).contains("iniciar");

		log.info("Logout successful, user {}", name);
	}

	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
