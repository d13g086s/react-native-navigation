package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.anim.TopBarCollapseBehavior;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Button;
import com.reactnativenavigation.parse.Color;
import com.reactnativenavigation.parse.Number;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsViewPager;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class TopBar extends AppBarLayout implements ScrollEventListener.ScrollAwareView {
    private final Toolbar titleBar;
    private TitleBarButton.OnClickListener onClickListener;
    private final TopBarCollapseBehavior collapsingBehavior;
    private final TopBarAnimator animator;
    private TopTabs topTabs;

    public TopBar(final Context context, View contentView, ScrollEventListener scrollEventListener, TitleBarButton.OnClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;
        collapsingBehavior = new TopBarCollapseBehavior(this, scrollEventListener);
        titleBar = new Toolbar(context);
        topTabs = new TopTabs(getContext());
        this.animator = new TopBarAnimator(this, contentView);
        addView(titleBar);
    }

    public void setTitle(String title) {
        titleBar.setTitle(title);
    }

    public String getTitle() {
        return titleBar.getTitle() != null ? titleBar.getTitle().toString() : "";
    }

    public void setTitleTextColor(@ColorInt int color) {
        titleBar.setTitleTextColor(color);
    }

    public void setTitleFontSize(float size) {
        TextView titleTextView = getTitleTextView();
        if (titleTextView != null) {
            titleTextView.setTextSize(size);
        }
    }

    public void setTitleTypeface(Typeface typeface) {
        TextView titleTextView = getTitleTextView();
        if (titleTextView != null) {
            titleTextView.setTypeface(typeface);
        }
    }

    public void setTopTabFontFamily(int tabIndex, Typeface fontFamily) {
        topTabs.setFontFamily(tabIndex, fontFamily);
    }

    public void applyTopTabsColors(Color selectedTabColor, Color unselectedTabColor) {
        topTabs.applyTopTabsColors(selectedTabColor, unselectedTabColor);
    }

    public void applyTopTabsFontSize(Number fontSize) {
        topTabs.applyTopTabsFontSize(fontSize);
    }

    public void setButtons(ArrayList<Button> leftButtons, ArrayList<Button> rightButtons) {
        setLeftButtons(leftButtons);
        setRightButtons(rightButtons);
    }

    public TextView getTitleTextView() {
        return findTextView(titleBar);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        titleBar.setBackgroundColor(color);
    }

    @Nullable
    private TextView findTextView(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (view instanceof TextView) {
                return (TextView) view;
            }
            if (view instanceof ViewGroup) {
                return findTextView((ViewGroup) view);
            }
        }
        return null;
    }

    private void setLeftButtons(ArrayList<Button> leftButtons) {
        if (leftButtons == null || leftButtons.isEmpty()) {
            titleBar.setNavigationIcon(null);
            return;
        }

        if (leftButtons.size() > 1) {
            Log.w("RNN", "Use a custom TopBar to have more than one left button");
        }

        Button leftButton = leftButtons.get(0);
        setLeftButton(leftButton);
    }

    private void setLeftButton(final Button button) {
        TitleBarButton leftBarButton = new TitleBarButton(this.titleBar, button, onClickListener);
        leftBarButton.applyNavigationIcon(getContext());
    }

    private void setRightButtons(ArrayList<Button> rightButtons) {
        if (rightButtons == null || rightButtons.size() == 0) {
            return;
        }

        Menu menu = getTitleBar().getMenu();
        menu.clear();

        for (int i = 0; i < rightButtons.size(); i++) {
            Button button = rightButtons.get(i);
            TitleBarButton titleBarButton = new TitleBarButton(this.titleBar, button, onClickListener);
            titleBarButton.addToMenu(getContext(), menu);
        }
    }

    public Toolbar getTitleBar() {
        return titleBar;
    }

    public void setupTopTabsWithViewPager(TopTabsViewPager viewPager) {
        initTopTabs();
        topTabs.setupWithViewPager(viewPager);
    }

    private void initTopTabs() {
        topTabs = new TopTabs(getContext());
        addView(topTabs);
    }

    public void enableCollapse() {
        collapsingBehavior.enableCollapse();
    }

    public void disableCollapse() {
        collapsingBehavior.disableCollapse();
    }

    public void show(Options.BooleanOptions animated) {
        if (getVisibility() == View.VISIBLE) {
            return;
        }
        if (animated == Options.BooleanOptions.True) {
            animator.show();
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    public void hide(Options.BooleanOptions animated) {
        if (getVisibility() == View.GONE) {
            return;
        }
        if (animated == Options.BooleanOptions.True) {
            animator.hide();
        } else {
            setVisibility(View.GONE);
        }
    }
}