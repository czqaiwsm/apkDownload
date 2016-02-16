package com.download.update;


/**
 * 
 * @author yuancheng
 * 
 * @version 2013-7-9 上午8:52:19
 */
public class UpdateInfo {

	private UpdateMode mUpdateMode;
	private String mUpdateVersion;
	private String mDownloadUrl;
	private String mUpdateDesc;

	private boolean isForceUpdate = false;

	public UpdateInfo() {
	}

	public void setUpdateMode(int updateMode) {
		this.mUpdateMode = UpdateMode.values()[updateMode];
	}

	public UpdateMode getUpdateMode() {
		return this.mUpdateMode;
	}

	public void setUpdateDesc(String updateInfo) {
		this.mUpdateDesc = updateInfo;
	}

	public String getUpdateDesc() {
		return mUpdateDesc;
	}

	public void setUpdateVersion(String updateVersion) {
		this.mUpdateVersion = updateVersion;
	}

	public String getUpdateVersion() {
		return mUpdateVersion;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.mDownloadUrl = downloadUrl;
	}

	public String getDownloadUrl() {
		return mDownloadUrl;
	}

	public boolean isForceUpdate() {
		return isForceUpdate;
	}

	public void setIsForceUpdate(boolean isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
		mUpdateMode = UpdateMode.USER_SELECT;
		if (isForceUpdate){
			mUpdateMode = UpdateMode.FORCE_UPDATE;
		}
	}
}
