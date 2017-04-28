package com.wellee.automaticrollingpictures;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wellee.library_autorollpic.AutoRollPic;

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
