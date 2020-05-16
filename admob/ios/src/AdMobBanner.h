#import <GoogleMobileAds/GoogleMobileAds.h>
#import <GoogleMobileAds/GADBannerView.h>

#import "app_delegate.h"
#import "object.h"

@interface AdMobBanner: NSObject <GADBannerViewDelegate> {
    GADBannerView *_banner_view;
    NSString *_test_device_id;
    int _instance_id;
    bool _initialized;

    ViewController *_root_controller;

    int _gravity;
    NSString *_size;
}

- (void)initialize :(int)instance_id :(NSString*)test_device_id;
- (void)load_banner :(NSString*)ad_unit_id :(int)gravity :(NSString*)size;
- (void)destroy_banner;

@end