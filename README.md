# Godot AdMob (3.1.2)
### Android only, iOS still in development
- Banner 
- Interstitial
- Rewarded
- Unified Native (Native Ads)

Is high recommended that when you use AdMob, please include it as AutoLoad and Singleton

Download example project to see how the Plugin works!


### API References
---
Signals:
```GDScript
banner_loaded #when an ad finishes loading
banner_destroyed #when banner view is destroyed
banner_failed_to_load(error_code : int) #when an ad request fails
banner_opened #when an ad opens an overlay that
banner_clicked #when the user clicks on an ad
banner_left_application #when the user has left the app
banner_closed #when the user is about to return to the app after tapping on an ad

interstitial_loaded #when an ad finishes loading
interstitial_failed_to_load(error_code : int) #when an ad request fails
interstitial_opened #when the ad is displayed
interstitial_clicked #when the user clicks on an ad
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
init(is_for_child_directed_treatment := true, is_personalized := false, max_ad_content_rating := "G", instance_id := get_instance_id(), test_device_id := "") #init the AdMob

load_banner(gravity : int = GRAVITY.BOTTOM, size : String = "SMART_BANNER", unit_id : String = ad.banner.unit_id[OS.get_name()]) #load the banner will make him appear instantly
load_interstitial(unit_id: String = ad.interstitial.unit_id[OS.get_name()]) #loads the interstitial and make ready for show
load_rewarded(unit_id : String = ad.rewarded.unit_id[OS.get_name()]) #loads the rewarded and make ready for show
load_unified_native(control_node_to_be_replaced : Control = Control.new(), unit_id : String = ad.unified_native.unit_id[OS.get_name()]) #load the unified native will make him appear instantly (unified native and banner are View in Android and iOS, it is recommended to only use one of them at a time, if you try to use both, the module will not allow it, it will remove the older view

destroy_banner() #completely destroys the Banner View
destroy_unified_native() #completely destroys the Unified Native View

show_interstitial() #shows interstitial
show_rewarded() #shows rewarded

_on_AdMob_*() #just to emit signals
```
