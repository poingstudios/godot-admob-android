//
//  Interstitial.h
//  Interstitial
//
//  Created by Gustavo Maciel on 24/01/21.
//


#import <GoogleMobileAds/GADInterstitialAd.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"
#import "view_controller.h"
#include "os_iphone.h"
#include "object.h"

@class Interstitial;

@interface Interstitial: NSObject<GADFullScreenContentDelegate> {
    GADInterstitialAd * interstitial;
    bool initialized;
    int instanceId;
    NSString *adUnitId;
    ViewController *rootController;
}


- (instancetype)init: (int) instance_id;
- (void)load_interstitial: (NSString*)ad_unit_id;
- (void)show_interstitial;

@end
