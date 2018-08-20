package com.zs.easy.common.utils;

import java.io.File;

public interface IFileDownloadCallBack {
	public void onStart();
	public void onLoading(long count, long current);
	public void onLoading(int progress);
	public void onSuccess(File file);
	public void onFailure(Exception e);
}
