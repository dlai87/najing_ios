//
//  DetectionTask.h
//  aimm
//
//  Created by dehualai on 1/24/18.
//  Copyright © 2018 NanoMed. All rights reserved.
//

#import "SyncTask.h"
#import <CoreData/CoreData.h>


#define TASK_PULL 0
#define TASK_PUSH 1

@interface DetectionTask : SyncTask

-(id)initWithContext:(NSManagedObjectContext*)context;

@end
