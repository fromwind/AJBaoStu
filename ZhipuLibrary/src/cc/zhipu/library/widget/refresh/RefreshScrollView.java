package cc.zhipu.library.widget.refresh;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.zhipu.library.R;

/**
 * Created by Administrator on 2015/5/29.
 */
public class RefreshScrollView extends ScrollView {
	private static final String TAG = "RefreshScrollView";

	private final static int SCROLL_DURATION = 400;
	private final static float OFFSET_RADIO = 1.8f;
	private int headerHeight = 0;
	private boolean enableRefresh = true;
	private boolean refreshing = false;
	private float lastY = -1;
	private Scroller scroller = null;
	private OnRefreshScrollViewListener listener = null;
	private LinearLayout scrollContainer = null;
	private RefreshHeaderView headerView = null;
	private boolean isStart = false;

	public RefreshScrollView(Context context) {
		this(context, null);
	}

	public RefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			initView(context);
		}
	}

	public RefreshScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode()) {
			initView(context);
		}
	}

	/**
	 * 初始化view
	 */
	private void initView(Context context) {
		scroller = new Scroller(context);
		headerView = new RefreshHeaderView(context);
		headerView.setBackgroundColor(Color.WHITE);
//		headerView.setContainerVisibility(View.INVISIBLE);//其他地方使用需要删除
		
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				setHeaderView();
			}
		});
	}

	public void setHeaderView() {
		LinearLayout.LayoutParams headerViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		scrollContainer = (LinearLayout) getChildAt(0);
		scrollContainer.addView(headerView, 0, headerViewParams);
		//提前获取headerView的高度
		headerView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						headerHeight = headerView.findViewById(
								R.id.refreshable_list_head_content_layoput)
								.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	/**
	 * 设置scroll是否可以刷新
	 *
	 * @param enableRefresh
	 */
	public void setEnableRefresh(boolean enableRefresh) {
		this.enableRefresh = enableRefresh;
		headerView.setContainerVisibility(enableRefresh ? VISIBLE : INVISIBLE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (lastY == -1)
			lastY = ev.getRawY();
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				lastY = (int) ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int deltY = (int) (ev.getRawY() - lastY);
				lastY = (int) ev.getRawY();
				if (getScrollY() == 0
						&& (deltY > 0 || headerView.getVisibleHeight() > 0)) {
					updateHeader(deltY / OFFSET_RADIO);
					return true;
				}
				break;
			default:
				lastY = -1;
				//这里没有使用action_up的原因是，可能会受到viewpager的影响接收到action_cacel事件
				if (getScrollY() == 0) {
					if (headerView.getVisibleHeight() > headerHeight && enableRefresh && !refreshing) {
						refreshing = true;
						headerView.setState(RefreshHeaderView.STATE_REFRESHING);
						if (listener != null) {
							listener.onRefresh();
						}
					}
				}
				resetHeaderView();
				break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 更新headerview的高度,同时更改状态
	 *
	 * @param deltY
	 */
	public void updateHeader(float deltY) {
		int currentMargin = (int) (headerView.getVisibleHeight() + deltY);
		headerView.setVisibleHeight(currentMargin);
		if (enableRefresh && !refreshing) {
			if (currentMargin > headerHeight) {
				headerView.setState(RefreshHeaderView.STATE_READY);
			} else {
				headerView.setState(RefreshHeaderView.STATE_NORMAL);
			}
		}
	}

	/**
	 * 重置headerview的高度
	 */
	public void resetHeaderView() {
		int height = headerView.getVisibleHeight();
		if (height <= 0 || (refreshing && height <= headerHeight)) {
			return;
		}
		int finalHeight = 0;
		if (height > headerHeight && refreshing) {
			finalHeight = headerHeight;
		}
		//松开刷新，或者下拉刷新，又松手，没有触发刷新
		scroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		invalidate();
	}

	/**
	 * 开始刷新
	 */
	public void startRefresh() {
		scroller.startScroll(0, getScrollY(), 0, headerHeight, SCROLL_DURATION);
		isStart = true;
		postInvalidate();
	}

	/**
	 * 停止刷新
	 */
	public void stopRefresh() {
		if (refreshing) {
			refreshing = false;
			resetHeaderView();
		}
	}

	@Override
	public void computeScroll() {
		if (isInEditMode()) {
			super.computeScroll();
			return;
		}
		if (scroller.computeScrollOffset()) {
			headerView.setVisibleHeight(scroller.getCurrY());
			postInvalidate();//继续重绘
		}
		if (isStart) {
			int height = headerView.getVisibleHeight();
			if (height == headerHeight) {
				isStart = false;
				refreshing = true;
				headerView.setState(RefreshHeaderView.STATE_REFRESHING);
				if (listener != null) {
					invalidate();
					listener.onRefresh();
				}
			}
		}
		super.computeScroll();
	}

	public void setOnRefreshScrollViewListener(OnRefreshScrollViewListener listener) {
		this.listener = listener;
	}

	public interface OnRefreshScrollViewListener {
		public void onRefresh();
	}
}
