package com.zs.easy.common.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by zhangshun on 2019/3/8.
 */
public class AnimationUtil {

    /**
     * 渐变动画
     *
     * @param view
     * @param time
     */
    public static void alpha(View view, int time) {
        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(time);
        view.startAnimation(anim);
    }

    /**
     * 渐变动画
     *
     * @param view
     * @param time
     */
    public static void alpha(View view, int time, float formAlpha, float toAlpha) {
        AlphaAnimation anim = new AlphaAnimation(formAlpha, toAlpha);
        anim.setDuration(time);
        view.startAnimation(anim);
    }

    /**
     * 重复平移动画
     *
     * @param view
     */
    public static void startRepeatTraAni(View view, float fromX, float toX, float fromY, float toY, long duration) {
        //做动画
        Animation moveAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        moveAnimation.setDuration(duration);
        moveAnimation.setRepeatCount(Animation.INFINITE);
        view.setAnimation(moveAnimation);
    }

    /**
     * 平移动画
     *
     * @param view
     */
    public static void startTraAni(View view, float fromX, float toX, float fromY, float toY, long duration) {
        //做动画
        Animation moveAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        moveAnimation.setDuration(duration);
        view.setAnimation(moveAnimation);
    }

    /**
     * 平移动画并保持
     *
     * @param view
     */
    public static void startTraAniAndKeep(View view, float fromX, float toX, float fromY, float toY, long duration) {
        //做动画
        Animation moveAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        moveAnimation.setDuration(duration);
        moveAnimation.setFillAfter(true);
        view.setAnimation(moveAnimation);
    }

    public static void clearAnimation(View view) {
        Animation animation = view.getAnimation();
        if (animation != null) {
            animation.cancel();
        }
        view.clearAnimation();
    }

    /**
     * 缩放动画
     *
     * @param view
     * @param propertyName
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    /**
     * 位移动画
     *
     * @param view
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    /**
     * 透明度动画
     *
     * @param view
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    /**
     * 旋转动画
     * @param view
     * @param time
     * @param delayTime
     * @param values
     * @return
     */
    public static ObjectAnimator rotation(View view, long time, long delayTime, float... values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        return rotation;
    }

    /**
     * 签到添加金币的动画
     */
    public static void startSignSuccessAnim(final View view, float formY, float toY) {
        //设置控件的动画
        AnimatorSet animatorSet = new AnimatorSet();
        //缩放动画，X轴2倍缩小至0.9倍
        animatorSet.play(translationY(view, formY, toY, 500, 0))
                //缩放动画，Y轴2倍缩放至0.9倍
                .with(scale(view, "scaleY", 2f, 0.9f, 500, 0))
                //缩放动画，X轴0.9倍缩小至
                .with(scale(view, "scaleX", 2f, 0.9f, 500, 0))
                //透明动画，从1-0
                .with(alpha(view, 1, 0, 500, 0));
        //开始动画
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //当动画结束以后，需要把控件从父布局移除
                view.setVisibility(View.GONE);
            }
        });
    }
}