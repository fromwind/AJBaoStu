package com.udit.aijiabao.activitys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ViewPagerScroller;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.adapters.RealPractice4Adapter;
import com.udit.aijiabao.adapters.RealPracticeAdapter;
import com.udit.aijiabao.dialog.PromptDialog;
import com.udit.aijiabao.entitys.Question;
import com.udit.aijiabao.entitys.QuestionPosition;
import com.udit.aijiabao.entitys.RealTest;
import com.udit.aijiabao.utils.ui.VoteSubmitViewPager;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_practice_test)
public class RealPractice4Activity extends BaseActivity {

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;
    @ViewInject(R.id.vote_submit_viewpager)
    VoteSubmitViewPager viewPager;

    RealPractice4Adapter pagerAdapter;
    List<View> viewItems = new ArrayList<View>();
    List<RealTest> dataItems = new ArrayList<RealTest>();
    private int errortopicNums;// 错题数
    private String errorMsg = "";

    Dialog builderSubmit;

    Timer timer;
    TimerTask timerTask;
    int minute = 30;
    int second = 2;

    boolean isPause = false;
    int isFirst;

    DbManager dbManager;

    @Override
    public void setContentView() {

        dbManager = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig4());

        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("全真模拟");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
        mtitleView.setOperateText("30:00");
        mtitleView.setBackOnClick(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isPause = true;
                showTimeOutDialog(true, "1");
                Message msg = new Message();
                msg.what = 0;
                handlerStopTime.sendMessage(msg);
            }
        });
    }

    @Override
    public void initData() {
        initViewPagerScroll();
    }

    @Override
    public void initView() {
        int position = readPosition();
        Log.e("bum", "判断是否有position>1_：" + position);
        if (position > 0) {
            showNoticeDialog();
        } else {
            clearRealTest();
            int s = 100;//题数
            dataItems = randomDb(s);
            for (int i = 0; i < dataItems.size(); i++) {
                viewItems.add(getLayoutInflater().inflate(
                        R.layout.vote_submit_viewpager_item, null));
            }
            pagerAdapter = new RealPractice4Adapter(
                    RealPractice4Activity.this, viewItems,
                    dataItems);
            viewPager.setAdapter(pagerAdapter);
            viewPager.getParent()
                    .requestDisallowInterceptTouchEvent(false);
        }
    }

    private void showNoticeDialog() {
        // 构造对话框
        PromptDialog.Builder builder = new PromptDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage1("是否继续上次未完成的模拟");
        builder.setButton1("取消", new PromptDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                clearRealTest();
                int s = 100;//题数
                dataItems = randomDb(s);
                for (int i = 0; i < dataItems.size(); i++) {
                    viewItems.add(getLayoutInflater().inflate(
                            R.layout.vote_submit_viewpager_item, null));
                }
                pagerAdapter = new RealPractice4Adapter(
                        RealPractice4Activity.this, viewItems,
                        dataItems);
                viewPager.setAdapter(pagerAdapter);
                viewPager.getParent()
                        .requestDisallowInterceptTouchEvent(false);
                dialog.dismiss();
            }
        });
        builder.setButton2TextColor(getResources().getColor(R.color.white));
        builder.setButton2("继续", new PromptDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                dataItems = readRealtest();
                for (int i = 0; i < dataItems.size(); i++) {
                    viewItems.add(getLayoutInflater().inflate(
                            R.layout.vote_submit_viewpager_item, null));
                }
                pagerAdapter = new RealPractice4Adapter(
                        RealPractice4Activity.this, viewItems,
                        dataItems);
                viewPager.setAdapter(pagerAdapter);
                viewPager.getParent()
                        .requestDisallowInterceptTouchEvent(false);
                setCurrentView(readPosition());
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private List<RealTest> readRealtest() {
        List<RealTest> list = new ArrayList<RealTest>();
        try {
            list = dbManager.selector(RealTest.class).findAll();
            Log.e("bum", "realTest" + list.size());
        } catch (DbException e) {
            e.printStackTrace();
        }
        for (RealTest realTest : list) {
            Log.e("bum", "realTest" + realTest.toString());
        }
        return list;
    }

    private List<RealTest> randomDb(int a) {
        Log.e("bum_","random_run!");
        List<RealTest> list = new ArrayList<RealTest>();
        int size = 1714;
        int[] aa = new int[a];
        for (int i = 0; i < a; i++) {
            Random rand = new Random();
            int randNum = rand.nextInt(size);
            aa[i] = randNum;
            for (int j = 0; j < i; j++) {
                if (randNum == aa[j]) {
                    i--;
                    break;
                }
            }
        }
        Arrays.sort(aa);
        for (int k = 0; k < a; k++) {
            Question question = null;
            try {
                question = dbManager.findById(Question.class, aa[k]);
            } catch (DbException e) {
                e.printStackTrace();
            }
            RealTest realTest = new RealTest(question);
            list.add(realTest);
        }
        for (RealTest re : list) {
            try {
                dbManager.saveOrUpdate(re);
            } catch (DbException e) {
                e.printStackTrace();
            }
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

    /**
     * @param index 根据索引值切换页面
     */
    public void setCurrentView(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        stopTime();
        minute = -1;
        second = -1;
        super.onDestroy();
    }

    // 提交试卷
    public void uploadExamination(int errortopicNum) {
        // TODO Auto-generated method stub
        String resultlist = "[";
        errortopicNums = errortopicNum;
        Message msg = handlerSubmit.obtainMessage();
        msg.what = 1;
        handlerSubmit.sendMessage(msg);

    }

    protected void showBackDialog() {
        final Dialog builder = new Dialog(this, R.style.DialogStyle);
        builder.setContentView(R.layout.my_dialog);
        TextView title = (TextView) builder.findViewById(R.id.dialog_title);
        TextView content = (TextView) builder.findViewById(R.id.dialog_content);
        content.setText("您要结束本次模拟答题吗？");
        final Button confirm_btn = (Button) builder
                .findViewById(R.id.dialog_sure);
        Button cancel_btn = (Button) builder.findViewById(R.id.dialog_cancle);

        confirm_btn.setText("退出");
        cancel_btn.setText("继续答题");

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                finish();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();

            }
        });
        builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                return true;
            }
        });
        builder.show();
    }

    // 弹出对话框通知用户答题时间到
    protected void showTimeOutDialog(final boolean flag, final String backtype) {
        final Dialog builder = new Dialog(this, R.style.DialogStyle);
        builder.setContentView(R.layout.my_dialog);
        TextView title = (TextView) builder.findViewById(R.id.dialog_title);
        TextView content = (TextView) builder.findViewById(R.id.dialog_content);
        if (backtype.equals("0")) {
            content.setText("您的答题时间结束,是否提交试卷?");
        } else if (backtype.equals("1")) {
            content.setText("您要结束本次模拟答题吗？");
        } else {
            content.setText(errorMsg + "");
        }
        final Button confirm_btn = (Button) builder
                .findViewById(R.id.dialog_sure);
        Button cancel_btn = (Button) builder.findViewById(R.id.dialog_cancle);
        if (backtype.equals("0")) {
            confirm_btn.setText("提交");
            cancel_btn.setText("退出");
        } else if (backtype.equals("1")) {
            confirm_btn.setText("退出");
            cancel_btn.setText("继续答题");
        } else {
            confirm_btn.setText("确定");
            cancel_btn.setVisibility(View.GONE);
        }
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backtype.equals("0")) {
                    builder.dismiss();
                    uploadExamination(pagerAdapter.errorTopicNum());
                } else {
                    builder.dismiss();
                    finish();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backtype.equals("0")) {
                    finish();
                    builder.dismiss();
                } else {
                    isPause = false;
                    builder.dismiss();
                    Message msg = new Message();
                    msg.what = 1;
                    handlerStopTime.sendMessage(msg);
                }
            }
        });
        builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                return flag;
            }
        });
        builder.show();
    }

    // 弹出对话框通知用户提交成功
    protected void showSubmitDialog() {
        builderSubmit = new Dialog(this, R.style.DialogStyle);
        builderSubmit.setContentView(R.layout.my_dialog);
        TextView title = (TextView) builderSubmit.findViewById(R.id.dialog_title);
        TextView content = (TextView) builderSubmit.findViewById(R.id.dialog_content);
        content.setText("提交成功，感谢您的参与!");
        final Button confirm_btn = (Button) builderSubmit
                .findViewById(R.id.dialog_sure);
        confirm_btn.setVisibility(View.GONE);
        Button cancel_btn = (Button) builderSubmit.findViewById(R.id.dialog_cancle);
        cancel_btn.setVisibility(View.GONE);
        builderSubmit.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
        builderSubmit.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                return true;
            }
        });
        builderSubmit.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            isPause = true;
            showTimeOutDialog(true, "1");
            Message msg = new Message();
            msg.what = 0;
            handlerStopTime.sendMessage(msg);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        Message msg = new Message();
        msg.what = 0;
        handlerStopTime.sendMessage(msg);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Message msg = new Message();
        msg.what = 1;
        handlerStopTime.sendMessage(msg);
        super.onResume();
    }

    private void startTime() {
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    handlerTime.sendMessage(msg);
                }
            };
        }
        if (timer != null && timerTask != null) {
            timer.schedule(timerTask, 0, 1000);
        }
    }

    private void stopTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void clearRealTest() {
        try {
            dbManager.delete(RealTest.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            QuestionPosition que = dbManager.findById(QuestionPosition.class, 4);
            que.setPosition(0);
            dbManager.update(que);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private int readPosition() {
        int a = 0;
        try {
            a = dbManager.findById(QuestionPosition.class, 4).getPosition();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return a;
    }

    private Handler handlerSubmit = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showSubmitDialog();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            builderSubmit.dismiss();
                            finish();
                        }
                    }, 3000);
                    break;
                default:
                    break;
            }
        }
    };

    Handler handlerTime = new Handler() {
        public void handleMessage(Message msg) {
            // 判断时间快到前2分钟字体颜色改变
            if (minute < 1) {
                mtitleView.setOperateTextColor(Color.RED);
            } else {
                mtitleView.setOperateTextColor(Color.WHITE);
            }
            if (minute == 0) {
                if (second == 0) {
                    isFirst += 1;
                    // 时间到
                    if (isFirst == 1) {
                        showTimeOutDialog(true, "0");
                    }
                    mtitleView.setOperateText("00:00");
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask = null;
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        mtitleView.setOperateText("0" + minute + ":" + second);
                    } else {
                        mtitleView.setOperateText("0" + minute + ":0" + second);
                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        mtitleView.setOperateText(minute + ":" + second);
                    } else {
                        mtitleView.setOperateText("0" + minute + ":" + second);
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            mtitleView.setOperateText(minute + ":" + second);
                        } else {
                            mtitleView.setOperateText("0" + minute + ":" + second);
                        }
                    } else {
                        if (minute >= 10) {
                            mtitleView.setOperateText(minute + ":0" + second);
                        } else {
                            mtitleView.setOperateText("0" + minute + ":0" + second);
                        }
                    }
                }
            }
        }
    };

    private Handler handlerStopTime = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    stopTime();
                    break;
                case 1:
                    startTime();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

}