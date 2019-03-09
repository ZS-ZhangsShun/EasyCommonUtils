package com.zs.easy.common.utils;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zs on 2019/3/9.
 */
public class EasyGestureListener {

    private float mPosX, mPosY, mCurPosX, mCurPosY;

    public void add(View view, final EasyGestureInterface easyGestureInterface) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCurPosY - mPosY > 0 && (Math.abs(mCurPosY - mPosY) > DensityUtil.dp2px(10))) {
                            if (easyGestureInterface != null) {
                                easyGestureInterface.onDownSlide();
                            }
                        } else if (mCurPosY - mPosY < 0 && (Math.abs(mCurPosY - mPosY) > DensityUtil.dp2px(10))) {
                            if (easyGestureInterface != null) {
                                easyGestureInterface.onUpSlide();
                            }
                        } else if (mCurPosX - mPosX < 0 && (Math.abs(mCurPosX - mPosX) > DensityUtil.dp2px(10))) {
                            if (easyGestureInterface != null) {
                                easyGestureInterface.onLeftSlide();
                            }
                        } else if (mCurPosX - mPosX > 0 && (Math.abs(mCurPosX - mPosX) > DensityUtil.dp2px(10))) {
                            if (easyGestureInterface != null) {
                                easyGestureInterface.onRightSlide();
                            }
                        }
                        break;
                }
                return true;
            }

        });
    }

    public void add(View view, final EasyGestureInterface easyGestureInterface, final int distance) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCurPosY - mPosY > 0 && (Math.abs(mCurPosY - mPosY) > DensityUtil.px2dp(distance))) {
                            if (easyGestureInterface != null) {
                                easyGestureInterface.onDownSlide();
                            }
                        } else if (mCurPosY - mPosY < 0 && (Math.abs(mCurPosY - mPosY) > DensityUtil.px2dp(distance))) {
                            if (easyGestureInterface != null) {
                                easyGestureInterface.onUpSlide();
                            }
                        } else if (mCurPosX - mPosX < 0 && (Math.abs(mCurPosX - mPosX) > DensityUtil.px2dp(distance))) {
                            if (easyGestureInterface != null) {
                                easyGestureInterface.onLeftSlide();
                            }
                        } else if (mCurPosX - mPosX > 0 && (Math.abs(mCurPosX - mPosX) > DensityUtil.px2dp(distance))) {
                            if (easyGestureInterface != null) {
                                easyGestureInterface.onRightSlide();
                            }
                        }
                        break;
                }
                return true;
            }

        });
    }
}
