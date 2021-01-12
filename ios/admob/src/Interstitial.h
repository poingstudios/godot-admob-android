#import <GoogleMobileAds/GADInterstitial.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"
#import "view_controller.h"
#import "godot_view.h"
#include "object.h"

@class Interstitial;

@interface Interstitial: NSObject <GADInterstitialDelegate> {
    GADInterstitial *interstitial;
    bool initialized;
    int instanceId;
    bool isPersonalized;
    NSString *adUnitId;
    ViewController *rootController;
}

@property (nonatomic, strong) Interstitial *interstitial;


- (instancetype)init: (int) instance_id : (bool) is_personalized;
- (void)load_interstitial: (NSString*)ad_unit_id;
- (void)show_interstitial;

@end
