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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.activitys.ApplyKnow;
import com.udit.aijiabao.activitys.ClassList4Activity;
import com.udit.aijiabao.activitys.ClassListActivity;
import com.udit.aijiabao.activitys.Practice4Activity;
import com.udit.aijiabao.activitys.PracticeActivity;
import com.udit.aijiabao.activitys.RealPractice4Activity;
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
public class SubjectFourFragment extends Fragment {

    private final String TAG = SubjectFourFragment.class.getSimpleName();
    private static SubjectFourFragment fourFragment;
    File AJBaoDir;
    File zip401file;
    File zip402file;
    File picture401;
    File picture402;
    File db4file;
    File dbzipfile;
    DbManager dbManager;
    private List<Integer> localImages;

    @ViewInject(R.id.convenientBanner)
    private ConvenientBanner banner;

    public static SubjectFourFragment newInstance() {
        fourFragment = new SubjectFourFragment();
        return fourFragment;
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
        zip401file = new File(AJBaoDir.toString() + "/project401.zip");
        zip402file = new File(AJBaoDir.toString() + "/project402.zip");
        db4file = new File(AJBaoDir.toString() + "/DB/db4");
        dbzipfile = new File(AJBaoDir.toString() + "/Db.zip");
        picture401 = new File(AJBaoDir.toString() + "/project4/39.001.gif");
        picture402 = new File(AJBaoDir.toString() + "/project4/42.001.jpg");
        dbManager = x.getDb(((MyApplication) getActivity().getApplicationContext()).getDaoConfig4());
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
                haveText04("order");
                break;
            case R.id.project01_real_practice:
                haveText04("real");
                break;
            case R.id.project01_collect_practice:
                if (collectDb().size() != 0) {
                    haveText04("collect");
                } else T.show(getActivity(), "您暂时没有收藏的题目");
                break;
            case R.id.project01_error_practice:
                if (errorDb().size() != 0) {
                    haveText04("error");
                } else T.show(getActivity(), "您暂时没有错题");
                break;
            case R.id.project01_class_practice:
                haveText04("class");
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

    public void haveText04(String sty) {
        if (db4file.exists()) {
            Log.e("bum_db4", db4file.toString() + "存在");
            if (!picture402.exists()) {
                Log.e("bum_picture402", picture402.toString() + "不存在");
                if (!picture401.exists()) {
                    Log.e("bum_picture401", picture401.toString() + "也不存在");
                    showNoticeDialog();
                }
            } else {
                Log.e("bum_picture401", picture401.toString() + "存在");
                LoadDialog.showLoad(getActivity(), "正在加载...", null);
                if (sty.equals("real")) {
                    startActivity(new Intent(getActivity(), RealPractice4Activity.class));
                }else if(sty.equals("class")){
                    startActivity(new Intent(getActivity(), ClassList4Activity.class));
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("style", sty);
                    intent.setClass(getActivity(), Practice4Activity.class);
                    startActivity(intent);
                }
            }

        } else {
            Log.e("bumdb4", db4file.toString() + "不存在");
            new DbThread().run();
            if (!picture402.exists()) {
                if (!picture401.exists()) {
                    showNoticeDialog();
                }
            }

        }

    }

    public class download401Thread extends Thread {

        @Override
        public void run() {
            super.run();

            BmobFile bmobFile = new BmobFile("project401.zip", "", FileUtils.PROJECT_401_URL);
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
                        if (zip401file.exists()) {
                            zip401file.delete();
                            super.onStart();
                        }
                    }
                }

                @Override
                public void onProgress(Integer value, long newworkSpeed) {
                    if (value != null) {
                        mProgress1.setProgress(value);
                        speed1.setText(value + "%");
                    }
                }
            });

        }

    }

    public class download402Thread extends Thread {
        @Override
        public void run() {
            super.run();

            BmobFile bmobFile = new BmobFile("project402.zip", "", FileUtils.PROJECT_402_URL);
            final File saveFile = new File(AJBaoDir, bmobFile.getFilename());
            bmobFile.download(saveFile, new DownloadFileListener() {

                @Override
                public void onStart() {
                    Log.e("bum", "开始下载...402");
                }

                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.e("bum", "下载成功,保存路径:" + s);
                        handler.sendEmptyMessage(2);
                    } else {
                        Log.e("bum", "下载失败：" + e.getErrorCode() + "," + e.getMessage());
                        new WarnDialog(getActivity())
                                .builder()
                                .setTip1("温馨提示")
                                .setTip2("连接服务器超时，请稍后重试")
                                .show();
                        if (zip402file.exists()) {
                            zip402file.delete();
                            super.onStart();
                        }
                    }
                }

                @Override
                public void onProgress(Integer value, long newworkSpeed) {
                    mProgress2.setProgress(value);
                    speed2.setText(value + "%");
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
                    if (zip401file.exists()) {
                        ZipExtractorTask task = new ZipExtractorTask(zip401file.toString(), FileUtils.getproject4Path
                                (), getActivity(), true);
                        task.execute();
                    }
                    break;
                case 2:
                    if (zip402file.exists()) {
                        ZipExtractorTask task1 = new ZipExtractorTask(zip402file.toString(), FileUtils
                                .getproject4Path(), getActivity(), true);
                        task1.execute();
                    }
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
        builder.setMessage2("需要下载科目四题库！共两个文件大小约79MB");
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
    private ProgressBar mProgress1;
    private TextView speed1;
    private PromptDialog mDownloadDialog;
    private ProgressBar mProgress2;
    private TextView speed2;
    private String value;
    private FrameLayout frameLayout;
    PromptDialog.Builder builder;

    private void showDownloadDialog() {
        // 构造软件下载对话框
        builder = new PromptDialog.Builder(getActivity());
        builder.setTitle("下载进度");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        frameLayout= (FrameLayout) v.findViewById(R.id.download_progress2);
        mProgress1 = (ProgressBar) v.findViewById(R.id.update_progress1);
        speed1 = (TextView) v.findViewById(R.id.networkspeed1);
        mProgress2 = (ProgressBar) v.findViewById(R.id.update_progress2);
        speed2 = (TextView) v.findViewById(R.id.networkspeed2);
        frameLayout.setVisibility(View.VISIBLE);
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
        new download401Thread().run();
        new download402Thread().run();
    }

}
