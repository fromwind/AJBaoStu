package com.udit.aijiabao.activitys;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.entitys.School;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.widgets.ClearEditText;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.zhipu.library.imageloader.core.DisplayImageOptions;
import cc.zhipu.library.imageloader.core.ImageLoader;
import cc.zhipu.library.imageloader.core.assist.ImageScaleType;
import cc.zhipu.library.imageloader.core.display.FadeInBitmapDisplayer;
import cc.zhipu.library.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Administrator on 2016/5/14.
 */
@ContentView(R.layout.activity_school_list_layout)
public class SearchSchoolActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = SearchSchoolActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView titleView;

    @ViewInject(R.id.search_edittext)
    private ClearEditText clearEditText;

    @ViewInject(R.id.swipeLayout)
    private SwipeRefreshLayout refreshLayout;

    @ViewInject(R.id.school_recycleView)
    private RecyclerView mRecyclerView;

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

    private int lastVisibleItem = 0;

    private SchoolListAdapter adapter;

    private List<School> mList;
    private List<School> allList;
    private String[] schoolname;
    private final int PAGE_SIZE = 10;

    private int orderBy = 1;

    private int page = 1;

    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {

        titleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        titleView.showBack(true);
        titleView.setOperateText("搜索");
        titleView.setOperateOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clearEditText.getText().toString().isEmpty()) {
                    String aa = clearEditText.getText().toString();
                    Pattern p = Pattern.compile(aa);
                    List<School> we = new ArrayList<School>();
                    we.clear();
                    for (int i = 0; i < allList.size(); i++) {
                        String pp = allList.get(i).getName();
                        Matcher matcher = p.matcher(pp);
                        if (matcher.find()) {
                            we.add(allList.get(i));
                        }
                    }
                    adapter.setList(we);
                    adapter.notifyDataSetChanged();
                }
            }

        });

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

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    page = page + 1;
                    getSchoolList(page, orderBy);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        };

        mRecyclerView.setOnScrollListener(onScrollListener);

        if (null == mList) {
            mList = new ArrayList<>();
        }
        if (null == allList) {
            allList = new ArrayList<>();
            findallschool();
        }

        adapter = new SchoolListAdapter(this);
        adapter.setList(mList);
        mRecyclerView.setAdapter(adapter);

        clearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String aa = s.toString();
                Pattern p = Pattern.compile(aa);
                List<School> we = new ArrayList<School>();
                we.clear();
                for (int i = 0; i < allList.size(); i++) {
                    String pp = allList.get(i).getName();
                    Matcher matcher = p.matcher(pp);
                    if (matcher.find()) {
                        we.add(allList.get(i));
                    }
                }
                adapter.setList(we);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void findallschool() {
        RequestParams params = new RequestParams(UrlConfig.CROP_URL);

        params.addBodyParameter("user_type", Constants.USER_TYPE);
        params.addBodyParameter("method", "queryCorpForPage");
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));
        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("cityId", "320100");
        params.addBodyParameter("longitude", PreferencesUtils.getString(Constants.LONGITUDE));
        params.addBodyParameter("latitude", PreferencesUtils.getString(Constants.LATITUDE));
        params.addBodyParameter("orderBy", "1");
        params.addBodyParameter("page", "1");
        params.addBodyParameter("pageSize", "100");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Bum_findAllSchool", "onSuccess:" + result);

                List<School> list = new Gson().fromJson(result, new TypeToken<List<School>>() {
                }.getType());
                if (null != list && list.size() > 0) {
                    Log.e("Bum_findAllSchool", "list.size:" + list.size());
                    allList.addAll(list);
                    Log.e("Bum_findAllSchool", "allList.size:" + allList.size());
                    for (School sc : list) {
                        Log.e("bum", sc.getName() + "id" + sc.getLogo());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("Bum_findAllSchool", "onError" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void initData() {
        filterClick(findViewById(R.id.location_layout));
    }

    @Event(value = {R.id.location_layout, R.id.praise_layout, R.id.price_layout,})
    private void filterClick(View view) {

        switch (view.getId()) {

            case R.id.location_layout:
                locationText.setTextColor(getResources().getColor(R.color.blue_title));
                praiseText.setTextColor(getResources().getColor(R.color.black));
                priceText.setTextColor(getResources().getColor(R.color.black));

                locationImg.setBackgroundResource(R.mipmap.arrow_down);
                praiseImg.setBackgroundResource(R.mipmap.arrow_up);
                priceImg.setBackgroundResource(R.mipmap.arrow_up);

                page = 1;
                isRefresh = false;
                orderBy = 1;
                getSchoolList(page, orderBy);
                break;

            case R.id.praise_layout:
                locationText.setTextColor(getResources().getColor(R.color.black));
                praiseText.setTextColor(getResources().getColor(R.color.blue_title));
                priceText.setTextColor(getResources().getColor(R.color.black));

                locationImg.setBackgroundResource(R.mipmap.arrow_up);
                praiseImg.setBackgroundResource(R.mipmap.arrow_down);
                priceImg.setBackgroundResource(R.mipmap.arrow_up);

                page = 1;
                isRefresh = false;
                orderBy = 2;
                getSchoolList(page, orderBy);
                break;
            case R.id.price_layout:
                locationText.setTextColor(getResources().getColor(R.color.black));
                praiseText.setTextColor(getResources().getColor(R.color.black));
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

    @Override
    public void onRefresh() {
        page = 1;
        isRefresh = true;
        getSchoolList(page, orderBy);
    }

    private boolean isRefresh = false;

    private void getSchoolList(final int page, int orderBy) {

        if (!isRefresh) {
            LoadDialog.showLoad(this, "正在加载中,请稍后!", null);
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

        Log.e(TAG, "long" + PreferencesUtils.getString(Constants.LONGITUDE));
        Log.e(TAG, "lat" + PreferencesUtils.getString(Constants.LONGITUDE));

        params.addBodyParameter("orderBy", String.valueOf(orderBy));

        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("pageSize", String.valueOf(PAGE_SIZE));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Bum_School_activity", "onSuccess:" + page + result);

                List<School> list = new Gson().fromJson(result, new TypeToken<List<School>>() {
                }.getType());
                if (null != list && list.size() > 0) {
                    mList.addAll(list);
                }
                adapter.setList(mList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
                //Log.e("Bum",position+"---:"+school.getLogo());
                imageLoader.displayImage(school.getLogo(),
                        viewHolder.itemImg, options);
                /*if (school.getLogo()!=null){
                    viewHolder.itemImg.setImageResource(getResources().getIdentifier("id"+school.getLogo(),
                            "mipmap",getPackageName()));
                }*/
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
