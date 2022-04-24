package com.zs.easy.common.http.retrofit;

import com.zs.easy.common.constants.EasyConstants;
import com.zs.easy.common.constants.EasyVariable;
import com.zs.easy.common.utils.LogUtil;
import com.zs.easy.common.utils.NetUtil;
import com.zs.easy.common.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Subscriber基类,可以在这里处理client网络连接状况
 * （比如没有wifi，没有4g，没有联网等）
 */

public abstract class EasySubscriber<T> implements Observer<T> {

    private T curT;

    @Override
    public void onSubscribe(Disposable disposable) {
        LogUtil.i("EasySubscriber.onSubscribe()");
        //接下来可以检查网络连接等操作
        if (!NetUtil.isHaveNetWork(EasyVariable.mContext)) {
            ToastUtil.showShortToast("无网络");
            // 一定好主动调用下面这一句,取消本次Subscriber订阅
            onError(new ExceptionHandle.ResponseThrowable(new Throwable("no network"), ExceptionHandle.ERROR.NETWORD_ERROR));
            //取消订阅
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            return;
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(EasyConstants.TAG, "MySubscriber.throwable =" + e.toString());
        LogUtil.writeLogtoDefaultPath(EasyConstants.HTTP_TAG, "http error =" + e.toString());

        if (e instanceof Exception) {
            //访问获得对应的Exception
            onError(ExceptionHandle.handleException(e));
        } else {
            //将Throwable 和 未知错误的status code返回
            onError(new ExceptionHandle.ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    public abstract void onError(ExceptionHandle.ResponseThrowable responseThrowable);

    public abstract void onComplete(T t);

    @Override
    public void onComplete() {
        try {
            LogUtil.i("EasySubscriber.onComplete() -------------> ");
            onComplete(curT);
        } catch (Exception e) {
            LogUtil.e("EasySubscriber onCompleted error = " + e.toString());
        }
    }

    @Override
    public void onNext(T t) {
        LogUtil.i("EasySubscriber.onNext()");
        curT = t;
    }
}