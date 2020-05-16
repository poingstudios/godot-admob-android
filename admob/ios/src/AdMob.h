#include "reference.h"

#ifdef __OBJC__
@class AdMobBanner;
typedef AdMobBanner * BannerPtr;
#else
typedef void * BannerPtr;
#endif

class AdMob : public Reference {
    GDCLASS(AdMob, Reference);
    //Ads Formats
    BannerPtr _banner;

    bool initialized;
    static AdMob* instance;


protected:
    static void _bind_methods();

public:
    void init(bool is_for_child_directed_treatment, bool is_personalized, const String &max_ad_content_rating, int instance_id, const String &test_device_id);
    void load_banner(const String &ad_unit_id, int gravity, const String &size);
    void destroy_banner();
    AdMob();
    ~AdMob();
};
