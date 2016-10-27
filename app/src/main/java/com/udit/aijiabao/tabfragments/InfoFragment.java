package com.udit.aijiabao.tabfragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.udit.aijiabao.R;
import com.udit.aijiabao.fragments.SubjectFourFragment;
import com.udit.aijiabao.fragments.SubjectOneFragment;
import com.udit.aijiabao.fragments.SubjectThreeFragment;
import com.udit.aijiabao.fragments.SubjectTwoFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
@ContentView(R.layout.infofragment_layout)
public class InfoFragment extends Fragment {

    private final String TAG = InfoFragment.class.getSimpleName();

    private static InfoFragment infoFragment;

    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.tab_one)
    private TextView tab_one;

    @ViewInject(R.id.tab_two)
    private TextView tab_two;

    @ViewInject(R.id.tab_three)
    private TextView tab_three;

    @ViewInject(R.id.tab_four)
    private TextView tab_four;

    private PageAdapter pageAdapter;

    @ViewInject(R.id.img_tabLine)
    private ImageView line;


    private DisplayMetrics dm = new DisplayMetrics();

    private int tabWidth;


    public static InfoFragment newInstance() {

        if (null == infoFragment) {
            infoFragment = new InfoFragment();
        }
        return infoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //设置系统栏设置颜色
            //tintManager.setTintColor(R.color.red);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.blue_title);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
            tintManager.setNavigationBarTintResource(R.color.blue_title);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPageData();
        resetTab();
        tab_one.setTextColor(getResources().getColor(R.color.blue_title));
        tab_one.setBackgroundResource(R.color.gray_10);
        viewPager.setCurrentItem(0);

        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("YANG", "知识onResume");
    }

    private void initPageData() {
        pageAdapter = new PageAdapter(getChildFragmentManager());
        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Matrix matrix = new Matrix();

                // 设置激活条的最终位置
                switch (position) {
                    case 0:
                        // 使用set直接设置到那个位置
                        matrix.setTranslate(0, 0);
                        break;
                    case 1:
                        matrix.setTranslate(tabWidth, 0);
                        break;

                    case 2:
                        matrix.setTranslate(2 * tabWidth, 0);
                        break;

                    case 3:
                        matrix.setTranslate(3 * tabWidth, 0);
                        break;
                }
                // 在滑动的过程中，计算出激活条应该要滑动的距离
                float t = (tabWidth) * positionOffset;
                // 使用post追加数值
                matrix.postTranslate(t, 0);
                line.setImageMatrix(matrix);

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("Bum_pageSelected", "onPageSelected--" + position);

                switch (position) {
                    case 0:
                        resetTab();
                        tab_one.setTextColor(getResources().getColor(R.color.blue_title));
                        tab_one.setBackgroundResource(R.color.gray_10);
                        break;

                    case 1:
                        resetTab();
                        tab_two.setTextColor(getResources().getColor(R.color.blue_title));
                        tab_two.setBackgroundResource(R.color.gray_10);
                        break;

                    case 2:
                        resetTab();
                        tab_three.setTextColor(getResources().getColor(R.color.blue_title));
                        tab_three.setBackgroundResource(R.color.gray_10);
                        break;

                    case 3:
                        resetTab();
                        tab_four.setTextColor(getResources().getColor(R.color.blue_title));
                        tab_four.setBackgroundResource(R.color.gray_10);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetTab() {

        tab_one.setTextColor(getResources().getColor(R.color.gray_80));
        tab_two.setTextColor(getResources().getColor(R.color.gray_80));
        tab_three.setTextColor(getResources().getColor(R.color.gray_80));
        tab_four.setTextColor(getResources().getColor(R.color.gray_80));

        tab_one.setBackgroundResource(R.color.white);
        tab_two.setBackgroundResource(R.color.white);
        tab_three.setBackgroundResource(R.color.white);
        tab_four.setBackgroundResource(R.color.white);

    }

    private void initData() {
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        tabWidth = dm.widthPixels / 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.tab_line);
        // /Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, tabWidth, 8);
        // 设置tab的宽度和高度
        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, tabWidth, 8, true);
        line.setImageBitmap(bitmap2);
    }

    @Event(value = {R.id.tab_one,
             R.id.tab_two,
           R.id.tab_three,
            R.id.tab_four}, type = View.OnClickListener.class)
    private void clickTab(View view) {

        switch (view.getId()) {

            case R.id.tab_one:

                resetTab();
                tab_one.setTextColor(getResources().getColor(R.color.blue_title));
                viewPager.setCurrentItem(0);
                break;

            case R.id.tab_two:
                resetTab();
                tab_two.setTextColor(getResources().getColor(R.color.blue_title));

                viewPager.setCurrentItem(1);
                break;

            case R.id.tab_three:
                resetTab();
                tab_three.setTextColor(getResources().getColor(R.color.blue_title));
                viewPager.setCurrentItem(2);
                break;

            case R.id.tab_four:
                resetTab();
                tab_four.setTextColor(getResources().getColor(R.color.blue_title));
                viewPager.setCurrentItem(3);
                break;

        }

    }

    private class PageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        private String tabTitles[] = new String[]{
                "科目一",
                "科目二",
                "科目三",
                "科目四"};

        public PageAdapter(FragmentManager fm) {
            super(fm);

            fragments = new ArrayList<>();

            fragments.add(SubjectOneFragment.newInstance());

            fragments.add(SubjectTwoFragment.newInstance());

            fragments.add(SubjectThreeFragment.newInstance());

            fragments.add(SubjectFourFragment.newInstance());

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

}
