import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


TestObject makeTestObject(String id, String xpathExpression) {
	TestObject tObj = new TestObject(id)
	tObj.addProperty("xpath", ConditionType.EQUALS, xpathExpression)
	return tObj
}

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path html = projectDir.resolve("page.html")
URL htmlURL = html.toFile().toURI().toURL()
String urlString = htmlURL.toExternalForm()

WebUI.openBrowser('')
WebUI.navigateToUrl(urlString)
WebUI.setViewPortSize(800, 600)
WebDriver driver = DriverFactory.getWebDriver()

TestObject btnTO = makeTestObject("btn", "//button[@id='btn']")
WebUI.verifyElementPresent(btnTO, 10, FailureHandling.STOP_ON_FAILURE)
WebElement btnELM = WebUI.findWebElement(btnTO, 10, FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

try {
	btnELM.click()
} catch (Exception e) {
	println "An Exceptio was thrown, caught it by try {} catch () {} : " + e.getMessage()
}
WebUI.delay(5)

WebUI.closeBrowser()