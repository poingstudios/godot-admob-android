#import "AdMobInterstitial.h"
#include "reference.h"

@implementation AdMobInterstitial

- (void)dealloc {
    interstitialView.delegate = nil;
    [interstitialView release];
    [super dealloc];
}

- (void)initialize:(BOOL)is_real: (int)instance_id {
    isReal = is_real;
    initialized = true;
    instanceId = instance_id;
    rootController = [AppDelegate getViewController];
}


- (void) load_interstitial:(NSString*)ad_unit_id {
    NSLog(@"Calling load_interstitial");
    
    if (!initialized || (!ad_unit_id.length)) {
        return;
    }
    else{
        NSLog(@"interstitial will load with the id");
        NSLog(ad_unit_id);
    }
    
    if (interstitialView == nil) {
        
        if(!isReal) {
            interstitialView = [[GADInterstitial alloc] initWithAdUnitID:"ca-app-pub-3940256099942544/4411468910"];
        }
        else {
            interstitialView = [[GADInterstitial alloc] initWithAdUnitID:ad_unit_id];
        }

        interstitialView.delegate = self;
        interstitialView.rootViewController = rootController;
    }
    
    GADRequest *request = [GADRequest request];
    [interstitialView loadRequest:request];
    
}

- (void) show_interstitial {
    if (!initialized) {
        return;
    }

    if (interstitialView.isReady) {
        [interstitialView presentFromRootViewController];
    } else {
        NSLog(@"Ad wasn't ready");
    }
}


/// Tells the delegate an ad request succeeded.
- (void)interstitialDidReceiveAd:(GADInterstitial *)ad {
  NSLog(@"interstitialDidReceiveAd");
}

/// Tells the delegate an ad request failed.
- (void)interstitial:(GADInterstitial *)ad
    didFailToReceiveAdWithError:(GADRequestError *)error {
  NSLog(@"interstitial:didFailToReceiveAdWithError: %@", [error localizedDescription]);
}

/// Tells the delegate that an interstitial will be presented.
- (void)interstitialWillPresentScreen:(GADInterstitial *)ad {
  NSLog(@"interstitialWillPresentScreen");
}

/// Tells the delegate the interstitial is to be animated off the screen.
- (void)interstitialWillDismissScreen:(GADInterstitial *)ad {
  NSLog(@"interstitialWillDismissScreen");
}

/// Tells the delegate the interstitial had been animated off the screen.
- (void)interstitialDidDismissScreen:(GADInterstitial *)ad {
  NSLog(@"interstitialDidDismissScreen");
}

/// Tells the delegate that a user click will open another app
/// (such as the App Store), backgrounding the current app.
- (void)interstitialWillLeaveApplication:(GADInterstitial *)ad {
  NSLog(@"interstitialWillLeaveApplication");
}

@end