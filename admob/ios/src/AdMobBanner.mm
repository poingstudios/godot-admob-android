#import "AdMobBanner.h"
#include "reference.h"

@implementation AdMobBanner

- (void)dealloc {
    bannerView.delegate = nil;
    [bannerView release];
    [super dealloc];
}

- (void)initialize:(BOOL)is_real: (int)instance_id {
    isReal = is_real;
    initialized = true;
    instanceId = instance_id;
    rootController = [AppDelegate getViewController];
}


- (void) load_banner:(NSString*)ad_unit_id :(int)gravity :(NSString*)size {
    NSLog(@"Calling load_banner");
    
    isOnTop = false;
    
    if (!initialized || (!ad_unit_id.length)) {
        return;
    }
    else{
        NSLog(@"banner will load with the banner id");
        NSLog(ad_unit_id);
    }
    

    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    
    if (bannerView == nil) {
        if (orientation == 0 || orientation == UIInterfaceOrientationPortrait) { //portrait
            bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeSmartBannerPortrait];
        }
        else { //landscape
            bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeSmartBannerLandscape];
        }
        
        if(!isReal) {
            bannerView.adUnitID = @"ca-app-pub-3940256099942544/2934735716";
        }
        else {
            bannerView.adUnitID = ad_unit_id;
        }

        bannerView.delegate = self;
        bannerView.rootViewController = rootController;
        

        [self addBannerViewToView:bannerView:isOnTop];
    }
    
    GADRequest *request = [GADRequest request];
    [bannerView loadRequest:request];
    
}


- (void)addBannerViewToView:(UIView *_Nonnull)bannerView: (BOOL)is_on_top{
    bannerView.translatesAutoresizingMaskIntoConstraints = NO;
    [rootController.view addSubview:bannerView];
    if (@available(ios 11.0, *)) {
        [self positionBannerViewFullWidthAtSafeArea:bannerView:is_on_top];
    } else {
        [self positionBannerViewFullWidthAtView:bannerView:is_on_top];
    }
}



- (void)positionBannerViewFullWidthAtSafeArea:(UIView *_Nonnull)bannerView: (BOOL)is_on_top  NS_AVAILABLE_IOS(11.0) {
    UILayoutGuide *guide = rootController.view.safeAreaLayoutGuide;
    
    if (is_on_top) {
        [NSLayoutConstraint activateConstraints:@[
            [guide.leftAnchor constraintEqualToAnchor:bannerView.leftAnchor],
            [guide.rightAnchor constraintEqualToAnchor:bannerView.rightAnchor],
            [guide.topAnchor constraintEqualToAnchor:bannerView.topAnchor]
        ]];
    } else {
        [NSLayoutConstraint activateConstraints:@[
            [guide.leftAnchor constraintEqualToAnchor:bannerView.leftAnchor],
            [guide.rightAnchor constraintEqualToAnchor:bannerView.rightAnchor],
            [guide.bottomAnchor constraintEqualToAnchor:bannerView.bottomAnchor]
        ]];
    }
}


- (void)positionBannerViewFullWidthAtView:(UIView *_Nonnull)bannerView: (BOOL)is_on_top {
    
    [rootController.view addConstraint:[NSLayoutConstraint constraintWithItem:bannerView
                                                                    attribute:NSLayoutAttributeLeading
                                                                    relatedBy:NSLayoutRelationEqual
                                                                       toItem:rootController.view
                                                                    attribute:NSLayoutAttributeLeading
                                                                   multiplier:1
                                                                     constant:0]];
    [rootController.view addConstraint:[NSLayoutConstraint constraintWithItem:bannerView
                                                                    attribute:NSLayoutAttributeTrailing
                                                                    relatedBy:NSLayoutRelationEqual
                                                                       toItem:rootController.view
                                                                    attribute:NSLayoutAttributeTrailing
                                                                   multiplier:1
                                                                     constant:0]];
    
    if (is_on_top) {
        [rootController.view addConstraint:[NSLayoutConstraint constraintWithItem:bannerView
                                                                        attribute:NSLayoutAttributeTop
                                                                        relatedBy:NSLayoutRelationEqual
                                                                           toItem:rootController.topLayoutGuide
                                                                        attribute:NSLayoutAttributeTop
                                                                       multiplier:1
                                                                         constant:0]];
        
    } else {
        [rootController.view addConstraint:[NSLayoutConstraint constraintWithItem:bannerView
                                                              attribute:NSLayoutAttributeBottom
                                                              relatedBy:NSLayoutRelationEqual
                                                                 toItem:rootController.bottomLayoutGuide
                                                              attribute:NSLayoutAttributeTop
                                                             multiplier:1
                                                               constant:0]];
    }
}


@end