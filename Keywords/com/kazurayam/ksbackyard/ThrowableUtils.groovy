package com.kazurayam.ksbackyard

import com.kms.katalon.core.exception.StepFailedException

public class ThrowableUtils {

	public static Throwable getCauseExceptStepFailedException(Throwable t) {
		return getCauseExcept(t, StepFailedException.class)
	}

	private static Throwable getCauseExcept(Throwable t, Class<Throwable> except) {
		assert t != null
		assert except != null
		if (t.getCause() != null) {
			Throwable cause = t.getCause()
			println "*** " + cause.getClass().getName()
			if (cause.getClass() != except) {
				return cause
			} else {
				return getCauseExcept(cause, except)
			}
		} else {
			return t
		}
	}
}
