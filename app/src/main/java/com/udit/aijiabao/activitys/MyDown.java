package com.udit.aijiabao.activitys;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Gravity;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_my_down)
public class MyDown extends BaseActivity {
    @ViewInject(R.id.collapsing_toolbar)
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        collapsingToolbarLayout.setTitle("18404972477");
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
