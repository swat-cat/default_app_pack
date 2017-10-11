package com.swatcat.your_name.services.utils;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.swatcat.your_name.base.App;

import java.lang.reflect.Field;

/**
 * Created by max_ermakov on 9/22/16.
 */
public class ViewUtils {
    private ViewUtils() {}

    public static void setAlpha(View view, float alpha) {
        if (Build.VERSION.SDK_INT < 11) {
            final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        } else {
            view.setAlpha(alpha);
        }
    }

    public static void underline(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @SuppressWarnings("deprecation")
    public static void stealBackground(View from, View to) {
        Drawable background = from.getBackground();
        from.setBackgroundDrawable(null);
        to.setBackgroundDrawable(background);
    }

    public static boolean isViewVisible(View container, View targetView) {
        boolean viewVisible;
        Rect scrollBounds = new Rect();
        container.getHitRect(scrollBounds);
        if (!targetView.getLocalVisibleRect(scrollBounds)
                || scrollBounds.height() < targetView.getHeight()) {
            viewVisible = false;
        } else {
            viewVisible = true;
        }
        return viewVisible;
    }

    public static Boolean isEllipsized(TextView textView) {
        Layout layout = textView.getLayout();
        if (layout != null) {
            int lines = layout.getLineCount();
            if (lines > 0)
                return layout.getEllipsisCount(lines - 1) > 0;
        }
        return null;
    }

    public static void addViewSafeStrict(ViewGroup parentNew, View view) {
        if (parentNew != null && view != null) {
            addViewSafe(parentNew, view);
        } else {
            throw new NullPointerException("parentNew = " + parentNew + ", view = " + view);
        }
    }

    public static void addViewSafe(@Nullable ViewGroup parentNew, @Nullable View view) {
        addViewSafe(parentNew, view, null, null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void addViewSafe(@Nullable ViewGroup parentNew, @Nullable View view, @Nullable Integer index, @Nullable ViewGroup.LayoutParams layoutParams) {
        if (parentNew == null || view == null)
            return;

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            LayoutTransition transition = parent.getLayoutTransition();

            parent.setLayoutTransition(null);
            parent.removeView(view);
            parent.setLayoutTransition(transition);
        }

        if (index != null) {
            if (layoutParams != null) {
                parentNew.addView(view, index, layoutParams);
            } else {
                parentNew.addView(view, index);
            }
        } else {
            if (layoutParams != null) {
                parentNew.addView(view, layoutParams);
            } else {
                parentNew.addView(view);
            }
        }
    }

    public static void detachViewFromParent(@Nullable View view) {
        if (view == null) return;

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }

    public static void setDrawableLeft(TextView textView, Drawable left) {
        Drawable[] drawables = textView.getCompoundDrawables();
        textView.setCompoundDrawablesWithIntrinsicBounds(left, drawables[1], drawables[2], drawables[3]);
    }

    public static void setDrawableRight(TextView textView, Drawable right) {
        Drawable[] drawables = textView.getCompoundDrawables();
        textView.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], right, drawables[3]);
    }

    public static int getYInWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location[1];
    }

    public static int getVisibleHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.height();
    }

    public static void smoothScrollToPosition(final RecyclerView recyclerView, final int itemPosition) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(itemPosition);
            }
        });
    }

    public static void scrollToPosition(final RecyclerView recyclerView, final int itemPosition) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(itemPosition);
            }
        });
    }

    /**
     * To avoid view resizing on text change.
     *
     * @param textView
     */
    public static void adjustDurationTextViewWidth(TextView textView) {
        LayoutHelper.applyPaddingRight(textView, 0);
        adjustMinWidth(textView, TimeUtils.formatDuration(88, 88, 88) + "1");
    }

    public static void adjustMinWidth(TextView textView, String text) {
        CharSequence realText = textView.getText();

        textView.setText(text);
        textView.measure(0, 0);
        textView.setMinWidth(textView.getMeasuredWidth());

        textView.setText(realText);
    }

    public static void makeDrawablePressable(ImageView imageView) {
        Drawable icon = imageView.getDrawable();
        if (icon == null || icon instanceof StateListDrawable)
            return;

        Drawable selector = createPressableIcon(icon);

        imageView.setImageDrawable(selector);
    }

    public static Drawable createPressableIcon(int resId) {
        Drawable icon = App.getInstance().getContext().getResources().getDrawable(resId);
        return createPressableIcon(icon);
    }

    private static Drawable createPressableIcon(Drawable icon) {
        return new PressedEffectStateListDrawable(icon, App.getInstance().getContext().getResources().getColor(R.color.colorAccent));
    }

    public static <T extends TextView> void makeTextPressable(T textView, boolean white) {
        if (white) {
            textView.setTextColor(textView.getResources().getColorStateList(R.color.white));
        }
        else {
            textView.setTextColor(textView.getResources().getColorStateList(android.R.color.black));
        }
    }

    public static <T extends TextView> void makeListTextPressable(T textView) {
        textView.setTextColor(textView.getResources().getColorStateList(R.color.colorAccent));
    }

    public static void setErrorTextColor(TextInputLayout textInputLayout, int color) {
        try {
            Field fErrorView = TextInputLayout.class.getDeclaredField("mErrorView");
            fErrorView.setAccessible(true);
            TextView mErrorView = (TextView) fErrorView.get(textInputLayout);
            Field fCurTextColor = TextView.class.getDeclaredField("mCurTextColor");
            fCurTextColor.setAccessible(true);
            fCurTextColor.set(mErrorView, color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
