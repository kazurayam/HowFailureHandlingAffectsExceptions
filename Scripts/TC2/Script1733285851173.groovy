import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC2
 *
 * This script demonstrates the WebUI.waitForElementNotClickable keyword throws
 * Selenium StaleElementReferenceException (SERE).
 *
 * @author kazurayam
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path html = projectDir.resolve("docs/targetPage.html")
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
	// at 3 secs after the initial page loading,
	// the old <button id='myButton'> was removed, but soon
	// a new <button id='myButton'> was recreated.
	// The keyword will see the HTML node stays clickable untile the timeout expires
    // However, the keyword throws a SERE for some reason.
   // Do you know why?
	WebUI.waitForElementNotClickable(myButtonTestObject,
		                        10,
								FailureHandling.STOP_ON_FAILURE)
} catch (Exception e) {
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
	println "==========================================================================="
	e.printStackTrace()
	println "==========================================================================="

}

WebUI.closeBrowser()
