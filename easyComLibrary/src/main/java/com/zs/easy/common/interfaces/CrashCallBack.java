package com.zs.easy.common.interfaces;

public interface CrashCallBack {
    void onCrash(Thread thread, Throwable throwable, String exceptionForPrint);
}
