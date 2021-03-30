extends Control

onready var EnableBanner : Button = $CenterContainer/VBoxContainer/Banner/EnableBanner
onready var DisableBanner : Button = $CenterContainer/VBoxContainer/Banner/DisableBanner
onready var Interstitial : Button = $CenterContainer/VBoxContainer/Interstitial
onready var Rewarded : Button = $CenterContainer/VBoxContainer/Rewarded
onready var EnableNative : Button = $CenterContainer/VBoxContainer/UnifiedNative/EnableUnifiedNative
onready var DisableNative : Button = $CenterContainer/VBoxContainer/UnifiedNative/DisableUnifiedNative
onready var UnifiedNativePanel : Panel = $UnifiedNative
onready var UnifiedNativeHBox : HBoxContainer = $CenterContainer/VBoxContainer/UnifiedNative
onready var Advice : RichTextLabel = $Advice

func _add_text_Advice_Node(text_value : String):
	Advice.bbcode_text += text_value + "\n"

func _ready():
	OS.center_window()
	$AudioStreamPlayer.play()
	if OS.get_name() == "Android" or OS.get_name() == "iOS":
		for i in ["BANNER", "MEDIUM_RECTANGLE", "FULL_BANNER", "LEADERBOARD", "SMART_BANNER"]:
			$BannerSizes.add_item(i)

		# warning-ignore:return_value_discarded
		MobileAds.connect("consent_info_update_failure", self, "_on_AdMob_consent_info_update_failure")
		# warning-ignore:return_value_discarded
		MobileAds.connect("banner_loaded", self, "_on_AdMob_banner_loaded")
		# warning-ignore:return_value_discarded
		MobileAds.connect("banner_destroyed", self, "_on_AdMob_banner_destroyed")
		# warning-ignore:return_value_discarded
		MobileAds.connect("interstitial_loaded", self, "_on_AdMob_interstitial_loaded")
		# warning-ignore:return_value_discarded
		MobileAds.connect("interstitial_closed", self, "_on_AdMob_interstitial_closed")
		# warning-ignore:return_value_discarded
		MobileAds.connect("rewarded_ad_loaded", self, "_on_AdMob_rewarded_ad_loaded")
		# warning-ignore:return_value_discarded
		MobileAds.connect("rewarded_ad_closed", self, "_on_AdMob_rewarded_ad_closed")
		# warning-ignore:return_value_discarded
		MobileAds.connect("rewarded_user_earned_rewarded", self, "_on_AdMob_rewarded_user_earned_rewarded")
		# warning-ignore:return_value_discarded
		MobileAds.connect("initialization_complete", self, "_on_AdMob_initialization_complete")
		if OS.get_name() == "Android":
			# warning-ignore:return_value_discarded
			MobileAds.connect("unified_native_loaded", self, "_on_AdMob_unified_native_loaded")
			# warning-ignore:return_value_discarded
			MobileAds.connect("unified_native_destroyed", self, "_on_AdMob_unified_native_destroyed")
		else:
			UnifiedNativePanel.hide()
			UnifiedNativeHBox.hide()
	else:
		_add_text_Advice_Node("AdMob only works on Android or iOS devices!")

func _on_AdMob_initialization_complete(status, _adapter_name):
	if status == MobileAdsBase.INITIALIZATION_STATUS.READY:
		MobileAds.load_interstitial()
		MobileAds.load_rewarded()
		_add_text_Advice_Node("AdMob initialized! With parameters:")
		_add_text_Advice_Node("is_real: " + str(MobileAds.is_real))
		_add_text_Advice_Node("is_for_child_directed_treatment: " + str(MobileAds.is_for_child_directed_treatment))
		_add_text_Advice_Node("max_ad_content_rating: " + str(MobileAds.max_ad_content_rating))
		_add_text_Advice_Node("instance_id: " + str(get_instance_id()))
		EnableBanner.disabled = false
		EnableNative.disabled = false
	else:
		_add_text_Advice_Node("AdMob not initialized, check your configuration")
	_add_text_Advice_Node("---------------------------------------------------")
func _on_AdMob_interstitial_loaded():
	Interstitial.disabled = false
	_add_text_Advice_Node("Interstitial loaded")

func _on_AdMob_interstitial_closed():
	MobileAds.load_interstitial()
	_add_text_Advice_Node("Interstitial closed")

func _on_Interstitial_pressed():
	MobileAds.show_interstitial()
	Interstitial.disabled = true
	
func reset_banner_and_unified_buttons():
	DisableBanner.disabled = true
	DisableNative.disabled = true
	EnableBanner.disabled = false
	EnableNative.disabled = false

func _on_AdMob_banner_destroyed():
	reset_banner_and_unified_buttons()
	_add_text_Advice_Node("Banner destroyed")

func _on_AdMob_unified_native_destroyed():
	reset_banner_and_unified_buttons()
	_add_text_Advice_Node("Unified Natived destroyed")

func _on_AdMob_banner_loaded():
	DisableNative.disabled = true
	DisableBanner.disabled = false
	EnableBanner.disabled = true
	EnableNative.disabled = true
	_add_text_Advice_Node("Banner loaded")

func _on_EnableBanner_pressed():
	EnableBanner.disabled = true
	EnableNative.disabled = true
	MobileAds.load_banner()

func _on_DisableBanner_pressed():
	DisableBanner.disabled = true
	EnableBanner.disabled = false
	EnableNative.disabled = false
	MobileAds.destroy_banner()

func _on_Rewarded_pressed():
	MobileAds.show_rewarded()
	Rewarded.disabled = true
	
func _on_AdMob_rewarded_ad_loaded():
	Rewarded.disabled = false
	_add_text_Advice_Node("Rewarded ad loaded")
	
func _on_AdMob_rewarded_ad_closed():
	MobileAds.load_rewarded()
	_add_text_Advice_Node("Rewarded ad closed")
	
func _on_AdMob_rewarded_user_earned_rewarded(currency : String, amount : int):
	Advice.bbcode_text += "EARNED " + currency + " with amount: " + str(amount) + "\n"


func _on_AdMob_unified_native_loaded():
	DisableNative.disabled = false
	DisableBanner.disabled = true
	EnableBanner.disabled = true
	EnableNative.disabled = true
	_add_text_Advice_Node("Unified Native loaded")

func _on_EnableUnifiedNative_pressed():
	EnableNative.disabled = true
	EnableBanner.disabled = true
	MobileAds.load_unified_native($UnifiedNative)

func _on_DisableUnifiedNative_pressed():
	DisableNative.disabled = true
	EnableNative.disabled = false
	EnableBanner.disabled = false
	MobileAds.destroy_unified_native()


func _on_BannerSizes_item_selected(index):
	if MobileAds.is_initialized:
		var item_text : String = $BannerSizes.get_item_text(index)
		MobileAds.banner_size = item_text
		_add_text_Advice_Node("Banner Size changed:" + item_text)
		if MobileAds.banner_enabled:
			MobileAds.load_banner()
