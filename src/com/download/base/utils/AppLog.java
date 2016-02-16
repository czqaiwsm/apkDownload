package com.download.base.utils;

import android.util.Log;

public class AppLog {
	private static final String TAG = "hylapp";
	public static final boolean DEBUG = true;

	public static final void Logv(String content) {
		Logv(TAG, content);
	}

	public static final void Logv(String tag, String content) {
		if (DEBUG) {
			Log.v(tag, content);
		}
	}

	public static final void Loge(String content) {
		Loge(TAG, content);
	}

	public static final void Loge(String tag, String content) {
		if (DEBUG) {
			Log.e(tag, content);
		}
	}

	public static final void Logi(String content) {
		Logi(TAG, content);
	}

	public static final void Logi(String tag, String content) {
		if (DEBUG) {
			Log.i(tag, content);
		}
	}

	public static final void Logw(String content) {
		Logw(TAG, content);
	}

	public static final void Logw(String tag, String content) {
		if (DEBUG) {
			Log.w(tag, content);
		}
	}

	public static final void Logd(String content) {
		Logd(TAG, content);
	}

	public static final void Logd(String tag, String content) {
		if (DEBUG) {
			Log.d(tag, content);
		}
	}

}