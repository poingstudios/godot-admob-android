//
//  Banner.mm
//  Banner
//
//  Created by Gustavo Maciel on 24/01/21.
//


#import "Banner.h"

@implementation Banner

- (void)dealloc {
    bannerView.delegate = nil;
}

- (instancetype)init: (int)instance_id{
    if ((self = [super init])) {
        initialized = true;
        instanceId = instance_id;
        rootController = (ViewController *)((AppDelegate *)[[UIApplication sharedApplication] delegate]).window.rootViewController;
    }
    return self;
}


- (void) load_banner:(NSString*)ad_unit_id :(int)position :(NSString*)size {
    NSLog(@"Calling load_banner");
        
    if (!initialized || (!ad_unit_id.length)) {
        return;
    }
    else{
        NSLog(@"banner will load with the banner id %@", ad_unit_id);
    }
    
    positionBanner = position;
    NSLog(@"banner position = %i", positionBanner);
    
    
    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;

    if (bannerView != nil) {
        [self destroy_banner];
    }

    
    if ([size isEqualToString:@"BANNER"]) {
        bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeBanner];
        NSLog(@"Banner created");
    } else if ([size isEqualToString:@"LARGE_BANNER"]) {
        bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeLargeBanner];
        NSLog(@"Large banner created");
    } else if ([size isEqualToString:@"MEDIUM_RECTANGLE"]) {
        bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeMediumRectangle];
        NSLog(@"Medium banner created");
    } else if ([size isEqualToString:@"FULL_BANNER"]) {
        bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeFullBanner];
        NSLog(@"Full banner created");
    } else if ([size isEqualToString:@"LEADERBOARD"]) {
        bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeLeaderboard];
        NSLog(@"Leaderboard banner created");
    } else { //smart banner
        if (orientation == 0 || orientation == UIInterfaceOrientationPortrait) { //portrait
            bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeSmartBannerPortrait];
            NSLog(@"Smart portait banner created");
        }
        else { //landscape
            bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeSmartBannerLandscape];
            NSLog(@"Smart landscape banner created");
        }
    }
    
    bannerView.adUnitID = ad_unit_id;

    bannerView.delegate = self;
    bannerView.rootViewController = rootController;
    
    GADRequest *request = [GADRequest request];
    [bannerView loadRequest:request];

    
    
}

- (void)addBannerViewToView {
    bannerView.translatesAutoresizingMaskIntoConstraints = NO;
    [rootController.view addSubview:bannerView];
    //CENTER ON MIDDLE OF SCREEM
    [rootController.view addConstraint:
        [NSLayoutConstraint constraintWithItem:bannerView
                                     attribute:NSLayoutAttributeCenterX
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:rootController.view
                                     attribute:NSLayoutAttributeCenterX
                                    multiplier:1
                                      constant:0]];

    if (positionBanner == 0)//BOTTOM
    {
        [rootController.view addConstraint:
            [NSLayoutConstraint constraintWithItem:bannerView
                                        attribute:NSLayoutAttributeBottom
                                        relatedBy:NSLayoutRelationEqual
                                            toItem:rootController.view.safeAreaLayoutGuide
                                        attribute:NSLayoutAttributeBottom
                                        multiplier:1
                                        constant:0]];
    }
    else if(positionBanner == 1)//TOP
    {
        [rootController.view addConstraint:
            [NSLayoutConstraint constraintWithItem:bannerView
                                        attribute:NSLayoutAttributeTop
                                        relatedBy:NSLayoutRelationEqual
                                            toItem:rootController.view.safeAreaLayoutGuide
                                        attribute:NSLayoutAttributeTop
                                        multiplier:1
                                        constant:0]];
    }
}


- (void)destroy_banner
{
    if (!initialized)
        return;
    
    if (bannerView != nil)
    {
        [bannerView setHidden:YES];
        [bannerView removeFromSuperview];
        bannerView = nil;
        Object *obj = ObjectDB::get_instance(instanceId);
        obj->call_deferred("_on_AdMob_banner_destroyed");
    }
}

//LISTENERS

- (void)bannerViewDidReceiveAd:(GADBannerView *)bannerView {
    NSLog(@"bannerViewDidReceiveAd");
    [self addBannerViewToView];
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_banner_loaded");

}

- (void)bannerView:(GADBannerView *)bannerView didFailToReceiveAdWithError:(NSError *)error {
    NSLog(@"bannerView:didFailToReceiveAdWithError: %@", [error localizedDescription]);
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_banner_failed_to_load", (int) error.code);
}

- (void)bannerViewDidRecordImpression:(GADBannerView *)bannerView {
  NSLog(@"bannerViewDidRecordImpression");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_banner_recorded_impression");
}

- (void)bannerViewWillPresentScreen:(GADBannerView *)bannerView {
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_banner_clicked");
}

- (void)bannerViewWillDismissScreen:(GADBannerView *)bannerView {
    NSLog(@"bannerViewWillDismissScreen");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_banner_closed");
}

- (void)bannerViewDidDismissScreen:(GADBannerView *)bannerView {
    NSLog(@"bannerViewDidDismissScreen");
    Object *obj = ObjectDB::get_instance(instanceId);
    obj->call_deferred("_on_AdMob_banner_opened");
}

@end
