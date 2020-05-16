#include "AdMob.h"
#import "app_delegate.h" //necessary to use NSLog

AdMob *AdMob::instance = NULL;

AdMob::AdMob() 
{
	ERR_FAIL_COND(instance != NULL);
	instance = this;
	initialized = false;
}

AdMob::~AdMob() 
{
	if (initialized)
	{
		[_banner release];
	}
	if (instance == this)
	{
		instance = NULL;
	}
}

void AdMob::init(bool is_for_child_directed_treatment, bool is_personalized, const String &max_ad_content_rating, int instance_id, const String &test_device_id) 
{
    if (initialized) return;

    initialized = true;
    _banner = [AdMobBanner alloc];

	[_banner initialize :test_device_id :instance_id];
}

void AdMob::load_banner(const String &ad_unit_id, int gravity, const String &size) {
    if (!initialized) return;
    
    NSString *ad_unit_id_NSString = [NSString stringWithCString:ad_unit_id.utf8().get_data() encoding: NSUTF8StringEncoding];
    NSString *size_NSString       = [NSString stringWithCString:size.utf8().get_data() encoding: NSUTF8StringEncoding];
    [_banner load_banner: ad_unit_id_NSString : gravity: size_NSString];

}

void AdMob::destroy_banner() {
    if (!initialized) return;
    
    [_banner destroy_banner];
}



void AdMob::_bind_methods() 
{
    ClassDB::bind_method("init", &AdMob::init);
    ClassDB::bind_method("load_banner", &AdMob::load_banner);
    ClassDB::bind_method("destroy_banner", &AdMob::destroy_banner);
}
