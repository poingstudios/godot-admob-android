package com.poing.admob;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

abstract public class UI {
    private static Activity activity;
    private static FrameLayout godotLayout; // store the godot layout
    private static FrameLayout.LayoutParams godotLayoutParams; // Store the godot layout params

    protected static void setActivity(final Activity pActivity){
        if (activity == null){
            activity = pActivity;
        }
    }
    protected static void setGodotLayout(final FrameLayout pGodotLayout){
        if (godotLayout == null){
            godotLayout = pGodotLayout;
        }
    }

    public static Activity getActivity() {
        return activity;
    }
    public static View getGodotLayout() {
        return godotLayout;
    }
}
