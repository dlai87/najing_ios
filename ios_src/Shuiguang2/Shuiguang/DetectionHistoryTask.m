//
//  DetectionHistoryTask.m
//  Shuiguang
//
//  Created by dehualai on 3/25/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "DetectionHistoryTask.h"
#import "DBUser+Operation.h"
#import "DBDetection+Operation.h"
#import <BmobSDK/BmobObject.h>
#import <BmobSDK/BmobObjectsBatch.h>
#import "DetectionData.h"
#import "Detection.h"
#import "Util.h"
#import <BmobSDK/BmobQuery.h>


@interface DetectionHistoryTask()

@property (nonatomic, strong) NSManagedObjectContext* context;


@end


@implementation DetectionHistoryTask

-(id)initWithContext:(NSManagedObjectContext*)context{
    self = [super init];
    self.context = context;
    return self;
}


// Override
-(void)sync:(NSDictionary*)params{
    
    NSNumber* task = (NSNumber*) [params objectForKey:PARAM_TASK];
    switch (task.intValue) {
        case TASK_PULL:
            [self pull];
            break;
        case TASK_PUSH:
            [self push];
            break;
        default:
            break;
    }
}


-(void)pull{
    NSArray* users = [DBUser getUserList:self.context];
   // [DBDetection print:self.context];
    if ([users count] > 0 ) {
        NSString* user_id = [[users firstObject]user_id];
        
        BmobQuery *bquery = [BmobQuery queryWithClassName:@"Detection"];
        [bquery whereKey:@"user_id" equalTo:user_id];
        [bquery setLimit:99999];
        [bquery findObjectsInBackgroundWithBlock:^(NSArray *array, NSError *error) {
            for (BmobObject *obj in array) {
                
                NSString* valueStr = [obj objectForKey:@"value"];
                NSString* bodyPartStr = [obj objectForKey:@"body_part"];
                NSString* nursingStatusStr = [obj objectForKey:@"nursing_status"];
                NSString* detectionTimeStr = [obj objectForKey:@"date_time"];
                
                DetectionData* data = [[DetectionData alloc]init];
                [data setValue: @([valueStr floatValue])] ;
                [data setBodyPart:bodyPartStr];
                [data setPre_post:nursingStatusStr];
                [data setTransmission_status:TRANS_STATUS_TRANSMITTED]; 
                [data setDetectionTime:[Util convertStringToDate:detectionTimeStr withFormat:FORMAT_DATE_TIME]];
                [DBDetection smartInsert:self.context detectionData:data];
            }
            
            [DBDetection print:self.context];

        }];
    }

}

-(void)push{
    NSArray* users = [DBUser getUserList:self.context];
    if ([users count] > 0 ) {
        NSString* user_id = [[users firstObject]user_id];
        NSArray* list = [DBDetection getDetectionList:self.context];
        NSMutableArray* dataHaveNotTransmitted = [[NSMutableArray alloc]init];

        BmobObjectsBatch *batch = [[BmobObjectsBatch alloc] init] ;
        for(DetectionData* data in list){
            if(![TRANS_STATUS_TRANSMITTED isEqualToString:[data transmission_status]]){
                [dataHaveNotTransmitted addObject:data];
                [batch saveBmobObjectWithClassName:@"Detection" parameters:@{@"user_id": user_id,
                                                                             @"body_part": [data bodyPart],
                                                                             @"value": [NSString stringWithFormat:@"%@", [data value]],
                                                                             @"nursing_status": [data pre_post],
                                                                             @"date_time": [Util convertDateToString:[data detectionTime] withFormat:FORMAT_DATE_TIME]                                                                             }];
            }
        }
        
        if ([dataHaveNotTransmitted count] <=0 ) {
            return;
        }
        
        
        [batch batchObjectsInBackgroundWithResultBlock:^(BOOL isSuccessful, NSError *error) {
            if(isSuccessful){
                for(DetectionData* data in dataHaveNotTransmitted){
                    data.transmission_status = TRANS_STATUS_TRANSMITTED;
                    [DBDetection updateTransmissionStatus:self.context detectionData:data];
                }
            }
            NSLog(@"batch update result: %d", isSuccessful);
            NSLog(@"batch error %@",[error description]);
        }];
        
    }
}


@end
