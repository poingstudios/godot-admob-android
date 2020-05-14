#include "core/reference.h"

class AdMob : public Reference {
	GDCLASS(AdMob, Reference);

protected:
	static void _bind_methods();

public:
	void init();
    AdMob();
    ~AdMob();
}