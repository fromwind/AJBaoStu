package com.udit.aijiabao.activitys;

import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
@ContentView(R.layout.activity_apply_know)
public class ApplyKnow extends BaseActivity {
    @ViewInject(R.id.apply_know_titleView)
    private TitleView mtitleView;

    private View view1, view2, view3, view4;
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList;//view数组
    private List<String> titleList;  //标题列表数组
    private TabLayout tabLayout;

    @Override
    public void setContentView() {
        viewPager = (ViewPager) findViewById(R.id.apply_know_viewPager);
        tabLayout= (TabLayout) findViewById(R.id.apply_know_tabLayout);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.apply_know_age, null);
        view2 = inflater.inflate(R.layout.apply_know_body, null);
        view3 = inflater.inflate(R.layout.apply_know_notice, null);
        view4 = inflater.inflate(R.layout.apply_know_prepare, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);

        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("年龄要求");
        titleList.add("身体条件");
        titleList.add("注意事项");
        titleList.add("准备材料");
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("报名须知");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setTag(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setTag(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setTag(titleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setTag(titleList.get(3)));
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    //Key是作为返回值与当前装入Container中的视图对应起来
    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            //根据传来的key，找到view,判断与传来的参数View arg0是不是同一个视图
            return arg0 == viewList.get(Integer.parseInt(arg1.toString()));
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(viewList.get(position));
            //把当前新增视图的位置（position）作为Key传过去
            return position;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //根据key返回PageTitle的文字
            //采用SpannableStringBuilder来构造了下包含图片的扩展String对像
            SpannableStringBuilder ssb = new SpannableStringBuilder("  "+titleList.get(position));
            // space added before text for
            Drawable myDrawable = getResources().getDrawable(
                    R.mipmap.d00);
            myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(),
                    myDrawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(myDrawable,
                    ImageSpan.ALIGN_BASELINE);

            ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.blue_title));// 字体颜色设置为绿色
            ssb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置图标
            ssb.setSpan(fcs, 1, ssb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置字体颜色
            ssb.setSpan(new RelativeSizeSpan(1.2f), 1, ssb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return titleList.get(position);
        }
    };
}
