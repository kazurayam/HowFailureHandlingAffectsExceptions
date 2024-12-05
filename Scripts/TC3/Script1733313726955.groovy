import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC3
 *
 * A variation derived from the TC2.
 * 
 * This script demonstrates that FailureHandling.CONTINUE_ON_FAILURE makes 
 * all keywords silent.
 * No exception will be raised by a keyword invokation.
 * Your Test Case script can not catch any Exception.
 * 
 * @author kazurayam
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path html = projectDir.resolve("targetPage.html")
URL htmlURL = html.toFile().toURI().toURL()
String urlString = htmlURL.toExternalForm()
WebUI.comment("navigating to " + urlString)

WebUI.openBrowser('')
WebUI.navigateToUrl(urlString)
WebUI.setViewPortSize(800, 600)

TestObject myButtonTestObject = findTestObject("Object Repository/myButton")

WebUI.verifyElementPresent(myButtonTestObject, 10, FailureHandling.STOP_ON_FAILURE)

try {
	WebUI.verifyElementNotPresent(myButtonTestObject,
								10,
								FailureHandling.CONTINUE_ON_FAILURE)
	// The keyword will throw no Exception
} catch (Exception e) {
	// You can not catch SERE here
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
}

WebUI.closeBrowser()

// In the end, in the Console, you will find a long Stack trace of StepFailedException is printed.