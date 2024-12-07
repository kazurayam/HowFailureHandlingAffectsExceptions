import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC5
 *
 * A variation derived from the TC2.
 * This script list the WebUI keywords that takes a timeout second as argument to check
 * if these raises Selenium StaleElementPresentException.
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
	//WebUI.waitForElementNotHasAttribute(myButtonTestObject, 'class', 5, FailureHandling.STOP_ON_FAILURE)  // => threw SERE
	//WebUI.waitForElementNotPresent(myButtonTestObject, 5, FailureHandling.STOP_ON_FAILURE)                  // => no SERE
	WebUI.waitForElementNotVisible(myButtonTestObject, 5, FailureHandling.STOP_ON_FAILURE)                  // => threw SERE

} catch (Exception e) {
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
	println "==========================================================================="
	e.printStackTrace()
	println "==========================================================================="

}

WebUI.closeBrowser()
