//
//  DetectionHistoryTask.h
//  Shuiguang
//
//  Created by dehualai on 3/25/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "SyncTask.h"
#import <CoreData/CoreData.h>

#define TASK_PULL 0
#define TASK_PUSH 1
@interface DetectionHistoryTask : SyncTask

-(id)initWithContext:(NSManagedObjectContext*)context;


@end
