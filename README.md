# EasyCommonUtils
 快速搭建项目时常用的工具类集合

## 简介

 此组件是一些自定义的公共库的集合，包括自定义工具类和UI控件两部分。

## 功能

1、时间日期转换的工具<br>
2、日志打印工具<br>
3、线程池管理工具<br>
4、网络状态工具<br>
7、Apk文件的处理工具<br>
8、Activity管理工具<br>
9、异常处理工具<br>
12、线程通信工具<br>
15、自定义提示框<br>
16、自定义ImageView支持圆形展示图片<br>

## 使用方法

### 第一步：在project的build.gradle 文件中添加JitPack依赖

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

### 第二步: 在Module的build.gradle文件中添加对本库的依赖

    dependencies {
        ...
        compile 'com.github.ZS-ZhangsShun:EasyCommonUtils:1.1'
    }


### 第三步：开始使用

#### 初始化

    在项目的Application中初始化组件如下：
    （1）组件初始化 EasyCommon.init(xxx ,xxx,xxx);

    public static void init(Context context, String spTag , boolean isPrintLog)
    三个参数分别为：Context、全局公共sp的名称、是否打印日志

#### 调用其中的工具类使用即可 例如：线程池+handler的使用
     EasyVariable.threadPoolUtil.poolExecute(new Runnable() {
             @Override
             public void run() {
                 //要执行的任务

                 //更新UI
                  MainUIHandler.handler().post(new Runnable() {
                          @Override
                          public void run() {
                              //此处代码会在主线程执行
                          }
                  });
             }
      });