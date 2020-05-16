#include "reference.h"

class AdMob : public Reference {
    GDCLASS(AdMob, Reference);

protected:
    static void _bind_methods();

public:
    void init(bool pIsForChildDirectedTreatment, bool pIsPersonalized, const String &pMaxAdContentRating, int pInstanceId, const String &pTestDeviceId);
    AdMob();
    ~AdMob();
};
