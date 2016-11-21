package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.project2_classlist)
public class project2_skillclass extends BaseActivity {
    @ViewInject(R.id.titleView)
    private TitleView mtitleView;
    @ViewInject(R.id.special_list06)
    private RelativeLayout relativeLayout6;
    @ViewInject(R.id.special_list07)
    private RelativeLayout relativeLayout7;
    @ViewInject(R.id.special_list08)
    private RelativeLayout relativeLayout8;
    @ViewInject(R.id.special_list09)
    private RelativeLayout relativeLayout9;
    @ViewInject(R.id.special_list10)
    private RelativeLayout relativeLayout10;
    @ViewInject(R.id.special_list_num1_txt)
    private TextView textView1;
    @ViewInject(R.id.special_list_num2_txt)
    private TextView textView2;
    @ViewInject(R.id.special_list_num3_txt)
    private TextView textView3;
    @ViewInject(R.id.special_list_num4_txt)
    private TextView textView4;
    @ViewInject(R.id.special_list_num5_txt)
    private TextView textView5;
    private String ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("学车技巧");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
        mtitleView.setBackOnClick(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();

            }
        });
        Intent intent = getIntent();
        ss=intent.getStringExtra("project");
        Log.e("bum","ss:"+ss);
    }

    @Override
    public void initView() {
        if (ss.equals("3")) {
            relativeLayout6.setVisibility(View.GONE);
            relativeLayout9.setVisibility(View.GONE);
            relativeLayout8.setVisibility(View.GONE);
            relativeLayout7.setVisibility(View.GONE);
            relativeLayout10.setVisibility(View.GONE);
            textView1.setText("车距判断");
            textView2.setText("档位操作");
            textView3.setText("灯光");
            textView4.setText("直行");
            textView5.setText("经验技巧");
        }
    }

    @Override
    public void initData() {

    }

    //科目二
    private String urlload = "file:///android_asset/";
    private String LIST01 = urlload + "anquandai.html";
    private String LIST02 = urlload + "dianhuokaiguan.html";
    private String LIST03 = urlload + "fangxiangpan.html";
    private String LIST04 = urlload + "liheqi.html";
    private String LIST05 = urlload + "jiasutaban.html";
    private String LIST06 = urlload + "zhidongtaban.html";
    private String LIST07 = urlload + "zhuchezhidong.html";
    private String LIST08 = urlload + "zuoyitiaozheng.html";
    private String LIST09 = urlload + "houshijing.html";
    private String LIST10 = urlload + "jingyanjiqiao2.html";
    //科目三
    private String LIST11 = urlload + "chejvpanduan.html";
    private String LIST12 = urlload + "dangweicaozuo.html";
    private String LIST13 = urlload + "dengguang.html";
    private String LIST14 = urlload + "zhixing.html";
    private String LIST15 = urlload + "jingyanjiqiao3.html";

    @Event(value = {R.id.special_list01, R.id.special_list02, R.id.special_list03, R.id.special_list04, R.id
            .special_list05, R.id.special_list06, R.id.special_list07, R.id.special_list08, R.id.special_list09, R.id
            .special_list10}, type = View.OnClickListener.class)
    private void mclink(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.special_list01:
                if (ss.equals("3")) intent.putExtra("url", LIST11);
                else intent.putExtra("url", LIST01);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list02:
                if (ss.equals("3")) intent.putExtra("url", LIST12);
                else intent.putExtra("url", LIST02);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list03:
                if (ss.equals("3")) intent.putExtra("url", LIST13);
                else intent.putExtra("url", LIST03);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list04:
                if (ss.equals("3")) intent.putExtra("url", LIST14);
                else intent.putExtra("url", LIST04);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list05:
                if (ss.equals("3")) intent.putExtra("url", LIST15);
                else intent.putExtra("url", LIST05);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list06:
                intent.putExtra("url", LIST06);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list07:
                intent.putExtra("url", LIST07);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list08:
                intent.putExtra("url", LIST08);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list09:
                intent.putExtra("url", LIST09);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list10:
                intent.putExtra("url", LIST10);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;

        }

    }
}
