package com.zs.easy.common.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
}