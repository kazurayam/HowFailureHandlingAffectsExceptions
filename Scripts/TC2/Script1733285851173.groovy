import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
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

TestObject btn = makeTestObject("btn", "//button[@id='btn']")

WebUI.verifyElementPresent(btn, 10, FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.click(btn, FailureHandling.CONTINUE_ON_FAILURE)

WebUI.delay(5)

WebUI.click(btn, FailureHandling.STOP_ON_FAILURE)

WebUI.closeBrowser()