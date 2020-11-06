@import GoogleMobileAds;
#import "app_delegate.h"

@interface AdMobBanner: NSObject <GADBannerViewDelegate> {
	GADBannerView *bannerView;
	bool initialized;
	bool isOnTop;
	int instanceId;
	bool isPersonalized;
	NSString *adUnitId;
	ViewController *rootController;
}

- (void)initialize:(int)instance_id:(bool)is_personalized;
- (void)load_banner:(NSString*)ad_unit_id :(int)gravity :(NSString*)size;
- (void)destroy_banner;

@end
