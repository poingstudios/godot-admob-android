package com.poing.admob.ads;

import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.poing.admob.UI;
import com.poing.admob.util.GlobalSettings;

import org.godotengine.godot.GodotLib;

public class Banner extends AdFormatView {
    public Banner(String adUnit) {
        super(adUnit);
    }

    @Override
    public void load() {
        //if (adView != null) destroy_banner();
        adView = new AdView(UI.getActivity());

        adView.setAdUnitId(getAdUnit());
        /*switch (adSize) {
            case "BANNER":
                adView.setAdSize(AdSize.BANNER);
                break;
            case "LARGE_BANNER":
                adView.setAdSize(AdSize.LARGE_BANNER);
                break;
            case "MEDIUM_RECTANGLE":
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                break;
            case "FULL_BANNER":
                adView.setAdSize(AdSize.FULL_BANNER);
                break;
            case "LEADERBOARD":
                adView.setAdSize(AdSize.LEADERBOARD);
                break;
            case "ADAPTIVE":
                //adView.setAdSize(getAdSizeAdaptive());
                break;
            default:
                adView.setAdSize(AdSize.SMART_BANNER);
                break;
        }
        adSize = adView.getAdSize(); //store AdSize of banner due a bug (throws error when do aAdView.getAdSize(); called by Godot)

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_loaded", new Object[]{});
                if (pShowInstantly){
                    show_banner();
                }
                else{
                    hide_banner();
                }
                aIsBannerLoaded = true;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
                //GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_failed_to_load", new Object[]{adError.getCode()});
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_opened", new Object[]{});
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the native ad is closed.
                GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_clicked", new Object[]{});
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_closed", new Object[]{});
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_recorded_impression", new Object[]{});
            }
        });
        aGodotLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        if (pPosition == 0)//BOTTOM
        {
            aGodotLayoutParams.gravity = Gravity.BOTTOM;
        } else if (pPosition == 1)//TOP
        {
            aGodotLayoutParams.gravity = Gravity.TOP;
        }
        godotLayout.addView(aAdView, aGodotLayoutParams);

        adView.loadAd(GlobalSettings.getAdRequest());
        */
    }
}
