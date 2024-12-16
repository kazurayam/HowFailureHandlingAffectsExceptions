import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * https://forum.katalon.com/t/stale-element-not-found-is-this-relate-to-using-same-object/97973
 */
TestObject makeTestObject(String id, String xpathExpression) {
	TestObject tObj = new TestObject(id)
	tObj.addProperty("xpath", ConditionType.EQUALS, xpathExpression)
	return tObj
}

assert GlobalVariable.ACCOUNT != null, "need to apply Profile/myMicrosoftAcount"

String url = "https://dev.azure.com/${GlobalVariable.ACCOUNT}"
WebUI.openBrowser('')
WebUI.setViewPortSize(680, 800)
WebUI.navigateToUrl(url)

TestObject loginfmt = makeTestObject("loginfmtText", "//input[@name='loginfmt']")
WebUI.waitForElementClickable(loginfmt, 8)
WebUI.click(loginfmt)
WebUI.sendKeys(loginfmt, GlobalVariable.EMAIL)

TestObject nextButton = makeTestObject("NextButton", "//input[@id='idSIButton9']")
WebUI.waitForElementClickable(nextButton, 8)
WebUI.click(nextButton)

TestObject passwd = makeTestObject("Passwd", "//input[@name='passwd']")
WebUI.waitForElementClickable(passwd, 8)

WebUI.delay(3)    // !important

WebUI.click(passwd)    //=>  throws a StaleElementReferenceException
WebUI.sendKeys(passwd, GlobalVariable.PASSWD)

TestObject signinButton = makeTestObject("SigninButton", "//button[@id='idSIButton9']")
WebUI.waitForElementClickable(signinButton, 8)
WebUI.click(signinButton)

TestObject yesButton = makeTestObject("YesButton", "//button[@id='acceptButton']")
WebUI.waitForElementClickable(yesButton, 20)
WebUI.click(yesButton)

WebUI.delay(10)
WebUI.closeBrowser()