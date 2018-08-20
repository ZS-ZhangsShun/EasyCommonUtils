package com.zs.easy.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.SyncStateContract;

import com.zs.easy.common.constants.EasyConstants;

public class NetUtil {


    public static boolean isHaveNetWork(final Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            LogUtil.i(EasyConstants.TAG, "mNetworkInfo.getType() = " + mNetworkInfo.getType());
            LogUtil.i(EasyConstants.TAG, "mNetworkInfo.getDetailedState() = " + mNetworkInfo.getDetailedState());
            if (mNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            } else {
                LogUtil.i(EasyConstants.TAG, "mNetworkInfo.getType() = null" + mNetworkInfo.getType());
                LogUtil.i(EasyConstants.TAG, "mNetworkInfo.getDetailedState() = null" + mNetworkInfo.getDetailedState());
            }
        } else {
            LogUtil.i(EasyConstants.TAG, "mNetworkInfo.getType() = null");
            return false;
        }
        return false;
    }

}
