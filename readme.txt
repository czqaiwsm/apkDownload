本demo是为android强制更新apk使用


1.配置manifest：注册广播 和 相关权限

2.查看UpdateMgr类的 checkUpdateInfo方法，完善相关功能。

3.为ApkDownloadConfig的CHEKC_UPDATE_URL赋值。

4.在需要更新的地方调用如下代码
        ScreenUtils.getScreenSize(this);
        UpdateMgr.getInstance(this).checkUpdateInfo(null, false);
