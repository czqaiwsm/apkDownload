package com.download.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.widget.Toast;
import com.apk.download.R;
import com.download.dialog.AskDialog;
import com.download.dialog.AskDialogActivity;
import com.download.base.utils.ApkDownloadConfig;
import com.download.base.utils.AppLog;

import java.io.File;

/**
 * 
 * @author
 * 
 * @version 2015-7-7
 */

public class UpdateMgr {


	private static Context mContext;
	private UpdateInfo mInfo;
	private String mApkPath = Environment.getExternalStorageDirectory().getPath() + File.separator + ApkDownloadConfig.APP_NAME + ".apk";
	private UpdateEventCallback mUpdateCallback;
	private boolean isDialoShow = false;//解决连续多次显示更新对话框的问题
	private static boolean IS_FORCE_UPDATE = false;

	public interface UpdateEventCallback {
		public void onUpdateFailEvent();

		public void onUpdateCancelEvent();

		public void onUpdateCompleteEvent();
	}

	public class onAdviceUpdateEvent implements UpdateEventCallback {

		@Override
		public void onUpdateFailEvent() {
			Toast.makeText(mContext, mContext.getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onUpdateCancelEvent() {
		}

		@Override
		public void onUpdateCompleteEvent() {
			installApk();
		}

	}

	public class onForceUpdateEvent implements UpdateEventCallback {
		@Override
		public void onUpdateFailEvent() {
			Toast.makeText(mContext, mContext.getString(R.string.update_failed), Toast.LENGTH_SHORT).show();

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000 * 2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					exitApp();
				}
			}).start();
		}

		@Override
		public void onUpdateCancelEvent() {
			exitApp();
		}

		@Override
		public void onUpdateCompleteEvent() {
			installApk();
		}
	}

	public class onSilentUpdateEvent implements UpdateEventCallback {

		@Override
		public void onUpdateFailEvent() {
		}

		@Override
		public void onUpdateCancelEvent() {
		}

		@Override
		public void onUpdateCompleteEvent() {
			AskDialogActivity.setOnAskDialogClickListener(new AskDialog.OnAskDialogClickListener() {

				@Override
				public void onAskDialogConfirm() {
					installApk();
				}

				@Override
				public void onAskDialogCancel() {
				}
			});
			Intent intent = new Intent(mContext, AskDialogActivity.class);
			intent.putExtra(AskDialogActivity.TAG_MESSAGE, mInfo.getUpdateDesc());
			mContext.startActivity(intent);
		}
	}

	public class onDownLoadUpdateEvent implements UpdateEventCallback {

		@Override
		public void onUpdateFailEvent() {
			Toast.makeText(mContext, mContext.getString(R.string.down_failed), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onUpdateCancelEvent() {

		}

		@Override
		public void onUpdateCompleteEvent() {
			Toast.makeText(mContext, mContext.getString(R.string.down_success), Toast.LENGTH_SHORT).show();
		}

	}

	public void setUpdateEventCallback(UpdateEventCallback callback) {
		this.mUpdateCallback = callback;
	}

	public static UpdateMgr getInstance(Context context) {
		mContext = context;

		return new UpdateMgr(context);
	}

	/**
	 * 检测版本更新
	 * 
	 * @param callback
	 *            ，可以为null
	 * @param autoUpdate
	 *            ,判断是否显示当前已是最新版本!
	 */
	public void checkUpdateInfo(UpdateEventCallback callback, boolean autoUpdate) {
		this.mUpdateCallback = callback;
		// TODO 后台请求获取下载地址
		PackageManager pm = mContext.getPackageManager();
		try {
			PackageInfo pInfo = pm.getPackageInfo(mContext.getPackageName(), 0);

			/*******1.start---模仿请求接口*********/
			Thread.sleep(1000);
			UpdateInfo updateInfo = new UpdateInfo();
			updateInfo.setDownloadUrl("http://sz.hylapp.com/apk/source/customer.apk");
			updateInfo.setIsForceUpdate(false);
			updateInfo.setUpdateDesc("描述");
			updateInfo.setUpdateVersion("5.0");
			Thread.sleep(1000);
			/*******1.end---模仿请求接口*********/

		    /******2.start 接口请求结束，开始下载**********/

			//todo 改变 IS_FORCE_UPDATE 的值。确定是否是强制更新
							/*
							* 1.根据后台返回数据，获得UpdateInfo对象
							*/
		    IS_FORCE_UPDATE = updateInfo.isForceUpdate();
			update(updateInfo, autoUpdate);
			/******2.end 接口请求结束，开始下载**********/

//			RequestParam param = new RequestParam();
//			HttpURL url = new HttpURL();
//			if(autoUpdate){
//				url.setmBaseUrl(Constants.UPDATE_VERSION);
//			}else {
//				url.setmBaseUrl(Constants.UPDATE_VERSION_INDEX);
//			}
//			url.setmGetParam(RequestParamConfig.VERSION2, pInfo.versionCode + "");
//			Log.i("qinxu", "UpdateMgr checkUpdateInfo url = " + url);
//			param.setmHttpURL(url);
//			param.setmParserClassName(UpdateParser.class.getName());
//			RequestManager.getRequestData(mContext, createMyReqSuccessListener(autoUpdate), createMyReqErrorListener(), param);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据接口返回结果改变IS_FORCE_UPDATE的值
	 * @return
	 */
	/*private Listener<Object> createMyReqSuccessListener(final boolean autoUpdate) {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (object instanceof UpdateResultInfo) {
					UpdateInfo info = (UpdateInfo) object;
					if (info.getmCode() == 200) {// 请求成功
						if (info.getmStates() == 1) {
							//todo 改变 IS_FORCE_UPDATE 的值。确定是否是强制更新
							*//*
							* 1.根据后台返回数据，获得UpdateInfo对象
							* *//*
							update(info, autoUpdate);
						} else {
							if (autoUpdate) {
								SmartToast.makeText(mContext, "当前已是最新版本!", Toast.LENGTH_SHORT).show();
							}
						}
					}
				}

			}
		};
	}*/

	/*private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
			}
		};
	}*/

	public void update(final UpdateInfo info, boolean autoUpdate) {
		if (info == null) {
			return;
		}
		mInfo = info;
		generateApkPath(mContext.getString(R.string.app_name), mInfo.getUpdateVersion());
		AskDialog dialog;
		dialog = new AskDialog(mContext, mContext.getString(R.string.update),mContext.getString(R.string.update_notes)+mInfo.getUpdateDesc());
		dialog.setListener(new AskDialog.OnAskDialogClickListener() {

			@Override
			public void onAskDialogConfirm() {
				startDownload(mInfo);
			}

			@Override
			public void onAskDialogCancel() {
				isDialoShow = false;
				forceExit();
			}
		});
		if (!isDialoShow) {
			dialog.show();
			isDialoShow = true;
		}

		if(IS_FORCE_UPDATE){//强制更新，对话框在点击back键时不可消失
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
		}
	}

	private UpdateMgr(Context context) {
		mContext = context;
	}

	private void startDownload(final UpdateInfo info) {
		final DownloadMgr mgr = new DownloadMgr(mContext, mContext.getString(R.string.app_name), null,IS_FORCE_UPDATE);
		mgr.setShowProgress(true);

		mgr.setListener(new DownloadMgr.DownloadListener() {

			@Override
			public void onDownloadError() {
				getUpdateCallback(info.getUpdateMode()).onUpdateFailEvent();
			}

			@Override
			public void onDownloadComplete() {
				installApk();
			}

			@Override
			public void onDownloadCancel() {
				getUpdateCallback(info.getUpdateMode()).onUpdateCancelEvent();
			}
		});
		if (!mgr.submitTask(info.getDownloadUrl(), mApkPath)) {
			getUpdateCallback(info.getUpdateMode()).onUpdateFailEvent();
		}

	}


	private void generateApkPath(String appName, String version) {
		mApkPath = Environment.getExternalStorageDirectory().getPath() + File.separator + appName + version + ".apk";
		AppLog.Logd("Fly", " mApkPath" + mApkPath);
	}

	private UpdateEventCallback getUpdateCallback(UpdateMode mode) {
		if (mUpdateCallback != null) {
			return mUpdateCallback;
		}
		switch (mode) {
		case USER_SELECT:
			return new onAdviceUpdateEvent();
		case FORCE_UPDATE:
			return new onForceUpdateEvent();
		case SILENT_UPDATE:
			return new onSilentUpdateEvent();
		case DOWNLOAD:
			return new onDownLoadUpdateEvent();
		default:
			return null;
		}
	}

	private void installApk() {
		File file = new File(mApkPath);
		if (file.exists()) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			mContext.startActivity(intent);
		}

		forceExit();
	}

	private static void exitApp() {
		Process.killProcess(Process.myPid());
		System.exit(0);
	}

	public static void forceExit(){
		if(IS_FORCE_UPDATE){
			exitApp();
		}
	}

}
