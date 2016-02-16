package com.download.base.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	
	/**
	 * getScreen size
	 * 
	 * @param activity
	 */
	public static int SCREEN_WIDTH = 480;
	public static int SCREEN_HEIGHT = 800;
	public static float SCREEN_DENSITY = (float)1.0;
	public static void getScreenSize(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mWm.getDefaultDisplay().getMetrics(dm);
		SCREEN_WIDTH = dm.widthPixels;
		SCREEN_HEIGHT = dm.heightPixels;
		SCREEN_DENSITY = dm.density;
	}

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if("".equals(s))
			return true;
		if("null".equals(s))
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	public static boolean isInternetWorkValid(Context context) {
		if (context != null) {
			ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	// 判断是否为手机号
	public static boolean isPhone(String strPhone) {
		String str = "^1[3|4|6|7|5|8][0-9]\\d{8}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(strPhone);
		return m.matches();
	}

	public static boolean isValidUrl(String url) {
		if (!TextUtils.isEmpty(url) && !url.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static boolean isSDCardExist(Context context) {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	// 判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	public static String readTextFile(InputStream inputStream) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String str = null;
		byte buf[] = new byte[1024];

		int len;

		try {

			while ((len = inputStream.read(buf)) != -1) {

				outputStream.write(buf, 0, len);

			}
		} catch (IOException e) {
			AppLog.Loge("Fly", "IOException" + e.getMessage());
		} finally {
			try {
				if (null != outputStream) {
					str = outputStream.toString();
					outputStream.close();
					outputStream = null;
				}
				if (null != inputStream) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				AppLog.Loge("Fly", "IOException" + e.getMessage());
			}

		}
		return str;

	}

	public static long getContentLength(final Map<String, List<String>> resProperty) {
		if (null != resProperty) {
			List<String> contentLengthProperty = resProperty.get("Content-Length");
			long resFileSize = 0;
			if (contentLengthProperty != null && contentLengthProperty.size() > 0) {
				resFileSize = Long.parseLong(contentLengthProperty.get(0));
				return resFileSize;
			}
		}
		return -1;
	}

	/*
	 * public static void resizeViewHeight(final View calculatedview, final View ResizedView, final float scale){
	 * ResizedView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	 * @Override public void onGlobalLayout() { int width = calculatedview.getWidth(); int height = (int)(width*scale); LinearLayout.LayoutParams
	 * params = new LinearLayout.LayoutParams(width, height); ResizedView.setLayoutParams(params);
	 * ResizedView.getViewTreeObserver().removeGlobalOnLayoutListener(this); } }); }
	 */

	public static void resizeViewHeight(final View calculatedview, final View ResizedView, final float scale) {
		ResizedView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				int width = calculatedview.getWidth();
				int height = (int) (width * scale);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
				ResizedView.setLayoutParams(params);
				return true;

			}
		});
	}

	public static String List2String(ArrayList<String> list) {
		String string = "";
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				string += list.get(i) + ",";
			}
		}
		return string;
	}

	public static ArrayList<String> String2List(String string) {
		ArrayList<String> list = new ArrayList<String>();
		String[] strings = string.split(",");
		if (strings.length > 0) {
			int size = strings.length;
			for (int i = 0; i < size; i++) {
				if (!"".equals(strings[i])) {
					list.add(strings[i]);
				}
			}
		}
		return list;
	}

	public static void getAllList(ArrayList<?> dataList, ArrayList<?> list) {
		if (dataList != null && list != null) {
			for (int i = 0; i < dataList.size(); i++) {
				if (list.contains(dataList.get(i))) {
					dataList.remove(i);
				}
			}
		}
	}

	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine() method. We iterate until the BufferedReader return null which
		 * means there's no more data to read. Each line will appended to a StringBuilder and returned as String.
		 */
		if (is == null) {
			return "";
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static Map<String, List<String>> executeHttpHeadRequest(final String urlStr) {
		try {
			String newUrl = UrlEncode.encodeUTF8(urlStr);
			URL url = new URL(newUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15 * 1000);
			conn.setRequestMethod("HEAD");
			conn.connect();
			Map<String, List<String>> headers = conn.getHeaderFields();
			conn.disconnect();
			return headers;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * listView内嵌套listView，listView重置高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
		listView.setLayoutParams(params);
	}

	// 将BitMap转换成字节流
	public static byte[] BitMap2Byte(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArray;
	}

}
