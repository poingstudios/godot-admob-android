package com.poing.admob.ads;

abstract public class AdFormat {
    private String adUnit;
    private boolean loaded;

    public AdFormat(final String adUnit){
        this.adUnit = adUnit;
    }

    public boolean getLoaded() {
        return this.loaded;
    }

    protected void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    abstract public void load();

    public String getAdUnit() {
        return adUnit;
    }

    public void setAdUnit(String adUnit) {
        this.adUnit = adUnit;
    }
}
