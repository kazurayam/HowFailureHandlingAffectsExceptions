import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC7_click
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
	WebUI.click(tObj, FailureHandling.STOP_ON_FAILURE)
} catch (Exception e) {
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
	println "==========================================================================="
	e.printStackTrace()
	println "==========================================================================="
}

WebUI.closeBrowser()
