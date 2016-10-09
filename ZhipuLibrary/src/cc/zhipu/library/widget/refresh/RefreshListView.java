package cc.zhipu.library.widget.refresh;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.zhipu.library.R;

import cc.zhipu.library.utils.Helper;

public class RefreshListView extends ListView implements OnScrollListener {

	private static final String TAG = "RefreshableListView";
	private final static int SCROLL_BACK_HEADER = 0;
	private final static int SCROLL_BACK_FOOTER = 1;
	private final static int SCROLL_DURATION = 300; // scroll back duration
	private final static float OFFSET_RADIO = 2.0f; // support iOS like pull
	private static int PULL_LOAD_MORE_DELTA;// when pull up >= 50px
	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener
	private IXListViewListener mListViewListener;
	// -- header view
	private RefreshHeaderView mHeaderView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.
	// -- footer view
	private RefreshFooterView mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;
	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;
	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	// feature.
	private boolean isStart = false;

	/**
	 * @param context
	 */
	public RefreshListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		PULL_LOAD_MORE_DELTA = Helper.dip2px(context, 50);
		setCacheColorHint(Color.TRANSPARENT);
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);
		// init header view
		mHeaderView = new RefreshHeaderView(context);
		addHeaderView(mHeaderView, null, false);
		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderView.findViewById(
								R.id.refreshable_list_head_content_layoput)
								.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						Log.i(TAG, "mHeaderViewHeight=" + mHeaderViewHeight);
					}
				});
		// init footer view
		mFooterView = new RefreshFooterView(context);
		setHeaderDividersEnabled(false);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (!mIsFooterReady) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * enable or disable pull down refresh feature.
	 *
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderView.setContainerVisibility(INVISIBLE);
		} else {
			mHeaderView.setContainerVisibility(VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(RefreshFooterView.STATE_NORMAL);
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	public void stopRefresh() {
		if (mPullRefreshing) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	public void stopLoadMore() {
		if (mPullLoading) {
			mPullLoading = false;
			mFooterView.setState(RefreshFooterView.STATE_NORMAL);
		}
	}

	public void setRefreshTime(String time) {
		mHeaderView.setRefreshTime(time);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisibleHeight((int) delta
				+ mHeaderView.getVisibleHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
				mHeaderView.setState(RefreshHeaderView.STATE_READY);
			} else {
				mHeaderView.setState(RefreshHeaderView.STATE_NORMAL);
			}
		}
		setSelection(0);
	}

	public void resetHeaderHeight() {
		int height = mHeaderView.getVisibleHeight();
		// refreshing and header isn't shown fully. do nothing.
		if (height == 0 || (mPullRefreshing && height <= mHeaderViewHeight))
			return;
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLL_BACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) {
				mFooterView.setState(RefreshFooterView.STATE_READY);
			} else {
				mFooterView.setState(RefreshFooterView.STATE_NORMAL);
			}
		}
		if (mEnablePullLoad)
			mFooterView.setBottomMargin(height);
	}

	public void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLL_BACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(RefreshFooterView.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	public void startRefresh() {
		if (mHeaderViewHeight != 0) {
			isStart = true;
			mEnablePullRefresh = true;
			mScroller.startScroll(0, 0, 0, mHeaderViewHeight, SCROLL_DURATION);
			postInvalidate();
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startRefresh();
				}
			}, 50);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				if (getFirstVisiblePosition() == 0
						&& (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
					// the first item is showing, header has shown or pull down.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					// last item, already pulled up or want to pull up.
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				invokeOnScrolling();
				break;
			default:
				mLastY = -1; // reset
				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh
							&& mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
						mPullRefreshing = true;
						mHeaderView
								.setState(RefreshHeaderView.STATE_REFRESHING);
						if (mListViewListener != null) {
							mListViewListener.onRefresh();
						}
					}
					resetHeaderHeight();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad
							&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
							&& !mPullLoading) {
						startLoadMore();
					}
					resetFooterHeight();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLL_BACK_HEADER) {
				mHeaderView.setVisibleHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		if (isStart) {
			if (mHeaderView.getVisibleHeight() == mHeaderViewHeight) {
				isStart = false;
				mPullRefreshing = true;
				mHeaderView.setState(RefreshHeaderView.STATE_REFRESHING);
				if (mListViewListener != null) {
					mListViewListener.onRefresh();
				}
				resetHeaderHeight();
			}
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
	                     int visibleItemCount, int totalItemCount) {
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}
}
