/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udit.aijiabao.activitys;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.app.Activity;

import com.udit.aijiabao.R;
import com.udit.aijiabao.entitys.ScreenBean;
import com.udit.aijiabao.utils.FileUtils;
import com.udit.aijiabao.utils.LocUtil;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 * 播放在线视频流界面
 */
public class VideoViewActivity extends Activity {
    /** 当前视频路径 */
    private String path;
    /** 当前声音 */
    private int mVolume = -1;
    /** 最大音量 */
    private int mMaxVolume;
    /** 当前亮度 */
    private float mBrightness = -1f;
    /** 手势数目 */
    private int finNum=0;

    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private VideoView mVideoView;
    private GestureDetector gestDetector;
    private ScaleGestureDetector scaleDetector;

    private ScreenBean screenBean;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        /*Intent Intent = getIntent();
        if (Intent != null && Intent.getExtras().containsKey("url")) {
            path = Intent.getExtras().getString("url");
        }*/
        path= FileUtils.getAJBaoPath()+"/童梦.mp4";
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.acy_play);
        init();
    }

    private void init() {
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

        mMaxVolume = LocUtil.getMaxVolume(this);
        gestDetector = new GestureDetector(this, new SingleGestureListener());
        scaleDetector = new ScaleGestureDetector(this,
                new MultiGestureListener());

        screenBean = LocUtil.getScreenPix(this);
        if (path == "") {
            return;
        } else {
            mVideoView.setVideoPath(path);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.requestFocus();
            mVideoView
                    .setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.setPlaybackSpeed(1.0f);
                        }
                    });
        }
    }

    /** 定时隐藏 */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        finNum=event.getPointerCount();

        if (1 == finNum) {
            gestDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    endGesture();
            }
        } else if (2 ==finNum) {
            scaleDetector.onTouchEvent(event);
        }
        return true;
    }

    /** 手势结束 */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    /**
     * 视频缩放
     */
    public void changeLayout(int size) {
        mVideoView.setVideoLayout(size, 0);
    }

    /**
     * 声音大小
     *
     * @param percent
     */
    public void changeVolume(float percent) {
        if (mVolume == -1) {
            mVolume = LocUtil.getCurVolume(this);
            if (mVolume < 0)
                mVolume = 0;
            // 显示
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        LocUtil.setCurVolume(this, index);

        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 亮度大小
     *
     * @param percent
     */
    public void changeBrightness(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
            // 显示
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 单点触屏
     *
     * @author jin
     *
     */
    private class SingleGestureListener implements
            GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            Log.d("Fling", velocityY);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            if(2==finNum){
                return false;
            }

            float moldX = e1.getX();
            float moldY = e1.getY();
            float y = e2.getY();
            if (moldX > screenBean.getsWidth() * 9.0 / 10)// 右边滑动
                changeVolume((moldY - y) / screenBean.getsHeight());
            else if (moldX < screenBean.getsWidth() / 10.0)// 左边滑动
                changeBrightness((moldY - y) / screenBean.getsHeight());
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    /**
     * 多点缩放
     *
     * @author jin
     *
     */
    private class MultiGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            // 返回true ，才能进入onscale()函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            float oldDis = detector.getPreviousSpan();
            float curDis = detector.getCurrentSpan();
            if (oldDis - curDis > 50) {
                // 缩小
                changeLayout(0);
            } else if (oldDis - curDis < -50) {
                // 放大
                changeLayout(1);
            }
        }

    }




    /* public static final String VIDEO_PATH = "url";
   private VideoView mVideoView;
    private LinearLayout mLoadingLayout;
    private ImageView mLoadingImg;
    private ObjectAnimator mOjectAnimator;
    *//**
     * 当前进度
     *//*
    private Long currentPosition = (long) 0;
    private String mVideoPath = "";
    *//**
     * setting
     *//*
    private boolean needResume;

    *//**
     * 视频播放控制界面
     *//*
    CustomMediaController mediaController;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (!LibsChecker.checkVitamioLibs(this)) return;
        setContentView(R.layout.video_play_layout);
        mediaController = new CustomMediaController(this);
        getDataFromIntent();
        initviews();
        initVideoSettings();
    }

    private void getDataFromIntent() {
        Intent Intent = getIntent();
        if (Intent != null && Intent.getExtras().containsKey(VIDEO_PATH)) {
            mVideoPath = Intent.getExtras().getString(VIDEO_PATH);
        }
    }

    private void initviews() {
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_LinearLayout);
        mLoadingImg = (ImageView) findViewById(R.id.loading_image);
    }

    private void initVideoSettings() {
        mVideoView.requestFocus();
        mVideoView.setBufferSize(1024 * 1024);
        mVideoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoPath(mVideoPath);

    }

    public void onResume() {
        super.onResume();
        preparePlayVideo();
    }

    private void preparePlayVideo() {
        startLoadingAnimator();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO Auto-generated method stub
                stopLoadingAnimator();

                if (currentPosition > 0) {
                    mVideoView.seekTo(currentPosition);
                } else {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
                startPlay();
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
                arg0.getBufferProgress();
                Log.e("bum", "" + arg0.getBufferProgress());
                mediaController.setSecondaryProgress(arg0.getBufferProgress());
                switch (arg1) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始缓存，暂停播放
                        Log.e("bum", "开始缓存");
                        startLoadingAnimator();
                        if (mVideoView.isPlaying()) {
                            stopPlay();
                            needResume = true;
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //缓存完成，继续播放
                        stopLoadingAnimator();
                        if (needResume) startPlay();
                        Log.e("bum", "缓存完成");
                        break;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        //显示 下载速度
                        Log.e("bum","download rate:" + arg2);
                        break;
                }
                return true;
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Log.e("bum", "what=" + what);
                return false;
            }
        });
        mVideoView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
            }
        });
        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.e("bum", "percent" + percent);
                mediaController.setSecondaryProgress(percent);
            }
        });
    }

    @NonNull
    private void startLoadingAnimator() {
        if (mOjectAnimator == null) {
            mOjectAnimator = ObjectAnimator.ofFloat(mLoadingImg, "rotation", 0f, 360f);
        }
        mLoadingLayout.setVisibility(View.VISIBLE);

        mOjectAnimator.setDuration(1000);
        mOjectAnimator.setRepeatCount(-1);
        mOjectAnimator.start();
    }

    private void stopLoadingAnimator() {
        mLoadingLayout.setVisibility(View.GONE);
        mOjectAnimator.cancel();
    }

    private void startPlay() {
        mVideoView.start();
    }

    private void stopPlay() {
        mVideoView.pause();
    }

    public void onPause() {
        super.onPause();
        currentPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }*/

}
