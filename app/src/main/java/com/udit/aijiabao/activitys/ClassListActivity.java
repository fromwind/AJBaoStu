package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.entitys.ChapterName;
import com.udit.aijiabao.entitys.Question;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_special_list)
public class ClassListActivity extends BaseActivity {
    @ViewInject(R.id.special_list_num1_num)
    private TextView textView1;
    @ViewInject(R.id.special_list_num2_num)
    private TextView textView2;
    @ViewInject(R.id.special_list_num3_num)
    private TextView textView3;
    @ViewInject(R.id.special_list_num4_num)
    private TextView textView4;
    @ViewInject(R.id.special_list_num5_num)
    private TextView textView5;
    @ViewInject(R.id.special_list_num6_num)
    private TextView textView6;
    @ViewInject(R.id.special_list_num7_num)
    private TextView textView7;
    @ViewInject(R.id.titleView)
    private TitleView mtitleView;
    DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("专项练习");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
        mtitleView.setBackOnClick(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();

            }
        });
        dbManager = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig1());
        try {
            textView1.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.01").findFirst().getPosition()+"/"+"600)");
            textView2.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.02").findFirst().getPosition()+"/"+"329)");
            textView3.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.03").findFirst().getPosition()+"/"+"254)");
            textView4.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.04").findFirst().getPosition()+"/"+"130)");
            textView5.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.61").findFirst().getPosition()+"/"+"196)");
            textView6.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.62").findFirst().getPosition()+"/"+"62)");
            textView7.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.63").findFirst().getPosition()+"/"+"142)");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        LoadDialog.close();
        super.onResume();
        try {
            textView1.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.01").findFirst().getPosition()+"/"+"600)");
            textView2.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.02").findFirst().getPosition()+"/"+"329)");
            textView3.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.03").findFirst().getPosition()+"/"+"254)");
            textView4.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.04").findFirst().getPosition()+"/"+"130)");
            textView5.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.61").findFirst().getPosition()+"/"+"196)");
            textView6.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.62").findFirst().getPosition()+"/"+"62)");
            textView7.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.63").findFirst().getPosition()+"/"+"142)");
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initData() {

    }
    private String SPECIAL_LIST01="1";
    private String SPECIAL_LIST02="2";
    private String SPECIAL_LIST03="3";
    private String SPECIAL_LIST04="4";
    private String SPECIAL_LIST05="61";
    private String SPECIAL_LIST06="62";
    private String SPECIAL_LIST07="63";
    @Event(value = {R.id.special_list01,R.id.special_list02,R.id.special_list03,R.id.special_list04,R.id.special_list05,R.id.special_list06,R.id.special_list07},type = View.OnClickListener.class)
    private void mclink(View view){
        Intent intent = new Intent();

        switch (view.getId()){
            case R.id.special_list01:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST01);
                intent.setClass(this, PracticeActivity.class);
                startActivity(intent);
                break;
            case R.id.special_list02:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST02);
                intent.setClass(this, PracticeActivity.class);
                startActivity(intent);
                break;
            case R.id.special_list03:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST03);
                intent.setClass(this, PracticeActivity.class);
                startActivity(intent);
                break;
            case R.id.special_list04:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST04);
                intent.setClass(this, PracticeActivity.class);
                startActivity(intent);
                break;
            case R.id.special_list05:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST05);
                intent.setClass(this, PracticeActivity.class);
                startActivity(intent);
                break;
            case R.id.special_list06:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST06);
                intent.setClass(this, PracticeActivity.class);
                startActivity(intent);
                break;
            case R.id.special_list07:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST07);
                intent.setClass(this, PracticeActivity.class);
                startActivity(intent);
                break;

        }

    }
}
