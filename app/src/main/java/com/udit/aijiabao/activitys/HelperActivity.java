package com.udit.aijiabao.activitys;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/5/17.
 */

@ContentView(R.layout.activity_help)
public class HelperActivity extends BaseActivity {

    @ViewInject(R.id.titleView)
    private TitleView titleView;

    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {
        titleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        titleView.setTitleText("帮助");
        titleView.setTitleTextColor(getResources().getColor(R.color.white));

        titleView.showBack(true);
        titleView.hideOperate();
    }

    @Override
    public void initData() {

    }
}
