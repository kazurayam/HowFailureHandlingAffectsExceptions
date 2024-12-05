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
 * This script can reproduce a Selenium StaleElementReferenceException (SERE).
 * The target HTML is dynamically modified by JavaScript inside it.
 * An HTML node will be removed and recreated at 3 seconds after the initial page load.
 * 
 * WebUI.verifyElementNotPresent keyword against the problem HTML element will cause
 * an SERE.
 * 
 * @author kazurayam
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path html = projectDir.resolve("targetPage.html")
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
	// The verifyElementNotPresent keyword will not notice the change.
	// The keyword will see the HTML node stays present untile the timeout expires
	WebUI.verifyElementNotPresent(myButtonTestObject, 
		                        10,
								FailureHandling.STOP_ON_FAILURE)
	// so the keyword will throw a SERE
} catch (Exception e) {
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
}

WebUI.closeBrowser()