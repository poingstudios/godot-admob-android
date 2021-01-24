//
//  Interstitial.mm
//  Interstitial
//
//  Created by Gustavo Maciel on 24/01/21.
//

#import "Interstitial.h"

@implementation Interstitial

- (void)dealloc {
    interstitial.delegate = nil;
}

- (instancetype)init: (int)instance_id{
    if ((self = [super init])) {
        initialized = true;
        instanceId = instance_id;
        rootController = (ViewController *)((AppDelegate *)[[UIApplication sharedApplication] delegate]).window.rootViewController;
    }
    return self;
}


- (void) load_interstitial:(NSString*)ad_unit_id {
    NSLog(@"Calling load_interstitial");
    
    if (!initialized) {
        return;
    }
    else{
        NSLog(@"interstitial will load with the id");
        NSLog(@"%@", ad_unit_id);
    }
    
    interstitial = [[GADInterstitial alloc] initWithAdUnitID:ad_unit_id];
    NSLog(@"interstitial created with the id");
    NSLog(@"%@", ad_unit_id);
    
    interstitial.delegate = self;
    
    GADRequest *request = [GADRequest request];
    [interstitial loadRequest:request];
    
}

- (void) show_interstitial {
    if (!initialized) {
        return;
    }
    
    if (interstitial.isReady) {
        [interstitial presentFromRootViewController:rootController];
    } else {
        NSLog(@"Interstitial ad wasn't ready");
    }
}


/// Tells the delegate an ad request succeeded.
- (void)interstitialDidReceiveAd:(GADInterstitial *)ad {
    NSLog(@"interstitialDidReceiveAd");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_interstitial_loaded");
}

/// Tells the delegate an ad request failed.
- (void)interstitial:(GADInterstitial *)ad
didFailToReceiveAdWithError:(GADRequestError *)error {
    NSLog(@"interstitial:didFailToReceiveAdWithError: %@", [error localizedDescription]);
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_interstitial_failed_to_load", (int) error.code);    
}

/// Tells the delegate that an interstitial will be presented.
- (void)interstitialWillPresentScreen:(GADInterstitial *)ad {
    NSLog(@"interstitialWillPresentScreen");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_interstitial_opened");
}

/// Tells the delegate the interstitial is to be animated off the screen.
- (void)interstitialWillDismissScreen:(GADInterstitial *)ad {
    NSLog(@"interstitialWillDismissScreen");
}

/// Tells the delegate the interstitial had been animated off the screen.
- (void)interstitialDidDismissScreen:(GADInterstitial *)ad {
    NSLog(@"interstitialDidDismissScreen");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_interstitial_closed");
}

/// Tells the delegate that a user click will open another app
/// (such as the App Store), backgrounding the current app.
- (void)interstitialWillLeaveApplication:(GADInterstitial *)ad {
    NSLog(@"interstitialWillLeaveApplication");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_interstitial_left_application");
}

@end
