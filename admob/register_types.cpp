#include <core/class_db.h>
#include <core/engine.h>

#include "register_types.h"
#include "ios/src/AdMob.h"

void register_admob_types() {
    Engine::get_singleton()->add_singleton(Engine::Singleton("AdMob", memnew(AdMob))); //register AdMob module as a singleton (iOS)
}

void unregister_admob_types() {
}
