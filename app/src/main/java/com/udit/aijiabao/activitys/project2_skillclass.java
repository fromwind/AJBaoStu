package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.DbManager;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.project2_classlist)
public class project2_skillclass extends BaseActivity {
    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

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
    }

    @Override
    public void initView() {

    }
    @Override
    public void initData() {

    }
    private String urlload="file:///android_asset/";
    private String LIST01=urlload+ "anquandai.html";
    private String LIST02=urlload+"dianhuokaiguan.html";
    private String LIST03=urlload+"fangxiangpan.html";
    private String LIST04=urlload+"liheqi.html";
    private String LIST05=urlload+"jiasutaban.html";
    private String LIST06=urlload+"zhidongtaban.html";
    private String LIST07=urlload+"zhuchezhidong.html";
    private String LIST08=urlload+"zuoyitiaozheng.html";
    private String LIST09=urlload+"houshijing.html";
    private String LIST10=urlload+"jingyanjiqiao.html";
    @Event(value = {R.id.special_list01,R.id.special_list02,R.id.special_list03,R.id.special_list04,R.id.special_list05,R.id.special_list06,R.id.special_list07,R.id.special_list08,R.id.special_list09,R.id.special_list10},type = View.OnClickListener.class)
    private void mclink(View view){
        Intent intent = new Intent();

        switch (view.getId()){
            case R.id.special_list01:
                intent.putExtra("url", LIST01);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list02:
                intent.putExtra("url", LIST02);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list03:
                intent.putExtra("url", LIST03);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list04:
                intent.putExtra("url", LIST04);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list05:
                intent.putExtra("url", LIST05);
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
