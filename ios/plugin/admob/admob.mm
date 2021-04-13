//
//  admob_plugin.mm
//  admob_plugin
//
//  Created by Gustavo Maciel on 16/01/21.
//

#import <Foundation/Foundation.h>

#import "admob.h"
#import "admob_implementation.h"

#import "core/engine.h"

AdMob * admob;

void register_admob_types() {
    NSLog(@"init admob plugin");

    admob = memnew(AdMob);
    Engine::get_singleton()->add_singleton(Engine::Singleton("AdMob", admob));
}

void unregister_admob_types() {
    NSLog(@"deinit admob plugin");
    
    if (admob) {
       memdelete(admob);
   }
}
