extends Control

signal initialized

signal banner_loaded
signal banner_destroyed
signal banner_failed_to_load(error_code)
signal banner_opened
signal banner_clicked
signal banner_left_application
signal banner_closed

signal interstitial_loaded
signal interstitial_failed_to_load(error_code)
signal interstitial_opened
signal interstitial_clicked
signal interstitial_left_application
signal interstitial_closed

signal rewarded_ad_loaded
signal rewarded_ad_failed_to_load
signal rewarded_ad_opened
signal rewarded_ad_closed
signal rewarded_user_earned_rewarded(currency, amount)
signal rewarded_ad_failed_to_show(error_code)

signal unified_native_loaded
signal unified_native_destroyed
signal unified_native_failed_to_load(error_code)
signal unified_native_opened
signal unified_native_closed

#private attributes
var _is_initialized : bool = false
var _admob_singleton : Object
enum _gravity_options {TOP, BOTTOM, CENTER, NO_GRAVITY}
var _banner_gravity : Dictionary = {
	"iOS": {
		_gravity_options.TOP : 0,
		_gravity_options.BOTTOM : 0,
		_gravity_options.CENTER : 0,
		_gravity_options.NO_GRAVITY : 0
	},
	"Android": {
		_gravity_options.TOP : 48,
		_gravity_options.BOTTOM : 80,
		_gravity_options.CENTER : 17,
		_gravity_options.NO_GRAVITY : 0
	}
}
onready var _unified_native_scale : Dictionary = {
	"x" : OS.get_screen_size().x / get_viewport_rect().size.x,
	"y" : OS.get_screen_size().y / get_viewport_rect().size.y,
}


#public attributes
export var admob_enabled := true
export var is_real := false
export(String, "BANNER", "MEDIUM_RECTANGLE", "FULL_BANNER", "LEADERBOARD", "SMART_BANNER") var banner_size : String = "BANNER"
export(_gravity_options) var banner_gravity_option = _gravity_options.BOTTOM
export var is_for_child_directed_treatment := true
export(String, "G", "PG", "T", "MA") var max_ad_content_rating = "G"
export var is_personalized := false
export (Dictionary) var unit_ids : Dictionary = {
	"banner": {
		"iOS" : "ca-app-pub-3940256099942544/2934735716",
		"Android" : "ca-app-pub-3940256099942544/6300978111",
	},
	"interstitial" : {
		"iOS" : "ca-app-pub-3940256099942544/4411468910",
		"Android" : "ca-app-pub-3940256099942544/1033173712",
	},
	"rewarded" : {
		"iOS" : "ca-app-pub-3940256099942544/1712485313",
		"Android" : "ca-app-pub-3940256099942544/5224354917",
	},
	"unified_native" : {
		"iOS" : "",
		"Android" : "ca-app-pub-3940256099942544/2247696110",
	}
}

func _ready():
	if admob_enabled:
		if (Engine.has_singleton("AdMob")):
			_admob_singleton = Engine.get_singleton("AdMob")
			_initialize()
			# warning-ignore:return_value_discarded
			get_tree().connect("screen_resized", self, "_on_get_tree_resized")

func _initialize():
	if _admob_singleton and !_is_initialized:
		_admob_singleton.initialize(is_for_child_directed_treatment, is_personalized, max_ad_content_rating, is_real, get_instance_id())
		_is_initialized = false
		load_interstitial()
		load_rewarded()
		emit_signal("initialized")

func load_banner():
	if _admob_singleton:
		_admob_singleton.load_banner(unit_ids.banner[OS.get_name()], _banner_gravity[OS.get_name()][banner_gravity_option], banner_size)

func load_interstitial():
	if _admob_singleton:
		_admob_singleton.load_interstitial(unit_ids.interstitial[OS.get_name()])

func load_rewarded():
	if _admob_singleton:
		_admob_singleton.load_rewarded(unit_ids.rewarded[OS.get_name()])

func load_unified_native(control_node_to_be_replaced : Control):
	if _admob_singleton:
		var params := {
			"size" : {
				"w" : control_node_to_be_replaced.rect_size.x * _unified_native_scale.x,
				"h" : control_node_to_be_replaced.rect_size.y * _unified_native_scale.y
			},
			"position" : {
				"x" : control_node_to_be_replaced.rect_position.x * _unified_native_scale.x,
				"y" : control_node_to_be_replaced.rect_position.y * _unified_native_scale.y
			}
		}
		_admob_singleton.load_unified_native(unit_ids.unified_native[OS.get_name()], [params.size.w, params.size.h], [params.position.x, params.position.y])

func destroy_banner():
	if _admob_singleton:
		_admob_singleton.destroy_banner()

func destroy_unified_native():
	if _admob_singleton:
		_admob_singleton.destroy_unified_native()

func show_interstitial():
	if _admob_singleton:
		_admob_singleton.show_interstitial()

func show_rewarded():
	if _admob_singleton:
		_admob_singleton.show_rewarded()

func _on_get_tree_resized():
	if _admob_singleton:
		unit_ids.unified_native.scale = {
			"x" : OS.get_screen_size().x / get_viewport_rect().size.x,
			"y" : OS.get_screen_size().y / get_viewport_rect().size.y
		}


#SIGNALS
func _on_AdMob_banner_loaded():
	emit_signal("banner_loaded")
	
func _on_AdMob_banner_destroyed():
	emit_signal("banner_destroyed")

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
	load_interstitial()
	emit_signal("interstitial_closed")
	get_tree().paused = false

func _on_AdMob_rewarded_ad_opened():
	emit_signal("rewarded_ad_opened")
	get_tree().paused = true

func _on_AdMob_rewarded_ad_closed():
	load_rewarded()
	emit_signal("rewarded_ad_closed")
	get_tree().paused = false

func _on_AdMob_rewarded_ad_loaded():
	emit_signal("rewarded_ad_loaded")

func _on_AdMob_rewarded_ad_failed_to_load():
	emit_signal("rewarded_ad_failed_to_load")
	
func _on_AdMob_user_earned_rewarded(currency : String, amount : int):
	emit_signal("rewarded_user_earned_rewarded", currency, amount)

func _on_AdMob_rewarded_ad_failed_to_show(error_code : int):
	emit_signal("rewarded_ad_failed_to_show", error_code)

func _on_AdMob_unified_native_loaded():
	emit_signal("unified_native_loaded")

func _on_AdMob_unified_native_destroyed():
	emit_signal("unified_native_destroyed")

func _on_AdMob_unified_native_failed_to_load(error_code : int):
	emit_signal("unified_native_failed_to_load", error_code)

func _on_AdMob_unified_native_opened():
	emit_signal("unified_native_opened")
	
func _on_AdMob_unified_native_closed():
	emit_signal("unified_native_closed")
