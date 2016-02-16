package com.download.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtils {
	private static final String SP_FILE_NAME = "hylwash";
	private static final String JOINYEAR = "join_year";
	private static final String SCHOOL = "school_name";
	private static final String DIYONG = "credit_diyong";
	private static final String RATIO = "credit_ratio";
	private static final String NAME = "userName";
	private static final String MOBILE = "phone";
	private static final String PASS = "pass";
	private static final String ID_NUM = "id_card";
	private static final String ADRESS = "address";
	private static final String POINTS = "points";
	private static final String ID = "id";
	private static final String REALNAME = "real_name";
	private static final String IS_FISRT = "is_first";
	private static SharePreferenceUtils sharePreferenceUtils = null;
	private SharedPreferences mSharePreference = null;
	private Context mContext = null;

	private static final String AREA_ID = "area_id";
	private static final String AREA_NAME = "area_name";
	private static final String AREA_PARENTID = "area_parentid";

	public static SharePreferenceUtils getInstance(Context context) {
		if (sharePreferenceUtils == null) {
			sharePreferenceUtils = new SharePreferenceUtils(
					context.getApplicationContext());
		}
		return sharePreferenceUtils;
	}

	private SharePreferenceUtils(Context context) {
		mContext = context;
		mSharePreference = mContext.getSharedPreferences(SP_FILE_NAME,
				Context.MODE_WORLD_WRITEABLE);
	}


	public synchronized void clearUser() {
		if (mSharePreference != null) {
			Editor editor = mSharePreference.edit();
			editor.remove(ID);
			editor.remove(MOBILE);
			editor.remove(SCHOOL);
			editor.remove(PASS);
			editor.remove(JOINYEAR);
			editor.remove(REALNAME);
			editor.remove(NAME);
			editor.remove(DIYONG);
			editor.remove(RATIO);
			editor.remove(ID_NUM);
			editor.remove(ADRESS);
			editor.remove(POINTS);
			editor.commit();
		}
	}


	public Boolean isFirst() {
		if (mSharePreference != null) {
			Boolean isDisplay = mSharePreference.getBoolean(IS_FISRT, true);
			return isDisplay;
		}
		return null;
	}

	public void saveIsFirst(Boolean isDisplay) {
		if (mSharePreference != null && isDisplay != null) {
			Editor editor = mSharePreference.edit();
			editor.putBoolean(IS_FISRT, isDisplay);
			editor.commit();
		}
	}


	public synchronized void clearArea() {
		if (mSharePreference != null) {
			Editor editor = mSharePreference.edit();
			editor.remove(AREA_ID);
			editor.remove(AREA_NAME);
			editor.remove(AREA_PARENTID);
			editor.commit();
		}
	}
}
