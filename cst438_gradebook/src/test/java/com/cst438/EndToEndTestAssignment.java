package com.cst438;

import static org.assertj.core.api.Assertions.*;
import java.util.concurrent.TimeUnit;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EndToEndTestAssignment {

    public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/katesawtell/Downloads/chromedriver-mac-arm64/chromedriver";
    public static final String URL = "http://localhost:3000";
    public static final int SLEEP_DURATION = 2000; 
    public static final String NEW_ASSIGNMENT_NAME = "Testing Assignment";
    public static final String NEW_ASSIGNMENT_DUE_DATE = "2023-11-30";
    public static final String COURSE_ID = "31045"; 
    
    @Test
    public void addAssignmentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Debugging: Output the current URL and page source
            System.out.println("Navigated to: " + driver.getCurrentUrl());
            System.out.println("Page source: " + driver.getPageSource());

            // Find and click the "Add Assignment" button
            WebElement addAssignmentButton = driver.findElement(By.xpath("//button[text()='Add Assignment']"));
            addAssignmentButton.click();
            Thread.sleep(SLEEP_DURATION);

            // Enter assignment details
            WebElement assignmentNameInput = driver.findElement(By.name("assignmentName"));
            assignmentNameInput.sendKeys(NEW_ASSIGNMENT_NAME);

            WebElement dueDateInput = driver.findElement(By.name("dueDate"));
            dueDateInput.sendKeys(NEW_ASSIGNMENT_DUE_DATE);

            WebElement courseIdInput = driver.findElement(By.name("courseId"));
            courseIdInput.sendKeys(COURSE_ID);

            // Find and click the "Save Assignment" button
            WebElement saveAssignmentButton = driver.findElement(By.id("add"));
            saveAssignmentButton.click();
            Thread.sleep(SLEEP_DURATION);
            // Check if the assignment is added successfully
            WebElement addedAssignment = driver.findElement(By.xpath("//h4[contains(text(),'Assignment added.')]"));
            assertThat(addedAssignment).isNotNull();
        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }

    
    @Test
    public void updateAssignmentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            WebElement updateButton = driver.findElement(By.xpath("//button[text()='Edit']"));
            updateButton.click();
            Thread.sleep(SLEEP_DURATION);

            // Modify assignment details
            WebElement updatedAssignmentNameInput = driver.findElement(By.name("assignmentName"));
            WebElement updatedDueDateInput = driver.findElement(By.name("dueDate"));

            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value = '';", updatedAssignmentNameInput);
            js.executeScript("arguments[0].value = '';", updatedDueDateInput);
            
            Thread.sleep(SLEEP_DURATION);

            updatedAssignmentNameInput.sendKeys("Testing update Assignment Name");
            //updatedDueDateInput.sendKeys("2023-12-31");

            // Find and click the "Save Update" button
            WebElement saveUpdateButton = driver.findElement(By.xpath("//button[text()='Save']"));
            saveUpdateButton.click();
            Thread.sleep(SLEEP_DURATION);

            // Check if the assignment is updated successfully
            WebElement updatedAssignment = driver.findElement(By.xpath("//h4[contains(text(),'Assignment saved.')]"));
            assertThat(updatedAssignment).isNotNull();


        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
    
    @Test
    public void deleteAssignmentByNameTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate the assignment in the list by its name and click the corresponding "Delete" button
            WebElement deleteButton = driver.findElement(By.xpath("//td[contains(text(),'" + NEW_ASSIGNMENT_NAME + "')]/following-sibling::td/button[text()='Delete']"));
            deleteButton.click();
            Thread.sleep(SLEEP_DURATION);

            // Assuming successful deletion, the assignment should not be present in the list
            WebElement deletedAssignment = driver.findElement(By.xpath("//h4[contains(text(),'Assignment deleted.')]"));
            assertThat(deletedAssignment).isNotNull();
        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
}
