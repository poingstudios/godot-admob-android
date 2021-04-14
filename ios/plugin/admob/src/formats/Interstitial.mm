//
//  Interstitial.mm
//  Interstitial
//
//  Created by Gustavo Maciel on 24/01/21.
//

#import "Interstitial.h"

@implementation Interstitial

- (void)dealloc {
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
    
    
    GADRequest *request = [GADRequest request];
    
    [GADInterstitialAd loadWithAdUnitID:ad_unit_id
                                    request:request
                          completionHandler:^(GADInterstitialAd *ad, NSError *error) {
      if (error) {
        NSLog(@"interstitial:didFailToReceiveAdWithError: %@", [error localizedDescription]);
        Object *obj = ObjectDB::get_instance(self->instanceId);
        obj->call_deferred("_on_AdMob_interstitial_failed_to_load", (int) error.code);

        return;
      }
      else{
          NSLog(@"interstitial created with the id");
          NSLog(@"interstitialDidReceiveAd");
          Object *obj = ObjectDB::get_instance(self->instanceId);
          obj->call_deferred("_on_AdMob_interstitial_loaded");
      }
      self->interstitial = ad;
      self->interstitial.fullScreenContentDelegate = self;

    }];
    
}

- (void) show_interstitial {
    if (!initialized) {
        return;
    }
    
    if (interstitial) {
        [interstitial presentFromRootViewController:rootController];
    } else {
        NSLog(@"Interstitial ad wasn't ready");
    }
}


/// Tells the delegate that the ad failed to present full screen content.
- (void)ad:(nonnull id<GADFullScreenPresentingAd>)ad didFailToPresentFullScreenContentWithError:(nonnull NSError *)error {
    NSLog(@"Ad did fail to present full screen content.");
    Object *obj = ObjectDB::get_instance(self->instanceId);
    obj->call_deferred("_on_AdMob_interstitial_failed_to_load", (int) error.code);

}

/// Tells the delegate that the ad presented full screen content.
- (void)adDidPresentFullScreenContent:(nonnull id<GADFullScreenPresentingAd>)ad {
    NSLog(@"interstitialWillPresentScreen");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_interstitial_opened");
    OSIPhone::get_singleton()->on_focus_out();
}

/// Tells the delegate that the ad dismissed full screen content.
- (void)adDidDismissFullScreenContent:(nonnull id<GADFullScreenPresentingAd>)ad {
    NSLog(@"Ad did dismiss full screen content.");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_interstitial_closed");
    OSIPhone::get_singleton()->on_focus_in();
}


@end
