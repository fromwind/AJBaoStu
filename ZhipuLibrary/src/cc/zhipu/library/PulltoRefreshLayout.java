package cc.zhipu.library;

import android.app.Activity;
import android.text.format.DateUtils;

import cc.zhipu.library.scrollview.ILoadingLayout;
import cc.zhipu.library.scrollview.PullToRefreshBase;
import cc.zhipu.library.scrollview.PullToRefreshExpandableListView;
import cc.zhipu.library.scrollview.PullToRefreshListView;
import cc.zhipu.library.scrollview.PullToRefreshScrollView;


/**
 * Created by Administrator on 2015/11/9 0009.
 */
public class PulltoRefreshLayout {

    public static PulltoRefreshLayout pulltoRefreshLayout;

    public PulltoRefreshLayout() {
    }

    public static PulltoRefreshLayout newInstance() {
        if (pulltoRefreshLayout == null) {
            return new PulltoRefreshLayout();

        }
        return pulltoRefreshLayout;

    }

    public void initLayoutScrollView(PullToRefreshScrollView pullToRefreshScrollView, Activity activity) {

        ILoadingLayout layout = pullToRefreshScrollView.getLoadingLayoutProxy(false, true);
        layout.setRefreshingLabel("正在加载中...");
        layout.setReleaseLabel("释放加载");
        layout.setPullLabel("上拉加载");
        layout.setLastUpdatedLabel(DateUtils.formatDateTime(
                activity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

        ILoadingLayout layout2 = pullToRefreshScrollView.getLoadingLayoutProxy(true, false);
        layout2.setRefreshingLabel("正在刷新中...");
        layout2.setPullLabel("下拉刷新");
        layout2.setReleaseLabel("释放刷新");

        layout2.setLastUpdatedLabel(DateUtils.formatDateTime(
                activity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

    }

    //可以全部用这个不需要单独写，泛型多肽
    public void initLayoutExpandListView(PullToRefreshBase pullToRefreshScrollView, Activity activity) {

        ILoadingLayout layout = pullToRefreshScrollView.getLoadingLayoutProxy(false, true);
        layout.setRefreshingLabel("正在加载中...");
        layout.setReleaseLabel("释放加载");
        layout.setPullLabel("上拉加载");
        layout.setLastUpdatedLabel(DateUtils.formatDateTime(
                activity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

        ILoadingLayout layout2 = pullToRefreshScrollView.getLoadingLayoutProxy(true, false);
        layout2.setRefreshingLabel("正在刷新中...");
        layout2.setPullLabel("下拉刷新");
        layout2.setReleaseLabel("释放刷新");

        layout2.setLastUpdatedLabel(DateUtils.formatDateTime(
                activity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

    }


    public void initLayoutListView(PullToRefreshListView pullToRefreshListView, Activity activity) {
        ILoadingLayout layout = pullToRefreshListView.getLoadingLayoutProxy(false, true);
        layout.setRefreshingLabel("正在加载中...");
        layout.setReleaseLabel("释放加载");
        layout.setPullLabel("上拉加载");
        layout.setLastUpdatedLabel(DateUtils.formatDateTime(
                activity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

        ILoadingLayout layout2 = pullToRefreshListView.getLoadingLayoutProxy(true, false);
        layout2.setRefreshingLabel("正在刷新中...");
        layout2.setPullLabel("下拉刷新");
        layout2.setReleaseLabel("释放刷新");

        layout2.setLastUpdatedLabel(DateUtils.formatDateTime(
                activity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

    }

    /**
     * 当前库没有PullNestedScrolView这个类
     * @param pullToRefreshExpandableListView
     * @param activity
     */

//    public void initLayoutNestedScrolView(PullNestedScrolView pullNestedScrolView, Activity activity) {
//
//        if (pullNestedScrolView != null) {
//
//            ILoadingLayout layout = pullNestedScrolView.getLoadingLayoutProxy(false, true);
//            layout.setRefreshingLabel("正在加载中...");
//            layout.setReleaseLabel("释放加载");
//            layout.setPullLabel("上拉加载");
//            layout.setLastUpdatedLabel(DateUtils.formatDateTime(
//                    activity, System.currentTimeMillis(),
//                    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
//                            | DateUtils.FORMAT_ABBREV_ALL));
//
//            ILoadingLayout layout2 = pullNestedScrolView.getLoadingLayoutProxy(true, false);
//            layout2.setRefreshingLabel("正在刷新中...");
//            layout2.setPullLabel("下拉刷新");
//            layout2.setReleaseLabel("释放刷新");
//
//            layout2.setLastUpdatedLabel(DateUtils.formatDateTime(
//                    activity, System.currentTimeMillis(),
//                    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
//                            | DateUtils.FORMAT_ABBREV_ALL));
//        }
//    }


    public void initLayoutExpandableListview(PullToRefreshExpandableListView pullToRefreshExpandableListView, Activity activity) {
        ILoadingLayout layout = pullToRefreshExpandableListView.getLoadingLayoutProxy(false, true);
        layout.setRefreshingLabel("正在加载中...");
        layout.setReleaseLabel("释放加载");
        layout.setPullLabel("上拉加载");
        layout.setLastUpdatedLabel(DateUtils.formatDateTime(
                activity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

        ILoadingLayout layout2 = pullToRefreshExpandableListView.getLoadingLayoutProxy(true, false);
        layout2.setRefreshingLabel("正在刷新中...");
        layout2.setPullLabel("下拉刷新");
        layout2.setReleaseLabel("释放刷新");

        layout2.setLastUpdatedLabel(DateUtils.formatDateTime(
                activity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

    }


}
