package com.swatcat.your_name.services.utils;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class PressedEffectStateListDrawable extends StateListDrawable {

    private int selectionColor;

    public PressedEffectStateListDrawable(Drawable drawable, int selectionColor) {
        super();
        this.selectionColor = selectionColor;
        addState(new int[]{android.R.attr.state_pressed}, drawable);
        addState(new int[]{}, drawable);
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean isStatePressedInArray = false;
        for (int state : states) {
            if (state == android.R.attr.state_pressed) {
                isStatePressedInArray = true;
            }
        }
        if (isStatePressedInArray) {
            super.setColorFilter(selectionColor, PorterDuff.Mode.MULTIPLY);
        } else {
            super.clearColorFilter();
        }
        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }
}