package com.wellee.banner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initRv();
    }

    private void initRv() {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RvAdapter(initData()));
    }

    private List<List<BannerInfo>> initData() {
        List<List<BannerInfo>> bannerInfos = new ArrayList<>();
        List<BannerInfo> bannerInfo1 = new ArrayList<>();
        BannerInfo info11 = new BannerInfo("https://img0.baidu.com/it/u=1904359974,2894644924&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info12 = new BannerInfo("https://img0.baidu.com/it/u=3824886304,665215047&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info13 = new BannerInfo("https://img1.baidu.com/it/u=2961974070,137066290&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo1.add(info11);
        bannerInfo1.add(info12);
        bannerInfo1.add(info13);
        bannerInfos.add(bannerInfo1);

        List<BannerInfo> bannerInfo2 = new ArrayList<>();
        BannerInfo info21 = new BannerInfo("https://img2.baidu.com/it/u=507575223,907330772&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info22 = new BannerInfo("https://img0.baidu.com/it/u=1514254201,1610764859&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info23 = new BannerInfo("https://img1.baidu.com/it/u=3596976274,3312190045&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo2.add(info21);
        bannerInfo2.add(info22);
        bannerInfo2.add(info23);
        bannerInfos.add(bannerInfo2);

        List<BannerInfo> bannerInfo3 = new ArrayList<>();
        BannerInfo info31 = new BannerInfo("https://img2.baidu.com/it/u=4247656867,4135832390&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info32 = new BannerInfo("https://img2.baidu.com/it/u=2470716442,3652076975&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info33 = new BannerInfo("https://img0.baidu.com/it/u=480941639,3775135897&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo3.add(info31);
        bannerInfo3.add(info32);
        bannerInfo3.add(info33);
        bannerInfos.add(bannerInfo3);

        List<BannerInfo> bannerInfo4 = new ArrayList<>();
        BannerInfo info41 = new BannerInfo("https://img0.baidu.com/it/u=3938874963,3007757929&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info42 = new BannerInfo("https://img2.baidu.com/it/u=1661128524,2980783236&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info43 = new BannerInfo("https://img0.baidu.com/it/u=473071857,616192074&fm=11&fmt=auto&gp=0.jpg");
        bannerInfo4.add(info41);
        bannerInfo4.add(info42);
        bannerInfo4.add(info43);
        bannerInfos.add(bannerInfo4);

        List<BannerInfo> bannerInfo5 = new ArrayList<>();
        BannerInfo info51 = new BannerInfo("https://img0.baidu.com/it/u=1904359974,2894644924&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info52 = new BannerInfo("https://img0.baidu.com/it/u=3824886304,665215047&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info53 = new BannerInfo("https://img1.baidu.com/it/u=2961974070,137066290&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo5.add(info51);
        bannerInfo5.add(info52);
        bannerInfo5.add(info53);
        bannerInfos.add(bannerInfo5);

        List<BannerInfo> bannerInfo6 = new ArrayList<>();
        BannerInfo info61 = new BannerInfo("https://img2.baidu.com/it/u=507575223,907330772&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info62 = new BannerInfo("https://img0.baidu.com/it/u=1514254201,1610764859&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info63 = new BannerInfo("https://img1.baidu.com/it/u=3596976274,3312190045&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo6.add(info61);
        bannerInfo6.add(info62);
        bannerInfo6.add(info63);
        bannerInfos.add(bannerInfo6);

        List<BannerInfo> bannerInfo7 = new ArrayList<>();
        BannerInfo info71 = new BannerInfo("https://img2.baidu.com/it/u=4247656867,4135832390&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info72 = new BannerInfo("https://img2.baidu.com/it/u=2470716442,3652076975&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info73 = new BannerInfo("https://img0.baidu.com/it/u=480941639,3775135897&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo7.add(info71);
        bannerInfo7.add(info72);
        bannerInfo7.add(info73);
        bannerInfos.add(bannerInfo7);

        List<BannerInfo> bannerInfo8 = new ArrayList<>();
        BannerInfo info81 = new BannerInfo("https://img0.baidu.com/it/u=3938874963,3007757929&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info82 = new BannerInfo("https://img2.baidu.com/it/u=1661128524,2980783236&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info83 = new BannerInfo("https://img0.baidu.com/it/u=473071857,616192074&fm=11&fmt=auto&gp=0.jpg");
        bannerInfo8.add(info81);
        bannerInfo8.add(info82);
        bannerInfo8.add(info83);
        bannerInfos.add(bannerInfo8);

        List<BannerInfo> bannerInfo9 = new ArrayList<>();
        BannerInfo info91 = new BannerInfo("https://img0.baidu.com/it/u=1904359974,2894644924&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info92 = new BannerInfo("https://img0.baidu.com/it/u=3824886304,665215047&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info93 = new BannerInfo("https://img1.baidu.com/it/u=2961974070,137066290&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo9.add(info91);
        bannerInfo9.add(info92);
        bannerInfo9.add(info93);
        bannerInfos.add(bannerInfo9);

        List<BannerInfo> bannerInfo10 = new ArrayList<>();
        BannerInfo info101 = new BannerInfo("https://img2.baidu.com/it/u=507575223,907330772&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info102 = new BannerInfo("https://img0.baidu.com/it/u=1514254201,1610764859&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info103 = new BannerInfo("https://img1.baidu.com/it/u=3596976274,3312190045&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo10.add(info101);
        bannerInfo10.add(info102);
        bannerInfo10.add(info103);
        bannerInfos.add(bannerInfo10);

        List<BannerInfo> bannerInfo11 = new ArrayList<>();
        BannerInfo info111 = new BannerInfo("https://img2.baidu.com/it/u=4247656867,4135832390&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info112 = new BannerInfo("https://img2.baidu.com/it/u=2470716442,3652076975&fm=26&fmt=auto&gp=0.jpg");
        BannerInfo info113 = new BannerInfo("https://img0.baidu.com/it/u=480941639,3775135897&fm=26&fmt=auto&gp=0.jpg");
        bannerInfo11.add(info111);
        bannerInfo11.add(info112);
        bannerInfo11.add(info113);
        bannerInfos.add(bannerInfo11);

        List<BannerInfo> bannerInfo12 = new ArrayList<>();
        BannerInfo info121 = new BannerInfo("https://img0.baidu.com/it/u=3938874963,3007757929&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info122 = new BannerInfo("https://img2.baidu.com/it/u=1661128524,2980783236&fm=11&fmt=auto&gp=0.jpg");
        BannerInfo info123 = new BannerInfo("https://img0.baidu.com/it/u=473071857,616192074&fm=11&fmt=auto&gp=0.jpg");
        bannerInfo12.add(info121);
        bannerInfo12.add(info122);
        bannerInfo12.add(info123);
        bannerInfos.add(bannerInfo12);

        return bannerInfos;
    }
}
