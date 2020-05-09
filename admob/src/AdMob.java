package org.godotengine.godot;

import android.app.Activity;

public class AdMob extends Godot.SingletonBase { // class name should be same as java file name

    public String sample_function() {
       return "sample_function working well"; // should return string
    }

    static public Godot.SingletonBase initialize(Activity p_activity) {
        return new AdMob(p_activity);
    }

    public AdMob(Activity p_activity) {
        //register class name and functions to bind
        registerClass("AdMob", new String[]{"sample_function"});
    }
}