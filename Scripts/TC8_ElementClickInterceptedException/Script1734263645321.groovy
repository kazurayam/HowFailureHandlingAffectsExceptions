import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * This script demonstrates how a ElementClickInterceptedException is thrown
 * 
 * https://www.browserstack.com/guide/element-click-intercepted-exception-selenium
 * 
 */
TestObject makeTestObject(String id, String xpath) {
	TestObject tObj = new TestObject(id)
	tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
	return tObj
}

TestObject username = makeTestObject('username', "//div[@id='username']")
TestObject loginBtn = makeTestObject('login-btn', "//button[@id='login-btn']")

WebUI.openBrowser('https://bstackdemo.com/signin')
WebUI.setViewPortSize(600, 800)
WebUI.verifyElementPresent(username, 10)

WebUI.click(username)
WebUI.click(loginBtn)

WebUI.delay(3)
WebUI.closeBrowser()