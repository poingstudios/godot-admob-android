#import <GoogleMobileAds/GADBannerView.h>

#import "app_delegate.h"

@interface AdMobBanner: NSObject <GADBannerViewDelegate> {
    GADBannerView *bannerView;
}

- (void)init:(NSString*)test_device_id: (int)instance_id;

@end