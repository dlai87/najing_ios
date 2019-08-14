//
//  Global.m
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "Global.h"

@implementation Global


+ (id)getInstance {
    static Global *global = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        global = [[self alloc] init];
        global.signInMode = SIGN_IN_MODE_WECHAT; 
        
    });
    return global;
}


@end
