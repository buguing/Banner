# AutomaticRollingPictures 这是一个轮播图库
*   无限滚动  
*   间隔时间自定义传入
*   手指按下图片会停下，松开后继续滚动
*   点击图片可获取当前图片position
*   RxJava控制滚动
*   Glide加载图片

![有标题](http://image18-c.poco.cn/mypoco/myphoto/20170503/09/18583790320170503095134087_640.jpg?1152x2048_120)
![无标题](http://image18-c.poco.cn/mypoco/myphoto/20170503/09/1858379032017050309514206_640.jpg?1152x2048_120)

## 依赖方式
1. maven仓库
*   项目的build.gradle文件添加
    
```
allprojects {
        repositories {
            jcenter()
            maven {url 'https://jitpack.io'}
        }
}
```
*   然后在主module里添加dependencies（compile 'com.github.BrightWade:AutomaticRollingPictures:v1.0.0'）
```
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:25.3.0'
    compile 'com.android.support.constraint:constraint-layout:1.0.2'
    testCompile 'junit:junit:4.12'

    compile 'com.github.BrightWade:AutomaticRollingPictures:v1.0.0'
}
```

2. 依赖module
*   download项目，复制module"library-autorollpic"到自己的项目，并在主module里添加dependencies
```
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:25.3.0'
    compile 'com.android.support.constraint:constraint-layout:1.0.2'
    testCompile 'junit:junit:4.12'

    compile project(':library-autorollpic')
}
```

## 使用方法
1. layout文件中
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.wellee.automaticrollingpictures.MainActivity">

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="200dp">

        <com.wellee.library_autorollpic.AutoRollPic
            android:id="@+id/arp"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            app:defaultPicture="@drawable/default_picture"
            app:dotSelector="@drawable/arp_dot_bg_selector"
            app:dotSize="@dimen/dimen_10"
            app:dotSpace="@dimen/dimen_10"
            app:hideTitle="false"
            app:indicatorBackground="#60000000"
            app:interval="5000"
            app:textColor="#ffffff"
            app:textSize="@dimen/textSize_18"
            android:visibility="gone">
        </com.wellee.library_autorollpic.AutoRollPic>

        <com.wellee.library_autorollpic.AutoRollPic
            android:id="@+id/arp_no_title"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            app:defaultPicture="@drawable/default_picture"
            app:dotSelector="@drawable/arp_dot_bg_selector"
            app:dotSize="@dimen/dimen_10"
            app:dotSpace="@dimen/dimen_10"
            app:hideTitle="true"
            app:interval="5000"
            android:visibility="gone">
        </com.wellee.library_autorollpic.AutoRollPic>
    </FrameLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:gravity="center_horizontal"
        android:orientation="horizontal">

        <Button
            android:id="@+id/btn_local"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="本地图片"
            android:textColor="#333333"
            android:textSize="@dimen/textSize_18" />

        <Button
            android:id="@+id/btn_net"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="网络加载"
            android:textColor="#333333"
            android:textSize="@dimen/textSize_18" />
    </LinearLayout>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp"
        android:gravity="center_horizontal"
        android:text="需要在你的manifest文件里添加相关权限"
        android:textColor="#666666"
        android:textSize="@dimen/textSize_18" />

</LinearLayout>
```
*   自定义属性在AutoRollPic中有标注

2 Activity中
```
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] urls = {"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493288083474&di=a073a53dfe8f64bceb03a70f773ec8b8&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2015%2Flcx%2F1%2F21%2F1%2F8.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493288083476&di=7d5e19e2e851476f6db06aed56068e91&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2013%2Fmxy%2F10%2F12%2F1%2F16.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493288083503&di=0808c38a79cd2c2af663cf1cefc13959&imgtype=0&src=http%3A%2F%2Ffile01.16sucai.com%2Fd%2Ffile%2F2013%2F0925%2Fe9f9cb4907dc0eacaa84a6c4c6123fcf.jpg"};
    private String[] titles = {"一入Java深似海，从此妹纸是路人", "曾经沧海难为水", "除却巫山不是云"};
    private Integer[] resArr = {R.drawable.earth, R.drawable.little, R.drawable.android};
    private AutoRollPic arp;
    private AutoRollPic arpNoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arp = (AutoRollPic) findViewById(R.id.arp);
        arpNoTitle = (AutoRollPic) findViewById(R.id.arp_no_title);
        arp.setOnClickListener(this);
        arpNoTitle.setOnClickListener(this);
        arp.setData(urls, titles);
        arp.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_local).setOnClickListener(this);
        findViewById(R.id.btn_net).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_net:
                arpNoTitle.setVisibility(View.GONE);
                arp.setData(urls, titles);
                arp.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_local:
                arp.setVisibility(View.GONE);
                arpNoTitle.setData(resArr, null);
                arpNoTitle.setVisibility(View.VISIBLE);
                break;
            case R.id.arp:
                Toast.makeText(MainActivity.this, "position:" + arp.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.arp_no_title:
                Toast.makeText(MainActivity.this, "position:" + arp.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arp != null) {
            arp.startAutoRoll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (arp != null) {
            arp.stopAutoRoll();
        }
    }
}
```
*   AutoRollPic.setData(urlsOrRes,titles);
*   参数一：传入图片的url或者本地资源列表或数组
*   参数二：传入title，不需要可传null
*   AutoRollPic.getCurrentPosition()可获得当前图片的位置
*   AutoRollPic.startAutoRoll()开始滚动轮播图
*   AutoRollPic.stopAutoRoll()停止滚动轮播图
