#include "AdMob.h"
#import "app_delegate.h" //necessary to use NSLog

AdMob::AdMob() {
}

AdMob::~AdMob() {
}

void AdMob::init(bool pIsForChildDirectedTreatment, bool pIsPersonalized, const String &pMaxAdContentRating, int pInstanceId, const String &pTestDeviceId) {
    NSLog(@"AdMob iOS initialized!");
}



void AdMob::_bind_methods() {
    ClassDB::bind_method("init", &AdMob::init);
}
