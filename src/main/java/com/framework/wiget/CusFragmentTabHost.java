package com.framework.wiget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lichen on 2018/6/6.
 */

public class CusFragmentTabHost extends RelativeLayout implements View.OnClickListener {

    public FrameLayout mFrameLayout;
    public LinearLayout mRelativeLayout;
    public Context mContext;
    public FragmentManager mFragmentManager;
    public boolean mAttached;
    public TabInfo mLastTab;
    public int mCurrentTab;
    public final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    public TabHostListener tabHostListener;

    private int indexLayout;
    private int containarLayout;

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tabHostListener != null) {
            if (!tabHostListener.onTabClick(tag)) {
                return;
            }
        }
        setCurrentTabByTag(tag);
    }


    public interface TabHostListener {
        /**
         * 切换时触发
         */
        void onTabChange(int postion, String tag);

        /**
         * 点击时触发,返回true则进行fragment切换,false则不切换
         */
        boolean onTabClick(String tag);
    }

    public static final class TabInfo {
        public final String tag;
        public final Class<?> clss;
        public final Bundle args;
        public Fragment fragment;
        public final int indicatorResId;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            this(_tag, _class, _args, 0);
        }

        TabInfo(String _tag, Class<?> _class, Bundle _args, int _indicatorResId) {
            tag = _tag;
            clss = _class;
            args = _args;
            indicatorResId = _indicatorResId;
        }
    }

    public CusFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFragmentTabHost();
    }

    private void initFragmentTabHost() {
        mCurrentTab = -1;
//        setOrientation(LinearLayout.VERTICAL);
    }

    public void setUp(Context context, FragmentManager manager, int indexLayout, int containarLayout) {
        this.containarLayout = containarLayout;
        mRelativeLayout = (LinearLayout) findViewById(indexLayout);
        if (mRelativeLayout == null) {
            throw new RuntimeException("Your FragmentTabHost must have a RelativeLayout whose id attribute is 'R.id.tab_host'");
        }
        mFrameLayout = (FrameLayout) findViewById(containarLayout);
        if (mFrameLayout == null) {
            throw new RuntimeException("Your FragmentTabHost must have a FrameLayout whose id attribute is 'R.id.continar'");
        }
        mContext = context;
        mFragmentManager = manager;
    }

    public void addTab(String tag, Class<?> clss, Bundle args, int indicatorResId) {
        TabInfo info = new TabInfo(tag, clss, args, indicatorResId);
        boolean needSelect = false;
        if (mAttached) {
            info.fragment = mFragmentManager.findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                if (info.fragment.isVisible()) {
                    needSelect = true;
                }
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.detach(info.fragment);
                info.fragment = null;
                ft.commitAllowingStateLoss();
            }
        }
        removeTag(tag);
        mTabs.add(info);
        if (mCurrentTab == -1) {
            mCurrentTab = 0;
            findViewById(indicatorResId).setSelected(true);
        }
        if (needSelect) {
            setCurrentTabByTag(tag);
        }
        findViewById(indicatorResId).setTag(tag);
        findViewById(indicatorResId).setOnClickListener(this);
    }

    private boolean removeTag(String tag) {
        Iterator<TabInfo> iterator = mTabs.iterator();
        while (iterator.hasNext()) {
            TabInfo info = iterator.next();
            if (info.tag.equals(tag)) {
                iterator.remove();
                return true;
            }

        }
        return false;
    }

    public void addTab(String tag, Class<?> clss, Bundle args) {
        addTab(tag, clss, args, 0);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttached = true;
        if (isInEditMode()) {
            return;
        }
        String currentTab = getCurrentTabTag();
        FragmentTransaction ft = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if (tab.fragment != null && !tab.fragment.isDetached()) {
                if (tab.tag.equals(currentTab)) {
                    mLastTab = tab;
                } else {
                    if (ft == null) {
                        ft = mFragmentManager.beginTransaction();
                    }
                    ft.detach(tab.fragment);
                    tab.fragment = null;
                }
            }
        }
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            ft.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    public void setCurrentTabByTag(String tag) {
        int i;
        FragmentTransaction ft = null;
        for (i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i).tag.equals(tag)) {
                ft = doTabChanged(tag, null);
                mCurrentTab = i;
                break;
            }
        }
        if (ft != null) {
            ft.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
            if (tabHostListener != null) {
                tabHostListener.onTabChange(i, tag);
            }

            for (i = 0; i < mTabs.size(); i++) {
                if (mTabs.get(i).indicatorResId > 0) {
                    View tab = findViewById(mTabs.get(i).indicatorResId);
                    if (tab != null) {
                        if (mCurrentTab == i) {
                            findViewById(mTabs.get(i).indicatorResId).setSelected(true);
                        } else {
                            findViewById(mTabs.get(i).indicatorResId).setSelected(false);
                        }
                    }
                }
            }
        }
    }

    public FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.hide(mLastTab.fragment);
                }
            }
//                ft.setCustomAnimations(R.anim.fragment_tab_in, R.anim.fragment_tab_out);
            if (newTab.fragment == null) {
                newTab.fragment = Fragment.instantiate(mContext, newTab.clss.getName(), newTab.args);
                deleteOldFragment(newTab.tag, ft);
                ft.add(containarLayout, newTab.fragment, newTab.tag);
            } else {
                ft.show(newTab.fragment);
            }
            mLastTab = newTab;
        }
        return ft;
    }


    private void deleteOldFragment(String tag, FragmentTransaction ft) {
        Fragment f = mFragmentManager.findFragmentByTag(tag);
        if (f != null) {
            ft.remove(f);
        }
    }

    public String getCurrentTabTag() {
        if (mCurrentTab >= 0 && mCurrentTab < mTabs.size()) {
            return mTabs.get(mCurrentTab).tag;
        }
        return null;
    }

    public TabInfo getCurrentTab() {
        return mLastTab;
    }

    public TabHostListener getTabHostListener() {
        return tabHostListener;
    }

    public void setTabHostListener(TabHostListener tabHostListener) {
        this.tabHostListener = tabHostListener;
    }
}
