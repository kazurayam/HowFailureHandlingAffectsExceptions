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
 * This script demonstrates that the WebUI.click keyword may throw
 * a StaleElementReferenceException when the target web page is a web form.
 *
 * The "form.html" works as follows:
 *
 * 1. It has a <button id="myButton"> which is initially "disabled"; so that is not clickable
 * 2. At 3 seconds after the page load, the button element is removed; and recreated.
 * 3. The recreated <button id="myButton"> is enabled; so that it is clickable.
 *
 * When the script called the WebUI.click keyword, the target `<button id="myButton">` element is
 * disabled; it is unable to click. Therefore an ElementClickInterceptedException will be thrown
 * by the WebElement object.
 * The WebUI.click is implemented to catch ElementClickInterceptedException and wait for
 * the target element to become enabled = clickable.
 * At 3 secs when the <button id="myButton"> is removed and recreated.
 * The element is enabled and clickable; but the reference to the <button id="myButton"> gets stale.
 * So that a SERE is thrown by the WebUI.click keyword.
 *
 * See https://www.browserstack.com/guide/element-click-intercepted-exception-selenium 
 * 2. Disabled Elements
 * for reference
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
	WebElement btn = WebUI.findWebElement(myButtonTestObject, 5)
	btn.click()   // expected to throw a ElementClickInterceptedException, but not
	
	//WebUI.click(myButtonTestObject, FailureHandling.STOP_ON_FAILURE)                  // => threw SERE
} catch (Exception e) {
	println ">>> An Exception was caught: " + e.getClass().getName() + ": " + e.getMessage() + " <<<"
	println "==========================================================================="
	e.printStackTrace()
	println "==========================================================================="
}

//WebUI.closeBrowser()

