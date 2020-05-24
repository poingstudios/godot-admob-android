#import "AdMobRewarded.h"
#include "reference.h"

@implementation AdMobRewarded

- (void)dealloc 
{
	[rewarded release];
	[super dealloc];
}

- (void)initialize :(int)instance_id :(NSString*)test_device_id 
{
	initialized = true;
	instanceId = instance_id;
	testDeviceId = test_device_id;
	rootController = [AppDelegate getViewController];
}


- (void) load_rewarded :(NSString*)ad_unit_id 
{
	NSLog(@"Calling load_rewarded");
	
	if ((!initialized) || (!ad_unit_id.length)) 
	{
		return;
	}

	rewarded = [[GADRewardedAd alloc] initWithAdUnitID:ad_unit_id];
	NSLog(@"rewarded created with the id");
	NSLog(ad_unit_id);
	GADRequest *request = [GADRequest request];

	if (testDeviceId.length)
	{
		request.testDevices = @[testDeviceId];
		NSLog(@"Using test device with id");
		NSLog(testDeviceId);
	}
	[rewarded loadRequest :request completionHandler :^(GADRequestError * _Nullable error) {
		if (error) 
		{
			NSLog(@"error while creating reward");
			Object *obj = ObjectDB::get_instance(instanceId);
			obj->call_deferred("_on_AdMob_rewarded_ad_failed_to_show", error.code);
		}
		else 
		{
			NSLog(@"reward successfully loaded");
			Object *obj = ObjectDB::get_instance(instanceId);
			obj->call_deferred("_on_AdMob_rewarded_ad_loaded");
		}
	}];
	
}

- (void) show_rewarded 
{
	if (!initialized) 
	{
		return;
	}

	if (rewarded.isReady) 
	{
		[rewarded presentFromRootViewController:rootController delegate:self];
		Object *obj = ObjectDB::get_instance(instanceId);
		obj->call_deferred("_on_AdMob_rewarded_ad_opened");
	} 
	else 
	{
		NSLog(@"reward ad wasn't ready");
	}
}


/// Tells the delegate that the user earned a reward.
- (void)rewardedAd:(GADRewardedAd *)rewardedAd userDidEarnReward:(GADAdReward *)reward 
{
  // TODO: Reward the user.
	NSLog(@"rewardedAd:userDidEarnReward:");
	NSString *rewardMessage = [NSString stringWithFormat:@"Reward received with currency %@ , amount %lf",
		reward.type, [reward.amount doubleValue]];
	NSLog(rewardMessage);
	Object *obj = ObjectDB::get_instance(instanceId);
	obj->call_deferred("_on_AdMob_user_earned_rewarded", [reward.type UTF8String], reward.amount.doubleValue);
}

/// Tells the delegate that the rewarded ad was presented.
- (void)rewardedAdDidPresent:(GADRewardedAd *)rewardedAd 
{
	NSLog(@"rewardedAdDidPresent:");
}

/// Tells the delegate that the rewarded ad failed to present.
- (void)rewardedAd:(GADRewardedAd *)rewardedAd didFailToPresentWithError:(NSError *)error 
{
	NSLog(@"rewardedAd:didFailToPresentWithError");
	Object *obj = ObjectDB::get_instance(instanceId);
	obj->call_deferred("_on_AdMob_rewarded_ad_failed_to_show", error.code);
}

/// Tells the delegate that the rewarded ad was dismissed.
- (void)rewardedAdDidDismiss:(GADRewardedAd *)rewardedAd 
{
	NSLog(@"rewardedAdDidDismiss:");
	Object *obj = ObjectDB::get_instance(instanceId);
	obj->call_deferred("_on_AdMob_rewarded_ad_closed");
}


@end