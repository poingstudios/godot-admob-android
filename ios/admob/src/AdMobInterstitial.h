#import <GoogleMobileAds/GADInterstitial.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"

@interface AdMobInterstitial: NSObject <GADInterstitialDelegate> {
    GADInterstitial *interstitial;
    bool initialized;
    int instanceId;
    bool isPersonalized;
    NSString *adUnitId;
    ViewController *rootController;
}

- (instancetype)initialize: (int) instance_id : (bool) is_personalized;
- (void)load_interstitial: (NSString*)ad_unit_id;
- (void)show_interstitial;

@end
