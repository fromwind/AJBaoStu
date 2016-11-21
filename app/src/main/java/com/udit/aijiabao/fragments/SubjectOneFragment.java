package com.udit.aijiabao.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.activitys.PracticeActivity;
import com.udit.aijiabao.activitys.ApplyKnow;
import com.udit.aijiabao.activitys.ClassListActivity;
import com.udit.aijiabao.activitys.RealPracticeActivity;
import com.udit.aijiabao.activitys.project_lawsclass;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.dialog.PromptDialog;
import com.udit.aijiabao.dialog.WarnDialog;
import com.udit.aijiabao.entitys.Question;
import com.udit.aijiabao.madapters.LocalImageHolderView;
import com.udit.aijiabao.utils.FileUtils;
import com.udit.aijiabao.utils.NetWorkUtils;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.utils.ZipExtractorTask;

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

@ContentView(R.layout.fragment_subject)
public class SubjectOneFragment extends Fragment {

    private final String TAG = SubjectOneFragment.class.getSimpleName();
    private static SubjectOneFragment oneFragment;
    File AJBaoDir;
    File zipfile;
    File db1file;
    File dbzipfile;
    DbManager dbManager;
    String filename = "project1";
    private List<Integer> localImages;

    @ViewInject(R.id.convenientBanner)
    private ConvenientBanner banner;

    public static SubjectOneFragment newInstance() {
        oneFragment = new SubjectOneFragment();
        return oneFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initBanner();
        initBannerDatas();
        AJBaoDir = new File(FileUtils.getAJBaoPath());
        zipfile = new File(AJBaoDir.toString() + "/project1.zip");
        db1file = new File(AJBaoDir.toString() + "/DB/db1");
        dbzipfile = new File(AJBaoDir.toString() + "/Db.zip");
        dbManager = x.getDb(((MyApplication) getActivity().getApplicationContext()).getDaoConfig1());
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
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        //        banner.startTurning(5000);
    }

    private void initBanner() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int windowsHeight = metric.heightPixels;

        ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.height = windowsHeight / 5;
        banner.setLayoutParams(params);
    }

    @Event(value = {R.id.project01_order_practice,
            R.id.project01_real_practice, R.id.project01_collect_practice, R.id.project01_class_practice, R.id
            .project01_error_practice, R.id
            .project01_apply, R.id.project01_green_hand, R.id.project01_laws}, type = View.OnClickListener.class)
    private void mClick(View view) {
        switch (view.getId()) {
            case R.id.project01_apply:
                startActivity(new Intent(getActivity(), ApplyKnow.class));
                break;
            case R.id.project01_green_hand:
                startActivity(new Intent(getActivity(), project_lawsclass.class).putExtra("lawsorgreen","green"));
                break;
            case R.id.project01_laws:
                startActivity(new Intent(getActivity(), project_lawsclass.class).putExtra("lawsorgreen","laws"));
                break;
            case R.id.project01_order_practice:
                haveText01("order");
                break;
            case R.id.project01_real_practice:
                haveText01("real");
                break;
            case R.id.project01_collect_practice:
                if (collectDb().size() != 0) {
                    haveText01("collect");
                } else T.show(getActivity(), "您暂时没有收藏的题目");
                break;
            case R.id.project01_error_practice:
                if (errorDb().size() != 0) {
                    haveText01("error");
                } else T.show(getActivity(), "您暂时没有错题");
                break;
            case R.id.project01_class_practice:
                haveText01("class");
                break;
        }
    }

    private List<Question> collectDb() {
        List<Question> list = new ArrayList<Question>();
        try {
            list = dbManager.selector(Question.class).where("collect", "=", true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Question> errorDb() {
        List<Question> list = new ArrayList<Question>();
        try {
            list = dbManager.selector(Question.class).where("error", "is not", null).findAll();
            Log.e("bum", "error——size=" + list.size());
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.startTurning(5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopTurning();
    }

    public void haveText01(String sty) {
        if (db1file.exists()) {
            Log.e("bum_db1", db1file.toString() + "存在");
            LoadDialog.showLoad(getActivity(), "正在加载...", null);
            if (sty.equals("real")) {
                startActivity(new Intent(getActivity(), RealPracticeActivity.class));
            } else if (sty.equals("class")) {
                startActivity(new Intent(getActivity(), ClassListActivity.class));
            } else {
                Intent intent = new Intent();
                intent.putExtra("style", sty);
                intent.setClass(getActivity(), PracticeActivity.class);
                startActivity(intent);
            }

        } else {
            Log.e("bumdb1", db1file.toString() + "不存在");
            new DbThread().run();
            showNoticeDialog();
        }

    }

    private class downloadThread extends Thread {

        @Override
        public void run() {
            super.run();
            BmobFile bmobFile = new BmobFile("project1.zip", "", FileUtils.PROJECT_1_URL);
            final File saveFile = new File(AJBaoDir, bmobFile.getFilename());
            bmobFile.download(saveFile, new DownloadFileListener() {

                @Override
                public void onStart() {
                    Log.e("bum", "开始下载...");
                }

                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.e("bum", "下载成功,保存路径:" + s);
                        mDownloadDialog.dismiss();
                        handler.sendEmptyMessage(1);
                    } else {
                        Log.e("bum", "下载失败：" + e.getErrorCode() + "," + e.getMessage());
                        mDownloadDialog.dismiss();
                        new WarnDialog(getActivity())
                                .builder()
                                .setTip1("温馨提示")
                                .setTip2("连接服务器超时，请稍后重试")
                                .show();
                        if (zipfile.exists()) {
                            zipfile.delete();
                            super.onStart();
                        }
                    }
                }

                @Override
                public void onProgress(Integer value, long newworkSpeed) {
                    mProgress.setProgress(value);
                    speed.setText(value + "%");
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
                    ZipExtractorTask task = new ZipExtractorTask(zipfile.toString(), FileUtils.getproject1Path(),
                            getActivity(), true);
                    task.execute();
                    break;
            }
        }
    };

    public class DbThread extends Thread {
        @Override
        public void run() {
            super.run();
            BmobFile bmobFile1 = new BmobFile("Db.zip", "", FileUtils.PROJECT_DB_URL);
            final File saveFile1 = new File(AJBaoDir, bmobFile1.getFilename());
            bmobFile1.download(saveFile1, new DownloadFileListener() {

                @Override
                public void onStart() {
                    Log.e("bum", "db开始下载...");
                }

                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.e("bum", "db下载成功,保存路径:" + s);
                        ZipExtractorTask task1 = new ZipExtractorTask(dbzipfile.toString(), FileUtils.getDBPath(),
                                getActivity(), true);
                        task1.execute();
                    } else {
                        Log.e("bum", "db下载失败：" + e.getErrorCode() + "," + e.getMessage());

                        if (dbzipfile.exists()) {
                            dbzipfile.delete();
                        }
                        new WarnDialog(getActivity())
                                .builder()
                                .setTip1("温馨提示")
                                .setTip2("连接服务器超时，请稍后重试")
                                .show();
                    }
                }

                @Override
                public void onProgress(Integer value, long newworkSpeed) {
                }
            });
        }
    }

    /**
     * 显示下载题库对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        PromptDialog.Builder builder = new PromptDialog.Builder(getActivity());
        builder.setTitle("提示");
        if (NetWorkUtils.isWifi(getActivity())) {
            builder.setMessage1("当前网络为wifi");
        } else builder.setMessage1("非wifi网络！");
        builder.setMessage2("需要下载科目一题库！大小约25MB");
        builder.setButton1("下载", new PromptDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
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

    /**
     * 显示下载进度对话框
     */
    private ProgressBar mProgress;
    private PromptDialog mDownloadDialog;
    private TextView speed;
    PromptDialog.Builder builder;

    private void showDownloadDialog() {
        // 构造软件下载对话框
        builder = new PromptDialog.Builder(getActivity());
        builder.setTitle("下载进度");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress1);
        speed = (TextView) v.findViewById(R.id.networkspeed1);
        builder.setView(v);
        builder.setButton1("隐藏", new PromptDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setButton1TextColor(R.color.white);
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        new downloadThread().run();
    }

}
