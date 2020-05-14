#include "register_types.h"
#include "core/class_db.h"
#include "ios/src/AdMob.h"

void register_calculator_types(){
	Engine::get_singleton()->add_singleton(Engine::Singleton("AdMob", memnew(AdMob)));
}

void unregister_calculator_types() {
}