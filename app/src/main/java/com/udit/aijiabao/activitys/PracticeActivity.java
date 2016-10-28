package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.bigkoo.convenientbanner.ViewPagerScroller;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.adapters.PracticeAdapter;
import com.udit.aijiabao.entitys.ChapterName;
import com.udit.aijiabao.entitys.Question;
import com.udit.aijiabao.entitys.QuestionPosition;
import com.udit.aijiabao.utils.ui.VoteSubmitViewPager;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_practice_test)
public class PracticeActivity extends BaseActivity {
    public static String a;
    @ViewInject(R.id.titleView)
    private TitleView mtitleView;
    @ViewInject(R.id.vote_submit_viewpager)
    VoteSubmitViewPager viewPager;
    PracticeAdapter pagerAdapter;
    List<View> viewItems = new ArrayList<View>();
    List<Question> dataItems = new ArrayList<Question>();

    DbManager dbManager;

    @Override
    public void setContentView() {
        Intent intent = getIntent();
        a = intent.getStringExtra("style");
        Log.e("bum", "intent" + a);

        dbManager = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig1());
        switch (a) {
            case "order":
                mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
                mtitleView.setTitleText("顺序练习");
                mtitleView.hideOperate();
                mtitleView.showBack(true);
                mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
                mtitleView.setBackOnClick(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                         finish();
                    }
                });

                break;
            case "collect":
                mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
                mtitleView.setTitleText("我的收藏");
                mtitleView.hideOperate();
                mtitleView.showBack(true);
                mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
                mtitleView.setBackOnClick(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                         finish();
                    }
                });
                break;
            case "error":
                mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
                mtitleView.setTitleText("我的错题");
                mtitleView.hideOperate();
                mtitleView.showBack(true);
                mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
                mtitleView.setBackOnClick(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();
                    }
                });
                break;
            default:
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
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        initViewPagerScroll();
        new dbthread().run();
    }

    private List<Question> FindSpecial(String s) {
        List<Question> list = new ArrayList<Question>();
        try {
            list = dbManager.selector(Question.class).where("topicId", "LIKE", s + ".%").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Question> findall() {
        List<Question> list = new ArrayList<Question>();
        try {
            list = dbManager.findAll(Question.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }


    public int readPosition(String type){
        int a=0;
        switch (type){
            case "order":
                try {
                    QuestionPosition questionPosition=dbManager.findById(QuestionPosition.class,2);
                    a= questionPosition.getPosition();
                } catch (DbException e) {
                    e.printStackTrace();

                }
                break;
            case "collect":
                try {
                    QuestionPosition questionPosition=dbManager.findById(QuestionPosition.class,1);
                    a= questionPosition.getPosition();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case "error":

                try {
                    QuestionPosition questionPosition=dbManager.findById(QuestionPosition.class,3);
                    a= questionPosition.getPosition();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            default:
                switch (type){
                    case "1":
                        try {
                            ChapterName chapterName=dbManager.selector(ChapterName.class).where("chapterNum","=","0.01").findFirst();
                            a= chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "2":
                        try {
                            ChapterName chapterName=dbManager.selector(ChapterName.class).where("chapterNum","=","0.02").findFirst();
                            a= chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "3":
                        try {
                            ChapterName chapterName=dbManager.selector(ChapterName.class).where("chapterNum","=","0.03").findFirst();
                            a= chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "4":
                        try {
                            ChapterName chapterName=dbManager.selector(ChapterName.class).where("chapterNum","=","0.04").findFirst();
                            a= chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "61":
                        try {
                            ChapterName chapterName=dbManager.selector(ChapterName.class).where("chapterNum","=","0.61").findFirst();
                            a= chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "62":
                        try {
                            ChapterName chapterName=dbManager.selector(ChapterName.class).where("chapterNum","=","0.62").findFirst();
                            a= chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "63":
                        try {
                            ChapterName chapterName=dbManager.selector(ChapterName.class).where("chapterNum","=","0.63").findFirst();
                            a= chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                break;
        }
        if (dataItems.size()>a)
        return a;
        else return 0;
    }
    /**
     * @param index 根据索引值切换页面
     */
    public void setCurrentView(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private class dbthread extends Thread {
        @Override
        public void run() {
            super.run();
            switch (a) {
                case "order":
                    dataItems = findall();
                    break;
                case "error":
                    dataItems=errorDb();
                    break;
                case "collect":
                    dataItems=collectDb();
                    break;
                default:
                    dataItems = FindSpecial(a);
            }

            for (int i = 0; i < dataItems.size(); i++) {
                viewItems.add(getLayoutInflater().inflate(
                        R.layout.vote_submit_viewpager_item, null));
            }
            pagerAdapter = new PracticeAdapter(
                    PracticeActivity.this, viewItems,
                    dataItems);
            viewPager.setAdapter(pagerAdapter);
            viewPager.getParent()
                    .requestDisallowInterceptTouchEvent(false);
            Log.e("bum_setCurrentView","a="+readPosition(a));
            setCurrentView(readPosition(a));
        }
    }

    private List<Question> collectDb() {
        List<Question> list = new ArrayList<Question>();
        try {
            list = dbManager.selector(Question.class).where("collect","=",true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }
    private List<Question> errorDb() {
        List<Question> list = new ArrayList<Question>();
        try {
            list = dbManager.selector(Question.class).where("error","is not",null).findAll();
            Log.e("bum","error——size="+list.size());
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }
}