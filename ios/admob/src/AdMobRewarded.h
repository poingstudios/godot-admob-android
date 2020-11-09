#import <GoogleMobileAds/GADRewardedAd.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"

@interface AdMobRewarded: NSObject <GADRewardedAdDelegate> {
	GADRewardedAd *rewarded;
	bool initialized;
	int instanceId;
	bool isPersonalized;
	NSString *adUnitId;
	ViewController *rootController;
}

- (instancetype)initialize: (int)instance_id: (bool)is_personalized;
- (void)load_rewarded: (NSString*)ad_unit_id;
- (void)show_rewarded;

@end