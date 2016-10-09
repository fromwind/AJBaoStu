package com.udit.aijiabao.activitys;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.entitys.CommentListBean;
import com.udit.aijiabao.entitys.RespCommentList;
import com.udit.aijiabao.entitys.School;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cc.zhipu.library.imageloader.core.DisplayImageOptions;
import cc.zhipu.library.imageloader.core.ImageLoader;
import cc.zhipu.library.imageloader.core.assist.ImageScaleType;
import cc.zhipu.library.imageloader.core.display.FadeInBitmapDisplayer;
import cc.zhipu.library.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Administrator on 2016/5/19.
 */

@ContentView(R.layout.activity_comment_list)
public class CommentListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = CommentListActivity.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;


    @ViewInject(R.id.swipeLayout)
    private SwipeRefreshLayout refreshLayout;

    @ViewInject(R.id.recycleView)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.comment_num)
    private TextView comment_num;

    @ViewInject(R.id.total_score)
    private TextView total_score;

    @ViewInject(R.id.ratingbar)
    private RatingBar ratingbar;

    @ViewInject(R.id.status)
    private TextView status;

    @ViewInject(R.id.fuprogress)
    private ProgressBar fubar;

    @ViewInject(R.id.jiaoprogress)
    private ProgressBar jiaobar;

    @ViewInject(R.id.changprogress)
    private ProgressBar chngbar;

    @ViewInject(R.id.shouprogress)
    private ProgressBar shobar;

    @ViewInject(R.id.futext)
    private TextView futext;

    @ViewInject(R.id.jiaotext)
    private TextView jiaotext;

    @ViewInject(R.id.changtext)
    private TextView changtext;

    @ViewInject(R.id.shoutext)
    private TextView shoutext;

    @ViewInject(R.id.write_comment_layout)
    private LinearLayout writeLayout;

    private List<CommentListBean> mCommentList;

    private int lastVisibleItem = 0;

    private int PAGE_SIZE = 10;

    private int page = 1;

    private String corpID;

    private CommentAdapter adapter;

    private boolean isWrite=false;

    @Override
    public void setContentView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("评价");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));


        Bundle bundle = getIntent().getExtras();

        isWrite=bundle.getBoolean("isWrite");

        corpID = bundle.getString("CorpId", "");
    }

    @Override
    public void initView() {

        if(isWrite){
            writeLayout.setVisibility(View.VISIBLE);
        }else {
            writeLayout.setVisibility(View.GONE);
        }

        refreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
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
                    getComment(page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        };

        mRecyclerView.setOnScrollListener(onScrollListener);

        mCommentList = new ArrayList<>();

        adapter = new CommentAdapter(this);

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getComment(page);
    }

    private void getComment(final int page) {

        RequestParams params = new RequestParams(UrlConfig.COMMENT_URL);

        params.addBodyParameter("user_type", Constants.USER_TYPE);
        params.addBodyParameter("method", "queryCommentForPage");
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));
        params.addBodyParameter("token", User.getAuthToken());

        params.addBodyParameter("corpId", corpID);

        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("pageSize", String.valueOf(PAGE_SIZE));

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess" + result);

                RespCommentList commentList = new Gson().fromJson(result, RespCommentList.class);

                if (commentList.isResult()) {
                    List<CommentListBean> list = commentList.getCommentList();


                    if (!TextUtils.isEmpty(commentList.getCount())) {
                        comment_num.setText("用户评论(" + commentList.getCount() + "条)");
                    } else {
                        comment_num.setText("用户评论(0条)");
                    }

                    DecimalFormat digits = new DecimalFormat("0.0");//取一位，如要取多位，写多几个0上去

                    if (!TextUtils.isEmpty(commentList.getFwtd())) {
                        fubar.setProgress((int) Float.parseFloat(commentList.getFwtd()));
                        futext.setText(digits.format(Float.parseFloat(commentList.getFwtd())));
                    }
                    if (!TextUtils.isEmpty(commentList.getJxnl())) {
                        jiaobar.setProgress((int) Float.parseFloat(commentList.getJxnl()));
                        jiaotext.setText(digits.format(Float.parseFloat(commentList.getJxnl())));
                    }
                    if (!TextUtils.isEmpty(commentList.getJxcd())) {
                        chngbar.setProgress((int) Float.parseFloat(commentList.getJxcd()));
                        changtext.setText(digits.format(Float.parseFloat(commentList.getJxcd())));
                    }
                    if (!TextUtils.isEmpty(commentList.getJxsf())) {
                        shobar.setProgress((int) Float.parseFloat(commentList.getJxsf()));
                        shoutext.setText(digits.format(Float.parseFloat(commentList.getJxsf())));
                    }

                    if (!TextUtils.isEmpty(commentList.getFwtd()) &&
                            !TextUtils.isEmpty(commentList.getJxnl()) &&
                            !TextUtils.isEmpty(commentList.getJxcd()) &&
                            !TextUtils.isEmpty(commentList.getJxsf())) {
                        total_score.setText(digits.format((
                                Float.parseFloat(commentList.getFwtd())
                                        + Float.parseFloat(commentList.getJxnl())
                                        + Float.parseFloat(commentList.getJxcd())
                                        + Float.parseFloat(commentList.getJxsf())) / 4));

                        ratingbar.setRating((Float.parseFloat(commentList.getFwtd())
                                + Float.parseFloat(commentList.getJxnl())
                                + Float.parseFloat(commentList.getJxcd())
                                + Float.parseFloat(commentList.getJxsf())) / 4);
                    }

                    status.setText("好评");

                    if (1 == page) {
                        mCommentList.clear();
                    }

                    if (null != list && list.size() > 0) {
                        mCommentList.addAll(list);
                        adapter.setList(mCommentList);
                        adapter.notifyDataSetChanged();
                    }

                }

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
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });


    }

    @Event(value = {R.id.write_comment_layout})
    private void clicks(View view){
        switch (view.getId()){

            case R.id.write_comment_layout:

                startActivity(CommentCoachActivity.class);

                break;

        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater inflater;

        private List<CommentListBean> list;

        private ImageLoader imageLoader;

        private DisplayImageOptions options;

        private Context context;

        public CommentAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);

            imageLoader = ImageLoader.getInstance();

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.defaut_cuser_img)
                    .showImageOnFail(R.mipmap.defaut_cuser_img)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(300))
                    .displayer(new RoundedBitmapDisplayer(6)).delayBeforeLoading(100).build();

            setHasStableIds(true);
        }

        public void setList(List<CommentListBean> list) {
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(inflater.inflate(
                    R.layout.item_comment, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof ItemViewHolder) {

                ItemViewHolder viewHolder = (ItemViewHolder) holder;

                CommentListBean bean = list.get(position);
                imageLoader.displayImage("", viewHolder.itemImg, options);

                viewHolder.name.setText(bean.getUserName());

                viewHolder.content.setText(bean.getContent());
                viewHolder.time.setText(bean.getCreatedDate());

                if (!TextUtils.isEmpty(bean.getZtmyd())) {
                    viewHolder.ratingBar.setRating(Float.parseFloat(bean.getZtmyd()));
                }

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


            private ImageView itemImg;
            private TextView name;
            private RatingBar ratingBar;
            private TextView content;

            private TextView time;

            public ItemViewHolder(View view) {
                super(view);
                itemImg = (ImageView) view.findViewById(R.id.item_img);
                name = (TextView) view.findViewById(R.id.name);
                ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
                content = (TextView) view.findViewById(R.id.content);
                time = (TextView) view.findViewById(R.id.time);
            }
        }
    }
}
