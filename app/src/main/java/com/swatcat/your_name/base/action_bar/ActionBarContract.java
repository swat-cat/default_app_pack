package com.swatcat.your_name.base.action_bar;

import rx.Observable;

/**
 * Created by max_ermakov on 9/22/16.
 */
public interface ActionBarContract {
    interface View{
        void showAB(boolean show);
        void showLeftButton(boolean show);
        void setupLeftButton(android.view.View view);
        void showRightButton(boolean show);
        void setupRightButton(android.view.View view);
        void showCenterText(boolean show);
        void setupCenterText(int res);
        void setupCenterText(String string);

        void setupBottomText(int res);
        void setupBottomText(String string);
        void showBottomText(boolean show);

        android.view.View getAB();
        Observable<Void> leftButtonAction();
        Observable<Void> rightButtonAction();
        void showFilterIndicator(boolean show);
    }
    interface Action{
        void setupView();
        void setupActions();
        void leftButtonAction();
        void rightButtonAction();
    }
}
