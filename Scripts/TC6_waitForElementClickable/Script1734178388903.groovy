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
 * The "initiallyDisabledLaterEnabled.html" works as follows:
 * 
 * 1. It has a <input type="button" id="myButton"> which is initially "disabled"; 
 *    so that is not clickable
 * 2. At 3 seconds after the page load, the button element is removed; and recreated.
 * 3. The recreated <input type="button" id="myButton"> is enabled; 
 *    so that it is clickable.
 * 
 * The WebUI.waitForElementClickable keyword checks the state of the `<input type="button" id="myButton">` element.
 * Initially, the keyword finds the button is not clickable.
 * The keyword waits for the button to become clickable.
 * At 3 secs when the <input type="button" id="myButton"> is removed and recreated.
 * The keyword tries to refer to the element but the reference has become stale;
 * so the keyword throws a SERE.
 * 
 * @author kazurayam
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())

Path html = projectDir.resolve("docs/initiallyDisabledLaterEnabled.html")

URL htmlURL = html.toFile().toURI().toURL()
String urlString = htmlURL.toExternalForm()
WebUI.comment("navigating to " + urlString)

// open a browser, navigate to the target web page
WebUI.openBrowser('')
WebUI.navigateToUrl(urlString)
WebUI.setViewPortSize(800, 600)
TestObject tObj = findTestObject("Object Repository/myInputButton")

// make sure <input type="button" id='myButton'> is displayed in the page initially
WebUI.verifyElementPresent(tObj, 10, FailureHandling.STOP_ON_FAILURE)

try {
	WebUI.waitForElementClickable(tObj, 5, FailureHandling.STOP_ON_FAILURE)  // => threw SERE
} catch (Exception e) {
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
	println "==========================================================================="
	e.printStackTrace()
	println "==========================================================================="
}

WebUI.closeBrowser()
