package org.godotengine.godot;
import com.godot.game.R;
import android.app.Activity; 

import android.widget.FrameLayout; //get Godot Layout

import com.google.android.gms.ads.MobileAds; //used for initialize
import com.google.android.gms.ads.AdRequest; //used for make requests of ads

import com.google.android.gms.ads.AdView; //used to banner ads
import com.google.android.gms.ads.AdSize; //used to set/get size banner ads
import com.google.android.gms.ads.AdListener; //used to get events of ads (banner, interstitial)

import com.google.android.gms.ads.InterstitialAd; //interstitialAd

import com.google.android.gms.ads.rewarded.RewardedAd; //rewardedAd 
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardItem;

import com.google.android.gms.ads.AdLoader; //used to native ads
import com.google.android.gms.ads.formats.UnifiedNativeAd; //
import com.google.android.gms.ads.formats.UnifiedNativeAdView; //view of native ads
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAd.Image;

import com.google.android.gms.ads.formats.MediaView;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import android.os.Bundle;

import com.google.ads.mediation.admob.AdMobAdapter;

public class AdMob extends Godot.SingletonBase 
{
    private Activity aActivity;
	private int aInstanceId;
	private String aTestDeviceId = "";

	private FrameLayout aGodotLayout; // store the godot layout
	private FrameLayout.LayoutParams aGodotLayoutParams; // Store the godot layout params

	private AdView aAdView; //view of banner
	private String aSize = ""; //size of banner

	private InterstitialAd aInterstitialAd;

	private RewardedAd pRewardedAd;

	private UnifiedNativeAdView aUnifiedNativeAdView;

	private boolean aIsForChildDirectedTreatment;
	private boolean aIsPersonalized;
	private String aMaxAdContentRating;


    public void init(boolean pIsForChildDirectedTreatment, boolean pIsPersonalized, String pMaxAdContentRating, int pInstanceId, String pTestDeviceId) 
    {
		aInstanceId = pInstanceId;
		MobileAds.initialize(aActivity); //initialize(Context context, OnInitializationCompleteListener listener) doesnt work due Godot cant download the package
		aIsForChildDirectedTreatment = pIsForChildDirectedTreatment;
		aIsPersonalized = pIsPersonalized;
		aMaxAdContentRating = pMaxAdContentRating;
		aTestDeviceId = pTestDeviceId;
    }

    //BANNER only one is allowed, please do not try to place more than one, as your ads on the app may have the chance to be banned!
    public void load_banner(final String pAdUnitId, final int pGravity, final String pSize)
    {
    	aActivity.runOnUiThread(new Runnable()
		{
			@Override public void run()
			{
				if (pSize != "") aSize = pSize;
				if (aAdView != null) destroy_banner();
				if (aUnifiedNativeAdView != null) destroy_unified_native();

				aAdView = new AdView(aActivity);
				aAdView.setAdUnitId(pAdUnitId);
				switch (aSize){
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

		    	aAdView.setAdListener(new AdListener() 
		    	{
				    @Override
				    public void onAdLoaded() 
				    {
				        // Code to be executed when an ad finishes loading.
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_loaded", new Object[]{ });
				    }

				    @Override
				    public void onAdFailedToLoad(int errorCode) {
				        // Code to be executed when an ad request fails.
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_failed_to_load", new Object[]{ errorCode });
				    }

				    @Override
				    public void onAdOpened() {
				        // Code to be executed when an ad opens an overlay that
				        // covers the screen.
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_opened", new Object[]{ });
				    }

				    @Override
				    public void onAdClicked() {
				        // Code to be executed when the user clicks on an ad.
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_banner_clicked", new Object[]{ });
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

				aGodotLayout = ((Godot) aActivity).layout;
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
				    public void onAdFailedToLoad(int errorCode) 
				    {
				        // Code to be executed when an ad request fails.
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_failed_to_load", new Object[]{ errorCode });
				    }

				    @Override
				    public void onAdOpened() 
				    {
				        // Code to be executed when the ad is displayed.
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_opened", new Object[]{ });
				    }

				    @Override
				    public void onAdClicked() 
				    {
				        // Code to be executed when the user clicks on an ad.
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_interstitial_clicked", new Object[]{ });
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
					if (aAdView != null) destroy_banner();
					if (aUnifiedNativeAdView != null) destroy_unified_native();

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
				pRewardedAd = new RewardedAd(aActivity, pAdUnitId);
				RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() 
				{
					@Override
					public void onRewardedAdLoaded() 
					{
						// Ad successfully loaded
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_loaded", new Object[] { });
					}

					@Override
					public void onRewardedAdFailedToLoad(int errorCode) 
					{
						// Ad failed to load
						GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_failed_to_load", new Object[] { errorCode });
					}
				};
				pRewardedAd.loadAd(getAdRequest(), adLoadCallback);
			}
		});
    }
    public void show_rewarded()
    {
		aActivity.runOnUiThread(new Runnable()
		{
			@Override public void run()
			{
		    	if (pRewardedAd != null && pRewardedAd.isLoaded()) 
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
		                public void onRewardedAdFailedToShow(int errorCode) 
		                {
		                	// Ad failed to display.
							GodotLib.calldeferred(aInstanceId, "_on_AdMob_rewarded_ad_failed_to_show", new Object[] { errorCode });		                	
		                }
		            };
				if (aAdView != null) destroy_banner();
				if (aUnifiedNativeAdView != null) destroy_unified_native();

	            pRewardedAd.show(aActivity, adCallback);
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
							aGodotLayout = ((Godot) aActivity).layout;
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
					    public void onAdFailedToLoad(int errorCode) 
					    {
					        // Code to be executed when an ad request fails.
							GodotLib.calldeferred(aInstanceId, "_on_AdMob_unified_native_failed_to_load", new Object[]{ errorCode });
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
    public void destroy_unified_native()//IF THIS METHOD IS CALLED ON GODOT, THE UNIFIED NATIVE AD WILL ONLY APPEAR AGAIN IF THE BANNER IS LOADED AGAIN
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
    private AdRequest getAdRequest()
    {
    	AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

    	adRequestBuilder.tagForChildDirectedTreatment(aIsForChildDirectedTreatment);
    	if (aIsForChildDirectedTreatment) {
			adRequestBuilder.setMaxAdContentRating("G");
    	}
    	else{
			adRequestBuilder.setMaxAdContentRating(aMaxAdContentRating);
    	}
		if (!aIsPersonalized) {
	    	Bundle extras = new Bundle();
    		extras.putString("npa", "1");
    		adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
		}
    	if (aTestDeviceId != "") {
	    	adRequestBuilder.addTestDevice(aTestDeviceId);
	    }

    	return adRequestBuilder.build();
    }

    static public Godot.SingletonBase initialize(Activity pActivity) 
    {
        return new AdMob(pActivity);
    }

    public AdMob(Activity pActivity) 
    {
        //register class name and functions to bind
        registerClass("AdMob", new String[]
        {
        	"init",
        	"test_init",
        	"load_banner",
        	"destroy_banner",
        	"load_interstitial",
        	"show_interstitial",
        	"load_rewarded",
        	"show_rewarded",
        	"load_unified_native",
        	"destroy_unified_native"
        });

        this.aActivity = pActivity;
    }
}