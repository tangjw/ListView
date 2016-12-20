package com.tjw.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * ^-^ Created by tang-jw on 12/20.
 */

public class SimHeaderView extends FrameLayout {


    private ImageView mIvSky;
    private ImageView mIvBuildings;
    private ImageView mIvSun;
    private RelativeLayout mContainer;
    private View mInflate;

    public SimHeaderView(Context context) {
        this(context, null);
    }

    public SimHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mInflate = inflate(getContext(), R.layout.header_simlist, null);
        //初始化设置头布局高度为 0.
        addView(mInflate, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        mIvSky = (ImageView) findViewById(R.id.iv_sky);
        mIvBuildings = (ImageView) findViewById(R.id.iv_buildings);
        mIvSun = (ImageView) findViewById(R.id.iv_sun);
        mContainer = (RelativeLayout) findViewById(R.id.container);
    }

    public void setVisibleHeight(int height) {
        height = height < 0 ? 0 : height;
        LayoutParams lp =  (FrameLayout.LayoutParams)mInflate.getLayoutParams();
        lp.height = height;
        mInflate.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        return mInflate.getLayoutParams().height;
    }

}
