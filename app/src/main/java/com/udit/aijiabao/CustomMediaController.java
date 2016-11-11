package com.udit.aijiabao;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.udit.aijiabao.activitys.VideoViewActivity;
import com.udit.aijiabao.utils.T;

import java.io.File;

import io.vov.vitamio.widget.MediaController;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public class CustomMediaController extends MediaController {
    private Context mContext;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mVolume = -1;
    private float mBrightness = -1f;
    private GestureDetector mGestureDetector;

    VideoViewActivity activity;
    /**
     * public static final int VIDEO_LAYOUT_ORIGIN
     缩放参数，原始画面大小。
     常量值：0

     public static final int VIDEO_LAYOUT_SCALE
     缩放参数，画面全屏。
     常量值：1

     public static final int VIDEO_LAYOUT_STRETCH
     缩放参数，画面拉伸。
     常量值：2

     public static final int VIDEO_LAYOUT_ZOOM
     缩放参数，画面裁剪。
     常量值：3
     */
    private String[] strDialogs=new String[]{"100%","全屏","拉伸","裁剪"};
    private int[] imgs=new int[]{R.mipmap.mediacontroller_sreen_size_100,R.mipmap.mediacontroller_screen_fit,R.mipmap.mediacontroller_screen_size,R.mipmap.mediacontroller_sreen_size_crop};
    private int mCurrentPageSize=2;
    private TextView currenttime_tv;
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mVolumeBrightnessLayout.setVisibility(View.GONE);
            }
        }
    };

    public CustomMediaController(Context context) {
        super(context);
        this.mContext = context;
        activity=(VideoViewActivity)context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mGestureDetector = new GestureDetector(mContext, new VolumeBrightnesGestureListener());
    }

    @Override
    protected View makeControllerView() {
        return ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(getResources().getIdentifier("mediacontroller", "layout", mContext.getPackageName()), this);
    }

    @Override
    protected void initOtherView() {
        currenttime_tv=(TextView) mRoot.findViewById(R.id.currenttime_tv);

        mVolumeBrightnessLayout = mRoot.findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) mRoot.findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) mRoot.findViewById(R.id.operation_percent);
        mRoot.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                }
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }
                return false;
            }
        });
    }

    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    private class VolumeBrightnesGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();
            if (mOldX > windowWidth * 4.0 / 5) {
                onVolumeSlide((mOldY - y) / windowHeight);
                return true;
            } else if (mOldX < windowWidth / 5.0) {
                onBrightnessSlide((mOldY - y) / windowHeight);
                return true;
            }
            return false;
        }
    }

    /**
     * 声音高低
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            mOperationBg.setImageResource(R.mipmap.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 处理屏幕亮暗
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = ((Activity) mContext).getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            mOperationBg.setImageResource(R.mipmap.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = ((Activity) getContext()).getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        ((Activity) mContext).getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

}
