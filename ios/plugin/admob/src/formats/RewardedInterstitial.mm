//
//  RewardedInterstitial.mm
//  RewardedInterstitial
//
//  Created by Gustavo Maciel on 25/06/21.
//

#import "RewardedInterstitial.h"

@implementation RewardedInterstitial

- (instancetype)init:(int) instance_id{
    if ((self = [super init])) {
        initialized = true;
        instanceId = instance_id;
        rootController = (ViewController *)((AppDelegate *)[[UIApplication sharedApplication] delegate]).window.rootViewController;
    }
    return self;
}


- (void) load_rewarded_interstitial:(NSString*) ad_unit_id {
    NSLog(@"Calling load_rewarded_interstitial");
    
    if (!initialized) {
        return;
    }
    else{
        NSLog(@"rewarded interstitial will load with the id");
        NSLog(@"%@", ad_unit_id);
    }
        
    GADRequest *request = [GADRequest request];
    [GADRewardedInterstitialAd
         loadWithAdUnitID:ad_unit_id
                  request:request
        completionHandler:^(GADRewardedInterstitialAd *ad, NSError *error) {
          if (error) {
            NSLog(@"Rewarded interstitial ad failed to load with error: %@", [error localizedDescription]);
            NSLog(@"error while creating rewarded interstitial");
            Object *obj = ObjectDB::get_instance(self->instanceId);
            obj->call_deferred("_on_AdMob_rewarded_interstitial_ad_failed_to_show", (int) error.code);

            return;
          }
          else{
              NSLog(@"reward interstitial successfully loaded");
              Object *obj = ObjectDB::get_instance(self->instanceId);
              obj->call_deferred("_on_AdMob_rewarded_interstitial_ad_loaded");

          }
        self->rewardedInterstitialAd = ad;
        self->rewardedInterstitialAd.fullScreenContentDelegate = self;
        }
     ];
    
}

- (void) show_rewarded_interstitial {
    if (!initialized) {
        return;
    }
    
    if (rewardedInterstitialAd) {
        [rewardedInterstitialAd presentFromRootViewController:rootController userDidEarnRewardHandler:^{
            GADAdReward *rewardAd = self->rewardedInterstitialAd.adReward;
            NSLog(@"rewardedAd:userDidEarnReward:");
            NSString *rewardMessage = [NSString stringWithFormat:@"Reward received with currency %@ , amount %lf",
                                       rewardAd.type, [rewardAd.amount doubleValue]];
            NSLog(@"%@", rewardMessage);
            Object *obj = ObjectDB::get_instance(self->instanceId);
            obj->call_deferred("_on_AdMob_user_earned_rewarded", [rewardAd.type UTF8String], rewardAd.amount.doubleValue);

          }];

        Object *obj = ObjectDB::get_instance(instanceId);
        obj->call_deferred("_on_AdMob_rewarded_interstitial_ad_opened");
        OSIPhone::get_singleton()->on_focus_out();
    } else {
        NSLog(@"reward interstitial ad wasn't ready");
    }
}


/// Tells the delegate that the ad failed to present full screen content.
- (void)ad:(nonnull id<GADFullScreenPresentingAd>)ad didFailToPresentFullScreenContentWithError:(nonnull NSError *)error {
    NSLog(@"rewardedAd:didFailToPresentWithError");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_rewarded_interstitial_ad_failed_to_show", (int) error.code);
}


/// Tells the delegate that the ad dismissed full screen content.
- (void)adDidDismissFullScreenContent:(nonnull id<GADFullScreenPresentingAd>)ad {
   NSLog(@"Ad did dismiss full screen content.");
   Object *obj = ObjectDB::get_instance(instanceId);
   obj->call_deferred("_on_AdMob_rewarded_interstitial_ad_closed");
   OSIPhone::get_singleton()->on_focus_in();
}



@end
