#import <GoogleMobileAds/GADInterstitial.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"
#import "view_controller.h"
#include "object.h"

@class Interstitial;

@interface Interstitial: NSObject <GADInterstitialDelegate> {
    GADInterstitial *interstitial;
    bool initialized;
    int instanceId;
    NSString *adUnitId;
    ViewController *rootController;
}

@property (nonatomic, strong) Interstitial *interstitial;


- (instancetype)init: (int) instance_id;
- (void)load_interstitial: (NSString*)ad_unit_id;
- (void)show_interstitial;

@end
