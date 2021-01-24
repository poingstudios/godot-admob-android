//
//  admob_plugin.mm
//  admob_plugin
//
//  Created by Gustavo Maciel on 16/01/21.
//

#import <Foundation/Foundation.h>

#import "admob_plugin.h"
#import "admob_plugin_implementation.h"

#import "core/engine.h"

AdMob * admob;

void admob_plugin_init() {
    NSLog(@"init admob plugin");

    admob = memnew(AdMob);
    Engine::get_singleton()->add_singleton(Engine::Singleton("AdMob", admob));
}

void admob_plugin_deinit() {
    NSLog(@"deinit admob plugin");
    
    if (admob) {
       memdelete(admob);
   }
}
