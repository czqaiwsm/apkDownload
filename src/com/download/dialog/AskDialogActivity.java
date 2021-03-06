package com.download.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.download.R;


public class AskDialogActivity extends Activity {

	public static String TAG_MESSAGE = "message";
	public static String TAG_TITLE = "title";

	private static AskDialog.OnAskDialogClickListener mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_dialog);
		Intent intent = getIntent();
		String title = intent.getStringExtra(TAG_TITLE);
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) findViewById(R.id.tv_title);
			tvTitle.setText(title);
		}
		String message = intent.getStringExtra(TAG_MESSAGE);
		if (!TextUtils.isEmpty(message)) {
			TextView tvMessage = (TextView) findViewById(R.id.tv_message);
			tvMessage.setVisibility(View.VISIBLE);
			tvMessage.setText(message);
		}
	}

	public void onBtnClick(View view) {
		if (view.getId() == R.id.btn_ok) {
			if (mListener != null) {
				mListener.onAskDialogConfirm();
			}
		} else if (view.getId() == R.id.btn_ok) {
			if (mListener != null) {
				mListener.onAskDialogCancel();
			}
		}
		mListener = null;
		this.finish();
	}

	public static void setOnAskDialogClickListener(
			AskDialog.OnAskDialogClickListener listener) {
		mListener = listener;
	}
}
