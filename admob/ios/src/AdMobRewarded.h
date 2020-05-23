#import <GoogleMobileAds/GADRewardedAd.h>
#import "app_delegate.h"
 
@interface AdMobRewarded: NSObject <GADRewardedAdDelegate> {
    GADRewardedAd *rewarded;
    bool initialized;
    bool isReal;
    int instanceId;
    NSString *adUnitId;
    ViewController *rootController;
}

- (void)initialize:(BOOL)is_real: (int)instance_id;
- (void)load_rewarded: (NSString*)ad_unit_id;
- (void)show_rewarded;

@end