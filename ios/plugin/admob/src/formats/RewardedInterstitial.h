//
//  RewardedInterstitial.h
//  RewardedInterstitial
//
//  Created by Gustavo Maciel on 25/06/21.
//

#import <GoogleMobileAds/GADRewardedAd.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"
#import "view_controller.h"
#include "os_iphone.h"
#include "object.h"

@class RewardedInterstitial;


@interface RewardedInterstitial: NSObject <GADFullScreenContentDelegate> {
    GADRewardedInterstitialAd *rewardedInterstitialAd;
    bool initialized;
    int instanceId;
    NSString *adUnitId;
    ViewController *rootController;
}

- (instancetype)init: (int) instance_id;
- (void)load_rewarded_interstitial: (NSString*) ad_unit_id;
- (void)show_rewarded_interstitial;

@end
