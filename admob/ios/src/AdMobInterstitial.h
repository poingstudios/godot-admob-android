#import <GoogleMobileAds/GADInterstitial.h>
#import "app_delegate.h"
 
@interface AdMobInterstitial: NSObject <GADInterstitialDelegate> {
    GADInterstitial *interstitial;
    bool initialized;
    bool isReal;
    int instanceId;
    NSString *adUnitId;
    ViewController *rootController;
}

- (void)initialize:(BOOL)is_real: (int)instance_id;
- (void)load_interstitial: (NSString*)ad_unit_id;
- (void)show_interstitial;

@end
