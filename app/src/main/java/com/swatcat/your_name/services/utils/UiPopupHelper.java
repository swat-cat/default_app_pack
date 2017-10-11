package com.swatcat.your_name.services.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class UiPopupHelper {

    private static final int HIDE_PROGRESS_DIALOG_DELAYED = 2000;

    private static final int TOAST_OFFSET_X = 0;
    private static final int TOAST_OFFSET_Y = LayoutHelper.pxToDp(150);

    private static ProgressBar dialogProgress;
    private static TextView dialogText;
    private static boolean hideScheduled;

    // Toast

    public static void showCustomToast(Activity activity, int stringResId, int gravity) {
        showCustomToast(activity, activity.getString(stringResId), gravity);
    }

    public static void showCustomToast(Activity activity, int stringResId) {
        showCustomToast(activity, activity.getString(stringResId));
    }

    public static void showCustomToast(Activity activity, String message) {
        showCustomToast(activity, message, Gravity.CENTER);
    }

    public static void showCustomToast(Activity activity, String message, int gravity) {
        if (Tools.isNullOrEmpty(message)) {
            return;
        }

        int xOffset = 0;
        int yOffset = 0;

        switch (gravity) {
            case Gravity.BOTTOM:
                xOffset = TOAST_OFFSET_X;
                yOffset = TOAST_OFFSET_Y;
                break;

            case Gravity.TOP:
                xOffset = TOAST_OFFSET_X;
                yOffset = -TOAST_OFFSET_Y;
        }

        showToast(activity, message, gravity, xOffset, yOffset);
    }

    public static void showCustomToast(Activity activity, String message, View anchor) {
        if (Tools.isNullOrEmpty(message)) {
            return;
        }

        int xOffset = 0;
        int yOffset = 0;

        Rect anchorRect = new Rect();
        if (anchor != null && anchor.getGlobalVisibleRect(anchorRect)) {
            int anchorCenterX = anchorRect.centerX();
            int anchorCenterY = anchorRect.centerY();

            View root = anchor.getRootView();
            int rootHalfWidth = root.getRight() / 2;
            int rootHalfHeight = root.getBottom() / 2;

            yOffset = anchorCenterY - rootHalfHeight;
            xOffset = anchorCenterX - rootHalfWidth;
        }

        showToast(activity, message, Gravity.CENTER, xOffset, yOffset);
    }

    private static void showToast(Activity activity, String message, int gravity, int xOffset, int yOffset) {
        View view = View.inflate(activity, R.layout.toast_screen, null);
        TextView textView = (TextView) view.findViewById(R.id.notification_text);
        textView.setText(message);

        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setView(view);
        toast.show();
    }
}
