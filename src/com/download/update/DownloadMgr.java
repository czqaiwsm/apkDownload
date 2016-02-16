package com.download.update;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import com.apk.download.R;
import com.download.base.utils.SDCardUtils;
import com.download.dialog.AskDialog;
import com.download.dialog.CustormProgressDialog;
import com.download.base.utils.AppLog;
import com.download.utils.IntentBundleKey;

public class DownloadMgr extends BroadcastReceiver implements
		CustormProgressDialog.IDownloadDialogListener {

	private CustormProgressDialog mDlg = null;
	private Context mContext = null;
	private DownloadListener mListener = null;
	private DownloadService mDownloadService = null;
	private String mUrl = null;
	private String mPath = null;
	private boolean mHasStopped = false;
	private boolean mIsShowProgress = false;
	private Intent mIntent;
	private boolean IS_FORCE_UPDATE = false;//是否强制更新


	public DownloadMgr(Context context, String title, String msg,boolean isForceUpdate) {
		mContext = context;
		mDlg = new CustormProgressDialog(mContext);
		mDlg.setTitle(title);
		mDlg.setMessage(msg);
		mDlg.setCancelable(false);
		mDlg.setDownloadDialogListener(this);
		mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				cancelTask();
				if (mListener != null) {
					mListener.onDownloadCancel();
				}
			}
		});

		mDlg.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				cancelTask();
			}
		});

		IntentFilter filter = new IntentFilter(
				DownloadService.DOWNLOAD_PROGRESS);
		filter.addAction(DownloadService.DOWNLOAD_COMPLETE);
		filter.addAction(DownloadService.DOWNLOAD_FAILED);
		filter.addAction(DownloadService.DOWNLOAD_CANCEL);
		filter.addAction(DownloadService.SDCARD_ERROE);
		mContext.registerReceiver(this, filter);
		mHasStopped = false;
		this.IS_FORCE_UPDATE = isForceUpdate;
	}

	public boolean submitTask(String url, String saveAs) {
		if (!SDCardUtils.checkSDCardStatus()) {
			Toast.makeText(mContext,
					mContext.getString(R.string.sdcardnotexits),
					Toast.LENGTH_LONG).show();
			UpdateMgr.forceExit();
			return false;
		}
		if (TextUtils.isEmpty(url)) {
			UpdateMgr.forceExit();
			return false;
		}
		if (mIsShowProgress) {
			mDlg.show();
		}
		mUrl = url;
		mPath = saveAs;
//		mDownloadService = new DownloadService();
		mIntent = new Intent(mContext,DownloadService.class);
		AppLog.Logd("Fly", "refeni" + mContext);
		mIntent.putExtra(IntentBundleKey.URL, mUrl);
		mIntent.putExtra(IntentBundleKey.PATH, mPath);
//		mContext.startService(mIntent);
		mContext.bindService(mIntent, conn, Context.BIND_AUTO_CREATE);
		return true;
	}

	public void cancelTask() {
		if (!mHasStopped) {
			// mContext.unbindService(conn);
			if(mContext != null && mIntent != null){
//				mContext.stopService(mIntent);
				mContext.unbindService(conn);
				mContext.unregisterReceiver(this);
			}
			mHasStopped = true;
		}
	}

	public void setShowProgress(boolean showProgress) {
		this.mIsShowProgress = showProgress;
	}

	public void setListener(DownloadListener listener) {
		mListener = listener;
	}

	private void dismissDlg() {
		if (mDlg != null && mDlg.isShowing()) {
			mDlg.dismiss();
			mDlg = null;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equalsIgnoreCase(DownloadService.DOWNLOAD_PROGRESS)) {
			int progress = intent.getIntExtra("progress", 0);
			mDlg.setProgress((int) progress);
		} else if (action.equalsIgnoreCase(DownloadService.DOWNLOAD_COMPLETE)) {
			// mContext.unbindService(conn);
			cancelTask();
			dismissDlg();
			if (mListener != null) {
				mListener.onDownloadComplete();
			}
		} else if (action.equalsIgnoreCase(DownloadService.DOWNLOAD_FAILED)) {
			cancelTask();
			dismissDlg();
			if (mListener != null) {
				mListener.onDownloadError();
			}
		} else if (action.equalsIgnoreCase(DownloadService.SDCARD_ERROE)) {
			cancelTask();
			dismissDlg();
			if (mListener != null) {
				mListener.onDownloadError();
			}
		} else if (action.equalsIgnoreCase(DownloadService.DOWNLOAD_CANCEL)) {
			if (mDownloadService != null) {
				mDownloadService.cancel(mUrl);
			}
			cancelTask();
			dismissDlg();
			if (mListener != null) {
				mListener.onDownloadCancel();
			}
		}
	}

	public static interface DownloadListener {
		public void onDownloadComplete();

		public void onDownloadCancel();

		public void onDownloadError();
	}

	@Override
	public void onKeyBack() {
		if(IS_FORCE_UPDATE){//强制更新，不能取消
			return;
		}
		if (mPath.contains("apk")) {
			AskDialog askDlg = new AskDialog(mContext,
					mContext.getString(R.string.app_name),
					mContext.getString(R.string.update_if_cancel));
			askDlg.setListener(new AskDialog.OnAskDialogClickListener() {
				@Override
				public void onAskDialogConfirm() {
					if (mDownloadService != null) {
						mDownloadService.cancel(mUrl);
					}
					dismissDlg();
					cancelTask();
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (mListener != null) {
						mListener.onDownloadCancel();
					}
				}

				@Override
				public void onAskDialogCancel() {

				}
			});
			askDlg.show();
		} else {
			AskDialog askDlg = new AskDialog(mContext,
					mContext.getString(R.string.app_name),
					mContext.getString(R.string.down_if_cancel));
			askDlg.setListener(new AskDialog.OnAskDialogClickListener() {
				@Override
				public void onAskDialogConfirm() {
					if (mDownloadService != null) {
						mDownloadService.cancel(mUrl);
					}
					dismissDlg();
					cancelTask();
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (mListener != null) {
						mListener.onDownloadCancel();
					}
				}

				@Override
				public void onAskDialogCancel() {

				}
			});
			askDlg.show();
		}

	}

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mDownloadService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mDownloadService = ((DownloadService.DownloadBinder) service).getService();
			mDownloadService.submit(mUrl, mPath, false);
		}
	};
}
