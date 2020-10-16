package com.poing.admob;

import org.godotengine.godot.Godot;
import org.godotengine.godot.GodotLib;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.MobileAds; //used for initialize
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.AdRequest; //used for make requests of ads

import com.google.android.gms.ads.AdView; //used to banner ads
import com.google.android.gms.ads.AdSize; //used to set/get size banner ads
import com.google.android.gms.ads.AdListener; //used to get events of ads (banner, interstitial)

import com.google.android.gms.ads.InterstitialAd; //interstitialAd

import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.rewarded.RewardedAd; //rewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardItem;

import com.google.android.gms.ads.AdLoader; //used to native ads
import com.google.android.gms.ads.formats.UnifiedNativeAd; //
import com.google.android.gms.ads.formats.UnifiedNativeAdView; //view of native ads
import com.google.android.gms.ads.formats.MediaView; // to mapUnifiedNativeAdToLayout

import com.google.ads.mediation.admob.AdMobAdapter;

import android.app.Activity;
import android.widget.FrameLayout; //get Godot Layout
import android.view.View;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AdMob extends org.godotengine.godot.plugin.GodotPlugin
{
    private Activity aActivity;
    private int aInstanceId;

    private FrameLayout aGodotLayout; // store the godot layout
    private FrameLayout.LayoutParams aGodotLayoutParams; // Store the godot layout params

    private AdView aAdView; //view of banner
    private InterstitialAd aInterstitialAd;
    private RewardedAd aRewardedAd;
    private UnifiedNativeAdView aUnifiedNativeAdView;

    private boolean aIsPersonalized;

    public AdMob(Godot godot)
    {
        super(godot);
        aActivity = godot;
    }

    @NonNull
    @Override
    public List<String> getPluginMethods()
    {
        return Arrays.asList(
                "initialize",
                "load_banner",
                "destroy_banner",
                "load_interstitial",
                "show_interstitial",
                "load_rewarded",
                "show_rewarded",
                "load_unified_native",
                "destroy_unified_native"
        );
    }

    @Override
    public View onMainCreate(Activity pActivity)
    {
        aGodotLayout= new FrameLayout(pActivity);
        return aGodotLayout;
    }

    @NonNull
    @Override
    public String getPluginName() {
        return getClass().getSimpleName();
    }

    public void initialize(boolean pIsForChildDirectedTreatment, boolean pIsPersonalized, String pMaxAdContentRating, boolean pIsReal, int pInstanceId)
    {
        aInstanceId = pInstanceId;
        aIsPersonalized = pIsPersonalized;

        setRequestConfiguration(pIsForChildDirectedTreatment, pIsPersonalized, pMaxAdContentRating, pIsReal); //First call MobileAds.setRequestConfigurationhttps://groups.google.com/g/google-admob-ads-sdk/c/17oVu0sABjs
        MobileAds.initialize(aActivity); //initializes the admob
    }

    private void setRequestConfiguration(boolean pIsForChildDirectedTreatment, boolean pIsPersonalized, String pMaxAdContentRating, boolean pIsReal)
    {
        RequestConfiguration requestConfiguration;
        RequestConfiguration.Builder requestConfigurationBuilder = new RequestConfiguration.Builder();

        if (!pIsReal) {
            requestConfigurationBuilder.setTestDeviceIds(Arrays.asList(getDeviceId()));
        }

        requestConfigurationBuilder.setTagForChildDirectedTreatment(pIsForChildDirectedTreatment ? 1 : 0);

        if (pIsForChildDirectedTreatment)
        {
            requestConfigurationBuilder.setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G);
        }
        else
        {
            switch (pMaxAdContentRating)
            {
                case RequestConfiguration.MAX_AD_CONTENT_RATING_G:
                case RequestConfiguration.MAX_AD_CONTENT_RATING_MA:
                case RequestConfiguration.MAX_AD_CONTENT_RATING_PG:
                case RequestConfiguration.MAX_AD_CONTENT_RATING_T:
                case RequestConfiguration.MAX_AD_CONTENT_RATING_UNSPECIFIED:
                    requestConfigurationBuilder.setMaxAdContentRating(pMaxAdContentRating);
                break;
            }
        }

        requestConfiguration = requestConfigurationBuilder.build();

        MobileAds.setRequestConfiguration(requestConfiguration);
    }

    private AdRequest getAdRequest()
    {
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        Bundle extras = new Bundle();
        extras.putString("npa", String.valueOf(!aIsPersonalized));
        adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter.class, extras);

        return adRequestBuilder.build();
    }

    //BANNER only one is allowed, please do not try to place more than one, as your ads on the app may have the chance to be banned!
    public void load_banner(final String pAdUnitId, final int pGravity, final String pSize)
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aAdView != null) destroy_banner();
                if (aUnifiedNativeAdView != null) destroy_unified_native();

                aAdView = new AdView(aActivity);
                aAdView.setAdUnitId(pAdUnitId);
                switch (pSize){
                    case "BANNER":
                        aAdView.setAdSize(AdSize.BANNER);
                        break;
                    case "LARGE_BANNER":
                        aAdView.setAdSize(AdSize.LARGE_BANNER);
                        break;
                    case "MEDIUM_RECTANGLE":
                        aAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                        break;
                    case "FULL_BANNER":
                        aAdView.setAdSize(AdSize.FULL_BANNER);
                        break;
                    case "LEADERBOARD":
                        aAdView.setAdSize(AdSize.LEADERBOARD);
                        break;
                    default:
                        aAdView.setAdSize(AdSize.SMART_BANNER);
                        break;
                }//ADAPTATIVE DOESNT WORK, NEED TO UPDATE THE PROJECT TO 18.3.0 MINIMUM ON FUTURE
                GodotLib.calldeferred(aInstanceId, "testeee", new Object[]{ pAdUnitId, pGravity, pSize });

                aAdView.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdLoaded()
                    {
                        // Code to be executed when an ad finishes loading.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_loaded", new Object[]{ });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_failed_to_load", new Object[]{ adError.getCode() });
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_opened", new Object[]{ });
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_left_application", new Object[]{ });
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when the user is about to return
                        // to the app after tapping on an ad.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_closed", new Object[]{ });
                    }
                });

                aGodotLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                aGodotLayoutParams.gravity = pGravity;
                aGodotLayout.addView(aAdView, aGodotLayoutParams);

                aAdView.loadAd(getAdRequest());
            }
        });
    }
    public void destroy_banner()//IF THIS METHOD IS CALLED ON GODOT, THE BANNER WILL ONLY APPEAR AGAIN IF THE BANNER IS LOADED AGAIN
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if(aAdView != null)
                {
                    aGodotLayout.removeView(aAdView);
                    aAdView.destroy();
                    aAdView = null;
                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_destroyed", new Object[]{ });
                }
            }
        });
    }
    //BANNER
    //INTERSTITIAL
    public void load_interstitial(final String pAdUnitId)
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                aInterstitialAd = new InterstitialAd(aActivity);
                aInterstitialAd.setAdUnitId(pAdUnitId);
                aInterstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdLoaded()
                    {
                        // Code to be executed when an ad finishes loading.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_loaded", new Object[]{ });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError)
                    {
                        // Code to be executed when an ad request fails.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_failed_to_load", new Object[]{ adError.getCode() });
                    }
                    @Override
                    public void onAdOpened()
                    {
                        // Code to be executed when the ad is displayed.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_opened", new Object[]{ });
                    }

                    @Override
                    public void onAdLeftApplication()
                    {
                        // Code to be executed when the user has left the app.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_left_application", new Object[]{ });
                    }

                    @Override
                    public void onAdClosed()
                    {
                        // Code to be executed when the interstitial ad is closed.
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_closed", new Object[]{ });
                    }
                });
                aInterstitialAd.loadAd(getAdRequest());
            }
        });
    }
    public void show_interstitial()
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aInterstitialAd != null && aInterstitialAd.isLoaded()) {

                    aInterstitialAd.show();
                }
            }
        });
    }
    //INTERSTITIAL
    //REWARDED
    public void load_rewarded(final String pAdUnitId)
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                aRewardedAd = new RewardedAd(aActivity, pAdUnitId);
                RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback()
                {
                    @Override
                    public void onRewardedAdLoaded()
                    {
                        // Ad successfully loaded
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_loaded", new Object[] { });
                    }

                    @Override
                    public void onRewardedAdFailedToLoad(LoadAdError adError)
                    {
                        // Ad failed to load
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_failed_to_load", new Object[] { adError.getCode() });
                    }
                };
                aRewardedAd.loadAd(getAdRequest(), adLoadCallback);
            }
        });
    }
    public void show_rewarded()
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aRewardedAd != null && aRewardedAd.isLoaded())
                {
                    RewardedAdCallback adCallback = new RewardedAdCallback()
                    {
                        @Override
                        public void onRewardedAdOpened()
                        {
                            // Ad opened.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_opened", new Object[] { });
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_closed", new Object[] { });
                        }

                        @Override
                        public void onUserEarnedReward(RewardItem reward)
                        {
                            // User earned reward.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_user_earned_rewarded", new Object[] { reward.getType(), reward.getAmount() });
                        }

                        @Override
                        public void onRewardedAdFailedToShow(AdError adError)
                        {
                            // Ad failed to display.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_failed_to_show", new Object[] { adError.getCode() });
                        }

                    };

                    aRewardedAd.show(aActivity, adCallback);
                }
            }
        });
    }
    //REWARDED
    //NATIVE
    public void load_unified_native(final String pAdUnitId, final int[] pSize, final int[] pMargins)
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aUnifiedNativeAdView != null) destroy_unified_native();
                if (aAdView != null) destroy_banner();

                AdLoader adLoader = new AdLoader.Builder(aActivity, pAdUnitId)
                        .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                            @Override
                            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                                // Show the ad.
                                //SECURE TO DESTROY THE UNIFIED NATIVE AND THE BANNER
                                if (aUnifiedNativeAdView != null) destroy_unified_native();
                                if (aAdView != null) destroy_banner();

                                aUnifiedNativeAdView = (UnifiedNativeAdView) aActivity.getLayoutInflater().inflate(R.layout.ad_unified, null);

                                mapUnifiedNativeAdToLayout(unifiedNativeAd, aUnifiedNativeAdView);
                                aGodotLayoutParams = new FrameLayout.LayoutParams(pSize[0], pSize[1]);
                                aGodotLayoutParams.setMargins(pMargins[0], pMargins[1], 0, 0);
                                aGodotLayout.addView(aUnifiedNativeAdView, aGodotLayoutParams);
                                GodotLib.calldeferred(aInstanceId, "_on_AdMob_unified_native_loaded", new Object[] { });
                            }
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(LoadAdError adError)
                            {
                                // Code to be executed when an ad request fails.
                                GodotLib.calldeferred(aInstanceId, "_on_AdMob_unified_native_failed_to_load", new Object[]{ adError.getCode() });
                            }

                            @Override
                            public void onAdOpened()
                            {
                                // Code to be executed when the ad is displayed.
                                GodotLib.calldeferred(aInstanceId, "_on_AdMob_unified_native_opened", new Object[]{ });
                            }
                            @Override
                            public void onAdClosed()
                            {
                                // Code to be executed when the unified native ad is closed.
                                GodotLib.calldeferred(aInstanceId, "_on_AdMob_unified_native_closed", new Object[]{ });
                            }

                        })
                        .build();


                adLoader.loadAd(getAdRequest());
            }
        });
    }
    public void destroy_unified_native()//IF THIS METHOD IS CALLED ON GODOT, THE UNIFIED NATIVE AD WILL ONLY APPEAR AGAIN IF THE AD IS LOADED AGAIN
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if(aUnifiedNativeAdView != null)
                {
                    aGodotLayout.removeView(aUnifiedNativeAdView);
                    aUnifiedNativeAdView.destroy();
                    aUnifiedNativeAdView = null;
                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_unified_native_destroyed", new Object[]{ });
                }
            }
        });
    }
    public void mapUnifiedNativeAdToLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdView)
    {
        MediaView mediaView = myAdView.findViewById(R.id.ad_media);
        myAdView.setMediaView(mediaView);

        myAdView.setNativeAd(adFromGoogle);
    }

    //NATIVE
    /**
     * Generate MD5 for the deviceID
     * @param  s The string to generate de MD5
     * @return String The MD5 generated
     */
    private String md5(final String s)
    {
        try
        {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        }
        catch(NoSuchAlgorithmException e)
        {
            //Logger.logStackTrace(TAG,e);
        }
        return "";
    }

    /**
     * Get the Device ID for AdMob
     * @return String Device ID
     */
    private String getDeviceId()
    {
        String android_id = Settings.Secure.getString(aActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase(Locale.US);
        return deviceId;
    }
}