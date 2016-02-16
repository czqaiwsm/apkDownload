package com.download.update;

/**
 * 版本更新类
 * 
 * @author ccs
 * 
 */
public class UpdateResultInfo {

	private int mCode = -1;// 请求结果code

	private int mStates;// 是否有更新，0表示无更新，1表示有更新

	private String mUrl;// apk下载地址

	private String mCode2;// 版本更新提示内容

	private String mData;// 更新json

	public String getmData() {
		return mData;
	}

	public void setmData(String mData) {
		this.mData = mData;
	}

	public int getmCode() {
		return mCode;
	}

	public void setmCode(int mCode) {
		this.mCode = mCode;
	}

	public int getmStates() {
		return mStates;
	}

	public void setmStates(int mStates) {
		this.mStates = mStates;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getmCode2() {
		return mCode2;
	}

	public void setmCode2(String mCode2) {
		this.mCode2 = mCode2;
	}

}
