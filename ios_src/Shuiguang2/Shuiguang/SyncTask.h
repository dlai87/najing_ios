//
//  SyncTask.h
//  Shuiguang
//
//  Created by dehualai on 3/1/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SyncTaskHandler.h"

#define PARAM_TASK @"task"

@interface SyncTask : NSObject

@property (nonatomic, strong) id<SyncTaskHandler> handler;


-(void)sync:(NSDictionary*)params;


@end
