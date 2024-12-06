import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebElement

import com.kazurayam.hack.MockWaitForElementNotClickableKeyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC4
 *
 * A small modification from TC2.
 * TC4 calls com.kazurayam.hack.MockWaitForElementNotClickable() keyword.
 * TC4 does NOT throw StaleElementReferenceException because
 * the MockWaitForElementNotClickable class is hacked.
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

WebUI.verifyElementPresent(myButtonTestObject, 10, FailureHandling.STOP_ON_FAILURE)

try {
	// the following statement does NOT throw StaleElementReferenceException
	boolean b = new MockWaitForElementNotClickableKeyword().waitForElementNotClickable(myButtonTestObject,
		                        10,
								FailureHandling.STOP_ON_FAILURE)
	// so the keyword will throw a SERE
} catch (Exception e) {
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
	println "==========================================================================="
	e.printStackTrace()
	println "==========================================================================="

}

WebUI.closeBrowser()
