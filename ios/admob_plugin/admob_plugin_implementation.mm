//
//  admob_plugin_implementation.m
//  admob_plugin
//
//  Created by Gustavo Maciel on 16/01/21.
//


#import <Foundation/Foundation.h>
#import <AppTrackingTransparency/AppTrackingTransparency.h>
#import <AdSupport/AdSupport.h>
#import <AdSupport/ASIdentifierManager.h>
#import <GoogleMobileAds/GoogleMobileAds.h>
#include <CommonCrypto/CommonDigest.h>
#include <UserMessagingPlatform/UserMessagingPlatform.h>

#include "core/project_settings.h"
#include "core/class_db.h"


#import "admob_plugin_implementation.h"
   
AdMob *AdMob::instance = NULL;

AdMob::AdMob() {
    initialized = false;
    bannerObj = NULL;
    interstitialObj = NULL;
    rewardedObj = NULL;
    objectDB = NULL;
    self_max_ad_content_rating = "";

    ERR_FAIL_COND(instance != NULL);
    
    instance = this;
    NSLog(@"initialize admob");
}

AdMob::~AdMob() {
    if (instance == this) {
        instance = NULL;
    }
    NSLog(@"deinitialize admob");
}

void AdMob::loadConsentForm(bool is_for_child_directed_treatment, bool is_real, int instance_id)
{
    [UMPConsentForm
        loadWithCompletionHandler:^(UMPConsentForm *form, NSError *loadError)
        {
            if (loadError)
            {
                objectDB->call_deferred("_on_AdMob_consent_form_load_failure", (int) loadError.code, loadError.domain);
                initializeAfterUMP(is_for_child_directed_treatment, is_real, instance_id);
            }
            else
            {
                String consentStatusMsg = "";

                if (UMPConsentInformation.sharedInstance.consentStatus == UMPConsentStatusRequired)
                {
                    [form
                        presentFromViewController:(ViewController *)((AppDelegate *)[[UIApplication sharedApplication] delegate]).window.rootViewController
                        completionHandler:^(NSError *_Nullable dismissError)
                        {
                            objectDB->call_deferred("_on_AdMob_consent_form_dismissed");
                            if (UMPConsentInformation.sharedInstance.consentStatus == UMPConsentStatusObtained)
                            {
                                objectDB->call_deferred("_on_AdMob_consent_status_changed", "User consent obtained. Personalization not defined.");
                            }
                            initializeAfterUMP(is_for_child_directed_treatment, is_real, instance_id);
                        }
                     ];
                    consentStatusMsg = "User consent required but not yet obtained.";
                }

                if (UMPConsentInformation.sharedInstance.consentStatus == UMPConsentStatusUnknown)
                {
                    consentStatusMsg = "Unknown consent status.";
                    initializeAfterUMP(is_for_child_directed_treatment, is_real, instance_id);
                }
                else if (UMPConsentInformation.sharedInstance.consentStatus == UMPConsentStatusNotRequired)
                {
                    consentStatusMsg = "User consent not required. For example, the user is not in the EEA or the UK.";
                    initializeAfterUMP(is_for_child_directed_treatment, is_real, instance_id);
                }
                else if (UMPConsentInformation.sharedInstance.consentStatus == UMPConsentStatusObtained)
                {
                    consentStatusMsg = "User consent obtained. Personalization not defined.";
                    initializeAfterUMP(is_for_child_directed_treatment, is_real, instance_id);
                }
                objectDB->call_deferred("_on_AdMob_consent_status_changed", consentStatusMsg);
            }
         }
    ];
}
void AdMob::initialize(bool is_for_child_directed_treatment, const String &max_ad_content_rating, bool is_real, bool is_test_europe_user_consent, int instance_id)
{
    if (instance != this || initialized)
    {
        return;
    }
    objectDB = ObjectDB::get_instance(instance_id);
    self_max_ad_content_rating = max_ad_content_rating;
    UMPRequestParameters *parameters = [[UMPRequestParameters alloc] init];
    parameters.tagForUnderAgeOfConsent = is_for_child_directed_treatment;
    
    if (is_test_europe_user_consent)
    {
        NSLog(@"Testing the UMP");
        NSLog(@"UUID: %@", [[[UIDevice currentDevice] identifierForVendor] UUIDString] );
        NSLog(@"device ID: %@", [NSString stringWithCString: getDeviceId() encoding: NSUTF8StringEncoding] );
        [UMPConsentInformation.sharedInstance reset];
        UMPDebugSettings *debugSettings = [[UMPDebugSettings alloc] init];
        debugSettings.testDeviceIdentifiers = @[ [[[UIDevice currentDevice] identifierForVendor] UUIDString] ];
        debugSettings.geography = UMPDebugGeographyEEA;
        parameters.debugSettings = debugSettings;
    }

    // Request an update to the consent information.
    [UMPConsentInformation.sharedInstance requestConsentInfoUpdateWithParameters: parameters
        completionHandler:^(NSError *_Nullable error)
        {
            if (error)
            {
                objectDB->call_deferred("_on_AdMob_consent_info_update_failure", (int) error.code, error.domain);
                initializeAfterUMP(is_for_child_directed_treatment, is_real, instance_id);
            }
            else
            {
                UMPFormStatus formStatus = UMPConsentInformation.sharedInstance.formStatus;
                if (formStatus == UMPFormStatusAvailable)
                {
                    objectDB->call_deferred("_on_AdMob_consent_info_update_success", "Consent Form Available");
                    loadConsentForm(is_for_child_directed_treatment, is_real, instance_id);
                }
                else
                {
                    objectDB->call_deferred("_on_AdMob_consent_info_update_success", "Consent Form not Available");
                    initializeAfterUMP(is_for_child_directed_treatment, is_real, instance_id);
                }
            }
        }
    ];
}

void AdMob::initializeAfterUMP(bool is_for_child_directed_treatment, bool is_real, int instance_id)
{
    if (!initialized){
        if (!is_real){
            #if TARGET_IPHONE_SIMULATOR
                GADMobileAds.sharedInstance.requestConfiguration.testDeviceIdentifiers = @[ kGADSimulatorID ];
                NSLog(@"on Testing Simulator: %@", kGADSimulatorID);
            #else
                GADMobileAds.sharedInstance.requestConfiguration.testDeviceIdentifiers = @ [ [NSString stringWithCString: getDeviceId() encoding: NSUTF8StringEncoding] ];
                NSLog(@"on Testing Real Device: testDeviceIdentifiers: %@", [NSString stringWithCString: getDeviceId() encoding: NSUTF8StringEncoding]);
            #endif
        }
        
        [GADMobileAds.sharedInstance.requestConfiguration tagForChildDirectedTreatment:is_for_child_directed_treatment];

        if (self_max_ad_content_rating == "G") {
            GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingGeneral;
            NSLog(@"maxAdContentRating = GADMaxAdContentRatingGeneral");
        }
        else if (self_max_ad_content_rating == "PG") {
            GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingParentalGuidance;
            NSLog(@"maxAdContentRating = GADMaxAdContentRatingParentalGuidance");
        }
        else if (self_max_ad_content_rating == "T") {
            GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingTeen;
            NSLog(@"maxAdContentRating = GADMaxAdContentRatingTeen");
        }
        else if (self_max_ad_content_rating == "MA") {
            GADMobileAds.sharedInstance.requestConfiguration.maxAdContentRating = GADMaxAdContentRatingMatureAudience;
            NSLog(@"maxAdContentRating = GADMaxAdContentRatingMatureAudience");
        }

        if (@available(iOS 14, *)) {
            [ATTrackingManager requestTrackingAuthorizationWithCompletionHandler:^(ATTrackingManagerAuthorizationStatus status) {
                GADInitialize();
            }];
        }
        else{
            GADInitialize();
        }

        initialized = true;
        bannerObj = [[Banner alloc] init :instance_id];
        interstitialObj = [[Interstitial alloc] init :instance_id];
        rewardedObj = [[Rewarded alloc] init :instance_id];
    }
}

void AdMob::GADInitialize(){
    [[GADMobileAds sharedInstance] startWithCompletionHandler:^(GADInitializationStatus *_Nonnull status)
    {
        NSDictionary<NSString *, GADAdapterStatus *>* states = [status adapterStatusesByClassName];
        GADAdapterStatus * adapterStatus = states[@"GADMobileAds"];
        NSLog(@"%s : %ld", "GADMobileAds", adapterStatus.state);
        
        objectDB->call_deferred("_on_AdMob_initialization_complete", (int) adapterStatus.state, "GADMobileAds");
    }];
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



const char * AdMob::getDeviceId()
{
    NSUUID* adid = [[ASIdentifierManager sharedManager] advertisingIdentifier];
    const char *cStr = [adid.UUIDString UTF8String];
    unsigned char digest[16];
    CC_MD5( cStr, strlen(cStr), digest );

    NSMutableString *output = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];

    for(int i = 0; i < CC_MD5_DIGEST_LENGTH; i++)
    {
        [output appendFormat:@"%02x", digest[i]];
    }

    return [output UTF8String];
}
