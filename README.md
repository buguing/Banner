# Banner 这是一个轮播图库
*   无限滚动不卡顿
*   可动态设置间隔时间、item切换速度等
*   手指按下图片会停下，松开后继续滚动
*   点击图片可获取当前图片position
*   Adapter设计模式

## 依赖方式
*   项目的build.gradle文件添加
    
```
allprojects {
        repositories {
            maven {url 'https://jitpack.io'}
        }
}
```
*   implementation 'com.github.buguing:Banner:v2.1.1'

## 使用方法
*   详见sample

*   BannerView.setAdapter(new BannerAdapter()); Adapter模式设置item
*   BannerView.getCurrentPosition();            获得当前图片的位置
*   BannerView.startAutoRoll();                 开始滚动轮播图
*   BannerView.stopAutoRoll();                  停止滚动轮播图


## proguard
```
    -keep public class com.wellee.libbanner.**{*;}
    -dontwarn com.wellee.libbanner.**
```