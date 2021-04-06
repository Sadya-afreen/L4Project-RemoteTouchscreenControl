package com.example.trackball;

import android.content.Context;
import android.os.Build;
//import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;


/** Class for inner circular trackball view */
public class InnerTrackball extends View {

    public InnerTrackball(Context context) {
        super(context);
    }

    public InnerTrackball(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerTrackball(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public InnerTrackball(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

}
