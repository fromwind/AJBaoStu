package com.udit.aijiabao.tabfragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.activitys.ActivityWeb;
import com.udit.aijiabao.activitys.LoginActivity;
import com.udit.aijiabao.activitys.SchoolDetailActivity;
import com.udit.aijiabao.activitys.SearchLocationActivity;
import com.udit.aijiabao.activitys.SearchSchoolActivity;
import com.udit.aijiabao.adapters.BaseAdapterInject;
import com.udit.aijiabao.adapters.ViewHolderInject;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.dialog.PromptDialog;
import com.udit.aijiabao.dialog.WarnDialog;
import com.udit.aijiabao.entitys.Details;
import com.udit.aijiabao.entitys.Events;
import com.udit.aijiabao.entitys.Future;
import com.udit.aijiabao.entitys.RespAppoint;
import com.udit.aijiabao.entitys.RespBookInfo;
import com.udit.aijiabao.entitys.RespUserInfo;
import com.udit.aijiabao.entitys.School;
import com.udit.aijiabao.entitys.WeatherResp;
import com.udit.aijiabao.madapters.LocalImageHolderView;
import com.udit.aijiabao.tabfragments.callbacks.TabCallBack;
import com.udit.aijiabao.utils.EventEntity;
import com.udit.aijiabao.utils.NetWorkUtils;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.utils.ui.TestDecoration;
import com.udit.aijiabao.widgets.NoScrollListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import cc.zhipu.library.event.EventBus;
import cc.zhipu.library.imageloader.core.DisplayImageOptions;
import cc.zhipu.library.imageloader.core.ImageLoader;
import cc.zhipu.library.imageloader.core.assist.ImageScaleType;
import cc.zhipu.library.imageloader.core.display.FadeInBitmapDisplayer;
import cc.zhipu.library.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Administrator on 2016/5/11.
 */
@SuppressLint("ValidFragment")
@ContentView(R.layout.homefragment_layout)
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = HomeFragment.class.getSimpleName();
/*
    //天气
    @ViewInject(R.id.weather_more_img)
    private ImageView weather_more_img;
    @ViewInject(R.id.weather_dressing_advice)
    private TextView dressing_advice;
    @ViewInject(R.id.weather_date)
    private TextView weather_date;
    @ViewInject(R.id.home_recycler)
    private RecyclerView mRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<Future> mDatas;
    private LinearLayoutManager mLayoutManager;*/

    //驾校
    @ViewInject(R.id.schoolRecycleView)
    private RecyclerView mSchoolRecyclerView;
    private LinearLayoutManager schoolLayoutManager;
    private SchoolListAdapter schooListadapter;
    private List<School> mList;
    private int lastVisibleItem = 0;
    private int orderBy = 1;
    private int page = 1;
    private final int PAGE_SIZE = 10;
    //2222222222222222222222222222222222
    @ViewInject(R.id.text_location)
    private TextView locationText;

    @ViewInject(R.id.text_praise)
    private TextView praiseText;

    @ViewInject(R.id.text_price)
    private TextView priceText;
    @ViewInject(R.id.location_img)
    private ImageView locationImg;

    @ViewInject(R.id.praise_img)
    private ImageView praiseImg;

    @ViewInject(R.id.price_img)
    private ImageView priceImg;

    //222222222222
    private static HomeFragment homeFragment;
    private TabCallBack back;
    @ViewInject(R.id.swipeLayout)
    private SwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.layout_is_appoint)
    private LinearLayout layout_is_appoint;
    @ViewInject(R.id.layout_home_school)
    private LinearLayout layout_home_school;
    @ViewInject(R.id.locationView)
    private TextView mTextLocation;
    @ViewInject(R.id.img_weather)
    private ImageView mImgWeather;
    /*@ViewInject(R.id.search_edit)
    private ClearEditText mSearchView;

    @ViewInject(R.id.weather_future)
    private LinearLayout weatherfuture;*/

    @ViewInject(R.id.txt_weather)
    private TextView mTextWeather;

    @ViewInject(R.id.convenientBanner)
    private ConvenientBanner banner;

    @ViewInject(R.id.home_more_school)
    private TextView moreschool;
    /*@ViewInject(R.id.my_subject_text)
    private TextView my_subject_text;//科目

    @ViewInject(R.id.my_teacher_text)
    private TextView my_teacher_text;//教练

    @ViewInject(R.id.my_school_text)
    private TextView my_school_text;//驾校

    @ViewInject(R.id.search_school_btn)
    private TextView search_school_btn;//找驾校*/

    @ViewInject(R.id.listView)
    private NoScrollListView listView;

    @ViewInject(R.id.appoint_listview_layout)
    private LinearLayout mListView_Layout;

    @ViewInject(R.id.warn_text)
    private TextView warn_text;
    @ViewInject(R.id.txt_appoint_state)
    private TextView appoint_state;

    @ViewInject(R.id.title_no_connect)
    private RelativeLayout nonet;
    private List<Integer> localImages;

    private List<Details> mAppointList;

    private AppointListAdapter mAdapter;

    public HomeFragment(TabCallBack back) {
        this.back = back;
    }

    public HomeFragment() {
    }

    public static HomeFragment newInstance(TabCallBack back) {
        if (null == homeFragment) {
            homeFragment = new HomeFragment(back);
        }
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        EventBus.getDefault().register(this);//注册EventBus
        if (NetWorkUtils.isConnective(getContext())) {
            nonet.setVisibility(View.GONE);
        } else {
            nonet.setVisibility(View.VISIBLE);
        }
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

        if (!TextUtils.isEmpty(PreferencesUtils.getString(Constants.POSITION, ""))) {
            mTextLocation.setText(PreferencesUtils.getString(Constants.POSITION, "南京市"));
        }
        refreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android
                        .R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);

        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });

        initBanner();
        initBannerDatas();

        if (null == mAppointList) {
            mAppointList = new ArrayList<>();
        }

        mAdapter = new AppointListAdapter(getContext());
        listView.setAdapter(mAdapter);
        /*
        //创天气线性布局
        mLayoutManager = new LinearLayoutManager(getContext());
        //水平方向
        mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new TestDecoration(getContext()));*/

        //创建驾校线性布局
        schoolLayoutManager = new LinearLayoutManager(getContext());
        //垂直方向
        schoolLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        schoolLayoutManager.setSmoothScrollbarEnabled(true);
        schoolLayoutManager.setAutoMeasureEnabled(true);
        mSchoolRecyclerView.setLayoutManager(schoolLayoutManager);

        mSchoolRecyclerView.addItemDecoration(new TestDecoration(getContext()));
        mSchoolRecyclerView.setHasFixedSize(true);
        mSchoolRecyclerView.setNestedScrollingEnabled(false);
        mSchoolRecyclerView.setFocusable(false);
        if (null == mList) {
            mList = new ArrayList<>();
        }
        schooListadapter = new SchoolListAdapter(getContext());
        schooListadapter.setList(mList);
        mSchoolRecyclerView.setAdapter(schooListadapter);
        //getWeather(118.04, 32.00);
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
        banner.startTurning(5000);
    }

    @Override
    public void onRefresh() {
        page = 1;
        isRefresh = true;
        initDatas();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(BDLocation bdLocation) {
        if (null != bdLocation) {
            mTextLocation.setText(bdLocation.getCity());
            getWeather(bdLocation.getLongitude(), bdLocation.getLatitude());
        }
    }

    public void onEventMainThread(Events.Refresh refresh) {
        if (refresh.refresh) {
            Log.e(TAG, "onEventMainThread=" + refresh.refresh);
            initDatas();
        }
    }

    //发送消息在NetConnectReceiver,监听网络状态
    public void onEventMainThread(EventEntity.NetChange event) {
        /*String msg = "onEventMainThread收到了消息：" + event.name;
        Log.e("Bum_EventBus", msg);*/
        //Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        if (event.isConnected) {
            nonet.setVisibility(View.GONE);
        } else nonet.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Constants.LOCATION_RESQ == requestCode && Constants.LOCATION_RESP == resultCode) {

            Log.e(TAG, "onActivityResult--" + data.getStringExtra("ITEM"));

            if (null != data && data.hasExtra("ITEM")) {
                mTextLocation.setText(data.getStringExtra("ITEM"));
            }

        }
    }

    @Event(value = {R.id.home_img_findschool, R.id.home_img_appointing, R.id.home_more_school,R.id.locationView},
            type = View
            .OnClickListener.class)
    private void click(View view) {
        switch (view.getId()) {
            case R.id.home_img_appointing:

                if (TextUtils.isEmpty(User.getAuthToken())) {
                    back.clickView(1);
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constants.LOGIN_RESQ);
                    getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                            R.anim.push_right_to_left_out);
                } else {
                    back.clickView(0);
                }
                break;
            case R.id.home_more_school:
            case R.id.home_img_findschool:
                startActivity(new Intent(getActivity(), SearchSchoolActivity.class));
                getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                        R.anim.push_right_to_left_out);
                break;
            case R.id.locationView:
                startActivityForResult(new Intent(getActivity(), SearchLocationActivity.class), Constants
                        .LOCATION_RESQ);
                getActivity().overridePendingTransition(R.anim.bottom_open,
                        R.anim.bottom_);
                break;
        }
    }
    /*@Event(value = {R.id.appoint_btn, R.id.my_school_layout, R.id.locationView, R.id.my_teacher_text, R.id
            .my_subject_text}, type = View.OnClickListener.class)
    private void click(View v) {
        switch (v.getId()) {
            case R.id.appoint_btn:

                if (TextUtils.isEmpty(User.getAuthToken())) {
                    back.clickView(1);
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constants.LOGIN_RESQ);
                    getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                            R.anim.push_right_to_left_out);
                } else {
                    back.clickView(0);
                }
                break;

            case R.id.my_school_layout:
                if (TextUtils.isEmpty(User.getAuthToken())) {
                    back.clickView(1);
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constants.LOGIN_RESQ);
                    getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                            R.anim.push_right_to_left_out);
                } else {

                    if (TextUtils.isEmpty(PreferencesUtils.getString(Constants.SCHOOL, ""))) {
                        //找驾校
                        startActivity(new Intent(getActivity(), SearchSchoolActivity.class));
                        getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                                R.anim.push_right_to_left_out);
                    } else {
                        //startActivity(new Intent(getActivity(), MineSchoolActivity.class));
                        startActivity(new Intent(getActivity(), SearchSchoolActivity.class));
                        getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                                R.anim.push_right_to_left_out);
                    }
                }
                break;
            case R.id.locationView:

                startActivityForResult(new Intent(getActivity(), SearchLocationActivity.class), Constants
                        .LOCATION_RESQ);
                getActivity().overridePendingTransition(R.anim.bottom_open,
                        R.anim.bottom_);
                break;
            case R.id.my_teacher_text:
                if (TextUtils.isEmpty(User.getAuthToken())) {
                    back.clickView(1);
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constants.LOGIN_RESQ);
                    getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                            R.anim.push_right_to_left_out);
                } else {

                    if (TextUtils.isEmpty(PreferencesUtils.getString(Constants.TEACHER, ""))) {
                        Toast.makeText(getContext(), "系统还未为您绑定教练，请过一段时间再试！", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(getActivity(), MineCoachActivity.class));
                        getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                                R.anim.push_right_to_left_out);
                    }
                }
                break;
            case R.id.my_subject_text:
                if (TextUtils.isEmpty(User.getAuthToken())) {
                    back.clickView(1);
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constants.LOGIN_RESQ);
                    getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                            R.anim.push_right_to_left_out);
                } else {
                    if (TextUtils.isEmpty(PreferencesUtils.getString(Constants.SUBJECT, ""))) {
                        //找项目
                        Toast.makeText(getContext(), "没有获取到您的项目信息！", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(getActivity(), MineProjectActivty.class));
                        getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                                R.anim.push_right_to_left_out);
                    }
                }
                break;
            case R.id.weather_more:
                if (weatherfuture.isShown()) {
                    weatherfuture.setVisibility(View.GONE);
                    weatherfuture.setAnimation(AnimationUtil.moveToViewBottom());
                    weather_more_img.setImageResource(R.mipmap.arrow_downdown);
                } else {
                    weatherfuture.setVisibility(View.VISIBLE);
                    weather_more_img.setImageResource(R.mipmap.arrow_upup);
                    weatherfuture.setAnimation(AnimationUtil.moveToViewLocation());
                }
        }
    }*/

    /*private void getWeather(double lon, double lat) {
        RequestParams params = new RequestParams(UrlConfig.WEATHER_URL + "&lon=" + lon + "&lat=" + lat);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mDatas = new ArrayList<Future>();
                Future future;
                Log.e("Bum_weather_post", "" + result);
                JsonObject obj = new JsonParser().parse(result).getAsJsonObject().getAsJsonObject("result");
                JsonObject objtoday = obj.getAsJsonObject("today");
                objtoday.get("date_y").getAsString();
                weather_date.setText(objtoday.get("date_y").getAsString() + "  " + objtoday.get("week").getAsString()
                        + "  " + objtoday.get("weather").getAsString());
                dressing_advice.setText("气温：" + objtoday.get("temperature").getAsString() + "  出行建议：" + objtoday.get
                        ("travel_index").getAsString() + "\n" + "着装建议：" + objtoday.get("dressing_advice").getAsString
                        ());
                JsonArray ary = obj.getAsJsonArray("future");
                for (JsonElement jsonElement : ary) {
                    future = new Gson().fromJson(jsonElement, Future.class);
                    mDatas.add(future);
                }
                Log.e("Bum_mdatas", "" + mDatas.size());
                recyclerAdapter = new RecyclerAdapter(getContext(), mDatas);
                mRecyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.e("Bum_weather_post", "onError" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

                Log.e("Bum_weather_post", "" + cex.toString());
            }

            @Override
            public void onFinished() {

                Log.e("Bum_weather_post", "onFinished");
            }
        });
    }*/
    private void getWeather(double lon, double lat) {
        RequestParams params = new RequestParams(UrlConfig.WEATHER_URL + "&lon=" + lon + "&lat=" + lat);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Bum_weather_post", "" + result);
                WeatherResp weatherResp = new Gson().fromJson(result, new TypeToken<WeatherResp>() {
                }.getType());
                mImgWeather.setImageResource(getResources().getIdentifier("d" + weatherResp.getResult().getToday()
                                .getWeather_id().getFa(),
                        "mipmap", getContext().getPackageName()));
                mTextWeather.setText(weatherResp.getResult().getToday().getTemperature());
                Log.e("BUm", weatherResp.getResult().getToday().getTemperature());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initBanner() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        //int windowsHeight = metric.heightPixels;

        ViewGroup.LayoutParams params = banner.getLayoutParams();
        //params.height = windowsHeight / 5;
        banner.setLayoutParams(params);
    }

    private void initBannerDatas() {
        localImages = new ArrayList<>();

        localImages.add(R.mipmap.banner1);
        localImages.add(R.mipmap.banner2);
        localImages.add(R.mipmap.banner3);
        localImages.add(R.mipmap.banner4);

        banner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position + 1) {
                            case 1:
                                Log.e("bububu", "1");
                                /*startActivity(new Intent(getActivity(), ActivityWeb.class));
                                getActivity().overridePendingTransition(R.anim.push_right_to_left_in,
                                        R.anim.push_right_to_left_out);*/
                                break;
                            case 2:
                                Log.e("bububu", "2");
                                break;
                            case 3:
                                Log.e("bububu", "3");
                                break;
                            case 4:
                                Log.e("bububu", "4");
                                break;
                        }
                    }
                });
    }

    private void initDatas() {
        filterClick(getActivity().findViewById(R.id.location_layout));
        RequestParams params = new RequestParams(UrlConfig.HOME_STUFFINFO_URL);
        params.addBodyParameter("method", "query");
        params.addBodyParameter("user_type", Constants.USER_TYPE);
        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Bum_home_initdata", "onSuccess" + result);

                RespUserInfo userInfo = new Gson().fromJson(result, RespUserInfo.class);

                if (!TextUtils.isEmpty(userInfo.getError())) {
                    T.show(getContext(), userInfo.getError() + ",请重新登录");
                    return;
                }

                PreferencesUtils.putString(Constants.NICKNAME, userInfo.getName());

                PreferencesUtils.putString(Constants.SUBJECT, userInfo.getTraining_item_name());
                PreferencesUtils.putString(Constants.TEACHER, userInfo.getCoach_name());
                PreferencesUtils.putString(Constants.CARNUM, userInfo.getCar_no());
                PreferencesUtils.putString(Constants.TRAINED, userInfo.getTraining_items().getTrained_times());
                PreferencesUtils.putString(Constants.NO_TRAINED, userInfo.getTraining_items().getNo_training_times());
                PreferencesUtils.putString(Constants.TEACHER_PHONE, userInfo.getCoach_mobile());

                PreferencesUtils.putString(Constants.CORP_ID, String.valueOf(userInfo.getCorp_id()));
                PreferencesUtils.putString(Constants.COACH_ID, String.valueOf(userInfo.getCoach_id()));
                PreferencesUtils.putString(Constants.MEMBER_ID, String.valueOf(userInfo.getMember_id()));
                PreferencesUtils.putString(Constants.TRAINED_ITEM_ID, String.valueOf(userInfo.getTraining_item_id()));
                PreferencesUtils.putString(Constants.SCHOOL, userInfo.getCorp().getName());
                Log.e(TAG, userInfo.toString());
                initAppoiontDatas(userInfo);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                analysis(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void initAppoiontDatas(RespUserInfo userInfo) {
        RequestParams params = new RequestParams(UrlConfig.HOME_BOOk_URL);
        params.addBodyParameter("method", "query");
        params.addBodyParameter("user_type", Constants.USER_TYPE);
        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));
        params.addBodyParameter("corp_id", String.valueOf(userInfo.getCorp_id()));
        params.addBodyParameter("member_id", String.valueOf(userInfo.getMember_id()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<RespAppoint> appointList = new Gson().fromJson(result, new TypeToken<List<RespAppoint>>() {
                }.getType());
                analysis(appointList);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });

    }

    public void analysis(List<RespAppoint> r) {
        List<Details> shaiList = new ArrayList<Details>();
        shaiList.clear();
        mAppointList.clear();
        Date nowdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm", Locale.CHINA);

        if (null != r && r.size() > 0) {
            for (int i = 0; i < r.size(); i++) {
                shaiList.addAll(r.get(i).getDetail());
            }
            List<Details> new_shaiList = new ArrayList<Details>();
            new_shaiList.clear();
            Iterator<Details> it = shaiList.iterator();
            while (it.hasNext()) {
                Details item = it.next();
                try {

                    if (item.getStatus() == 0 &&
                            !sdf.parse(item.getBooking_date()
                                    + item.getBooking_time()
                                    .substring(0, item.getBooking_time().indexOf("-")))
                                    .before(nowdate)) {

                        new_shaiList.add(item);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            mAppointList.clear();
            mAppointList.addAll(new_shaiList);
            Collections.sort(mAppointList, new Comparator<Details>() {
                @Override
                public int compare(Details lhs, Details rhs) {
                    Date date1 = stringToDate(lhs.getBooking_date() + lhs.getBooking_time().substring(0, lhs
                            .getBooking_time().indexOf("-")));
                    Date date2 = stringToDate(rhs.getBooking_date() + rhs.getBooking_time().substring(0, rhs
                            .getBooking_time().indexOf("-")));
                    // 对日期字段进行升序，如果欲降序可采用after方法
                    if (date1.after(date2)) {
                        return 1;
                    }
                    return -1;
                }
            });

        }

        if (mAppointList.size() > 0) {
            warn_text.setText("您下次学车时间为:\n" + mAppointList.get(0).getBooking_date() + "日" + mAppointList.get(0)
                    .getBooking_time() + "\n请提前15分钟到达指定练习场地！");
            layout_is_appoint.setVisibility(View.VISIBLE);
            mListView_Layout.setVisibility(View.VISIBLE);
            layout_home_school.setVisibility(View.GONE);
        } else {
            warn_text.setText("您还没有预约练车时段，赶紧预约吧！");
            mListView_Layout.setVisibility(View.GONE);
            layout_is_appoint.setVisibility(View.GONE);
            layout_home_school.setVisibility(View.VISIBLE);
        }
        mAdapter.setData(mAppointList);
    }

    public Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopTurning();
    }

    private class AppointListAdapter extends BaseAdapterInject<Details> {

        Context context;

        public AppointListAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public int getConvertViewId(int position) {
            return R.layout.listview_appoint_item;
        }

        @Override
        public ViewHolderInject<Details> getNewHolder(int position) {
            return new ViewHolder(context);
        }

        class ViewHolder extends ViewHolderInject<Details> {
            Context context;
            @ViewInject(R.id.appoint_time)
            private TextView appoint_time;

            @ViewInject(R.id.appoint_time_slot)
            private TextView appoint_time_slot;

            @ViewInject(R.id.appoint_cancle)
            private TextView cancle;

            public ViewHolder(Context context) {
                super();
                this.context = context;
            }

            @Override
            public void loadData(Details data, int position) {
                Details details = getItem(position);

                if (null != details) {
                    appoint_time.setText(details.getBooking_date());
                    appoint_time_slot.setText(details.getBooking_time());

                    cancle.setTag(details);
                }
            }

            @Event(value = {R.id.appoint_cancle}, type = View.OnClickListener.class)
            private void click(View v) {
                final Details details = (Details) v.getTag();
                EventBus.getDefault().post(new Events.Refresh2(true));
                Log.e("click", "click-" + details.getBooking_time());

                PromptDialog.Builder pDialog = new PromptDialog.Builder(
                        getContext());
                pDialog.setTitle("温馨提示");
                pDialog.setMessage(context.getString(R.string.reminder_info));
                pDialog.setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                });
                pDialog.setButton2("确认", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        cancleAppoint(details);
                    }
                });
                pDialog.create().show();
            }
        }

        private void cancleAppoint(Details details) {

            RequestParams params = new RequestParams(UrlConfig.HOME_BOOk_URL);
            params.addBodyParameter("method", "cancel");
            params.addBodyParameter("user_type", Constants.USER_TYPE);
            params.addBodyParameter("token", User.getAuthToken());
            params.addBodyParameter("userId", String.valueOf(User.getUserId()));
            params.addBodyParameter("member_id", User.getMemberId());
            params.addBodyParameter("booking_date", details.getBooking_date());
            params.addBodyParameter("booking_time", details.getBooking_time());
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e(TAG, "" + result);

                    RespBookInfo info = new Gson().fromJson(result, RespBookInfo.class);

                    if (null != info && !info.isResult()) {
                        new WarnDialog(getActivity())
                                .builder()
                                .setTip1("温馨提示")
                                .setTip2(info.getMessage())
                                .show();
                    }
                    EventBus.getDefault().post(new Events.Refresh2(true));
                    initDatas();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    /*public class RecyclerAdapter extends
            RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private List<Future> mDatas;

        public RecyclerAdapter(Context context, List<Future> datats) {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            TextView weacher;
            TextView temperature;
            ImageView img;
            TextView wind;
            TextView week;
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        *//**
     * 创建ViewHolder
     *//*

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.weather_item,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.week = (TextView) view
                    .findViewById(R.id.weather_week);
            viewHolder.wind = (TextView) view
                    .findViewById(R.id.weather_wind);
            viewHolder.temperature = (TextView) view
                    .findViewById(R.id.weather_temperature);
            viewHolder.weacher = (TextView) view
                    .findViewById(R.id.weather_weather);
            viewHolder.img = (ImageView) view.findViewById(R.id.weather_img);
            return viewHolder;
        }

        */

    /**
     * 设置值
     *//*

        @Override

        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            viewHolder.week.setText(mDatas.get(i).getWeek());
            viewHolder.wind.setText(mDatas.get(i).getWind());
            //viewHolder.img.setImageResource(R.mipmap.d01);
            viewHolder.img.setImageResource(getResources().getIdentifier("d" + mDatas.get(i).getWeather_id().getFa(),
                    "mipmap", getContext().getPackageName()));
            viewHolder.temperature.setText(mDatas.get(i).getTemperature());
            if (mDatas.get(i).getWeather().length() > 5) {
                viewHolder.weacher.setTextSize(12);
            }
            viewHolder.weacher.setText(mDatas.get(i).getWeather());

        }

    }*/
    @Event(value = {R.id.location_layout, R.id.text_location, R.id.praise_layout, R.id.text_praise, R.id
            .price_layout, R.id.text_price})
    private void filterClick(View view) {

        switch (view.getId()) {

            case R.id.location_layout:
            case R.id.text_location:
                locationText.setTextColor(getResources().getColor(R.color.blue_title));
                praiseText.setTextColor(getResources().getColor(R.color.gray_60));
                priceText.setTextColor(getResources().getColor(R.color.gray_60));

                locationImg.setBackgroundResource(R.mipmap.arrow_down);
                praiseImg.setBackgroundResource(R.mipmap.arrow_up);
                priceImg.setBackgroundResource(R.mipmap.arrow_up);

                page = 1;
                isRefresh = false;

                orderBy = 1;
                getSchoolList(page, orderBy);
                break;

            case R.id.praise_layout:
            case R.id.text_praise:

                locationText.setTextColor(getResources().getColor(R.color.gray_60));
                praiseText.setTextColor(getResources().getColor(R.color.blue_title));
                priceText.setTextColor(getResources().getColor(R.color.gray_60));

                locationImg.setBackgroundResource(R.mipmap.arrow_up);
                praiseImg.setBackgroundResource(R.mipmap.arrow_down);
                priceImg.setBackgroundResource(R.mipmap.arrow_up);

                page = 1;
                isRefresh = false;

                orderBy = 2;
                getSchoolList(page, orderBy);
                break;

            case R.id.price_layout:
            case R.id.text_price:
                locationText.setTextColor(getResources().getColor(R.color.gray_60));
                praiseText.setTextColor(getResources().getColor(R.color.gray_60));
                priceText.setTextColor(getResources().getColor(R.color.blue_title));

                locationImg.setBackgroundResource(R.mipmap.arrow_up);
                praiseImg.setBackgroundResource(R.mipmap.arrow_up);
                priceImg.setBackgroundResource(R.mipmap.arrow_down);

                page = 1;
                isRefresh = false;
                orderBy = 3;
                getSchoolList(page, orderBy);
                break;
        }
    }

    private boolean isRefresh = false;

    private void getSchoolList(final int page, int orderBy) {
        if (NetWorkUtils.isConnective(getContext())) {
            if (!isRefresh) {
                LoadDialog.showLoad(getContext(), "正在加载中,请稍后!", null);
            }

            if (1 == page) {
                mList.clear();
            }

            RequestParams params = new RequestParams(UrlConfig.CROP_URL);

            params.addBodyParameter("user_type", Constants.USER_TYPE);
            params.addBodyParameter("method", "queryCorpForPage");
            params.addBodyParameter("userId", String.valueOf(User.getUserId()));
            params.addBodyParameter("token", User.getAuthToken());

            params.addBodyParameter("cityId", "320100");

            params.addBodyParameter("longitude", PreferencesUtils.getString(Constants.LONGITUDE));
            params.addBodyParameter("latitude", PreferencesUtils.getString(Constants.LATITUDE));

            params.addBodyParameter("orderBy", String.valueOf(orderBy));

            params.addBodyParameter("page", String.valueOf(page));
            params.addBodyParameter("pageSize", String.valueOf(PAGE_SIZE));

            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("Bum_School_fragment", "onSuccess:" + page + result);

                    List<School> list = new Gson().fromJson(result, new TypeToken<List<School>>() {
                    }.getType());
                    if (null != list && list.size() > 0) {
                        mList.addAll(list);
                    }
                    schooListadapter.notifyDataSetChanged();
                    moreschool.setText("查看更多");
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    moreschool.setText("服务器未响应，请稍后重试！");
                    moreschool.setTextSize(14);
                    Log.e(TAG, "onError" + ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LoadDialog.close();
                    refreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            });
        }

    }

    private class SchoolListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater inflater;

        private List<School> list;

        private ImageLoader imageLoader;

        private DisplayImageOptions options;

        private Context context;

        public SchoolListAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);

            imageLoader = ImageLoader.getInstance();

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.img_null)
                    .showImageOnFail(R.mipmap.img_null)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).displayer(new
                            FadeInBitmapDisplayer(300))
                    .displayer(new RoundedBitmapDisplayer(6)).delayBeforeLoading(100).build();

            setHasStableIds(true);
        }

        public void setList(List<School> list) {
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(inflater.inflate(
                    R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder viewHolder = (ItemViewHolder) holder;
                final School school = list.get(position);
                imageLoader.displayImage(school.getLogo(),
                        viewHolder.itemImg, options);
                /**根据图片名字设置本地资源*/
                /*viewHolder.itemImg.setImageResource(getResources().getIdentifier("id" + school.getLogo(),
                        "mipmap", getActivity().getPackageName()));*/

                //Log.e("Bum", "http://115.159.2.160:8080/ajb.manage/upload/showFile.htm?fileid=" + a);
                viewHolder.name.setText(school.getName());

                if (!TextUtils.isEmpty(school.getScore())) {
                    viewHolder.ratingBar.setRating(Float.parseFloat(school.getScore()));
                } else {
                    viewHolder.ratingBar.setRating(5);
                }

                if (!TextUtils.isEmpty(school.getMembers_total())) {
                    viewHolder.follow.setText(school.getMembers_total() + "人关注");
                } else {
                    viewHolder.follow.setText("0人关注");
                }

                if (!TextUtils.isEmpty(school.getPrice())) {
                    if (school.getPrice().contains(".")) {
                        viewHolder.price.setText(school.getPrice().substring(0, school.getPrice().indexOf(".")) + "元");
                    } else {
                        viewHolder.price.setText(school.getPrice() + " 元");
                    }
                }

                viewHolder.distance.setText("距离" + school.getDistance());

                viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();

                        bundle.putString(Constants.CORP_ID, school.getCorpId());
                        bundle.putString("feature", school.getFeature());
                        bundle.putString("profile", school.getProfile());
                        Intent intent = new Intent(context, SchoolDetailActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent, bundle);
                    /*
                    context.overridePendingTransition(R.anim.push_right_to_left_in,
                            R.anim.push_right_to_left_out);*/
                    }
                });
            }
        }

        @Override
        public int getItemCount() {

            if (null != list && list.size() > 0) {
                return list.size();
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private LinearLayout itemLayout;

            private ImageView itemImg;
            private TextView name;
            private RatingBar ratingBar;
            private TextView follow;

            private TextView price;
            private TextView distance;

            public ItemViewHolder(View view) {
                super(view);
                itemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
                itemImg = (ImageView) view.findViewById(R.id.item_img);
                name = (TextView) view.findViewById(R.id.name);
                ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
                follow = (TextView) view.findViewById(R.id.follow);
                price = (TextView) view.findViewById(R.id.price);
                distance = (TextView) view.findViewById(R.id.distance);
            }
        }
    }

}
