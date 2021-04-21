package com.poing.admob;

import org.godotengine.godot.Godot;
import org.godotengine.godot.GodotLib;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds; //used for initialize
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.AdRequest; //used for make requests of ads

import com.google.android.gms.ads.AdView; //used to banner ads
import com.google.android.gms.ads.AdSize; //used to set/get size banner ads
import com.google.android.gms.ads.AdListener; //used to get events of ads (banner, interstitial)

import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd; //interstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.rewarded.RewardedAd; //rewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;

import com.google.android.gms.ads.AdLoader; //used to native ads
import com.google.android.gms.ads.nativead.NativeAd; //
import com.google.android.gms.ads.nativead.NativeAdView; //view of native ads

import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

import android.app.Activity;
import android.os.Build;
import android.view.Gravity;
import android.widget.FrameLayout; //get Godot Layout
import android.view.View;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AdMob extends org.godotengine.godot.plugin.GodotPlugin
{
    private boolean aIsInitialized = false;
    private int aInstanceId;
    private Activity aActivity;
    private ConsentInformation aConsentInformation;

    private boolean aIsForChildDirectedTreatment;
    private String aMaxAdContentRating;
    private boolean aIsReal;

    private FrameLayout aGodotLayout; // store the godot layout
    private FrameLayout.LayoutParams aGodotLayoutParams; // Store the godot layout params

    private AdView aAdView; //view of banner
    private InterstitialAd aInterstitialAd;
    private RewardedAd aRewardedAd;
    private NativeAd aNativeAd;
    private NativeAdView aNativeAdView;

    public AdMob(Godot godot)
    {
        super(godot);
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
                "load_native",
                "destroy_native"
        );
    }

    @Override
    public View onMainCreate(Activity pActivity)
    {
        aActivity = pActivity;
        aGodotLayout = new FrameLayout(pActivity);
        return aGodotLayout;
    }

    @NonNull
    @Override
    public String getPluginName() {
        return getClass().getSimpleName();
    }

    public void loadConsentForm()
    {
        UserMessagingPlatform.loadConsentForm(
            aActivity,
            new UserMessagingPlatform.OnConsentFormLoadSuccessListener()
            {
                @Override
                public void onConsentFormLoadSuccess(ConsentForm consentForm)
                {
                    String consentStatusMsg = "";
                    if(aConsentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED)
                    {
                        consentForm.show(
                            aActivity,
                            new ConsentForm.OnConsentFormDismissedListener() {
                                @Override
                                public void onConsentFormDismissed(@Nullable FormError formError) {
                                    loadConsentForm();
                                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_consent_form_dismissed", new Object[]{ });
                                }
                            }
                        );
                        consentStatusMsg = "User consent required but not yet obtained.";
                    }
                    switch (aConsentInformation.getConsentStatus()){
                        case ConsentInformation.ConsentStatus.UNKNOWN:
                            consentStatusMsg = "Unknown consent status.";
                            initializeAfterUMP();
                            break;
                        case ConsentInformation.ConsentStatus.NOT_REQUIRED:
                            consentStatusMsg = "User consent not required. For example, the user is not in the EEA or the UK.";
                            initializeAfterUMP();
                            break;
                        case ConsentInformation.ConsentStatus.OBTAINED:
                            consentStatusMsg = "User consent obtained. Personalization not defined.";
                            initializeAfterUMP();
                            break;
                    }
                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_consent_status_changed", new Object[]{ consentStatusMsg });
                }
            },
            new UserMessagingPlatform.OnConsentFormLoadFailureListener()
            {
                @Override
                public void onConsentFormLoadFailure(FormError formError)
                {
                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_consent_form_load_failure", new Object[]{ formError.getErrorCode(), formError.getMessage() });
                    initializeAfterUMP();
                }
            }
        );
    }
    public void initialize(boolean pIsForChildDirectedTreatment, String pMaxAdContentRating, boolean pIsReal, boolean pIsTestEuropeUserConsent, int pInstanceId)
    {
        aInstanceId = pInstanceId;
        aIsForChildDirectedTreatment = pIsForChildDirectedTreatment;
        aMaxAdContentRating = pMaxAdContentRating;
        aIsReal = pIsReal;

        aConsentInformation = UserMessagingPlatform.getConsentInformation(aActivity);

        ConsentRequestParameters.Builder paramsBuilder = new ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(pIsForChildDirectedTreatment);

        ConsentRequestParameters params;
        if (pIsTestEuropeUserConsent)
        {
            aConsentInformation.reset();
            ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(aActivity)
                    .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                    .addTestDeviceHashedId(getDeviceId())
                    .build();
            params = paramsBuilder.setConsentDebugSettings(debugSettings).build();
        }
        else
        {
            params = paramsBuilder.build();
        }


        aConsentInformation.requestConsentInfoUpdate(aActivity, params,
            new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                @Override
                public void onConsentInfoUpdateSuccess() {
                    if (aConsentInformation.isConsentFormAvailable()) {
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_consent_info_update_success", new Object[]{ "Consent Form Available" });
                        loadConsentForm();
                    }
                    else{
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_consent_info_update_success", new Object[]{ "Consent Form not Available" });
                        initializeAfterUMP();
                    }
                }
            },
            new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                @Override
                public void onConsentInfoUpdateFailure(FormError formError) {
                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_consent_info_update_failure", new Object[]{ formError.getErrorCode(), formError.getMessage() });
                    initializeAfterUMP();
                }
            }
        );

    }

    private void initializeAfterUMP(){
        if (!aIsInitialized){
            setMobileAdsRequestConfiguration(aIsForChildDirectedTreatment, aMaxAdContentRating, aIsReal); //First call MobileAds.setRequestConfiguration https://groups.google.com/g/google-admob-ads-sdk/c/17oVu0sABjs
            MobileAds.initialize(aActivity, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                    aIsInitialized = true;
                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_initialization_complete", new Object[]{ initializationStatus.getAdapterStatusMap().get("com.google.android.gms.ads.MobileAds").getInitializationState().ordinal(), "GADMobileAds" });
                }
            }); //initializes the admob
        }
    }

    private void setMobileAdsRequestConfiguration(boolean pIsForChildDirectedTreatment, String pMaxAdContentRating, boolean pIsReal)
    {
        RequestConfiguration requestConfiguration;
        RequestConfiguration.Builder requestConfigurationBuilder = new RequestConfiguration.Builder();

        if (!pIsReal) {
            requestConfigurationBuilder.setTestDeviceIds(Collections.singletonList(getDeviceId()));
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

        return adRequestBuilder.build();
    }

    //BANNER only one is allowed, please do not try to place more than one, as your ads on the app may have the chance to be banned!
    public void load_banner(final String pAdUnitId, final int pPosition, final String pSize)
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run() {
                if (aIsInitialized) {
                    if (aAdView != null) destroy_banner();
                    if (aNativeAd != null) destroy_native();

                    aAdView = new AdView(aActivity);
                    aAdView.setAdUnitId(pAdUnitId);
                    switch (pSize) {
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

                    aAdView.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_loaded", new Object[]{});
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                            // Code to be executed when an ad request fails.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_failed_to_load", new Object[]{adError.getCode()});
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when an ad opens an overlay that
                            // covers the screen.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_opened", new Object[]{});
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the user is about to return
                            // to the app after tapping on an ad.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_closed", new Object[]{});
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
                    aGodotLayout.addView(aAdView, aGodotLayoutParams);

                    aAdView.loadAd(getAdRequest());
                }
            }
        });
    }
    public void destroy_banner()//IF THIS METHOD IS CALLED ON GODOT, THE BANNER WILL ONLY APPEAR AGAIN IF THE BANNER IS LOADED AGAIN
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aIsInitialized) {
                    if (aAdView != null) {
                        aGodotLayout.removeView(aAdView);
                        aAdView.destroy();
                        aAdView = null;
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_destroyed", new Object[]{});
                    }
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
                if (aIsInitialized) {
                    InterstitialAd.load(aActivity, pAdUnitId, getAdRequest(), new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // Code to be executed when an ad finishes loading.
                            aInterstitialAd = interstitialAd;
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_loaded", new Object[]{});
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    aInterstitialAd = null;
                                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_closed", new Object[]{});
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    aInterstitialAd = null;
                                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_failed_to_show", new Object[]{adError.getCode()});
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_opened", new Object[]{});
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                            // Code to be executed when an ad request fails.
                            aInterstitialAd = null;
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_failed_to_load", new Object[]{adError.getCode()});
                        }
                    });
                }
            }
        });
    }
    public void show_interstitial()
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aIsInitialized) {
                    if (aInterstitialAd != null) {
                        aInterstitialAd.show(aActivity);
                    }
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
            @Override public void run() {
                if (aIsInitialized) {
                    RewardedAd.load(aActivity, pAdUnitId, getAdRequest(), new RewardedAdLoadCallback(){
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            aRewardedAd = null;
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_failed_to_load", new Object[]{loadAdError.getCode()});
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            aRewardedAd = rewardedAd;
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_loaded", new Object[]{});
                        }
                    });
                }
            }
        });
    }
    public void show_rewarded()
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aIsInitialized) {
                    if (aRewardedAd != null) {
                        aRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_opened", new Object[]{});
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when ad fails to show.
                                aRewardedAd = null;
                                GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_failed_to_show", new Object[]{adError.getCode()});
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                aRewardedAd = null;
                                GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_closed", new Object[]{});
                            }
                        });

                        aRewardedAd.show(aActivity, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            GodotLib.calldeferred(aInstanceId, "_on_AdMob_user_earned_rewarded", new Object[]{rewardItem.getType(), rewardItem.getAmount()});
                            }
                        });
                    }
                }
            }
        });
    }
    //REWARDED
    //NATIVE
    public void load_native(final String pAdUnitId, final int[] pSize, final int[] pMargins)
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aIsInitialized) {

                    if (aNativeAdView != null) destroy_native();
                    if (aAdView != null) destroy_banner();

                    AdLoader adLoader = new AdLoader.Builder(aActivity, pAdUnitId)
                            .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                @Override
                                public void onNativeAdLoaded (@NonNull NativeAd nativeAd) {
                                // If this callback occurs after the activity is destroyed, you must call
                                // destroy and return or you may get a memory leak.
                                boolean isDestroyed = false;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    isDestroyed = aActivity.isDestroyed();
                                }
                                if (isDestroyed || aActivity.isFinishing() || aActivity.isChangingConfigurations()) {
                                    nativeAd.destroy();
                                    return;
                                }

                                //SECURE TO DESTROY THE NATIVE AND THE BANNER
                                if (aNativeAdView != null) destroy_native();
                                if (aAdView != null) destroy_banner();

                                aNativeAd = nativeAd;
                                aNativeAdView = (NativeAdView) aActivity.getLayoutInflater().inflate(R.layout.ad_native, null);

                                mapNativeAdToLayout(aNativeAd, aNativeAdView);
                                aGodotLayoutParams = new FrameLayout.LayoutParams(pSize[0], pSize[1]);
                                aGodotLayoutParams.setMargins(pMargins[0], pMargins[1], 0, 0);
                                aGodotLayout.removeAllViews();
                                aGodotLayout.addView(aNativeAdView, aGodotLayoutParams);
                                GodotLib.calldeferred(aInstanceId, "_on_AdMob_native_loaded", new Object[]{});
                                }
                            })
                            .withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                    // Code to be executed when an ad request fails.
                                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_native_failed_to_load", new Object[]{adError.getCode()});
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when the ad is displayed.
                                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_native_opened", new Object[]{});
                                }

                                @Override
                                public void onAdClosed() {
                                    // Code to be executed when the native ad is closed.
                                    GodotLib.calldeferred(aInstanceId, "_on_AdMob_native_closed", new Object[]{});
                                }

                            })
                            .build();

                    adLoader.loadAd(getAdRequest());
                }
            }
        });
    }
    public void destroy_native()//IF THIS METHOD IS CALLED ON GODOT, THE NATIVE AD WILL ONLY APPEAR AGAIN IF THE AD IS LOADED AGAIN
    {
        aActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                if (aIsInitialized) {
                    if (aNativeAdView != null) {
                        aGodotLayout.removeView(aNativeAdView);
                        aNativeAdView.destroy();
                        aNativeAdView = null;

                        aNativeAd.destroy();
                        aNativeAd = null;
                        GodotLib.calldeferred(aInstanceId, "_on_AdMob_native_destroyed", new Object[]{});
                    }
                }
            }
        });
    }

    public void mapNativeAdToLayout(NativeAd adFromGoogle, NativeAdView myAdView)
    {
        myAdView.setMediaView((MediaView) myAdView.findViewById(R.id.ad_media));

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
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & b));
                while (h.length() < 2) h.insert(0, "0");
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
        return md5(android_id).toUpperCase(Locale.US);
    }
}