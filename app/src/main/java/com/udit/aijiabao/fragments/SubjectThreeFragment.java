package com.udit.aijiabao.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.activitys.ActivityWeb;
import com.udit.aijiabao.activitys.VideoViewActivity;
import com.udit.aijiabao.activitys.project2_skillclass;
import com.udit.aijiabao.dialog.PromptDialog;
import com.udit.aijiabao.dialog.WarnDialog;
import com.udit.aijiabao.entitys.Movies;
import com.udit.aijiabao.madapters.LocalImageHolderView;
import com.udit.aijiabao.utils.FileUtils;
import com.udit.aijiabao.utils.NetWorkUtils;
import com.udit.aijiabao.utils.T;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by Administrator on 2016/5/26.
 */
@ContentView(R.layout.fragment_subject_two)
public class SubjectThreeFragment extends Fragment {

    private final String TAG = SubjectThreeFragment.class.getSimpleName();

    private static SubjectThreeFragment threeFragment;
    DbManager dbManager;

    @ViewInject(R.id.recycleView)
    private RecyclerView mRecyclerView;
    private SubjectAdapter adapter;
    private List<Movies> moviesList;
    @ViewInject(R.id.convenientBanner)
    ConvenientBanner banner;

    public static SubjectThreeFragment newInstance() {
        threeFragment = new SubjectThreeFragment();
        return threeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dbManager = x.getDb(((MyApplication) getActivity().getApplicationContext()).getDaoConfig());

        List<Integer> localImages = new ArrayList<>();
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
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        banner.startTurning(5000);
        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                T.show(getActivity(), position + "");
            }
        });

        dbManager = x.getDb(((MyApplication) getActivity().getApplicationContext()).getDaoConfig());
        initBanner();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new SubjectAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        moviesList = new ArrayList<Movies>();
        try {
            if (dbManager.selector(Movies.class).where("project", "=", "3").findAll() == null || dbManager.selector
                    (Movies.class).where("project", "=", "3").findAll().size() == 0) {
                Log.e("bum", "dbproject3==null!");
                Movies movie1 = new Movies(21, "掉头操作技巧视角1", R.mipmap.p21, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/90bc958f40a5cc79801e08d3a653c8aa.flv", "05:48", 11.9, "3");
                Movies movie2 = new Movies(22, "掉头操作技巧视角2", R.mipmap.p22, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/a228a83e402913e980c668d734a48706.flv", "03:58", 8.12, "3");
                Movies movie3 = new Movies(23, "夜间模拟灯光讲解", R.mipmap.p23, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/45c1d729401c34de801c4ed44f9590ed.flv", "09:45", 20.0, "3");
                Movies movie4 = new Movies(24, "右转须知", R.mipmap.p24, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/e2daf87540afc595802a347c0c688dc4.flv", "05:59", 12.2, "3");
                Movies movie5 = new Movies(25, "挂挡习惯养成", R.mipmap.p25, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/486a1383408aa48480e6985aee748e45.flv", "04:09", 8.52, "3");
                Movies movie6 = new Movies(26, "加减档时机", R.mipmap.p26, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/965bdbd340f119c880e0a35c14435cb3.flv", "06:12", 12.7, "3");
                Movies movie7 = new Movies(27, "油离配合技巧", R.mipmap.p27, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/d29bbe9f4013821e8079db889109a339.flv", "04:47", 9.85, "3");
                Movies movie8 = new Movies(28, "变道小常识", R.mipmap.p28, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/c934ee7540dedcaa80a997d9e04c7b7e.flv", "06:05", 12.5, "3");
                Movies movie9 = new Movies(29, "红绿灯起步技巧", R.mipmap.p29, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/e81be331402978d780f583c2cc87577f.flv", "06:11", 12.7, "3");
                Movies movie10 = new Movies(210, "靠边停车技巧", R.mipmap.p210, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/85d6a3ec40321a14807db3ae0158a910.flv", "07:27", 15.3, "3");
                Movies movie11 = new Movies(211, "后视镜利用技巧", R.mipmap.p211, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/406e5829400b0bf08022588a0c247f93.flv", "03:21", 6.61, "3");
                Movies movie12 = new Movies(212, "左转注意事项", R.mipmap.p212, "http://bmob-cdn-6792.b0.upaiyun" +
                        ".com/2016/11/09/2f09ef2e40ed580a80f8f60a2ce7dbd8.flv", "17:29", 35.9, "3");
                moviesList.clear();
                moviesList.add(movie1);
                moviesList.add(movie2);
                moviesList.add(movie3);
                moviesList.add(movie4);
                moviesList.add(movie5);
                moviesList.add(movie6);
                moviesList.add(movie7);
                moviesList.add(movie9);
                moviesList.add(movie8);
                moviesList.add(movie10);
                moviesList.add(movie11);
                moviesList.add(movie12);
                try {
                    dbManager.save(moviesList);
                    Log.e("bum", "save!size::::::" + dbManager.selector(Movies.class).where("project", "=", "3")
                            .findAll().size());
                } catch (DbException e) {
                    e.printStackTrace();
                }
            } else moviesList = dbManager.selector(Movies.class).where("project", "=", "3").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        Log.e("bum", "movie" + moviesList.size());
        adapter.setListMovies(moviesList);
        adapter.notifyDataSetChanged();
    }

    private class SubjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private List<Movies> moviesList;

        public void setListMovies(List<Movies> moviesList) {
            this.moviesList = moviesList;
        }

        public SubjectAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ItemHolder) {
                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (new File(FileUtils.getproject3Path() + "/" + moviesList.get(position).getName() + "" +
                                ".flv").exists()) {
                            if (moviesList.get(position).getPrecent() == 0) {
                                Intent appIntent = new Intent(getActivity(), VideoViewActivity.class);
                                appIntent.putExtra("url", FileUtils.getproject3Path() + "/" + moviesList.get
                                        (position).getName() + ".flv");
                                startActivity(appIntent);
                            } else T.show(getActivity(), "下载中，请稍后");
                        } else {
                            showNoticeDialog(moviesList.get(position).getSize(), moviesList.get(position)
                                    .getUrl(), moviesList.get(position).getName(), position);
                        }
                    }
                });
                itemHolder.time.setText(moviesList.get(position).getTime());
                itemHolder.movie_name.setText(moviesList.get(position).getName());
                itemHolder.movie_downtxt.setText(new File(FileUtils.getproject3Path() + "/" + moviesList.get
                        (position).getName() + ".flv").exists() ? "已下载" : "未下载");
                itemHolder.movie_downtxt.setTextColor(new File(FileUtils.getproject3Path() + "/" + moviesList.get
                        (position).getName() + ".flv").exists() ? getResources().getColor(R.color.green) :
                        getResources().getColor(R.color.red));
                if (new File(FileUtils.getproject3Path() + "/" + moviesList.get(position).getName() + ".flv")
                        .exists())
                    itemHolder.movie_download.setVisibility(View.GONE);
                else itemHolder.movie_download.setVisibility(View.VISIBLE);
                /*itemHolder.movie_download.setImageResource(new File(FileUtils.getproject3Path() + "/" + moviesList
                        .get(position).getName() + ".flv").exists() ? R.mipmap.delete : R.mipmap.download);*/
                itemHolder.movie_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moviesList.get(position).getPrecent() == 0) {
                            /*if (new File(FileUtils.getproject3Path() + "/" + moviesList.get(position).getName()
                             + "" +
                                    ".flv").exists()) {
                                new File(FileUtils.getproject3Path() + "/" + moviesList.get(position).getName() +
                                 "" +
                                        ".flv").delete();
                                if (!new File(FileUtils.getproject3Path() + "/" + moviesList.get(position).getName()
                                        + ".flv").exists()) {
                                    T.show(getActivity(), "删除成功！");
                                    adapter.notifyDataSetChanged();
                                }
                            } else {*/
                            downloadThread downloadThread = new downloadThread();
                            downloadThread.url = moviesList.get(position).getUrl();
                            downloadThread.name = moviesList.get(position).getName();
                            downloadThread.position = position;
                            downloadThread.run();
                            /*}*/
                        } else T.show(getActivity(), "下载中，请稍后");

                    }
                });
                itemHolder.img_proj2.setImageResource(moviesList.get(position).getImg());
                itemHolder.movie_big.setText(moviesList.get(position).getSize() + "MB");
                if (moviesList.get(position).getPrecent() != 0) {
                    itemHolder.percent.setVisibility(View.VISIBLE);
                    itemHolder.percent.setText(moviesList.get(position).getPrecent() + "%");
                    itemHolder.movie_downtxt.setText("正在下载...");
                } else itemHolder.percent.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        private class ItemHolder extends RecyclerView.ViewHolder {

            LinearLayout item;
            ImageView img_proj2;
            ImageView movie_download;
            TextView movie_name, movie_downtxt, movie_big, time, percent;

            public ItemHolder(View itemView) {
                super(itemView);
                item = (LinearLayout) itemView.findViewById(R.id.item_layout);
                img_proj2 = (ImageView) itemView.findViewById(R.id.item_movie);
                movie_download = (ImageView) itemView.findViewById(R.id.item_movie_down_or_delete);
                movie_name = (TextView) itemView.findViewById(R.id.item_movie_name);
                movie_downtxt = (TextView) itemView.findViewById(R.id.item_movie_downtxt);
                movie_big = (TextView) itemView.findViewById(R.id.item_movie_size);
                time = (TextView) itemView.findViewById(R.id.item_movie_time);
                percent = (TextView) itemView.findViewById(R.id.item_movie_down_percent);
            }
        }

    }

    private void initBanner() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int windowsHeight = metric.heightPixels;

        ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.height = windowsHeight / 5;
        banner.setLayoutParams(params);
    }

    @Event(value = {R.id.standard_layout, R.id.experience_layout, R.id.technique_layout}, type = View.OnClickListener
            .class)
    private void click(View v) {
        switch (v.getId()) {
            case R.id.standard_layout:
                Intent intent = new Intent();
                intent.putExtra("url", "file:///android_asset/project3_standom.html");
                intent.setClass(getActivity(), ActivityWeb.class);
                startActivity(intent);
                T.show(getActivity(), "标准");
                break;

            case R.id.experience_layout:
                T.show(getActivity(), "技巧");
                Intent intent1 = new Intent();
                intent1.putExtra("project", "3");
                intent1.setClass(getActivity(), project2_skillclass.class);
                startActivity(intent1);
                break;

            case R.id.technique_layout:
                T.show(getActivity(), "预约");
                break;
        }
    }

    /**
     * 显示下载题库对话框
     */
    private void showNoticeDialog(Double size, final String url, final String name, final int position) {
        // 构造对话框
        PromptDialog.Builder builder = new PromptDialog.Builder(getActivity());
        builder.setTitle("提示");
        if (NetWorkUtils.isWifi(getActivity())) {
            builder.setMessage1("当前网络为wifi");
        } else builder.setMessage1("非wifi网络！");
        builder.setMessage2("请先下载观看！此视频大小约" + size + "MB。");
        builder.setButton1("下载", new PromptDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                dialog.dismiss();
                downloadThread downloadThread = new downloadThread();
                downloadThread.url = url;
                downloadThread.name = name;
                downloadThread.position = position;
                downloadThread.run();
            }
        });
        builder.setButton2TextColor(getResources().getColor(R.color.white));
        builder.setButton2("取消", new PromptDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private class downloadThread extends Thread {
        public String url;
        public String name;
        public int position;

        @Override
        public void run() {
            super.run();
            BmobFile bmobFile = new BmobFile(name + ".flv", "", url);
            final File saveFile = new File(FileUtils.getproject3Path(), bmobFile.getFilename());
            bmobFile.download(saveFile, new DownloadFileListener() {

                @Override
                public void onStart() {
                    Log.e("bum", "开始下载...");
                }

                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        T.show(getActivity(), "下载成功,保存路径:" + s);
                        Message message = new Message();
                        message.what = 1;
                        message.arg1 = 0;
                        message.arg2 = position;
                        handler.sendMessage(message);
                    } else {
                        T.show(getActivity(), "下载失败！");
                        Log.e("bum", "下载失败：" + e.getErrorCode() + "," + e.getMessage());

                        new WarnDialog(getActivity())
                                .builder()
                                .setTip1("温馨提示")
                                .setTip2("连接服务器超时，请稍后重试")
                                .show();
                        Message message = new Message();
                        message.what = 1;
                        message.arg1 = 0;
                        message.arg2 = position;
                        handler.sendMessage(message);
                        if (new File(FileUtils.getproject2Path() + "/" + name + ".flv").exists()) {
                            new File(FileUtils.getproject2Path() + "/" + name + ".flv").delete();
                        }
                    }
                }

                @Override
                public void onProgress(Integer value, long newworkSpeed) {
                    //Log.e("bum", value.toString() + "%");
                    int value0 = 0;
                    Message message = new Message();
                    if (value % 3 == 0 && value != value0) {
                        value0 = value;
                        message.what = 1;
                        message.arg1 = value0;
                        message.arg2 = position;
                        handler.sendMessage(message);
                    }
                }
            });

        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter.moviesList.get(msg.arg2).setPrecent(msg.arg1);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
