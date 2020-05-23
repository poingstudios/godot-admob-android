#ifndef ADMOB_H
#define ADMOB_H

#include "reference.h"


#ifdef __OBJC__
@class AdMobBanner;
typedef AdMobBanner *bannerPtr;
@class AdMobInterstitial;
typedef AdMobInterstitial *interstitialPtr;
#else
typedef void *bannerPtr;
typedef void *interstitialPtr;
#endif



class AdMob : public Reference {
    
    GDCLASS(AdMob, Reference);

    bool initialized;
    static AdMob *instance; //fix
    
    bannerPtr banner;
    interstitialPtr interstitial;


protected:
    static void _bind_methods();

public:
    void init(bool is_for_child_directed_treatment, bool is_personalized, const String &max_ad_content_rating, int instance_id, const String &test_device_id);
    void load_banner(const String &ad_unit_id, int gravity, const String &size);
    void destroy_banner();
    void load_interstitial(const String &ad_unit_id);
    void show_interstitial();

    AdMob();
    ~AdMob();
};

#endif
