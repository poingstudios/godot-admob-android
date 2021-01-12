#import <GoogleMobileAds/GADInterstitial.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"
#import "view_controller.h"
#import "godot_view.h"
#include "reference.h"

@class AdMobInterstitial;


@interface AdMobInterstitial: NSObject <GADInterstitialDelegate> {
    GADInterstitial *interstitial;
    bool initialized;
    int instanceId;
    bool isPersonalized;
    NSString *adUnitId;
    ViewController *rootController;
}

@property (nonatomic, strong) AdMobInterstitial *adMobInterstitial;


- (instancetype)init: (int) instance_id : (bool) is_personalized;
- (void)load_interstitial: (NSString*)ad_unit_id;
- (void)show_interstitial;

@end
