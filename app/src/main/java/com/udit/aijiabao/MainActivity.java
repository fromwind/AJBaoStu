package com.udit.aijiabao;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.udit.aijiabao.entitys.MessageEntity;
import com.udit.aijiabao.tabfragments.AppointFragment;
import com.udit.aijiabao.tabfragments.HomeFragment;
import com.udit.aijiabao.tabfragments.InfoFragment;
import com.udit.aijiabao.tabfragments.InfoFragment0;
import com.udit.aijiabao.tabfragments.PersonFragment;
import com.udit.aijiabao.tabfragments.callbacks.TabCallBack;
import com.udit.aijiabao.utils.EventEntity;
import com.udit.aijiabao.utils.T;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

import cc.zhipu.library.event.EventBus;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, TabCallBack {

    @ViewInject(R.id.tabs)
    private RadioGroup radioGroup;
    @ViewInject(R.id.tab_person)
    private RadioButton tab_person;
    private Bundle mBundle;
    private HashMap<Integer, Fragment> fragmentMap = new HashMap<Integer, Fragment>(4);
    private int currentId = R.id.tab_home;
    private Long mExitTime = 0l;
    private static final int INTERVAL = 2000;
    DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = x.getDb(((MyApplication) getApplication()).getDaoConfig());
        x.view().inject(this);
        ActivityCollector.addActivity(this);
        LocationManager.startLocation(this);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(currentId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(User.getAuthToken()))
            redhint();
        else {Drawable topDrawable = getResources().getDrawable(R.drawable.tab_person_bg);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            tab_person.setCompoundDrawables(null, topDrawable, null, null);}
    }

    public void redhint() {
        if (!queryNotRead()) {
            Drawable topDrawable = getResources().getDrawable(R.drawable.tab_person_bg);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            tab_person.setCompoundDrawables(null, topDrawable, null, null);
        } else {
            Drawable topDrawable = getResources().getDrawable(R.drawable.tab_person_bg1);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            tab_person.setCompoundDrawables(null, topDrawable, null, null);
        }
    }

    public boolean queryNotRead() {
        List<MessageEntity> all;
        all = null;
        try {
            all = db.selector(MessageEntity.class).where("deleted", "=", false).and("read", "=", false).and("user",
                    "=", User.getLocalUserName()).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (all != null) {
            Log.e("Bum_not_read", "=" + all.size());
            if (all.size() > 0) return true;
            else return false;
        } else return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        initFragmentById(checkedId);
        if (!TextUtils.isEmpty(User.getAuthToken()))
            redhint();
    }

    public void initFragmentById(int id) {

        switch (id) {
            case R.id.tab_home:
                if (!fragmentMap.containsKey(R.id.tab_home)) {
                    fragmentMap.put(R.id.tab_home, HomeFragment.newInstance(this));
                }
                break;
            case R.id.tab_customer:

                if (!fragmentMap.containsKey(R.id.tab_customer)) {
                    fragmentMap.put(R.id.tab_customer, AppointFragment.newInstance(this));
                }

                break;
            case R.id.tab_call:

                if (!fragmentMap.containsKey(R.id.tab_call)) {
                    fragmentMap.put(R.id.tab_call, InfoFragment0.newInstance());
                }
                break;
            case R.id.tab_person:
                if (!fragmentMap.containsKey(R.id.tab_person)) {
                    fragmentMap.put(R.id.tab_person, PersonFragment.newInstance(this));
                }
                break;
        }
        switchFragment(fragmentMap.get(currentId), fragmentMap.get(id));
        currentId = id;
    }

    private Fragment mCurrentFragment = null;
    private FragmentTransaction ft;

    public void switchFragment(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            ft = this.getSupportFragmentManager().beginTransaction();
            if (null != to) {

                if (!to.isAdded() && !to.isVisible()
                        && !to.isRemoving()) {    // 先判断是否被add过
                    ft.hide(from).add(R.id.container_fragment0, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
                    ft.show(to);
                } else {
                    ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
                }
            }
            if (null != mBundle) {
                to.setArguments(mBundle);
                mBundle = null;
            }

        }
    }

    @Override
    public void clickView(int tab) {

        switch (tab) {
            case 0:
                radioGroup.check(R.id.tab_customer);
                break;
            case 1:
                radioGroup.check(R.id.tab_home);

                if (fragmentMap.containsKey(R.id.tab_customer)) {
                    ft.remove(fragmentMap.get(R.id.tab_customer));
                }
                fragmentMap.remove(R.id.tab_customer);
                break;

            case 2:
                if (fragmentMap.containsKey(R.id.tab_customer)) {
                    ft.remove(fragmentMap.get(R.id.tab_customer));
                }
                fragmentMap.remove(R.id.tab_customer);
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton mTab = (RadioButton) radioGroup.getChildAt(i);
            FragmentManager fm = getSupportFragmentManager();

            Fragment fragment = fragmentMap.get(mTab.getId());
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment != null) {
                if (!mTab.isChecked()) {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mExitTime = 0l;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    /**
     * 判断两次返回时间间隔,小于两秒则退出程序
     */
    private void exit() {
        if (System.currentTimeMillis() - mExitTime > INTERVAL) {
            T.show(this, "再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getInstance().exit(true);
                }
            }, 300);
        }
    }
}
