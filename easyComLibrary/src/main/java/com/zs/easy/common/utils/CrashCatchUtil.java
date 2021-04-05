package com.zs.easy.common.utils;

import android.os.Handler;
import android.os.Looper;

import com.zs.easy.common.constants.EasyVariable;
import com.zs.easy.common.interfaces.CrashCallBack;

public class CrashCatchUtil {
    public static void init() {
        init(null, true);
    }

    public static void init(boolean showExceptionToast) {
        init(null, showExceptionToast);
    }

    public static void init(final CrashCallBack callBack, final boolean showExceptionToast) {
        CrashException.install(new CrashException.ExceptionHandler() {
            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException
            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("version:" + CoreAPPUtil.getUtilInstance(EasyVariable.mContext).getVersionName() + "\n");

                            //============== Cause 信息 =================
                            Throwable cause = throwable.getCause();
                            if (cause != null) {
                                sb.append("Cause by " + cause.toString() + "\n");
                                StackTraceElement[] stackTrace = cause.getStackTrace();
                                if (stackTrace.length > 0) {
                                    appendStackElements(sb, stackTrace);
                                }
                            } else {
                                sb.append("Cause info is empty \n");
                            }

                            //=================  堆栈信息 =================
                            StackTraceElement[] stackElements = throwable.getStackTrace();
                            sb.append(throwable.toString() + "\n");
                            appendStackElements(sb, stackElements);

                            //=================  更多可能的信息 =================
                            Throwable[] suppressed = new Throwable[0];
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                suppressed = throwable.getSuppressed();
                            }
                            if (suppressed.length > 0) {
                                for (int i = 0; i < suppressed.length; i++) {
                                    sb.append(suppressed[i].toString() + "\n");
                                    StackTraceElement[] stackTrace1 = suppressed[i].getStackTrace();
                                    appendStackElements(sb, stackTrace1);

                                    Throwable causeSuppress = suppressed[i].getCause();
                                    if (causeSuppress == null) {
                                        continue;
                                    }
                                    sb.append("Suppressed Cause by " + causeSuppress.toString() + "\n");
                                    StackTraceElement[] stackTraceSuppress = causeSuppress.getStackTrace();
                                    if (stackTraceSuppress.length > 0) {
                                        appendStackElements(sb, stackTraceSuppress);
                                    }
                                }
                            } else {
                                sb.append("Suppressed info is empty \n");
                            }
                            sb.append("============= Exception print finish ! ============= \n");
                            final String finalExceptionInfo = sb.toString();
                            LogUtil.forceWriteLogtoDefaultPath("============= Exception ============ ", finalExceptionInfo);
                            LogUtil.i("============= Exception ============  \n" + finalExceptionInfo);

                            if (callBack != null) {
                                callBack.onCrash(thread, throwable, finalExceptionInfo);
                            }
                            if (showExceptionToast) {
                                ToastUtil.showLongToast("出现异常：\n" + finalExceptionInfo);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    /**
     * 拼接异常节点信息
     *
     * @param sb
     * @param stackElements
     */
    private static void appendStackElements(StringBuilder sb, StackTraceElement[] stackElements) {
        for (int i = 0; i < stackElements.length; i++) {
            sb.append("at "
                    + stackElements[i].getClassName()
                    + "."
                    + stackElements[i].getMethodName()
                    + "("
                    + stackElements[i].getFileName()
                    + ":"
                    + stackElements[i].getLineNumber()
                    + ")");
            sb.append("\n");
        }
    }
}
