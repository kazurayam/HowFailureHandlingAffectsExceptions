import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC1
 *
 * This script can reproduce a Selenium StaleElementReferenceException (SERE).
 * The target HTML is dynamically modifiedy by JavaScript inside it.
 * An HTML node will be removed and recreated at 3 seconds after the initial page load.
 * A reference to the HTML node as org.openqa.selenium.WebElement object will
 * get stale by the DOM modification by JavaScript.
 *
 * Referring to the stale WebElement object will cause a SERE.
 *
 * @author kazurayam
 */
// identify the location of target HTML file
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path html = projectDir.resolve("docs/targetPage.html")
URL htmlURL = html.toFile().toURI().toURL()
String urlString = htmlURL.toExternalForm()
WebUI.comment("navigating to " + urlString)

// open a browser, navigate to the target page
WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)
WebUI.navigateToUrl(urlString)

TestObject myButtonTestObject = findTestObject("Object Repository/myButton")

// make sure <button id='myButton'> is displayed in the page initially
WebUI.verifyElementPresent(myButtonTestObject, 10, FailureHandling.STOP_ON_FAILURE)

// get the reference to the HTML element <button id='myButton'>
WebElement myButtonWebElement = WebUI.findWebElement(myButtonTestObject, 10, FailureHandling.STOP_ON_FAILURE)

// the test intentionally does nothing for long enough seconds
WebUI.delay(5)

// At 3 seconds after the page load, JavaScript wil remove and recreate the HTML element

try {
	// at 3 secs after the initial page loading,
	// the old <button id='myButton'> is removed, then
	// a new <button id='myButton'> is recreted.
	myButtonWebElement.click()  // this statement will throw a StaleElementReferenceException
} catch (Exception e) {
	WebUI.comment(">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<")
}

//WebUI.closeBrowser()
