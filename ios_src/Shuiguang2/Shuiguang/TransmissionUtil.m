//
//  TransmissionUtil.m
//  Shuiguang
//
//  Created by dehualai on 3/25/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "TransmissionUtil.h"
#import "Global.h"
#import "DetectionHistoryTask.h"
#import "UserInfoTask.h"
#import "DBUser+Operation.h"
#import "DBDetection+Operation.h"
#import "USERTask.h"
#import "DetectionTask.h"

@implementation TransmissionUtil

+(void)syncAll:(NSManagedObjectContext*)context{
    [TransmissionUtil syncDetectionAWS:context];
    [TransmissionUtil syncUserAWS:context];
    [DBDetection print:context];
    [DBUser print:context]; 
    
}

+(void)syncDetection:(NSManagedObjectContext*)context{
    if(![[Global getInstance] DEMO_MODE]){
        DetectionHistoryTask* detectionHistoryTask = [[DetectionHistoryTask alloc]initWithContext:context];
        NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
        // pull
        [params setObject:@(TASK_PULL) forKey:PARAM_TASK];
        [detectionHistoryTask sync:params];
    
        params = [[NSMutableDictionary alloc]init];
        // push
        [params setObject:@(TASK_PUSH) forKey:PARAM_TASK];
        [detectionHistoryTask sync:params];
    }
}

+(void)syncUserInfo:(NSManagedObjectContext*)context{
    UserInfoTask* userInfoTask = [[UserInfoTask alloc]initWithContext:context];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:@(TASK_PUSH) forKey:PARAM_TASK];
    [userInfoTask sync:params];

}

+(void)syncUserAWS:(NSManagedObjectContext*)context{
    USERTask* userTask = [[USERTask alloc]initWithContext:context];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:@(TASK_PUSH) forKey:PARAM_TASK];
    [userTask sync:params]; 
}

+(void)syncDetectionAWS:(NSManagedObjectContext*)context{
    if(![[Global getInstance] DEMO_MODE]){
        DetectionTask* detectionHistoryTask = [[DetectionTask alloc]initWithContext:context];
        NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
        // pull
        [params setObject:@(TASK_PULL) forKey:PARAM_TASK];
        [detectionHistoryTask sync:params];
        
        params = [[NSMutableDictionary alloc]init];
        // push
        [params setObject:@(TASK_PUSH) forKey:PARAM_TASK];
        [detectionHistoryTask sync:params];
    }
    
}

@end
