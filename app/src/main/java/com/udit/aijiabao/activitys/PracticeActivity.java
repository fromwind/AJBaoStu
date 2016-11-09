package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
    /**
     * 动画控件
     */
    @ViewInject(R.id.loading)
    private ImageView mLoading;

    /**
     * 数据加载动画
     */
    private AnimationDrawable mLoadingAinm;
    @ViewInject(R.id.vote_submit_relative)
    public RelativeLayout relativeLayout;
    public static String a;
    public static int pos;
    public static int po=111;
    public static int allnum;
    public static int num = 100;
    public static boolean isNext = true;
    public boolean isfirst = true;
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
        //Log.e("bum", "intent" + a);
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
        pos = readPosition(a);
        mLoading.setBackgroundResource(R.drawable.anim);
        mLoadingAinm = (AnimationDrawable) mLoading.getBackground();
        initViewPagerScroll();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mLoading.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        mLoadingAinm.start();
        mhandler.sendEmptyMessageDelayed(2,10);
        mhandler.sendEmptyMessageDelayed(1, 1500);
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mLoading.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    mLoadingAinm.stop();
                    break;
                case 2:
                    new cdThread().run();
                    break;

            }
        }
    };

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    public int readPosition(String type) {
        int a = 1;
        switch (type) {
            case "order":
                try {
                    QuestionPosition questionPosition = dbManager.findById(QuestionPosition.class, 2);
                    a = questionPosition.getPosition();
                } catch (DbException e) {
                    e.printStackTrace();

                }
                break;
            case "collect":
                try {
                    QuestionPosition questionPosition = dbManager.findById(QuestionPosition.class, 1);
                    a = questionPosition.getPosition();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case "error":

                try {
                    QuestionPosition questionPosition = dbManager.findById(QuestionPosition.class, 3);
                    a = questionPosition.getPosition();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            default:
                switch (type) {
                    case "1":
                        try {
                            ChapterName chapterName = dbManager.selector(ChapterName.class).where("chapterNum", "=",
                                    "0.01").findFirst();
                            a = chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "2":
                        try {
                            ChapterName chapterName = dbManager.selector(ChapterName.class).where("chapterNum", "=",
                                    "0.02").findFirst();
                            a = chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "3":
                        try {
                            ChapterName chapterName = dbManager.selector(ChapterName.class).where("chapterNum", "=",
                                    "0.03").findFirst();
                            a = chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "4":
                        try {
                            ChapterName chapterName = dbManager.selector(ChapterName.class).where("chapterNum", "=",
                                    "0.04").findFirst();
                            a = chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "61":
                        try {
                            ChapterName chapterName = dbManager.selector(ChapterName.class).where("chapterNum", "=",
                                    "0.61").findFirst();
                            a = chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "62":
                        try {
                            ChapterName chapterName = dbManager.selector(ChapterName.class).where("chapterNum", "=",
                                    "0.62").findFirst();
                            a = chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "63":
                        try {
                            ChapterName chapterName = dbManager.selector(ChapterName.class).where("chapterNum", "=",
                                    "0.63").findFirst();
                            a = chapterName.getPosition();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                break;
        }
        if (a == 0)
            return 1;
        else return a;
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

    public class cdThread extends Thread {
        @Override
        public void run() {
            super.run();
            initdata(isNext);
        }
    }

    public void initdata(boolean isNext) {
        dataItems.clear();
        viewItems.clear();
        switch (a) {
            case "order":
                dataItems = findpos(pos, num);
                break;
            case "error":
                dataItems = errorDb();
                break;
            case "collect":
                dataItems = collectDb();
                break;
            default:
                dataItems = FindSpecial(a,pos,num);
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
        Log.e("bum_", "pos:"+pos+"po:"+po+"num:"+num);
        if (isfirst) {
            setCurrentView(0);
            isfirst = false;
        } else {
            if (isNext) {
                setCurrentView(0);
                Log.e("bum_setCurrentView", "=0");
            } else {
                if (po<num){
                    setCurrentView(po-2);
                }else {
                    setCurrentView(num - 1);
                }
            }
        }

    }

    private List<Question> FindSpecial(String s, int pos, int num) {
        List<Question> list = new ArrayList<Question>();
        try {
            allnum = dbManager.selector(Question.class).where("topicId", "LIKE", s + ".%").findAll().size();
            List<Question> list1 = new ArrayList<Question>();
            Question question;
            list1 = dbManager.selector(Question.class).where("topicId", "LIKE", s + ".%").findAll();
            for (int a = pos; a < pos + num; a++) {
                if (a <= allnum) {
                    question = list1.get(a-1);
                    list.add(question);
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Question> findpos(int pos, int num) {
        List<Question> list = new ArrayList<Question>();
        Question question;
        try {
            allnum = dbManager.findAll(Question.class).size();
            for (int a = pos; a < pos + num; a++) {
                if (a <=allnum) {
                    question = dbManager.findById(Question.class, a);
                    list.add(question);
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Question> collectDb() {
        List<Question> list = new ArrayList<Question>();
        try {
            list = dbManager.selector(Question.class).where("collect", "=", true).findAll();

            allnum = dbManager.selector(Question.class).where("collect", "=", true).findAll().size();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Question> errorDb() {
        List<Question> list = new ArrayList<Question>();
        try {
            list = dbManager.selector(Question.class).where("error", "is not", null).findAll();

            allnum = dbManager.selector(Question.class).where("error", "is not", null).findAll().size();
            Log.e("bum", "error——size=" + list.size());
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

}