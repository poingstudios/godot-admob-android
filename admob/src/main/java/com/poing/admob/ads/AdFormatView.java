package com.poing.admob.ads;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

abstract public class AdFormatView extends AdFormat {
    protected AdView adView;
    protected AdSize adSize;
    protected int position;
    protected String size;
    protected boolean showInstantly;

    public AdFormatView(String adUnit) {
        super(adUnit);
    }
}
