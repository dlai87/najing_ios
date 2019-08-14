//
//  USERTask.h
//  aimm
//
//  Created by dehualai on 1/20/18.
//  Copyright © 2018 NanoMed. All rights reserved.
//

#import "SyncTask.h"
#import <CoreData/CoreData.h>

#define TASK_PULL 0
#define TASK_PUSH 1


@interface USERTask : SyncTask

-(id)initWithContext:(NSManagedObjectContext*)context;

@end
