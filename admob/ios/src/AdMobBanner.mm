#import "AdMobBanner.h"

@implementation AdMobBanner

//https://developers.google.com/admob/ios/banner

- (void)initialize :(int)instance_id :(NSString*)test_device_id
{
	//
	[[GADMobileAds sharedInstance] startWithCompletionHandler:nil];
	NSLog(@"Banner Initialized");
	_test_device_id = test_device_id;
	_instance_id = instance_id;
	_root_controller = [AppDelegate getViewController];
	_initialized = true;
}

- (void)load_banner :(NSString*)ad_unit_id :(int)gravity :(NSString*)size
{
	if (!_initialized)
		return;
	if ((!_size.length))
		_size = size;
	if (_banner_view != nil)
		[self destroy_banner];


	UIInterfaceOrientation screen_orientation = [UIApplication sharedApplication].statusBarOrientation;
	if (screen_orientation == 0 || screen_orientation == UIInterfaceOrientationPortrait) 
	{
		_banner_view = [[GADBannerView alloc] initWithAdSize:kGADAdSizeSmartBannerPortrait];
    }
    else 
    { 
		_banner_view = [[GADBannerView alloc] initWithAdSize:kGADAdSizeSmartBannerLandscape];
	}

	_banner_view.adUnitID = ad_unit_id;
	_banner_view.delegate = self;
	_banner_view.rootViewController = _root_controller;

	[self addBannerViewToView:_banner_view];

    [_banner_view loadRequest:[GADRequest request]];

}
- (void)addBannerViewToView:(UIView *)bannerView {
  bannerView.translatesAutoresizingMaskIntoConstraints = NO;
  [_root_controller.view addSubview:bannerView];
  [_root_controller.view addConstraints:@[
    [NSLayoutConstraint constraintWithItem:bannerView
                               attribute:NSLayoutAttributeBottom
                               relatedBy:NSLayoutRelationEqual
                                  toItem:_root_controller.bottomLayoutGuide
                               attribute:NSLayoutAttributeTop
                              multiplier:1
                                constant:0],
    [NSLayoutConstraint constraintWithItem:bannerView
                               attribute:NSLayoutAttributeCenterX
                               relatedBy:NSLayoutRelationEqual
                                  toItem:_root_controller.view
                               attribute:NSLayoutAttributeCenterX
                              multiplier:1
                                constant:0]
                                ]];
}


- (void)destroy_banner
{
	if (!_initialized)
		return;

	if (_banner_view != nil)
	{
		[_banner_view setHidden:YES];
	    [_banner_view removeFromSuperview];
	    _banner_view = nil;
		Object *obj = ObjectDB::get_instance(_instance_id);
	    obj->call_deferred("_on_AdMob_banner_destroyed");
	}
}

/// Tells the delegate an ad request loaded an ad.
- (void)adViewDidReceiveAd:(GADBannerView *)adView {
    Object *obj = ObjectDB::get_instance(_instance_id);
    obj->call_deferred("_on_AdMob_banner_loaded");
}


@end