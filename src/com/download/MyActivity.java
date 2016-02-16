package com.download;

import android.app.Activity;
import android.os.Bundle;
import com.download.base.utils.ScreenUtils;
import com.download.update.UpdateMgr;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ScreenUtils.getScreenSize(this);
        UpdateMgr.getInstance(this).checkUpdateInfo(null, false);

    }
}
