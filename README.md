[![Build%20iOS](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Build%20iOS/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Build%20Android](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Build%20Android/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Build%20Android%20Mono](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Build%20Android%20Mono/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Copy%20admob_api%2F](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Copy%20admob_api%2F/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Export%20for%20Android%20and%20iOS](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Export%20for%20Android%20and%20iOS/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)


# Godot AdMob for Android and iOS
This repository uses [GitHub Actions](https://github.com/features/actions), this means that whenever a new update is sent to the repository, the action will automatically test the code of the module, compile, compress the binary files and export to the ["Releases tab"](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases) of the repository for the respective operational system and versions supported by the module, like v3.3.


<p align="center">
	<img align="center" src="https://i.imgur.com/u5y2GEx.png">
</p>

### Ad Formats
- Banner 
- Interstitial
- Rewarded
- Rewarded Interstitial (BETA) (soon on [Android](https://developers.google.com/admob/android/rewarded-interstitial) and [iOS](https://developers.google.com/admob/ios/rewarded-interstitial))
- Native (Only on Android, soon on [iOS](https://developers.google.com/admob/ios/native/start))

Is high recommended that when you use AdMob, please include it as AutoLoad and Singleton

Download example project to see how the Plugin works!

# Android (v3.2.2+):
- Download the ```android-template-v{{ your_godot_version }}.zip``` in the releases tab. [STABLE VERSION](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/tag/Android_v3.2.2%2B) and [MONO VERSION](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/tag/Android_mono_v3.2.2%2B)
- Enable Android Build Template. [Check the tutorial here](https://docs.godotengine.org/en/stable/getting_started/workflow/export/android_custom_build.html)
- Extract the content in ```android-template-v{{ your_godot_version }}.zip``` into ```res://android/plugins``` directory on your Godot project
- On your Game Project go to:
	- Project
		- Export
			- Android
				- Options
					- Custom Package 
						- [x] ```Use Custom Build```
					- Plugins 
						- [x] ```Ad Mob```
- Add your [AdMob App ID](https://support.google.com/admob/answer/7356431) to your app's ```res://android/build/AndroidManifest.xml``` file by adding a ```<meta-data>``` tag with name ```com.google.android.gms.ads.APPLICATION_ID```, as shown below.

``` xml
<!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
<meta-data
	tools:replace="android:value"
	android:name="com.google.android.gms.ads.APPLICATION_ID"
	android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
```

- (Optional) If you are using UMP, you can add too the [Delay app measurement](https://developers.google.com/admob/ump/android/quick-start#delay_app_measurement_optional) inside ```AndroidManifest.xml``` 

``` xml
<meta-data
	android:name="com.google.android.gms.ads.DELAY_APP_MEASUREMENT_INIT"
	android:value="true"/>
```

# iOS (v3.3+):
- Download the ```ios-template-v{{ your_godot_version }}.zip``` in the releases tab. [STABLE VERSION](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/tag/iOS_v3.0%2B) and [MONO VERSION](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/tag/iOS_mono_v3.0%2B)
- Extract the content in ```ios-template-v{{ your_godot_version }}.zip``` into ```res://ios/plugins``` directory on your Godot project
- On your Game Project go to and enable:
	- Project
		- Export
			- iOS
				- Options
					- Plugins 
						- [x] ```Ad Mob```

- Export your game to iOS
- Into your Xcode Project: [Update your GAMENAME-Info.plist file](https://developers.google.com/admob/ios/quick-start#update_your_infoplist), on GADApplicationIdentifier key with a string value of your [AdMob app ID](https://support.google.com/admob/answer/7356431):
![plist](https://i.imgur.com/1tcKXx5.png)
- [Enable SKAdNetwork to track conversions](https://developers.google.com/admob/ios/ios14#skadnetwork):
![SKAdNetwork](https://developers.google.com/admob/images/idfa/skadnetwork.png)
- [Request App Tracking Transparency authorization](https://developers.google.com/admob/ios/ios14#request)
![RequestAuthorization](https://developers.google.com/admob/images/idfa/editor.png)
- (Optional) If you are using UMP, you can add too the [Delay app measurement](https://developers.google.com/admob/ump/ios/quick-start#delay_app_measurement_optional)
![DelayAppMeasurement](https://developers.google.com/admob/images/delay_app_measurement_plist.png)


# User Messaging Platform (UMP):
- To use UMP due of EUROPE ePrivacy Directive and the General Data Protection Regulation (GDPR), you first need to do configure your [Funding Choices](https://support.google.com/fundingchoices/answer/9180084).
- If your app is "ForChildDirectedTreatment" then the UMP [won't appear and signals won't work for consent](https://stackoverflow.com/a/63232045), this is normal so don't worry.
- To show personalized or non-personalized ads, then you need to change inside your [AdMob Account](https://apps.admob.com/?utm_source=internal&utm_medium=et&utm_campaign=helpcentrecontextualopt&utm_term=http://goo.gl/6Xkfcf&subid=ww-ww-et-amhelpv4)
![npa-image](https://i.stack.imgur.com/0v1eL.png)

# API References
Signals:
```GDScript
initialization_complete(status, adapter_name) #when AdMob initializes, can be enum NOT_READY(0) or READY(1)

banner_loaded() #ad finishes loading
banner_destroyed() #banner view is destroyed
banner_failed_to_load(error_code : int) #ad request fails
banner_opened() #ad opens an overlay that
banner_clicked() #when user clicks on ad
banner_closed() # user is about to return to the app after tapping on an ad
banner_recorded_impression() #an impression has been recorded for an ad


interstitial_loaded() #ad finishes loading
interstitial_failed_to_load(error_code : int) #ad request fails
interstitial_opened() #ad is displayed
interstitial_left_application() #user has left the app
interstitial_closed() #interstitial ad is closed

rewarded_ad_loaded() #ad successfully loaded
rewarded_ad_failed_to_load() #ad failed to load
rewarded_ad_opened() #ad is displayed
rewarded_ad_closed() #ad is closed
rewarded_user_earned_rewarded(currency : String, amount : int) #user earner rewarded
rewarded_ad_failed_to_show(error_code) #ad request fails

native_loaded() #native loaded and shows the ad
native_destroyed() #native view destroyed
native_failed_to_load(error_code : int) #ad request fails
native_opened() #ad opens an overlay that
native_clicked() #when user clicks on ad
native_closed() #user is about to return to the app after tapping on an ad
native_recorded_impression() #an impression has been recorded for an ad

consent_form_dismissed() #then the consent is REQUIRED(user in EEA or UK) and user dismissed the form
consent_status_changed(consent_status_message) #get the ConsentStatus
consent_form_load_failure(error_code, error_message) #get the code and message of error to see why form is not loading
consent_info_update_success() #consent information state was updated
consent_info_update_failure(error_code, error_message) #get the code and message of error to see why info update consent fail
```

Methods
```GDScript
#Private
#-----------------
_initialize() #init the AdMob
_on_AdMob_*() #just to emit signals

#Public
#-----------------
load_banner() #load the banner will make him appear instantly
load_interstitial() #loads the interstitial and make ready for show
load_rewarded() #loads the rewarded and make ready for show
load_native(control_node_to_be_replaced : Control = Control.new()) #load the native will make him appear instantly (native and banner are View in Android and iOS, it is recommended to only use one of them at a time, if you try to use both, the module will not allow it, it will remove the older view

show_interstitial() #shows interstitial
show_rewarded() #shows rewarded

destroy_banner() #completely destroys the Banner View
destroy_native() #completely destroys the Native View

```
