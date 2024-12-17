package com.kazurayam.hack

import java.text.MessageFormat
import java.time.Duration

import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

public class StatelessWaitForElementNotClickableKeyword extends WebUIAbstractKeyword {

	@CompileStatic
	@Override
	public SupportLevel getSupportLevel(Object ...params) {
		return super.getSupportLevel(params)
	}

	@CompileStatic
	@Override
	public Object execute(Object ...params) {
		TestObject to = getTestObject(params[0])
		int timeOut = (int) params[1]
		FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
		return waitForElementNotClickable(to,timeOut,flowControl)
	}

	public boolean waitForElementNotClickable(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
		return WebUIKeywordMain.runKeyword({
			boolean isSwitchIntoFrame = false
			try {
				WebUiCommonHelper.checkTestObjectParameter(to)
				timeOut = WebUiCommonHelper.checkTimeout(timeOut)
				try {
					isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to, timeOut)
					WebDriverWait wait = new WebDriverWait(DriverFactory.getWebDriver(), Duration.ofSeconds(timeOut))
					WebElement webElement = wait.until(new ExpectedCondition<WebElement>() {
								@Override
								public WebElement apply(WebDriver driver) {
									WebElement foundElement = WebUIAbstractKeyword.findWebElement(to, timeOut)
									if (foundElement.isEnabled()) {
										return null
									} else {
										return foundElement
									}
								}
							})
					if (webElement != null) {
						logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_X_IS_NOT_CLICKABLE, to.getObjectId()))
					}
					return true
				} catch (WebElementNotFoundException e) {
					logger.logWarning(e.getMessage(), null, e)
					return false
				} catch (TimeoutException e) {
					logger.logWarning(MessageFormat.format(StringConstants.KW_MSG_OBJ_IS_CLICKABLE_AFTER_X_SEC, [to.getObjectId(), timeOut] as Object[]), null, e)
					return false
				}
			} finally {
				if (isSwitchIntoFrame) {
					WebUiCommonHelper.switchToDefaultContent()
				}
			}
		}, flowControl, RunConfiguration.getTakeScreenshotOption(), (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_WAIT_OBJ_X_TO_BE__NOTCLICKABLE, to.getObjectId())
		: StringConstants.KW_MSG_CANNOT_WAIT_FOR_OBJ_TO_BE_NOT_CLICKABLE)
	}
}
