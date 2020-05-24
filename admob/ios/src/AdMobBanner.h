#import <GoogleMobileAds/GADBannerView.h>
#import "app_delegate.h"

@interface AdMobBanner: NSObject <GADBannerViewDelegate> 
{
	GADBannerView *bannerView;
	bool initialized;
	bool isOnTop;
	int instanceId;
	NSString *testDeviceId;
	NSString *adUnitId;
	ViewController *rootController;
}

- (void)initialize :(int)instance_id :(NSString*)test_device_id;
- (void)load_banner :(NSString*)ad_unit_id :(int)gravity :(NSString*)size;
- (void)destroy_banner;

@end
