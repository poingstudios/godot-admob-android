[![Build%20iOS](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Build%20iOS/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Build%20iOS%20Mono](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Build%20iOS%20Mono/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Build%20Android](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Build%20Android/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Build%20Android%20Mono](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Build%20Android%20Mono/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Copy%20admob_api%2F](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Copy%20admob_api%2F/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)
[![Export%20for%20Android%20and%20iOS](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/workflows/Export%20for%20Android%20and%20iOS/badge.svg)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/actions)


# Godot AdMob for Android and iOS
This repository uses [GitHub Actions](https://github.com/features/actions), this means that whenever a new update is sent to the repository, the action will automatically test the code of the module, compile, compress the binary files and export to the ["Releases tab"](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases) of the repository for the respective operational system and versions supported by the module, like v3.2.3.


<p align="center">
	<img align="center" src="https://i.imgur.com/u5y2GEx.png">
</p>

### Ad Formats
- Banner 
- Interstitial
- Rewarded
- Unified Native (Native Ads) (ONLY Android at moment)

Is high recommended that when you use AdMob, please include it as AutoLoad and Singleton

Download example project to see how the Plugin works!

# Android (v3.2.2+):
- Download the ```android-template-v{{ your_godot_version }}.zip``` in the releases tab. [STABLE VERSION](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/tag/Android_v3.2.2%2B) and [MONO VERSION](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/tag/Android_mono_v3.2.2%2B)
- Enable Android Build Template. [Check the tutorial here](https://docs.godotengine.org/en/stable/getting_started/workflow/export/android_custom_build.html)
- Extract the content in ```android-template-v{{ your_godot_version }}.zip``` into ```res://android/plugins``` directory on your Godot project
- On your Game Project go to:
	1. Project
		1. Export
			1. Android
				1. Options
					1. Custom Package 
						- [x] ```Use Custom Build```
					1. Plugins 
						- [x] ```Ad Mob```
- ```(Required ONLY when you are TESTING OR RELEASING your app with your AdMob App ID, otherwise you don't need to):```
	- Add your [AdMob App ID](https://support.google.com/admob/answer/7356431) to your app's ```res://android/build/AndroidManifest.xml``` file by adding a ```<meta-data>``` tag with name ```com.google.android.gms.ads.APPLICATION_ID```, as shown below.

```
<meta-data
	tools:replace="android:value"
	android:name="com.google.android.gms.ads.APPLICATION_ID"
	android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
```

# iOS (v3.0.0+):
- Download the ```ios-template-v{{ your_godot_version }}.zip``` in the releases tab. [STABLE VERSION](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/tag/iOS_v3.0%2B) and [MONO VERSION](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/tag/iOS_mono_v3.0%2B)
- Export your game to iOS
- Copy the library ```libgodot.iphone.release.fat.a``` you have downloaded from releases tab inside the exported Xcode project. **You must delete the 'your_project_name.a' (example: AdMob.a) and rename the 'libgodot.iphone.release.fat.a' with "your_project_name.a", should be like: 'AdMob.a'.** 
- ![](https://media2.giphy.com/media/miNlL020ZQYjK4r8e7/giphy.gif)
- Add the following frameworks to the project linking it using the "Build Phases" -> "Link Binary with Libraries" option:
	- Extract the following .framework from [```googlemobileadssdkios.zip```](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/releases/download/iOS_v3.0%2B/googlemobileadssdkios.zip):
		- GoogleAppMeasurement.framework 
		- GoogleMobileAds.framework
		- GoogleUtilities.xcframework
		- nanopb.xcframework
		- PromisesObjC.xcframework
		- UserMessagingPlatform.xcframework
	- These frameworks are already in your computer
		- AppTrackingTransparency | ```Status: (Optional) ``` (if not appear: need to update XCode or SDK version to iOS 14.0)
		- AdSupport | ```Status: (Optional) ```
		- JavaScriptCore
- Add the -ObjC linker flag to Other Linker Flags in your project's build settings:
![-ObjC](https://developers.google.com/admob/images/ios/objc_linker_flag.png)
- [Update your GAMENAME-Info.plist file](https://developers.google.com/admob/ios/quick-start#update_your_infoplist), add a GADApplicationIdentifier key with a string value of your [AdMob app ID](https://support.google.com/admob/answer/7356431):
![plist](https://i.imgur.com/1tcKXx5.png)
- [Enable SKAdNetwork to track conversions](https://developers.google.com/admob/ios/ios14#skadnetwork):
![SKAdNetwork](https://developers.google.com/admob/images/idfa/skadnetwork.png)
- [Request App Tracking Transparency authorization](https://developers.google.com/admob/ios/ios14#request)
![RequestAuthorization](https://developers.google.com/admob/images/idfa/editor.png)


### API References
---
Signals:
```GDScript
banner_loaded #when an ad finishes loading
banner_destroyed #when banner view is destroyed
banner_failed_to_load(error_code : int) #when an ad request fails
banner_opened #when an ad opens an overlay that
banner_left_application #when the user has left the app
banner_closed #when the user is about to return to the app after tapping on an ad

interstitial_loaded #when an ad finishes loading
interstitial_failed_to_load(error_code : int) #when an ad request fails
interstitial_opened #when the ad is displayed
interstitial_left_application #when the user has left the app
interstitial_closed #when the interstitial ad is closed

rewarded_ad_loaded #when ad successfully loaded
rewarded_ad_failed_to_load #when ad failed to load
rewarded_ad_opened #when the ad is displayed
rewarded_ad_closed #when the ad is closed
rewarded_user_earned_rewarded(currency : String, amount : int) #when user earner rewarded
rewarded_ad_failed_to_show(error_code) #when the ad request fails

unified_native_loaded #when unified native loaded and shows the ad
unified_native_destroyed #when unified native view destroyed
unified_native_failed_to_load(error_code : int) #when an ad request fails
unified_native_opened #when an ad opens an overlay that
unified_native_closed #when the user is about to return to the app after tapping on an ad
```

Methods
```GDScript
#Private
#-----------------
_initialize() #init the AdMob

#Public
#-----------------
load_banner() #load the banner will make him appear instantly
load_interstitial() #loads the interstitial and make ready for show
load_rewarded() #loads the rewarded and make ready for show
load_unified_native(control_node_to_be_replaced : Control = Control.new()) #load the unified native will make him appear instantly (unified native and banner are View in Android and iOS, it is recommended to only use one of them at a time, if you try to use both, the module will not allow it, it will remove the older view

show_interstitial() #shows interstitial
show_rewarded() #shows rewarded

destroy_banner() #completely destroys the Banner View
destroy_unified_native() #completely destroys the Unified Native View

_on_AdMob_*() #just to emit signals
```
