class_name MobileAdsBase

extends Control
enum INITIALIZATION_STATUS {NOT_READY, READY}
signal banner_loaded()
signal banner_destroyed()
signal banner_failed_to_load(error_code)
signal banner_opened()
signal banner_clicked()
signal banner_left_application()
signal banner_closed()

signal interstitial_loaded()
signal interstitial_failed_to_load(error_code)
signal interstitial_opened()
signal interstitial_clicked()
signal interstitial_left_application()
signal interstitial_closed()

signal rewarded_ad_loaded()
signal rewarded_ad_failed_to_load()
signal rewarded_ad_opened()
signal rewarded_ad_closed()
signal rewarded_user_earned_rewarded(currency, amount)
signal rewarded_ad_failed_to_show(error_code)

signal unified_native_loaded()
signal unified_native_destroyed()
signal unified_native_failed_to_load(error_code)
signal unified_native_opened()
signal unified_native_closed()

signal consent_form_dismissed()
signal consent_status_changed(consent_status_message)
signal consent_form_load_failure(error_code, error_message)
signal consent_info_update_success(consent_status_message)
signal consent_info_update_failure(error_code, error_message)

signal initialization_complete(status, adapter_name)

#public attributes
var is_initialized : bool = false 
var banner_enabled : bool = false
var unified_native_enabled : bool = false
var interstitial_loaded : bool = false
var rewarded_loaded : bool = false

#private attributes
var _admob_singleton : Object
var _control_node_to_be_replaced : Control
enum _position_options {BOTTOM, TOP}

onready var _unified_native_scale : Dictionary = {
	"x" : OS.get_screen_size().x / get_viewport_rect().size.x,
	"y" : OS.get_screen_size().y / get_viewport_rect().size.y,
}

#SIGNALS
func _on_AdMob_banner_loaded():
	emit_signal("banner_loaded")
	banner_enabled = true
	
func _on_AdMob_banner_destroyed():
	emit_signal("banner_destroyed")
	banner_enabled = false

func _on_AdMob_banner_failed_to_load(error_code : int):
	emit_signal("banner_failed_to_load", error_code)
	
func _on_AdMob_banner_opened():
	emit_signal("banner_opened")
	
func _on_AdMob_banner_clicked():
	emit_signal("banner_clicked")
	
func _on_AdMob_banner_left_application():
	emit_signal("banner_left_application")
	
func _on_AdMob_banner_closed():
	emit_signal("banner_closed")
	

func _on_AdMob_interstitial_loaded():
	interstitial_loaded = true
	emit_signal("interstitial_loaded")

func _on_AdMob_interstitial_failed_to_load(error_code : int):
	emit_signal("interstitial_failed_to_load", error_code)
	
func _on_AdMob_interstitial_clicked():
	emit_signal("interstitial_clicked")
	
func _on_AdMob_interstitial_left_application():
	emit_signal("interstitial_left_application")

func _on_AdMob_interstitial_opened():
	emit_signal("interstitial_opened")
	get_tree().paused = true

func _on_AdMob_interstitial_closed():
	interstitial_loaded = false	
	emit_signal("interstitial_closed")
	get_tree().paused = false


func _on_AdMob_rewarded_ad_loaded():
	rewarded_loaded = true
	emit_signal("rewarded_ad_loaded")

func _on_AdMob_rewarded_ad_opened():
	emit_signal("rewarded_ad_opened")
	get_tree().paused = true

func _on_AdMob_rewarded_ad_closed():
	rewarded_loaded = false
	emit_signal("rewarded_ad_closed")
	get_tree().paused = false

func _on_AdMob_rewarded_ad_failed_to_load():
	emit_signal("rewarded_ad_failed_to_load")
	
func _on_AdMob_user_earned_rewarded(currency : String, amount : int):
	emit_signal("rewarded_user_earned_rewarded", currency, amount)

func _on_AdMob_rewarded_ad_failed_to_show(error_code : int):
	emit_signal("rewarded_ad_failed_to_show", error_code)


func _on_AdMob_unified_native_loaded():
	unified_native_enabled = true
	emit_signal("unified_native_loaded")

func _on_AdMob_unified_native_destroyed():
	emit_signal("unified_native_destroyed")

func _on_AdMob_unified_native_failed_to_load(error_code : int):
	emit_signal("unified_native_failed_to_load", error_code)

func _on_AdMob_unified_native_opened():
	emit_signal("unified_native_opened")
	
func _on_AdMob_unified_native_closed():
	unified_native_enabled = false
	emit_signal("unified_native_closed")


func _on_AdMob_consent_form_dismissed():
	emit_signal("consent_form_dismissed")

func _on_AdMob_consent_status_changed(consent_status_message : String):
	emit_signal("consent_status_changed", consent_status_message)

func _on_AdMob_consent_form_load_failure(error_code : int, error_message : String):
	emit_signal("consent_form_load_failure", error_code, error_message)

func _on_AdMob_consent_info_update_success(consent_status_message : String):
	emit_signal("consent_info_update_success", consent_status_message)

func _on_AdMob_consent_info_update_failure(error_code : int, error_message : String):
	emit_signal("consent_info_update_failure", error_code, error_message)


func _on_AdMob_initialization_complete(status : int, adapter_name : String):
	if status == INITIALIZATION_STATUS.READY and adapter_name == "GADMobileAds": 
		is_initialized = true
	else: 
		is_initialized = false

	emit_signal("initialization_complete", status, adapter_name)
