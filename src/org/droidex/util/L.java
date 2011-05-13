package org.droidex.util;

import android.util.Log;

public class L
{
	public static void d(String msg) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		Log.d("org.droidex", String.format("%s: %s", stackTrace[3].toString(), msg));
	}

	public static void e(String msg, Throwable e) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		Log.e("org.droidex", String.format("%s: %s", stackTrace[3].toString(), msg), e);
	}
}
