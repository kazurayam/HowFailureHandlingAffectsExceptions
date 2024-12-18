# Microsoft Account Login Procedure tricks Katalon WebUI.click keyword

-   @author kazurayam

-   @date Dec 2024

## Problem to solve

In the Katalon Community forum, there was a topic

-   ["Stale element not found, is this relate to using same Object?"](https://forum.katalon.com/t/stale-element-not-found-is-this-relate-to-using-same-object/97973)

originated by jirayu.s in Sep 2023. He wanted to develop a Test Case script in Katalon Studio that automates the login procedure in to Microsoft Account. Microsoft 365, Microsoft OneDrive, Azure DevOps --- these services require you to have a ["Microsoft Account"](https://account.microsoft.com/account). Everytime you use those cloud services, you need to pass the login procedure. If you are to create a Web UI test in Katalon Studio against those Microsoft cloud services, you must be able to automate the login procedure. jirayu.s wanted to do it, but encountered a problem: **`WebUI.click(testobject)` keyword threw a StaleElementReferenceException**. 13 people (including me, kazurayam) were involved in the topic and posted their comments. But eventually nobody could find the reason why jirayu.s’s test got a StaleElementReferenceException.

On the other hand, in Dec 2024, I made another topic

-   [Reproducing Selenium StaleElementReferenceException in Katalon Studio](https://forum.katalon.com/t/reproducing-selenium-staleelementreferenceexception-in-katalon-studio/157936/)

in this post, I argumed that some of the Katalon built-in keywords have defects that occasionally cause `StaleElementReferenceException`. The list of problematic keywords includes:

1.  `WebUI.waitForElementNotClickable(testobject, timeout)`

2.  `WebUI.waitForElementNotHasAttribute(testobject, attributeName, timeout)`

3.  `WebUI.waitForElementClickable(testobject, timeout)`

However, at that time of investigation, I could not reproduce a case where `WebUI.click(testobject)` keyword throws `StaleElementReferenceException` as jirayu.s reported.

Now, I went back to the jirayu’s topic. I had a careful look into the Microsoft Account Login procedure. Now I believe I have found out a definitive resolution to his problem. Let me explain it.

## Test Cases/MSAccountLogin\_failing --- reproducing the original issue

Let me demonstrate a Test Case that can reproduce the jirayu.s' problem.

Here I assume you have already have a Microsoft Account for you. You want to create a Execution Profile "myMicrosoftAccount" which includes 3 GlobalVariables: `ACCOUNT`, `EMAIL` and `PASSWD`.

<figure>
<img src="https://kazurayam.github.io/StaleElementReferenceExceptionReproduction/images/myMicrosoftAccount.png" alt="myMicrosoftAccount" />
</figure>

I made a Test Case script [Test Case/MSAccountLogin\_failing](https://github.com/kazurayam/StaleElementReferenceExceptionReproduction/blob/main/Scripts/MSAccountLogin_failing/Script1734339841468.groovy), which was derived from the script by jirayu.s.

    import com.kms.katalon.core.testobject.ConditionType
    import com.kms.katalon.core.testobject.TestObject
    import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

    import internal.GlobalVariable

    /**
     * Reproducing a problem reported at
     * https://forum.katalon.com/t/stale-element-not-found-is-this-relate-to-using-same-object/97973
     * 
     * WebUI.click keyword could throw StaleElementReferenceException
     * 
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
    WebUI.click(passwd)    //=>  throws a StaleElementReferenceException
    WebUI.sendKeys(passwd, GlobalVariable.PASSWD)

    TestObject signinButton = makeTestObject("SigninButton", "//button[@id='idSIButton9']")
    WebUI.waitForElementClickable(signinButton, 8)
    WebUI.click(signinButton)

    TestObject yesButton = makeTestObject("YesButton", "//button[@id='acceptButton']")
    WebUI.waitForElementClickable(yesButton, 20)
    WebUI.click(yesButton)

    WebUI.delay(3)
    WebUI.closeBrowser()

Please try running this script with the `myMicrosoftAccout` Profile applied.

The script opens a browser window, navigate to a URL

-   [https://dev.azure.com/${GlobalVariable.ACCOUNT}](https://dev.azure.com/${GlobalVariable.ACCOUNT})

As you well know, this is the entry point for Microsoft Azure DevOps. You will be redirected to a view with a title "Sign in". I will call this view as "the Sign-in view" for short.

<figure>
<img src="https://kazurayam.github.io/StaleElementReferenceExceptionReproduction/images/MsAccountLoginProcedure_1_Signin.png" alt="MsAccountLoginProcedure 1 Signin" />
</figure>

The test case script will send the value of `GlobalVariable.EMAIL` into the `<input @name='loginfmt'>` element, which will be accepted. The script will click the Next button. The browser window will transition to the view with a title "Enter password". I will call this view as "the Enter-password view" for short.

<figure>
<img src="https://kazurayam.github.io/StaleElementReferenceExceptionReproduction/images/MsAccountLoginProcedure_2_Password_SERE.png" alt="MsAccountLoginProcedure 2 Password SERE" />
</figure>

Of course, I expect the value of `GlobalVariable.PASSWD` is sent into the `<input name='passwd'>` element.

However, the script throws an StaleElementReferenceException and stops. You can have a look at a sample output in the console:

-   [console output](https://kazurayam.github.io/StaleElementReferenceExceptionReproduction/MsAccountLoginProcedure_console.txt)

You can find that a `StaleElementReferenceException` was thrown by the line \#38:

    WebUI.click(passwd)

This was a surprise for me.

## Problem analysis.

### WebUI.click keyword is a stateful keyword

I checked the source code of `WebUI.click` keyword of Katalon Studio v9.0.0:

-   [ClickKeyword source](https://kazurayam.github.io/StaleElementReferenceExceptionReproduction/10.0.0/source/com.kms.katalon.core.webui/com/kms/katalon/core/webui/keyword/builtin/ClickKeyword.groovy)

Let me dictate this source:

1.  The WebUI.click keyword internally calls a method `clickUntilSuccessWithTimeout(WebElement,long,int)` which contains a `while` loop.

2.  The loop will exit as soon as the call to `webElement.click()` finished without throwing any Exception.

3.  The loop continues untill the timeout expires (30 secs as default) while `webElement.click()` throws `ElementClickInterceptedException`.

4.  The loop terminates when `webElement.click()` throws an Exception other than `ElementClickInterceptedException`, for example a `StaleElementReferenceException`.

Thus, the `WebUI.click` keyword is implemented "stateful". It is not a "stateless" keyword. This was a surprise for me.

### The "Sign in" view also contains an invisible "Password" field as well as the "Password" view

I looked into the HTML source of the "Password view" to see how the input element for the "Password" is coded, which is as follows:

#### the Password view

    ...
        <input id="i0118" data-testid="i0118" name="passwd" placeholder="Password" type="password" maxlength="120" aria-label="Enter the password for ***********@gmail.com" aria-describedby="loginHeader " class="" autocomplete="current-password" value="" style="border-color: rgb(102, 102, 102);">
    ...

In order to select this input element, in the script, I used a XPath expression as this:

    //input[@name="passwd"]

I thought this XPath would be good enough. But in fact, it wasn’t …​ Just accidentally, I looked into the HTML source of the "Sign-in view" as well. There I found the following fragment:

#### the Sign-in view

    ...
        <input name="passwd" type="password" id="i0118"
            data-bind="moveOffScreen, textInput: passwordBrowserPrefill"
            class="moveOffScreen" tabindex="-1" aria-hidden="true">
    ...

Wow! The "Password" field was there in the "Sign-in view" as well, not only in the "Password view"!

I noticed the CSS class `moveOffScreen`. I found the `moveOffScreen` class is implemented as follows.

    .moveOffScreen {
        position:fixed; bottom:0; right:0;
        height:0 !important;
        width:0 !important;
        overflow:hidden; opacity:0; filter:alpha(opacity=0)
    }

The height is 0, the width is 0. This means, the "Password" field is present in the "Sign-in view" but is invisible.

> I have no idea why the "Sign-in view" has an invisible "Password" field. To me, it seems totally useless. Only the implementors of the Microsoft Account Login processing would know.

### Why WebUI.click keyword threw StaleElementReferenceException?

What will happen if my script tries to click a WebElement object for a `<input>` element with width: 0, hight:0? ---- A `ElementReferenceInterceptedException` will be thrown, because the element is there but invisible.

Now I should read the [`MsAccountLogin_Sign_failing`](https://github.com/kazurayam/StaleElementReferenceExceptionReproduction/blob/main/Scripts/MSAccountLogin_failing/Script1734339841468.groovy) script Line#32-38.

    32 TestObject nextButton = makeTestObject("NextButton", "//input[@id='idSIButton9']")
    33 WebUI.waitForElementClickable(nextButton, 8)
    34 WebUI.click(nextButton)
    35
    36 TestObject passwd = makeTestObject("Passwd", "//input[@name='passwd']")
    37 WebUI.waitForElementClickable(passwd, 8)
    38 WebUI.click(passwd)

At the Line#34, the script clicked the Next button. So the view will start transition from the "Sign-in view" to the "Password view". But it takes a few seconds.

I intended that the Line#37 will be enough to wait for the view transition to finish and make sure that we are on the "Password view".

But my intention failed, because the "Sign-in view" also contained a HTML element that matches with the XPath expression `//input[@name='passwd']`. The Line#37 will immediately pass while the view is still on the "Sign-in" state. The Line#37 will fire `WebUI.click(password)` on the "Sign-in view".

As I explained above, the `WebUI.click` keyword will fall into a loop while the view is on the "Sign-in view". Sooner or later the view will transition from the "Sign-in view" to the "Password view". On the "Password view", yes, there is an HTML element that matches with the XPath `//input[@name='passwd']`. However the HTML node is a newly created one. Therefore a StaleElementRefereceException will be thrown.

## Solutions

I have found out the internal mechanism how a StateElementReferenceException is thrown by `WebUI.click` keyword against the Microsoft Account Login procedure. Now I can propose a few solutions.

### A workaround: insert delay before click

How about inserting a few seconds of dumb delay just before calling `WebUI.click` so that my script can wait for the view transition from "Sign-in view" to "Password view" to finish?

I made the [Test Cases/MSAccountLogin\_passing\_with\_delay](https://github.com/kazurayam/StaleElementReferenceExceptionReproduction/blob/main/Scripts/MSAccountLogin_passing_with_delay/Script1734339327912.groovy), which contains lines as follows:

    TestObject passwd = makeTestObject("Passwd", "//input[@name='passwd']")
    WebUI.waitForElementClickable(passwd, 8)

    WebUI.delay(3)    // !important

    WebUI.click(passwd)    //=> no Exception thrown

This script passed! I could reach to my Azure DevOps console:

<figure>
<img src="https://kazurayam.github.io/StaleElementReferenceExceptionReproduction/images/MsAccountLoginProcedure_3_Success.png" alt="MsAccountLoginProcedure 3 Success" />
</figure>

### Locator to select the target element specifically

Both of the "Sign-in view" and the "Password view" have `<input name="passwd">` elements. Therefore my failing script was confused. But the two elements are coded slightly different. Escpecially, the `class` attribute can differentiate the two. It would be an idea to use an XPath expression more specific so that it matches the `<input>` node on the "Password view" only.

I made the [Test Cases/MSAccountLogin\_passing\_with\_specific\_locator](https://github.com/kazurayam/StaleElementReferenceExceptionReproduction/blob/main/Scripts/MSAccountLogin_passing_with_specific_locator/Script1734418319232.groovy), which contains lines as follows:

    TestObject passwd = makeTestObject("Passwd", "//input[@name='passwd' and @class='']")  // with more specific locator
    WebUI.waitForElementClickable(passwd, 8)
    WebUI.click(passwd)

This script also succeeded to open the Azure DevOps console.

## Conclusion

The Microsoft Account Login procedure is a terribly difficult target for Web UI automation, but I could find out a way to live with it.
