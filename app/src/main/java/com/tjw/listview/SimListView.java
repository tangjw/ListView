package com.tjw.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * ^-^ Created by tang-jw on 12/20.
 */

public class SimListView extends ListView implements AbsListView.OnScrollListener {


    private Context mContext;
    private Scroller mScroller;
    private SimHeaderView mHeaderView;
    private int mHeaderViewHeight;
    private View mHeaderContent;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    private OnScrollListener mScrollListener; // user's scroll listener

    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull

    public SimListView(Context context) {
        this(context, null);
    }

    public SimListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public SimListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mScroller = new Scroller(mContext, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        mHeaderView = new SimHeaderView(mContext);
        mHeaderContent = mHeaderView.findViewById(R.id.container);

        //设置头布局不会响应 onItemClick
        addHeaderView(mHeaderView, null, false);

        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderContent.getHeight();
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        System.out.println("mHeaderViewHeight -> " + mHeaderViewHeight);
                    }
                });


    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (startY == -1) {

            startY = ev.getRawY();
        }

        System.out.println("startY -> " + startY);

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - startY;
                startY = ev.getRawY();
                System.out.println("deltaY -> " + deltaY);
                if (getFirstVisiblePosition() == 0
                        && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);

                }
                break;

            case MotionEvent.ACTION_UP:
                resetHeaderHeight();
                break;
        }


        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {

            mHeaderView.setVisibleHeight(mScroller.getCurrY());

            postInvalidate();

        }
        super.computeScroll();
    }


    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisibleHeight((int) delta + mHeaderView.getVisibleHeight());
        /*if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
                mHeaderView.setState(RListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(RListViewHeader.STATE_NORMAL);
            }
        }*/
        setSelection(0); // scroll to top each time
    }

    private void resetHeaderHeight() {
        int height = mHeaderView.getVisibleHeight();

        if (height == 0) {
            return;
        }

        mScroller.startScroll(0, height, 0, -height, 400);

        invalidate();
    }


}
