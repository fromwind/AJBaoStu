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
public class project_lawsclass extends BaseActivity {
    @ViewInject(R.id.titleView)
    private TitleView mtitleView;
    @ViewInject(R.id.special_list03)
    private RelativeLayout relativeLayout3;
    @ViewInject(R.id.special_list04)
    private RelativeLayout relativeLayout4;
    @ViewInject(R.id.special_list05)
    private RelativeLayout relativeLayout5;
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
    private String ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        Intent intent = getIntent();
        ss = intent.getStringExtra("lawsorgreen");
        Log.e("bum", "ss:" + ss);
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        if (ss.equals("laws"))
            mtitleView.setTitleText("交通法规");
        else mtitleView.setTitleText("新手上路");
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
        if (ss.equals("laws")) {
            relativeLayout4.setVisibility(View.GONE);
            relativeLayout5.setVisibility(View.GONE);
            relativeLayout6.setVisibility(View.GONE);
            relativeLayout7.setVisibility(View.GONE);
            relativeLayout8.setVisibility(View.GONE);
            relativeLayout9.setVisibility(View.GONE);
            relativeLayout10.setVisibility(View.GONE);
            textView1.setText("道路交通违法行为处理程序规定");
            textView2.setText("道路交通事故处理程序规定");
            textView3.setText("酒驾醉驾新规");
        } else {
            relativeLayout3.setVisibility(View.GONE);
            relativeLayout4.setVisibility(View.GONE);
            relativeLayout5.setVisibility(View.GONE);
            relativeLayout6.setVisibility(View.GONE);
            relativeLayout7.setVisibility(View.GONE);
            relativeLayout8.setVisibility(View.GONE);
            relativeLayout9.setVisibility(View.GONE);
            relativeLayout10.setVisibility(View.GONE);
            textView1.setText("驾照年审");
            textView2.setText("驾照申领和使用新规");
        }

    }

    @Override
    public void initData() {

    }

    private String urlload = "file:///android_asset/";
    private String LIST01 = urlload + "weifaxingwei.html";
    private String LIST02 = urlload + "shiguchuli.html";
    private String LIST03 = urlload + "jiujiazuijia.html";

    private String LIST04 = urlload + "jiazhaonianshen.html";
    private String LIST05 = urlload + "jiazhaoyishi.html";

    @Event(value = {R.id.special_list01, R.id.special_list02, R.id.special_list03}, type = View.OnClickListener.class)
    private void mclink(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.special_list01:
                if (ss.equals("laws"))
                    intent.putExtra("url", LIST01);
                else
                    intent.putExtra("url", LIST04);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list02:
                if (ss.equals("laws"))
                    intent.putExtra("url", LIST02);
                else
                    intent.putExtra("url", LIST05);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
            case R.id.special_list03:
                intent.putExtra("url", LIST03);
                intent.setClass(this, ActivityWeb.class);
                startActivity(intent);
                break;
        }
    }
}
