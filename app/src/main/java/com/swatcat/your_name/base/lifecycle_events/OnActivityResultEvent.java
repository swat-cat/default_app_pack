package com.swatcat.your_name.base.lifecycle_events;

import android.content.Intent;

/**
 * Created by bohdan on 03.02.17.
 */

public class OnActivityResultEvent {
    public int requestCode,  resultCode;
    public Intent data;

    public OnActivityResultEvent(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

}
