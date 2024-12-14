import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

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
WebUI.sendKeys(loginfmt, GlobalVariable.EMAIL)

WebUI.delay(1)

TestObject nextButton = makeTestObject("NextButton", "//input[@id='idSIButton9']")
WebUI.waitForElementClickable(nextButton, 8)
WebUI.click(nextButton)

TestObject passwd = makeTestObject("PasswdText", "//input[@name='passwd']")
WebUI.waitForElementClickable(passwd, 8)

WebUI.sendKeys(passwd, GlobalVariable.PASSWD)
WebUI.delay(1)

TestObject signinButton = makeTestObject("SigninButton", "//input[@id='idSIButton9']")
WebUI.waitForElementClickable(signinButton, 8)
WebUI.click(signinButton)

TestObject yesButton = makeTestObject("YesButton", "//input[@id='idSIButton9']")
WebUI.waitForElementClickable(signinButton, 8)
WebUI.click(signinButton)

WebUI.closeBrowser()
