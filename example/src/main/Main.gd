extends Control


onready var EnableBanner : Button = $CenterContainer/VBoxContainer/Banner/EnableBanner
onready var DisableBanner : Button = $CenterContainer/VBoxContainer/Banner/DisableBanner
onready var Interstitial : Button = $CenterContainer/VBoxContainer/Interstitial
onready var Rewarded : Button = $CenterContainer/VBoxContainer/Rewarded
onready var EnableNative : Button = $CenterContainer/VBoxContainer/UnifiedNative/EnableUnifiedNative
onready var DisableNative : Button = $CenterContainer/VBoxContainer/UnifiedNative/DisableUnifiedNative

onready var UnifiedNativePanel : Panel = $UnifiedNative
onready var UnifiedNativeHBox : HBoxContainer = $CenterContainer/VBoxContainer/UnifiedNative

onready var Advice : Button = $Advice

func _ready():
	if OS.get_name() == "Android" or OS.get_name() == "iOS":
		EnableBanner.disabled = false
		EnableNative.disabled = false
		AdMob.load_interstitial()
		AdMob.load_rewarded()
		AdMob.connect("banner_destroyed", self, "_on_AdMob_banner_destroyed")
		AdMob.connect("interstitial_loaded", self, "_on_AdMob_interstitial_loaded")
		AdMob.connect("interstitial_closed", self, "_on_AdMob_interstitial_closed")
		AdMob.connect("rewarded_ad_closed", self, "_on_AdMob_rewarded_ad_closed")
		AdMob.connect("rewarded_ad_loaded", self, "_on_AdMob_rewarded_ad_loaded")
		AdMob.connect("rewarded_user_earned_rewarded", self, "_on_AdMob_rewarded_user_earned_rewarded")
		if OS.get_name() == "Android":
			AdMob.connect("unified_native_destroyed", self, "_on_AdMob_unified_native_destroyed")
		else:
			UnifiedNativePanel.hide()
			UnifiedNativeHBox.hide()
	else:
		Advice.text = "Module only works on Android or iOS devices!"
	
	
func _on_AdMob_interstitial_loaded():
	Interstitial.disabled = false

func _on_Interstitial_pressed():
	AdMob.show_interstitial()
	Interstitial.disabled = true
	
func _on_AdMob_interstitial_closed():
	AdMob.load_interstitial()
	

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

func _on_DisableBanner_pressed():
	DisableBanner.disabled = true
	EnableBanner.disabled = false
	EnableNative.disabled = false
	AdMob.destroy_banner()


func _on_Rewarded_pressed():
	AdMob.show_rewarded()
	Rewarded.disabled = true
	
func _on_AdMob_rewarded_ad_closed():
	AdMob.load_rewarded()

func _on_AdMob_rewarded_ad_loaded():
	Rewarded.disabled = false
	
func _on_AdMob_rewarded_user_earned_rewarded(currency : String, amount : int):
	Advice.text = "EARNED " + currency + " with amount: " + str(amount)


func _on_EnableUnifiedNative_pressed():
	DisableNative.disabled = false
	EnableNative.disabled = true
	EnableBanner.disabled = true
	AdMob.load_unified_native($UnifiedNative)

func _on_DisableUnifiedNative_pressed():
	DisableNative.disabled = true
	EnableNative.disabled = false
	EnableBanner.disabled = false
	AdMob.destroy_unified_native()


