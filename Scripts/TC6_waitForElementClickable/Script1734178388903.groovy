import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC6_waitForElementClickable
 *
 * This script demonstrates that the WebUI.waitForElementClickable keyword may throw
 * a StaleElementReferenceException when the target web page is a web form.
 * 
 * The "form.html" works as follows:
 * 
 * 1. It has a <button id="myButton"> which is initially "disabled"; so that is not clickable
 * 2. At 3 seconds after the page load, the button element is removed; and recreated.
 * 3. The recreated <button id="myButton"> is enabled; so that it is clickable.
 * 
 * The WebUI.waitForElementClickable keyword checks the state of the `<button id="myButton">`.
 * Initially, the keyword finds the button is not clickable.
 * The keyword waits for the button to be clikable.
 * At 3 secs when the <button id="myButton"> is removed and recreated, the keyword throws a SERE.
 * 
 * @author kazurayam
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())

Path html = projectDir.resolve("docs/formPage.html")

URL htmlURL = html.toFile().toURI().toURL()
String urlString = htmlURL.toExternalForm()
WebUI.comment("navigating to " + urlString)

// open a browser, navigate to the target web page
WebUI.openBrowser('')
WebUI.navigateToUrl(urlString)
WebUI.setViewPortSize(800, 600)

TestObject myButtonTestObject = findTestObject("Object Repository/myButton")

// make sure <button id='myButton'> is displayed in the page initially
WebUI.verifyElementPresent(myButtonTestObject, 10, FailureHandling.STOP_ON_FAILURE)

try {
	WebUI.waitForElementClickable(myButtonTestObject, 5, FailureHandling.STOP_ON_FAILURE)                  // => threw SERE
} catch (Exception e) {
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
	println "==========================================================================="
	e.printStackTrace()
	println "==========================================================================="
}

WebUI.closeBrowser()
