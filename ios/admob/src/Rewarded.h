#import <GoogleMobileAds/GADRewardedAd.h>
#import <GoogleMobileAds/GADExtras.h>
#import "app_delegate.h"
#import "view_controller.h"
#import "godot_view.h"
#include "object.h"

@class Rewarded;


@interface Rewarded: NSObject <GADRewardedAdDelegate> {
    GADRewardedAd *rewarded;
    bool initialized;
    int instanceId;
    bool isPersonalized;
    NSString *adUnitId;
    ViewController *rootController;
}
@property (nonatomic, strong) Rewarded * rewarded;


- (instancetype)init: (int) instance_id : (bool) is_personalized;
- (void)load_rewarded: (NSString*) ad_unit_id;
- (void)show_rewarded;

@end
