package com.swatcat.your_name.services.utils;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.swatcat.your_name.base.App;

public class LayoutHelper {

    public static int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView()) {
            return myView.getLeft();
        } else {
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
        }
    }

    public static int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView()) {
            return myView.getTop();
        } else {
            return myView.getTop() + getRelativeTop((View) myView.getParent());
        }
    }

    public static int pxToDp(int px) {
        Resources res = App.getInstance().getContext().getResources();
        float density = res.getDisplayMetrics().density;
        return (int) (px / density);
    }

    public static int dpToPx(float dp) {
        DisplayMetrics metrics = App.getInstance().getContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static boolean isLandscape(Context context) {
        int orient = context.getResources().getConfiguration().orientation;
        return orient == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static float getScreenWidthInDp(Context context) {
        Resources res = context.getResources();
        final DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    public static int getScreenWidthInPixel(Context context) {
        Resources res = context.getResources();
        final DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeightInPixel(Context context) {
        Resources res = context.getResources();
        final DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int convertDpToPixsels(Context context, int widthInDp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((widthInDp * scale) + 0.5);
    }

    public static int convertPixselsToDp(Context context, int widthInPixsels) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((widthInPixsels / scale) + 0.5);
    }

    public static int getScreenMaxDimension(Context context) {
        Resources res = context.getResources();
        final DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }



    public static int interpolateValue(int x1, int x2, int y1, int y2, int dx) {
        return (int) Math.round(y1 + calcTg(x1, x2, y1, y2) * dx);
    }

    private static double calcTg(int x1, int x2, int y1, int y2) {
        if (x2 == x1) {
            return 0;
        }

        return (y2 - y1) / ((double) (x2) - x1);
    }

    public static void applyWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;

        view.setLayoutParams(layoutParams);
    }

    public static void applyHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;

        view.setLayoutParams(layoutParams);
    }

    public static void applyViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void applyWidthWeight(View view, float weight) {
        LinearLayout.LayoutParams layoutParams = getLinearLayoutParams(view);

        layoutParams.width = 0;
        layoutParams.weight = weight;

        view.setLayoutParams(layoutParams);
    }

    public static void applyHeightWeight(View view, float weight) {
        LinearLayout.LayoutParams layoutParams = getLinearLayoutParams(view);

        layoutParams.height = 0;
        layoutParams.weight = weight;

        view.setLayoutParams(layoutParams);
    }

    public static void applyPadding(View view, Integer left, Integer top, Integer right, Integer bottom) {
        int leftNew = left != null ? left : view.getPaddingLeft();
        int topNew = top != null ? top : view.getPaddingTop();
        int rightNew = right != null ? right : view.getPaddingRight();
        int bottomNew = bottom != null ? bottom : view.getPaddingBottom();

        if (leftNew == view.getPaddingLeft() && topNew == view.getPaddingTop()
                && rightNew == view.getPaddingRight() && bottomNew == view.getPaddingBottom()) {
            return;
        }
        view.setPadding(leftNew, topNew, rightNew, bottomNew);
    }

    public static void applyPaddingLeft(View view, int left) {
        applyPadding(view, left, null, null, null);
    }

    public static void applyPaddingRight(View view, int right) {
        applyPadding(view, null, null, right, null);
    }

    public static void applyPaddingBottom(View view, int padding) {
        applyPadding(view, null, null, null, padding);
    }

    public static void applyPaddingTop(View view, int padding) {
        applyPadding(view, null, padding, null, null);
    }

    public static void applyPaddingLeftRight(View view, int left, int right) {
        applyPadding(view, left, null, right, null);
    }

    public static void applyPaddingLeftRight(View view, int padding) {
        applyPadding(view, padding, null, padding, null);
    }

    public static void applyPaddingTopBottom(View view, int padding) {
        applyPadding(view, null, padding, null, padding);
    }

    public static void clearLeftRightPadding(View view) {
        view.setPadding(0, view.getPaddingTop(), 0, view.getPaddingBottom());
    }

    public static int[] getPadding(View view) {
        return new int[]{
                view.getPaddingLeft(),
                view.getPaddingTop(),
                view.getPaddingRight(),
                view.getPaddingBottom()
        };
    }

    public static void applyPadding(View view, int[] padding) {
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    public static void applyMargin(View view, Integer left, Integer top, Integer right, Integer bottom) {
        ViewGroup.MarginLayoutParams layoutParams = getMarginLayoutParams(view);

        int leftNew = left != null ? left : layoutParams.leftMargin;
        int topNew = top != null ? top : layoutParams.topMargin;
        int rightNew = right != null ? right : layoutParams.rightMargin;
        int bottomNew = bottom != null ? bottom : layoutParams.bottomMargin;

        if (leftNew == layoutParams.leftMargin && topNew == layoutParams.topMargin
                && rightNew == layoutParams.rightMargin && bottomNew == layoutParams.bottomMargin
                && layoutParams == view.getLayoutParams()) { // everything is really the same
            return;
        }

        layoutParams.setMargins(leftNew, topNew, rightNew, bottomNew);
    }

    public static void applyTopBottomMargin(View view, int margin) {
        applyMargin(view, null, margin, null, margin);
    }

    public static void setRecyclerViewElementLeftRightMargins(View view, int margin) {
        ViewGroup.MarginLayoutParams layoutParams = getRecyclerViewLayoutParams(view);
        layoutParams.leftMargin = margin;
        layoutParams.rightMargin = margin;
        view.setLayoutParams(layoutParams);
    }

    private static LinearLayout.LayoutParams getLinearLayoutParams(View view) {
        LinearLayout.LayoutParams layoutParams;

        ViewGroup.LayoutParams currentLayoutParams = view.getLayoutParams();
        if (currentLayoutParams instanceof LinearLayout.LayoutParams) {
            layoutParams = (LinearLayout.LayoutParams) currentLayoutParams;
        } else {
            layoutParams = new LinearLayout.LayoutParams(currentLayoutParams);
        }

        return layoutParams;
    }

    private static ViewGroup.MarginLayoutParams getMarginLayoutParams(View view) {
        ViewGroup.MarginLayoutParams layoutParams;

        ViewGroup.LayoutParams currentLayoutParams = view.getLayoutParams();
        if (currentLayoutParams instanceof ViewGroup.MarginLayoutParams) {
            layoutParams = (ViewGroup.MarginLayoutParams) currentLayoutParams;
        } else {
            layoutParams = new ViewGroup.MarginLayoutParams(currentLayoutParams);
        }

        return layoutParams;
    }

    private static RecyclerView.LayoutParams getRecyclerViewLayoutParams(View view) {
        RecyclerView.LayoutParams layoutParams;

        ViewGroup.LayoutParams currentLayoutParams = view.getLayoutParams();
        if (currentLayoutParams instanceof RecyclerView.LayoutParams) {
            layoutParams = (RecyclerView.LayoutParams) currentLayoutParams;
        } else if (currentLayoutParams != null) {
            layoutParams = new RecyclerView.LayoutParams(currentLayoutParams);
        } else { // warning!
            layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return layoutParams;
    }
}
