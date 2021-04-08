//
//  Rewarded.mm
//  Rewarded
//
//  Created by Gustavo Maciel on 24/01/21.
//

#import "Rewarded.h"

@implementation Rewarded

- (instancetype)init:(int) instance_id{
    if ((self = [super init])) {
        initialized = true;
        instanceId = instance_id;
        rootController = (ViewController *)((AppDelegate *)[[UIApplication sharedApplication] delegate]).window.rootViewController;
    }
    return self;
}


- (void) load_rewarded:(NSString*) ad_unit_id {
    NSLog(@"Calling load_rewarded");
    
    if (!initialized) {
        return;
    }
    else{
        NSLog(@"rewarded will load with the id");
        NSLog(@"%@", ad_unit_id);
    }
        
    GADRequest *request = [GADRequest request];
    [GADRewardedAd
         loadWithAdUnitID:ad_unit_id
                  request:request
        completionHandler:^(GADRewardedAd *ad, NSError *error) {
          if (error) {
            NSLog(@"Rewarded ad failed to load with error: %@", [error localizedDescription]);
            NSLog(@"error while creating reward");
            Object *obj = ObjectDB::get_instance(self->instanceId);
            obj->call_deferred("_on_AdMob_rewarded_ad_failed_to_show", (int) error.code);

            return;
          }
          else{
              NSLog(@"reward successfully loaded");
              Object *obj = ObjectDB::get_instance(self->instanceId);
              obj->call_deferred("_on_AdMob_rewarded_ad_loaded");

          }
        self->rewarded = ad;
        self->rewarded.fullScreenContentDelegate = self;
        }
     ];
    
}

- (void) show_rewarded {
    if (!initialized) {
        return;
    }
    
    if (rewarded) {
        [rewarded presentFromRootViewController:rootController userDidEarnRewardHandler:^{
            GADAdReward *rewardAd = self->rewarded.adReward;
            NSLog(@"rewardedAd:userDidEarnReward:");
            NSString *rewardMessage = [NSString stringWithFormat:@"Reward received with currency %@ , amount %lf",
                                       rewardAd.type, [rewardAd.amount doubleValue]];
            NSLog(@"%@", rewardMessage);
            Object *obj = ObjectDB::get_instance(self->instanceId);
            obj->call_deferred("_on_AdMob_user_earned_rewarded", [rewardAd.type UTF8String], rewardAd.amount.doubleValue);

          }];

        Object *obj = ObjectDB::get_instance(instanceId);
        obj->call_deferred("_on_AdMob_rewarded_ad_opened");
    } else {
        NSLog(@"reward ad wasn't ready");
    }
}


/// Tells the delegate that the ad failed to present full screen content.
- (void)ad:(nonnull id<GADFullScreenPresentingAd>)ad didFailToPresentFullScreenContentWithError:(nonnull NSError *)error {
    NSLog(@"rewardedAd:didFailToPresentWithError");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_rewarded_ad_failed_to_show", (int) error.code);

}


/// Tells the delegate that the ad dismissed full screen content.
- (void)adDidDismissFullScreenContent:(nonnull id<GADFullScreenPresentingAd>)ad {
   NSLog(@"Ad did dismiss full screen content.");
   Object *obj = ObjectDB::get_instance(instanceId);
   obj->call_deferred("_on_AdMob_rewarded_ad_closed");
}



@end
