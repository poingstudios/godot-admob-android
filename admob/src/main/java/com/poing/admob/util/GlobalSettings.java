package com.poing.admob.util;

import com.google.android.gms.ads.AdRequest;

public class GlobalSettings {
    public static AdRequest getAdRequest() {
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        return adRequestBuilder.build();
    }

}
