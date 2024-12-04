import com.kazurayam.ksbackyard.ThrowableUtils
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

TestObject makeTestObject(String id, String xpath) {
	TestObject tObj = new TestObject(id)
	tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
	return tObj
}

WebUI.openBrowser("http://demoaut-mimic.kazurayam.com/")
TestObject validButton = makeTestObject("Make Appointment button",
	"//a[@id='btn-make-appointment']")

println "the case of CONTINUE_ON_FAILURE started"
try {
	WebUI.verifyElementNotPresent(validButton, 3, FailureHandling.CONTINUE_ON_FAILURE)
} catch (Exception e) {
	Throwable cause = ThrowableUtils.getCauseExceptStepFailedException(e)
	println(">>> the case of CONTINUE_ON_FAILURE caught an Exception: "
		+ cause.getCause().getClass().getName() + ": "+ cause.getMessage())
}
println "the case of CONTINUE_ON_FAILURE finished"

println "the case of STOP_ON_FAILURE started"
try {
	WebUI.verifyElementNotPresent(validButton, 3, FailureHandling.STOP_ON_FAILURE)
} catch (Exception e) {
	Throwable cause = ThrowableUtils.getCauseExceptStepFailedException(e)
	println(">>> the case of STOP_ON_FAILURE, caught an Exception: "
		+ cause.getCause().getClass().getName() + ": " + cause.getMessage())
}
println "the case of STOP_ON_FAILURE finished"
