package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.entitys.ChapterName;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_special_list4)
public class ClassList4Activity extends BaseActivity {
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
    @ViewInject(R.id.special_list_num8_num)
    private TextView textView8;
    @ViewInject(R.id.special_list_num9_num)
    private TextView textView9;
    @ViewInject(R.id.special_list_num10_num)
    private TextView textView10;
    @ViewInject(R.id.special_list_num11_num)
    private TextView textView11;
    @ViewInject(R.id.special_list_num12_num)
    private TextView textView12;
    @ViewInject(R.id.special_list_num13_num)
    private TextView textView13;
    @ViewInject(R.id.special_list_num14_num)
    private TextView textView14;
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
        dbManager = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig4());
        try {
            textView1.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.39").findFirst().getPosition()+"/"+"36)");
            textView2.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.40").findFirst().getPosition()+"/"+"192)");
            textView3.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.41").findFirst().getPosition()+"/"+"215)");
            textView4.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.42").findFirst().getPosition()+"/"+"65)");
            textView5.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.43").findFirst().getPosition()+"/"+"163)");
            textView6.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.44").findFirst().getPosition()+"/"+"94)");
            textView7.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.45").findFirst().getPosition()+"/"+"35)");
            textView8.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.390").findFirst().getPosition()+"/"+"36)");
            textView9.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.400").findFirst().getPosition()+"/"+"270)");
            textView10.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.410").findFirst().getPosition()+"/"+"209)");
            textView11.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.420").findFirst().getPosition()+"/"+"103)");
            textView12.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.430").findFirst().getPosition()+"/"+"253)");
            textView13.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.440").findFirst().getPosition()+"/"+"134)");
            textView14.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.450").findFirst().getPosition()+"/"+"42)");
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
            textView1.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.39").findFirst().getPosition()+"/"+"36)");
            textView2.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.40").findFirst().getPosition()+"/"+"192)");
            textView3.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.41").findFirst().getPosition()+"/"+"215)");
            textView4.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.42").findFirst().getPosition()+"/"+"65)");
            textView5.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.43").findFirst().getPosition()+"/"+"163)");
            textView6.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.44").findFirst().getPosition()+"/"+"94)");
            textView7.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.45").findFirst().getPosition()+"/"+"35)");
            textView8.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.390").findFirst().getPosition()+"/"+"36)");
            textView9.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.400").findFirst().getPosition()+"/"+"270)");
            textView10.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.410").findFirst().getPosition()+"/"+"209)");
            textView11.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.420").findFirst().getPosition()+"/"+"103)");
            textView12.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.430").findFirst().getPosition()+"/"+"253)");
            textView13.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.440").findFirst().getPosition()+"/"+"134)");
            textView14.setText("("+dbManager.selector(ChapterName.class).where("chapterNum","=","0.450").findFirst().getPosition()+"/"+"42)");
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initData() {

    }
    private String SPECIAL_LIST01="39";
    private String SPECIAL_LIST02="40";
    private String SPECIAL_LIST03="41";
    private String SPECIAL_LIST04="42";
    private String SPECIAL_LIST05="43";
    private String SPECIAL_LIST06="44";
    private String SPECIAL_LIST07="45";
    private String SPECIAL_LIST08="390";
    private String SPECIAL_LIST09="400";
    private String SPECIAL_LIST10="410";
    private String SPECIAL_LIST11="420";
    private String SPECIAL_LIST12="430";
    private String SPECIAL_LIST13="440";
    private String SPECIAL_LIST14="450";
    @Event(value = {R.id.special_list01,R.id.special_list02,R.id.special_list03,R.id.special_list04,R.id.special_list05,R.id.special_list06,R.id.special_list07,R.id.special_list08,R.id.special_list09,R.id.special_list10,R.id.special_list11,R.id.special_list12,R.id.special_list13,R.id.special_list14},type = View.OnClickListener.class)
    private void mclink(View view){
        Intent intent = new Intent();

        switch (view.getId()){
            case R.id.special_list01:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST01);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list02:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST02);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list03:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST03);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list04:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST04);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list05:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST05);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list06:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST06);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list07:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST07);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list08:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST08);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list09:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST09);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list10:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST10);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list11:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST11);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list12:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST12);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list13:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST13);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;
            case R.id.special_list14:
                LoadDialog.showLoad(this, "正在加载...", null);
                intent.putExtra("style", SPECIAL_LIST14);
                intent.setClass(this, Practice4Activity.class);
                startActivity(intent);
                break;

        }

    }
}
