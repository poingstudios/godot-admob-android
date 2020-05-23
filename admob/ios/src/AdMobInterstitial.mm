#import "AdMobInterstitial.h"
#include "reference.h"

@implementation AdMobInterstitial

- (void)dealloc {
    interstitial.delegate = nil;
    [interstitial release];
    [super dealloc];
}

- (void)initialize:(BOOL)is_real: (int)instance_id {
    isReal = is_real;
    initialized = true;
    instanceId = instance_id;
    rootController = [AppDelegate getViewController];
}


- (void) load_interstitial:(NSString*)ad_unit_id {
    NSLog(@"Calling load_interstitial");
    
    if (!initialized) {
        return;
    }
    else{
        NSLog(@"interstitial will load with the id");
        NSLog(ad_unit_id);
    }
    
    if(!isReal) {
        interstitial = [[GADInterstitial alloc] initWithAdUnitID:@"ca-app-pub-3940256099942544/4411468910"];
        NSLog(@"interstitial with test id created");
    }
    else {
        interstitial = [[GADInterstitial alloc] initWithAdUnitID:ad_unit_id];
        NSLog(@"interstitial created with the id");
        NSLog(ad_unit_id);
    }
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
        NSLog(@"Ad should be visible now");
    } else {
        NSLog(@"Ad wasn't ready");
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
}

/// Tells the delegate that an interstitial will be presented.
- (void)interstitialWillPresentScreen:(GADInterstitial *)ad {
  NSLog(@"interstitialWillPresentScreen");
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
}

@end