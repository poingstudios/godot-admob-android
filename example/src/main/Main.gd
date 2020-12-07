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
	if OS.get_name() == "Android" or OS.get_name() == "iOS":
		# warning-ignore:return_value_discarded
		AdMob.connect("banner_destroyed", self, "_on_AdMob_banner_destroyed")
		# warning-ignore:return_value_discarded
		AdMob.connect("interstitial_loaded", self, "_on_AdMob_interstitial_loaded")
		# warning-ignore:return_value_discarded
		AdMob.connect("rewarded_ad_loaded", self, "_on_AdMob_rewarded_ad_loaded")
		# warning-ignore:return_value_discarded
		AdMob.connect("rewarded_user_earned_rewarded", self, "_on_AdMob_rewarded_user_earned_rewarded")
		# warning-ignore:return_value_discarded
		AdMob.connect("initialized", self, "_on_AdMob_initialized")
		if OS.get_name() == "Android":
			# warning-ignore:return_value_discarded
			AdMob.connect("unified_native_destroyed", self, "_on_AdMob_unified_native_destroyed")
		else:
			UnifiedNativePanel.hide()
			UnifiedNativeHBox.hide()
	else:
		_add_text_Advice_Node("Module only works on Android or iOS devices!")

func _on_AdMob_initialized():
	_add_text_Advice_Node("AdMob initialized! With parameters:")
	_add_text_Advice_Node("is_for_child_directed_treatment: " + str(AdMob.is_for_child_directed_treatment))
	_add_text_Advice_Node("is_personalized: " + str(AdMob.is_personalized))
	_add_text_Advice_Node("max_ad_content_rating: " + str(AdMob.is_real))
	_add_text_Advice_Node("instance_id: " + str(get_instance_id()))
	_add_text_Advice_Node("---------------------------------------------------")
	EnableBanner.disabled = false
	EnableNative.disabled = false

func _on_AdMob_interstitial_loaded():
	Interstitial.disabled = false
	_add_text_Advice_Node("Interstitial loaded")

func _on_Interstitial_pressed():
	AdMob.show_interstitial()
	Interstitial.disabled = true
	
func reset_banner_and_unified_buttons():
	DisableBanner.disabled = true
	DisableNative.disabled = true
	EnableBanner.disabled = false
	EnableNative.disabled = false

func _on_AdMob_banner_destroyed():
	reset_banner_and_unified_buttons()
func _on_AdMob_unified_native_destroyed():
	reset_banner_and_unified_buttons()

func _on_EnableBanner_pressed():
	DisableBanner.disabled = false
	EnableBanner.disabled = true
	EnableNative.disabled = true
	AdMob.load_banner()
	_add_text_Advice_Node("Banner loaded")

func _on_DisableBanner_pressed():
	DisableBanner.disabled = true
	EnableBanner.disabled = false
	EnableNative.disabled = false
	AdMob.destroy_banner()
	_add_text_Advice_Node("Banner destroyed")

func _on_Rewarded_pressed():
	AdMob.show_rewarded()
	Rewarded.disabled = true
	
func _on_AdMob_rewarded_ad_loaded():
	Rewarded.disabled = false
	_add_text_Advice_Node("Rewarded ad loaded")
	
func _on_AdMob_rewarded_user_earned_rewarded(currency : String, amount : int):
	Advice.bbcode_text += "EARNED " + currency + " with amount: " + str(amount) + "\n"


func _on_EnableUnifiedNative_pressed():
	DisableNative.disabled = false
	EnableNative.disabled = true
	EnableBanner.disabled = true
	AdMob.load_unified_native($UnifiedNative)
	_add_text_Advice_Node("Unified Native loaded")

func _on_DisableUnifiedNative_pressed():
	DisableNative.disabled = true
	EnableNative.disabled = false
	EnableBanner.disabled = false
	AdMob.destroy_unified_native()
	_add_text_Advice_Node("Unified Natived destroyed")
