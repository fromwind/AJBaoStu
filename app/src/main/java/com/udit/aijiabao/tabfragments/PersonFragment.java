package com.udit.aijiabao.tabfragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.activitys.HelperActivity;
import com.udit.aijiabao.activitys.LoginActivity;
import com.udit.aijiabao.activitys.MineAppointActivty;
import com.udit.aijiabao.activitys.MineCoachActivity;
import com.udit.aijiabao.activitys.MineMessageActivity;
import com.udit.aijiabao.activitys.MineProjectActivty;
import com.udit.aijiabao.activitys.MineSchoolActivity;
import com.udit.aijiabao.activitys.MyDown;
import com.udit.aijiabao.activitys.SettingActivity;
import com.udit.aijiabao.activitys.UpdatePassActivty;
import com.udit.aijiabao.activitys.UserInfoActivity;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.dialog.WarnDialog;
import com.udit.aijiabao.entitys.MessageEntity;
import com.udit.aijiabao.tabfragments.callbacks.TabCallBack;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.utils.ui.BadgeView;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
@SuppressLint("ValidFragment")
@ContentView(R.layout.personfragment_layout)
public class PersonFragment extends Fragment {
    private final String TAG = PersonFragment.class.getSimpleName();
    DbManager db;
    private static PersonFragment personFragment;
    @ViewInject(R.id.me_message_layout)
    private RelativeLayout msg;
    @ViewInject(R.id.mLogin_btn)
    private TextView mLoginText;

    private TabCallBack back;
    private List<MessageEntity> mList;

    public PersonFragment(TabCallBack back) {
        this.back = back;
    }

    BadgeView badgeView;

    public PersonFragment() {

    }

    public static Fragment newInstance(TabCallBack back) {

        if (null == personFragment) {
            personFragment = new PersonFragment(back);
        }
        return personFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        db = x.getDb(((MyApplication) getActivity().getApplication()).getDaoConfig());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            //tintManager.setTintColor(R.color.red);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.blue_title);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
            tintManager.setNavigationBarTintResource(R.color.blue_title);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        badgeView = new BadgeView(getActivity(), msg);
    }

    private void initDatas() {
        badgeView.setTextSize(10);//文本字体大小
        badgeView.setTextColor(Color.WHITE);//文本内容的颜色
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);//在目标视图上的位置，如居中
        badgeView.setBadgeMargin(100, 40);//设置自定义提示视图与目标视图的horizontal，vertical 边距
        badgeView.setBadgeBackgroundColor(Color.RED);//文本背景色
        if (TextUtils.isEmpty(User.getAuthToken())) {
            mLoginText.setText("请登录");
            mLoginText.setClickable(true);
            badgeView.hide();
        } else {
            mLoginText.setText(User.getLocalUserName());
            mLoginText.setClickable(false);
            queryMessage();
        }

    }

    public void queryNotRead() {
        List<MessageEntity> all;
        all = null;
        try {
            all = db.selector(MessageEntity.class).where("deleted", "=", false).and("read", "=", false).and("user",
                    "=", User.getLocalUserName()).findAll();
            Log.e("Bum_not_read", "=" + all.size());
        } catch (DbException e) {
            e.printStackTrace();
        }
        String a = String.valueOf(all.size());
        if (!a.equals("0")) {
            badgeView.setText(a);
            if (badgeView.isShown()) {
                badgeView.setText(a);
            } else {
                badgeView.show();
            }
        } else {
            badgeView.hide();
        }
    }

    @Event(value = {R.id.mLogin_btn,
            R.id.me_info_layout,
            R.id.me_yuyue_layout,
            R.id.me_item_layout,
            R.id.me_message_layout,
            R.id.me_teacher_layout,
            R.id.me_password_layout,
            R.id.me_setup_layout,
            R.id.me_help_layout,
            R.id.me_school_layout,
            R.id.me_down_layout
    }, type = View.OnClickListener.class)
    private void click(View view) {
        switch (view.getId()) {
            case R.id.me_down_layout:
                startActivity(MyDown.class);
                break;
            case R.id.me_help_layout:
                startActivity(HelperActivity.class);
                break;
            default:
                if (TextUtils.isEmpty(User.getAuthToken())) {

                    back.clickView(2);
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constants.LOGIN_RESQ);
                    getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                            R.anim.push_right_to_left_out);
                } else {
                    switch (view.getId()) {
                        case R.id.mLogin_btn:
                            back.clickView(2);
                            startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constants.LOGIN_RESQ);
                            getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                                    R.anim.push_right_to_left_out);
                            break;

                        case R.id.me_info_layout:
                            startActivity(UserInfoActivity.class);
                            break;
                        case R.id.me_yuyue_layout:
                            if (TextUtils.isEmpty(PreferencesUtils.getString(Constants.SCHOOL, ""))) {
                                new WarnDialog(getActivity())
                                        .builder()
                                        .setTip1("温馨提示")
                                        .setTip2("亲，驾校还没有您的信息.请先报名哦！")
                                        .show();
                            } else {
                                startActivity(MineAppointActivty.class);
                            }
                            break;
                        case R.id.me_item_layout:
                            if (TextUtils.isEmpty(PreferencesUtils.getString(Constants.SCHOOL, ""))) {
                                new WarnDialog(getActivity())
                                        .builder()
                                        .setTip1("温馨提示")
                                        .setTip2("亲，驾校还没有您的信息.请先报名哦！")
                                        .show();
                            } else {
                                startActivity(MineProjectActivty.class);
                            }
                            break;
                        case R.id.me_message_layout:
                            startActivity(MineMessageActivity.class);
                            break;
                        case R.id.me_teacher_layout:
                            if (TextUtils.isEmpty(PreferencesUtils.getString(Constants.SCHOOL, ""))) {
                                new WarnDialog(getActivity())
                                        .builder()
                                        .setTip1("温馨提示")
                                        .setTip2("亲，驾校还没有您的信息.请先报名哦！")
                                        .show();
                            } else startActivity(MineCoachActivity.class);
                            break;
                        case R.id.me_school_layout:
                            if (TextUtils.isEmpty(PreferencesUtils.getString(Constants.SCHOOL, ""))) {
                                new WarnDialog(getActivity())
                                        .builder()
                                        .setTip1("温馨提示")
                                        .setTip2("亲，驾校还没有您的信息.请先报名哦！")
                                        .show();
                            } else {
                                startActivity(MineSchoolActivity.class);
                            }
                            break;
                        case R.id.me_password_layout:
                            startActivity(UpdatePassActivty.class);
                            break;
                        case R.id.me_setup_layout:
                            startActivity(SettingActivity.class);
                            break;

                    }
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Constants.LOGIN_RESQ == requestCode && Constants.LOGIN_RESP == resultCode) {
            Log.e("Bum_person", "onActivityResult");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Bum_person", "onResume");
        initDatas();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void queryMessage() {
        RequestParams params = new RequestParams(UrlConfig.MESSAGE_URL);
        params.addBodyParameter("member_id", User.getMemberId());
        params.addBodyParameter("corp_id", PreferencesUtils.getString(Constants.CORP_ID, ""));
        params.addBodyParameter("method", "query");
        params.addBodyParameter("user_type", Constants.USER_TYPE);
        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Bum_person_MsgOK", result);
                List<MessageEntity> list = new Gson().fromJson(result, new
                        TypeToken<List<MessageEntity>>() {
                        }.getType());
                if (null == mList) {
                    mList = new ArrayList<MessageEntity>();
                }
                mList.clear();
                mList.addAll(list);
                //Log.e("Bum_person_mList", String.valueOf(mList.size()));
                insert(mList);
                queryNotRead();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("Bum_person_Msg_error", "ex" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void insert(List<MessageEntity> msf) {
        //Log.e("Bum_person", "insert");
        String a;
        for (MessageEntity msg : msf) {
            a = msg.getContent();
            int ss = a.indexOf("：", 0);
            int dd = a.indexOf("：", ss);
            int cc = a.indexOf("：", dd + 3);
            int ff = a.indexOf("：", cc + 1);
            int gg = a.indexOf("：", ff + 26);
            //Log.e("Bum04", a.substring(ff + 2, gg - 11));
            msg = new MessageEntity(msg.getContent(), msg.getMsg_type(), msg.getCreated_at(), msg.getNotify_id(),
                    false, false, a.substring(ff + 2, gg - 11), User.getLocalUserName());
            try {
                db.save(msg);
            } catch (DbException e) {
                Log.d("e.getMessage()", e.getMessage().toString());
            }
        }
    }

    public void startActivity(Class Class) {
        Intent intent = new Intent(getContext(), Class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                R.anim.push_right_to_left_out);
    }

    public void startActivity(Class Class, Bundle bundle) {
        Intent intent = new Intent(getContext(), Class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                R.anim.push_right_to_left_out);
    }
}
