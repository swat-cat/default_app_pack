package com.swatcat.your_name.base.action_bar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.swatcat.your_name.base.BaseActivity;
import com.swatcat.your_name.services.utils.ViewUtils;


import rx.Observable;

/**
 * Created by max_ermakov on 9/22/16.
 */
public class ActionBarView implements ActionBarContract.View{
    private static final String TAG = ActionBarView.class.getSimpleName();
    private BaseActivity activity;
    private View root;

    private ViewGroup topContainer;
    private TextView centerText;
    private TextView bottomText;
    private FrameLayout leftContainer;
    private FrameLayout rightContainer;
    private ImageView leftIcon;
    private ImageView rightIcon;
    private ImageView topLogo;
    private View filterIndicator;

    public ActionBarView(BaseActivity activity, View root) {
        this.activity = activity;
        this.root = root;
        init();
    }

    private void init(){
        centerText = (TextView)root.findViewById(R.id.center_text);
        leftContainer = (FrameLayout)root.findViewById(R.id.left_container);
        rightContainer = (FrameLayout)root.findViewById(R.id.right_container);
        leftIcon = (ImageView)root.findViewById(R.id.left_icon);
        rightIcon = (ImageView)root.findViewById(R.id.right_icon);
        bottomText = (TextView) root.findViewById(R.id.bottom_text);
    }

    @Override
    public void showAB(boolean show) {
        if (show){
            root.setVisibility(View.VISIBLE);
        }
        else {
            root.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLeftButton(boolean show) {
        if (show){
            leftContainer.setVisibility(View.VISIBLE);
        }
        else {
            leftContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRightButton(boolean show) {
        if (show){
            rightContainer.setVisibility(View.VISIBLE);
        }
        else {
            rightContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showCenterText(boolean show) {
        if (show){
            centerText.setVisibility(View.VISIBLE);
        }
        else {
            centerText.setVisibility(View.GONE);
        }
    }

    @Override
    public void setupLeftButton(View view) {
        if (view!=null) {
            if (leftContainer.getChildCount()>0){
                leftContainer.removeAllViews();
            }
            ViewUtils.addViewSafe(leftContainer,view);
        }
        else {
            leftContainer.removeAllViews();
        }
    }

    @Override
    public void setupRightButton(View view) {
        if (view!=null) {
            if(rightContainer.getChildCount() > 0)
                rightContainer.removeAllViews();
            ViewUtils.addViewSafe(rightContainer,view);
        }
        else {
            rightContainer.removeAllViews();
        }
    }

    @Override
    public void setupBottomText(int res) {
        bottomText.setText(res);
    }

    @Override
    public void setupBottomText(String string) {
        bottomText.setText(string);
    }

    @Override
    public void showBottomText(boolean show) {
        if (show){
            bottomText.setVisibility(View.VISIBLE);
        }else {
            bottomText.setVisibility(View.GONE);
        }
    }

    public void removeMenuButton(){
        if(rightContainer.getChildCount() > 0)
            rightContainer.removeAllViews();
    }

    @Override
    public void setupCenterText(int res) {
        centerText.setText(res);
    }

    @Override
    public Observable<Void> leftButtonAction() {
        return RxView.clicks(leftContainer);
    }

    @Override
    public Observable<Void> rightButtonAction() {
        return RxView.clicks(rightContainer);
    }


    @Override
    public View getAB() {
        return root;
    }

    @Override
    public void setupCenterText(String string) {
        centerText.setText(string);
    }

    @Override
    public void showFilterIndicator(boolean show) {
        if (filterIndicator == null) return;
        filterIndicator.setVisibility(show? View.VISIBLE: View.GONE);
    }
}
