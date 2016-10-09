package com.udit.aijiabao.tabfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.udit.aijiabao.R;

import org.xutils.view.annotation.ContentView;


/**
 * Created by Administrator on 2016/5/11.
 */
@ContentView(R.layout.infofragment_layout000)
public class InfoFragment0 extends Fragment {

    private final String TAG = InfoFragment0.class.getSimpleName();

    private static InfoFragment0 infoFragment;

    public static InfoFragment0 newInstance() {

        if (null == infoFragment) {
            infoFragment = new InfoFragment0();
        }
        return infoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView imageView=new ImageView(getActivity());


        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        imageView.setLayoutParams(params);

        imageView.setImageResource(R.mipmap.info);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return imageView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("YANG", "知识onResume");
    }

}
