#include "AdMob.h"
#include <CommonCrypto/CommonDigest.h>
#import "app_delegate.h"
#import <AppTrackingTransparency/AppTrackingTransparency.h>
#import <AdSupport/AdSupport.h>
#import <AdSupport/ASIdentifierManager.h>
#import <GoogleMobileAds/GoogleMobileAds.h>

AdMob *AdMob::instance = NULL; 

AdMob::AdMob() {
    initialized = false;
    bannerObj = NULL;
    interstitialObj = NULL;
    rewardedObj = NULL;
    
    ERR_FAIL_COND(instance != NULL);
    
    instance = this;
}

AdMob::~AdMob() {    
    if (instance == this) {
        instance = NULL;
    }
}

void AdMob::initialize(bool is_for_child_directed_treatment, bool is_personalized, const String &max_ad_content_rating, bool is_real, int instance_id) {
    if (instance != this || initialized) {
        return;
    }

    if (!is_real){
        #if TARGET_IPHONE_SIMULATOR
            GADMobileAds.sharedInstance.requestConfiguration.testDeviceIdentifiers = @[ kGADSimulatorID ];
            NSLog(@"on Testing Simulator: %@", kGADSimulatorID);
        #else
            NSUUID* adid = [[ASIdentifierManager sharedManager] advertisingIdentifier];
            const char *cStr = [adid.UUIDString UTF8String];
            unsigned char digest[16];
            CC_MD5( cStr, strlen(cStr), digest );

            NSMutableString *output = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];

            for(int i = 0; i < CC_MD5_DIGEST_LENGTH; i++)
            {
                [output appendFormat:@"%02x", digest[i]];
            }
            GADMobileAds.sharedInstance.requestConfiguration.testDeviceIdentifiers = @[output];
            NSLog(@"on Testing Real Device: testDeviceIdentifiers: %@", output);
        #endif
    }
    [GADMobileAds.sharedInstance.requestConfiguration tagForChildDirectedTreatment:is_for_child_directed_treatment];
    if (max_ad_content_rating == "G") {
        GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingGeneral;
        NSLog(@"maxAdContentRating = GADMaxAdContentRatingGeneral");
    }
    else if (max_ad_content_rating == "PG") {
        GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingParentalGuidance;
        NSLog(@"maxAdContentRating = GADMaxAdContentRatingParentalGuidance");
    }
    else if (max_ad_content_rating == "T") {
        GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingTeen;
        NSLog(@"maxAdContentRating = GADMaxAdContentRatingTeen");
    }
    else if (max_ad_content_rating == "MA") {
        GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingMatureAudience;
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
    bannerObj = [[Banner alloc] init :instance_id: is_personalized];
    interstitialObj = [[Interstitial alloc] init :instance_id : is_personalized];
    rewardedObj = [[Rewarded alloc] init :instance_id : is_personalized];
}


void AdMob::load_banner(const String &ad_unit_id, int position, const String &size) {
    if (!initialized) {
        NSLog(@"AdMob Module not initialized");
        return;
    }
    
    NSString *ad_unit_id_NSString = [NSString stringWithCString:ad_unit_id.utf8().get_data() encoding: NSUTF8StringEncoding];
    NSString *size_NSString       = [NSString stringWithCString:size.utf8().get_data() encoding: NSUTF8StringEncoding];
    [bannerObj load_banner: ad_unit_id_NSString : position: size_NSString];
    
}

void AdMob::destroy_banner() {
    if (!initialized) return;
    
    [bannerObj destroy_banner];
}

void AdMob::load_interstitial(const String &ad_unit_id) {
    if (!initialized) {
        NSLog(@"AdMob Module not initialized");
        return;
    }
    
    NSString *ad_unit_id_NSString = [NSString stringWithCString:ad_unit_id.utf8().get_data() encoding: NSUTF8StringEncoding];
    [interstitialObj load_interstitial: ad_unit_id_NSString];
    
}

void AdMob::show_interstitial() {
    if (!initialized) return;
    
    [interstitialObj show_interstitial];
}

void AdMob::load_rewarded(const String &ad_unit_id) {
    if (!initialized) {
        NSLog(@"AdMob Module not initialized");
        return;
    }
    
    NSString *ad_unit_id_NSString = [NSString stringWithCString:ad_unit_id.utf8().get_data() encoding: NSUTF8StringEncoding];
    [rewardedObj load_rewarded: ad_unit_id_NSString];
    
}

void AdMob::show_rewarded() {
    if (!initialized) return;
    
    [rewardedObj show_rewarded];
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
