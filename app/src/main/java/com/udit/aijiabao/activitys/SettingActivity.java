package com.udit.aijiabao.activitys;

import android.nfc.Tag;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.UpdateManager;
import com.udit.aijiabao.User;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/5/17.
 */

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    private final String TAG = SettingActivity.class.getSimpleName();
    @ViewInject(R.id.titleView)
    private TitleView mtitleView;
    @ViewInject(R.id.txt_checkUpdate)
    private TextView checkUpdate;
    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("设置");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initData() {
    }

    @Event(value = {R.id.logout_layout, R.id.setting_about_layout,R.id.btnUpdate}, type = View.OnClickListener.class)
    private void clicks(View v) {
        switch (v.getId()) {
            case R.id.setting_about_layout:
                startActivity(AboutAJBActivity.class);
                break;
            case R.id.logout_layout:
                loginOut();
                break;
            case R.id.btnUpdate:
                UpdateManager manager = new UpdateManager(SettingActivity.this);
                // 检查软件更新
                manager.checkUpdate();
        }
    }

    private void loginOut() {
        LoadDialog.showLoad(this, "正在退出,请稍后!", null);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    User.clearUserInfo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LoadDialog.close();
                finish();
            }
        });
        t.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
