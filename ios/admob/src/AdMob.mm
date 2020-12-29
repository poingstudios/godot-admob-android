#include "AdMob.h"
#import "app_delegate.h"
#import <AppTrackingTransparency/AppTrackingTransparency.h>
#import <AdSupport/AdSupport.h>
#import <GoogleMobileAds/GoogleMobileAds.h>

AdMob *AdMob::instance = NULL; 

AdMob::AdMob() {
    initialized = false;
    banner = NULL;
    interstitial = NULL;
    rewarded = NULL;
    
    ERR_FAIL_COND(instance != NULL);
    
    instance = this;
}

AdMob::~AdMob() {
    if (initialized) {
        [banner release]; //free banner
        [interstitial release]; //free interstitial
        [rewarded release]; //free rewarded
    }
    
    if (instance == this) {
        instance = NULL;
    }
}

void AdMob::initialize(bool is_for_child_directed_treatment, bool is_personalized, const String &max_ad_content_rating, const String &test_device_id, int instance_id) {
    if (instance != this || initialized) {
        return;
    }
    [GADMobileAds.sharedInstance.requestConfiguration tagForChildDirectedTreatment:is_for_child_directed_treatment];
    if ([max_ad_content_rating isEqualToString:@"G"]) {
        [GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingGeneral];
        NSLog(@"maxAdContentRating = GADMaxAdContentRatingGeneral");
    }
    else if ([max_ad_content_rating isEqualToString:@"PG"]) {
        [GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingParentalGuidance];
        NSLog(@"maxAdContentRating = GADMaxAdContentRatingParentalGuidance");
    }
    else if ([max_ad_content_rating isEqualToString:@"T"]) {
        [GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingTeen];
        NSLog(@"maxAdContentRating = GADMaxAdContentRatingTeen");
    }
    else if ([max_ad_content_rating isEqualToString:@"MA"]) {
        [GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingMatureAudience];
        NSLog(@"maxAdContentRating = GADMaxAdContentRatingMatureAudience");
    }

    if (@available(iOS 14, *)) {
        [ATTrackingManager requestTrackingAuthorizationWithCompletionHandler:^(ATTrackingManagerAuthorizationStatus status) {
            [[GADMobileAds sharedInstance] startWithCompletionHandler:nil];
            NSLog(@"AdMob initialized with Request App Tracking Transparency");
        }];
    }
    else{
        [[GADMobileAds sharedInstance] startWithCompletionHandler:nil];
        NSLog(@"AdMob initialized without Request App Tracking Transparency");
    }

    initialized = true;
    banner = [[AdMobBanner alloc] initialize :instance_id: is_personalized];
    interstitial = [[AdMobInterstitial alloc] initialize :instance_id : is_personalized];
    rewarded = [[AdMobRewarded alloc] initialize :instance_id : is_personalized];
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

void AdMob::load_interstitial(const String &ad_unit_id) {
    if (!initialized) {
        NSLog(@"AdMob Module not initialized");
        return;
    }
    
    NSString *ad_unit_id_NSString = [NSString stringWithCString:ad_unit_id.utf8().get_data() encoding: NSUTF8StringEncoding];
    [interstitial load_interstitial: ad_unit_id_NSString];
    
}

void AdMob::show_interstitial() {
    if (!initialized) return;
    
    [interstitial show_interstitial];
}

void AdMob::load_rewarded(const String &ad_unit_id) {
    if (!initialized) {
        NSLog(@"AdMob Module not initialized");
        return;
    }
    
    NSString *ad_unit_id_NSString = [NSString stringWithCString:ad_unit_id.utf8().get_data() encoding: NSUTF8StringEncoding];
    [rewarded load_rewarded: ad_unit_id_NSString];
    
}

void AdMob::show_rewarded() {
    if (!initialized) return;
    
    [rewarded show_rewarded];
}

void AdMob::_bind_methods() {
    ClassDB::bind_method("initialize", &AdMob::initialize);
    ClassDB::bind_method("load_banner", &AdMob::load_banner);
    ClassDB::bind_method("destroy_banner", &AdMob::destroy_banner);
    ClassDB::bind_method("load_interstitial", &AdMob::load_interstitial);
    ClassDB::bind_method("show_interstitial", &AdMob::show_interstitial);
    ClassDB::bind_method("load_rewarded", &AdMob::load_rewarded);
    ClassDB::bind_method("show_rewarded", &AdMob::show_rewarded);
}
