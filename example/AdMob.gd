extends Control

signal banner_loaded
signal banner_destroyed

var initialized := false

var ad_unit_id = {
	"banner" : {
		"iOS" : "",
		"Android" : "ca-app-pub-3940256099942544/6300978111"
	},
	"interstitial" : {
		"iOS" : "",
		"Android" : "ca-app-pub-3940256099942544/1033173712"
	},
	"rewarded" : {
		"iOS" : "",
		"Android" : "ca-app-pub-3940256099942544/5224354917"
	},
	"unified_native" : {
		"iOS" : "",
		"Android" : "ca-app-pub-3940256099942544/2247696110"
	}
}


const GRAVITY = {
	"TOP" : 48,
	"BOTTOM" : 80,
	"CENTER" : 17,
	"NO_GRAVITY" : 0
}
var test_device_id := {
	"REAL_DEVICE" : OS.get_unique_id().md5_text().to_upper(),
	"EMULATOR_DEVICE" : "B3EEABB8EE11C2BE770B684D95219ECB"
}

onready var SCALE = {
	"x" : OS.get_screen_size().x / get_viewport_rect().size.x,
	"y" : OS.get_screen_size().y / get_viewport_rect().size.y
}

var AdMob
func _ready():
	if (Engine.has_singleton("AdMob")):
		AdMob = Engine.get_singleton("AdMob")
		get_tree().connect("screen_resized", self, "_on_get_tree_resized")

func init(is_for_child_directed_treatment := true, is_personalized := false, max_ad_content_rating := "G", instance_id := get_instance_id(), test_device_id := ""):
	if AdMob and !initialized:
		#IF test_device_id == "", then will be running as a test device
		AdMob.init(is_for_child_directed_treatment, is_personalized, max_ad_content_rating, instance_id, test_device_id)
		print("init on the gdscript code!")
		initialized = !initialized

func load_banner(unit_id : String = ad_unit_id.banner.Android, gravity : int = GRAVITY.BOTTOM, size : String = "SMART_BANNER"):
	if AdMob:
		AdMob.load_banner(unit_id, gravity, size)

func destroy_banner():
	if AdMob:
		AdMob.destroy_banner()

func load_interstitial(unit_id: String):
	if AdMob:
		AdMob.load_interstitial(unit_id)

func load_rewarded(unit_id : String):
	if AdMob:
		AdMob.load_rewarded(unit_id)

func load_unified_native(unit_id : String, control_node_to_be_replaced : Control):
	if AdMob:
		var ad := {
			"size" : {
				"w" : control_node_to_be_replaced.rect_size.x * SCALE.x,
				"h" : control_node_to_be_replaced.rect_size.y * SCALE.y
			},
			"position" : {
				"x" : control_node_to_be_replaced.rect_position.x * SCALE.x,
				"y" : control_node_to_be_replaced.rect_position.y * SCALE.y
			}
		}
		AdMob.load_unified_native(unit_id, [ad.size.w, ad.size.h], [ad.position.x, ad.position.y])
		
	
func _on_get_tree_resized():
	if AdMob:
		SCALE = {
			"x" : OS.get_screen_size().x / get_viewport_rect().size.x,
			"y" : OS.get_screen_size().y / get_viewport_rect().size.y
		}

func _on_AdMob_banner_loaded():
	emit_signal("banner_loaded")
func _on_AdMob_banner_destroyed():
	emit_signal("banner_destroyed")

func _on_AdMob_banner_failed_to_load(error_code : int):
	print("_on_AdMob_banner_failed_to_load" + " " + str(error_code))
	
func _on_AdMob_banner_opened():
	print("_on_AdMob_banner_opened")
	
func _on_AdMob_banner_clicked_opened():
	print("_on_AdMob_banner_clicked_opened")
	
func _on_AdMob_banner_left_application():
	print("_on_AdMob_banner_left_application")
	
func _on_AdMob_banner_closed():
	print("_on_AdMob_banner_closed")

	
	
func _on_AdMob_interstitial_loaded():
	print("_on_AdMob_interstitial_loaded")

func _on_AdMob_interstitial_failed_to_load(error_code : int):
	print("_on_AdMob_interstitial_failed_to_load" + " " + str(error_code))

func _on_AdMob_interstitial_opened():
	print("_on_AdMob_interstitial_opened")
	
func _on_AdMob_interstitial_clicked():
	print("_on_AdMob_interstitial_clicked")
	
func _on_AdMob_interstitial_left_application():
	print("_on_AdMob_interstitial_left_application")

func _on_AdMob_interstitial_closed():
	print("_on_AdMob_interstitial_closed")
	AdMob.load_interstitial(ad_unit_id.interstitial.Android)

func _on_AdMob_rewarded_ad_loaded():
	print("_on_AdMob_rewarded_ad_loaded")

func _on_AdMob_rewarded_ad_failed_to_load():
	print("_on_rewarded_ad_failed_to_load")
	
func _on_AdMob_rewarded_ad_opened():
	print("_on_AdMob_rewarded_ad_opened")
	
func _on_AdMob_rewarded_ad_closed():
	print("_on_AdMob_rewarded_ad_closed")
	AdMob.load_rewarded(ad_unit_id.rewarded.Android)

	
func _on_AdMob_user_earned_rewarded(currency : String, amount : int):
	print("_on_AdMob_user_earned_rewarded" + "currency: " + currency + ", amount: " + str(amount));

func _on_AdMob_rewarded_ad_failed_to_show(error_code : int):
	print("_on_AdMob_rewarded_ad_failed_to_show" + " " + str(error_code))


func _on_AdMob_unified_native_ad_loaded():
	print("_on_AdMob_unified_native_ad_loaded")

func _on_AdMob_unified_native_destroyed():
	print("_on_AdMob_unified_native_destroyed")


func _on_AdMob_unified_native_ad_failed_to_load(error_code : int):
	print("_on_AdMob_unified_native_ad_failed_to_load" + " " + str(error_code))

func _on_AdMob_unified_native_opened():
	print("_on_AdMob_unified_native_opened")

func _on_AdMob_unified_native_clicked():
	print("_on_AdMob_unified_native_clicked")

func _on_AdMob_unified_native_left_application():
	print("_on_AdMob_unified_native_left_application")
	
func _on_AdMob_unified_native_closed():
	print("_on_AdMob_unified_native_closed")
