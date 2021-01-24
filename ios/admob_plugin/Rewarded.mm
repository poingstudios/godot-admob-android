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
    
    rewarded = [[GADRewardedAd alloc] initWithAdUnitID:ad_unit_id];
    NSLog(@"rewarded created with the id");
    NSLog(@"%@", ad_unit_id);
    
    GADRequest *request = [GADRequest request];
    
    [rewarded loadRequest:request completionHandler:^(GADRequestError * _Nullable error) {
        if (error) {
            NSLog(@"error while creating reward");
            Object *obj = ObjectDB::get_instance(self->instanceId);
            obj->call_deferred("_on_AdMob_rewarded_ad_failed_to_show", (int) error.code);
        } else {
            NSLog(@"reward successfully loaded");
            Object *obj = ObjectDB::get_instance(self->instanceId);
            obj->call_deferred("_on_AdMob_rewarded_ad_loaded");
        }
    }];
    
}

- (void) show_rewarded {
    if (!initialized) {
        return;
    }
    
    if (rewarded.isReady) {
        [rewarded presentFromRootViewController:rootController delegate:self];
        Object *obj = ObjectDB::get_instance(instanceId);
        obj->call_deferred("_on_AdMob_rewarded_ad_opened");
    } else {
        NSLog(@"reward ad wasn't ready");
    }
}


/// Tells the delegate that the user earned a reward.
- (void)rewardedAd:(GADRewardedAd *)rewardedAd userDidEarnReward:(GADAdReward *)reward {
    // TODO: Reward the user.
    NSLog(@"rewardedAd:userDidEarnReward:");
    NSString *rewardMessage = [NSString stringWithFormat:@"Reward received with currency %@ , amount %lf",
                               reward.type, [reward.amount doubleValue]];
    NSLog(@"%@", rewardMessage);
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_user_earned_rewarded", [reward.type UTF8String], reward.amount.doubleValue);
}
/// Tells the delegate that the rewarded ad failed to present.
- (void)rewardedAd:(GADRewardedAd *)rewardedAd didFailToPresentWithError:(NSError *)error {
    NSLog(@"rewardedAd:didFailToPresentWithError");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_rewarded_ad_failed_to_show", (int) error.code);
}

/// Tells the delegate that the rewarded ad was dismissed.
- (void)rewardedAdDidDismiss:(GADRewardedAd *)rewardedAd {
    NSLog(@"rewardedAdDidDismiss:");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_rewarded_ad_closed");
}


@end
