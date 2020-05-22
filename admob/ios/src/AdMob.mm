#include "AdMob.h"
#import "app_delegate.h"

AdMob *AdMob::instance = NULL; 

AdMob::AdMob() {
    initialized = false;
    banner = NULL; 

    ERR_FAIL_COND(instance != NULL); 

    instance = this;
}

AdMob::~AdMob() {
   if (initialized) {
       [banner release]; //free banner
   }

   if (instance == this) {
       instance = NULL;
   }
}

void AdMob::init(bool is_for_child_directed_treatment, bool is_personalized, const String &max_ad_content_rating, int instance_id, const String &test_device_id) {
    if (instance != this || initialized) {
        return;
    }
    
    initialized = true;
    
    banner = [[AdMobBanner alloc] init];
    [banner initialize :false :instance_id];
    
}


void AdMob::load_banner(const String &ad_unit_id, int gravity, const String &size) {
    if (!initialized) {
        NSLog(@"AdMob Module not initialized");
        return;
    }
    
    NSString *ad_unit_id_NSString = [NSString stringWithCString:ad_unit_id.utf8().get_data() encoding: NSUTF8StringEncoding];
    NSString *size_NSString       = [NSString stringWithCString:size.utf8().get_data() encoding: NSUTF8StringEncoding];
    [banner load_banner: ad_unit_id_NSString : gravity: size_NSString];

}

void AdMob::destroy_banner() {
    if (!initialized) return;
    
    [banner destroy_banner];
}



void AdMob::_bind_methods() {
    ClassDB::bind_method("init", &AdMob::init);
    ClassDB::bind_method("load_banner", &AdMob::load_banner);
    ClassDB::bind_method("destroy_banner", &AdMob::destroy_banner);
}
