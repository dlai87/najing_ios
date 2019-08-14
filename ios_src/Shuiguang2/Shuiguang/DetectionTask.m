//
//  DetectionTask.m
//  aimm
//
//  Created by dehualai on 1/24/18.
//  Copyright © 2018 NanoMed. All rights reserved.
//

#import "DetectionTask.h"
#import "DBUser+Operation.h"
#import "DBDetection+Operation.h"
#import "DetectionData.h"
#import "Detection.h"
#import "Util.h"
#import "AWSDynamoDBObjectMapper.h"


@interface DetectionTask()

@property (nonatomic, strong) NSManagedObjectContext* context;


@end

@implementation DetectionTask
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
    /*
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
    */
}

-(void)push{
    NSArray* users = [DBUser getUserList:self.context];
    if ([users count] > 0 ) {
        NSString* user_id = [[users firstObject]user_id];
        NSArray* list = [DBDetection getDetectionList:self.context];
        NSMutableArray* dataHaveNotTransmitted = [[NSMutableArray alloc]init];
        AWSDynamoDBObjectMapper* dynamoDbObjectMapper = [AWSDynamoDBObjectMapper defaultDynamoDBObjectMapper];
        
        for(DetectionData* data in list){
            if(![TRANS_STATUS_TRANSMITTED isEqualToString:[data transmission_status]]){
                [dataHaveNotTransmitted addObject:data] ;
            }
        }
        
        DetectionData* data = [dataHaveNotTransmitted lastObject];
        if (data != nil) {
            Detection* detectionObj = [[Detection alloc]init];
            NSString* detect_id = [NSString stringWithFormat:@"%@%@",user_id,[Util convertDateToString:[data detectionTime] withFormat:FORMAT_DATE_TIME]];
            detectionObj._detectionId = [Util javaHashCode:detect_id];
            detectionObj._userId = user_id;
            detectionObj._bodyArea = [data bodyPart];
            detectionObj._value = [NSString stringWithFormat:@"%@", [data value]];
            detectionObj._nursingStatus = [data pre_post];
            detectionObj._dateTime = [Util convertDateToString:[data detectionTime] withFormat:FORMAT_DATE_TIME] ;
            
            [[dynamoDbObjectMapper save:detectionObj]
             continueWithBlock:^id(AWSTask *task) {
                 if (task.error) {
                     NSLog(@"|||||||The request failed. Error: [%@]", task.error);
                 } else {
                     //Do something with task.result or perform other operations.
                     NSLog(@"save detection success");
                     data.transmission_status = TRANS_STATUS_TRANSMITTED;
                     [DBDetection updateTransmissionStatus:self.context detectionData:data];
                 }
                 [dataHaveNotTransmitted removeLastObject];
                 return nil;
             }];
        }
    }
}







@end
