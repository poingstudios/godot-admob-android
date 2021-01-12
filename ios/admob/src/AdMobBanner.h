#import <GoogleMobileAds/GADBannerView.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"
#import "view_controller.h"
#import "godot_view.h"
#include "reference.h"

@class AdMobBanner;

@interface AdMobBanner: NSObject <GADBannerViewDelegate> {
    GADBannerView *bannerView;
    bool initialized;
    int instanceId;
    bool isPersonalized;
    int positionBanner;
    NSString *adUnitId;
    ViewController *rootController;
}
@property (nonatomic, strong) AdMobBanner *adMobBanner;

- (instancetype)init: (int) instance_id : (bool) is_personalized;
- (void)load_banner: (NSString*) ad_unit_id : (int) position : (NSString*) size;
- (void)destroy_banner;

@end
