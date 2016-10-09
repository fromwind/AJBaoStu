package com.udit.aijiabao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/5/18.
 */

@ContentView(R.layout.activity_message_detai)
public class MessageDetaiActivity extends BaseActivity {

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.content)
    private TextView content;

    @ViewInject(R.id.time)
    private TextView time;

    String a;
    @Override
    public void setContentView() {

        Bundle bundle=getIntent().getBundleExtra("bundle");

        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("消息详情");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));

        mtitleView.setBackOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(8);
                finish();
                overridePendingTransition(R.anim.push_left_to_right_in,
                        R.anim.push_left_to_right_out);
            }
        });

        time.setText(" "+bundle.getString("time"));
        a=bundle.getString("content").trim();
        int ss=a.indexOf("：",0);
        int dd=a.indexOf("：",ss);
        int cc=a.indexOf("：",dd+3);
        int ff=a.indexOf("：",cc+1);
        int gg=a.indexOf("：",ff+26);
        int tt=a.indexOf("：",gg+4);
        /*Log.e("Bum01",a.substring(1,ss-1));
        Log.e("Bum02",a.substring(dd+1,dd+3));
        Log.e("Bum03",a.substring(cc+2,ff-5));
        Log.e("Bum04",a.substring(ff+2,gg-4));
        Log.e("Bum05",a.substring(gg+2,tt-4));
        Log.e("Bum06",a.substring(tt+2,a.length()-1));*/
        content.setText(a.substring(1,ss-1)+":"+"\n"+"学员"+":"+a.substring(cc+2,ff-5)+"      教练："+a.substring(gg+2,tt-4)+"\n"+"时间："+a.substring(ff+2,gg-5)+"\n"+"车牌号："+a.substring(tt+2,a.length()-1));
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
