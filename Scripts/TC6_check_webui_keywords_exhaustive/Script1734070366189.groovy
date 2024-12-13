import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC6
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
TestObject myButtonTestObject = findTestObject("Object Repository/myButton")

List<Tuple2<String, Closure>> targets = [
	["waitForElementNotHasAttribute", { WebUI.waitForElementNotHasAttribute(myButtonTestObject, 'class', 5, FailureHandling.STOP_ON_FAILURE) }],  // threw SERE
	["waitForElementNotPresent", { WebUI.waitForElementNotPresent(myButtonTestObject, 5, FailureHandling.STOP_ON_FAILURE) }],  // no SERE
	["waitForElementNotVisible", { WebUI.waitForElementNotVisible(myButtonTestObject, 5, FailureHandling.STOP_ON_FAILURE) }]   // threw SERE
]

List<String> sereThrownBy = []

targets.each { target ->
	String kwName = target[0]
	Closure cls = target[1]
	// open a browser, navigate to the target web page, make sure the button is present
	WebUI.openBrowser('')
	WebUI.navigateToUrl(urlString)
	WebUI.setViewPortSize(800, 600)
	WebUI.verifyElementPresent(myButtonTestObject, 10, FailureHandling.STOP_ON_FAILURE)
	try {
		// call a WebUI keyword to see how it goes
		cls.call()
	} catch (Exception e) {
		if (causedBySERE(e)) {
			sereThrownBy.add(kwName)
		}
	}
	WebUI.closeBrowser()
}

println("=========================================================")
String msg = "SERE thrown by " + sereThrownBy.toString()
WebUI.comment(msg)

boolean causedBySERE(Throwable e) {
	if (e != null) {
		if (e.getClass().getName().equals("org.openqa.selenium.StaleElementReferenceException")) {
			return true
		} else {
			causedBySERE(e.getCause())
		}
	} else {
		return false
	}
}


