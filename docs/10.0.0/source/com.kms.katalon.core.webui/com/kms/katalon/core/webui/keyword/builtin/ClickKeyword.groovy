package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

import org.openqa.selenium.ElementClickInterceptedException
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "click")
public class ClickKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        click(to,flowControl)
    }

    private void scrollToElement(WebDriver webDriver, WebElement webElement) {
        try {
            Actions builder = new Actions(webDriver);
            builder.moveToElement(webElement);
            builder.build().perform();
        } catch(Exception e) {
            logger.logError(e.getMessage());
        }
        try {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        } catch(Exception e) {
            logger.logError(e.getMessage());
        }
    }
    @CompileStatic
    public void click(TestObject to, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                int timeout = KeywordHelper.checkTimeout(RunConfiguration.getTimeOut());
                WebElement webElement = WebUIAbstractKeyword.findWebElement(to,timeout)
                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_CLICKING_ON_OBJ, to.getObjectId()))
                clickUntilSuccessWithTimeout(webElement, timeout)
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_CLICKED, to.getObjectId()))
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_CLICK_ON_OBJ_X, to.getObjectId())
        : StringConstants.KW_MSG_CANNOT_CLICK_ON_OBJ)
    }

    private void clickUntilSuccessWithTimeout(WebElement webElement, long startTime = 0, int timeout) throws Exception {
        while (true) {
            try {
                webElement.click()
                break
            } catch (ElementClickInterceptedException exception) {
                if (startTime <= 0) {
                    startTime = System.currentTimeMillis()
                }
                long spentTimeout = (System.currentTimeMillis() - startTime) / 1000
                if (spentTimeout < timeout) {
                    logger.logDebug(MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_WAIT_UNTIL_CLICK_SUCCESS_LIMIT_TIMEOUT, timeout - spentTimeout))
                    Thread.sleep(1000)
                } else {
                    throw exception
                }
            }
        };
    }
}
