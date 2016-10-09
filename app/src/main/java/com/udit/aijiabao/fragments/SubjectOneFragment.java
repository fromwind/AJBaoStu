package com.udit.aijiabao.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.udit.aijiabao.R;
import com.udit.aijiabao.activitys.AnalogyExaminationActivity;
import com.udit.aijiabao.madapters.LocalImageHolderView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */

@ContentView(R.layout.fragment_subject)
public class SubjectOneFragment extends Fragment {

    private final String TAG = SubjectOneFragment.class.getSimpleName();

    private static SubjectOneFragment oneFragment;


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

    private void initBanner(){
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int windowsHeight = metric.heightPixels;

        ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.height = windowsHeight / 4;
        banner.setLayoutParams(params);
    }

    @Event(value = {R.id.order_practice,
    R.id.typicla_practice,R.id.random_practice,R.id.specail_practice},type = View.OnClickListener.class)
    private void mClick(View view){
        switch (view.getId()){
            case R.id.order_practice:
                startActivity(new Intent(getActivity(), AnalogyExaminationActivity.class));
                break;
            case R.id.typicla_practice:
                break;
            case R.id.random_practice:
                break;
            case R.id.specail_practice:
                break;
        }
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
}
