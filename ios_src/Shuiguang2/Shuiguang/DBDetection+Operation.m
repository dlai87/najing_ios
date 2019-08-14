//
//  Detection+Operation.m
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "DBDetection+Operation.h"
#import "Util.h"

@implementation DBDetection (Operation)



+(void)insert:(NSManagedObjectContext*)context detectionData:(DetectionData*)detectionData{
    DBDetection* temp = [NSEntityDescription insertNewObjectForEntityForName:@"DBDetection" inManagedObjectContext:context];
    temp.body_part = [detectionData bodyPart];
    temp.detect_date_time = [Util convertDateToString:[detectionData detectionTime] withFormat:FORMAT_DATE_TIME];
    temp.value = [detectionData value];
    temp.nursing_status = [detectionData pre_post];
    if ( [detectionData transmission_status]  == nil || [[detectionData transmission_status] isEqualToString:@""]) {
        temp.transmission_status = TRANS_STATUS_INSERTED;
    }else{
        temp.transmission_status = [detectionData transmission_status]; 
    }
    [Util save:context];
}

+(void)updateTransmissionStatus:(NSManagedObjectContext*)context detectionData:(DetectionData*)detectionData{
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"DBDetection"];
    NSError *error = nil;
    NSArray *results = [context executeFetchRequest:request error:&error];
    for(DBDetection* data in results){
        if ([data.body_part isEqualToString:[detectionData bodyPart]]
            &&[data.detect_date_time isEqualToString:
             [Util convertDateToString:[detectionData detectionTime] withFormat:FORMAT_DATE_TIME]]){
                data.transmission_status = TRANS_STATUS_TRANSMITTED; //[detectionData transmission_status];
        }
    }
    [Util save:context];
}

+(NSArray*)getDetectionList:(NSManagedObjectContext*)context{
    
    NSMutableArray* queryResult = [[NSMutableArray alloc]init];
    NSFetchRequest* request = [NSFetchRequest fetchRequestWithEntityName:@"DBDetection"];
    NSError* error = nil;
    NSSortDescriptor *sorter = [NSSortDescriptor sortDescriptorWithKey:@"detect_date_time" ascending:YES];
    request.sortDescriptors = [NSArray arrayWithObject:sorter];
    NSArray* results = [context executeFetchRequest:request error:&error];
    if (!results){
        NSLog(@"Query error");
    }else{
        for (DBDetection* data in results) {
            DetectionData* detectionData = [[DetectionData alloc]init];
            detectionData.bodyPart = data.body_part;
            detectionData.detectionTime = [Util convertStringToDate:data.detect_date_time withFormat:FORMAT_DATE_TIME];
            detectionData.value = data.value;
            detectionData.pre_post = data.nursing_status;
            detectionData.transmission_status = data.transmission_status;
            [queryResult addObject:detectionData]; 
        }
    }
    return queryResult ;
}

+(BOOL) isRecordExist:(NSManagedObjectContext*)context detectionData:(DetectionData*)detectionData{
    BOOL exist = NO;
    NSFetchRequest* request = [NSFetchRequest fetchRequestWithEntityName:@"DBDetection"];
    NSError* error = nil;
    NSSortDescriptor *sorter = [NSSortDescriptor sortDescriptorWithKey:@"detect_date_time" ascending:YES];
    request.sortDescriptors = [NSArray arrayWithObject:sorter];
    NSArray* results = [context executeFetchRequest:request error:&error];
    if (!results){
        NSLog(@"Query error");
    }else{
        for (DBDetection* data in results) {
            if ([data.detect_date_time isEqualToString:[Util convertDateToString:detectionData.detectionTime withFormat:FORMAT_DATE_TIME]]) {
                exist = YES;
            }
        }
    }
    return exist;
}


+(void)smartInsert:(NSManagedObjectContext*)context detectionData:(DetectionData*)detectionData{
    if (![self isRecordExist:context detectionData:detectionData]) {
        [self insert:context detectionData:detectionData];
    }
}

+(void)clean:(NSManagedObjectContext*)context{
    NSFetchRequest *request = [[NSFetchRequest alloc]initWithEntityName:@"DBDetection"];
    NSError *error;
    NSArray *objects = [context executeFetchRequest:request error:&error];
    if (objects != nil) {
        for (NSManagedObject *object in objects) {
            [context deleteObject:object];
        }
        [Util save:context];
    }
}

+(void)print:(NSManagedObjectContext*)context{
    NSLog(@"======= Table Detection ========");
    NSFetchRequest* request = [NSFetchRequest fetchRequestWithEntityName:@"DBDetection"];
    NSError* error = nil;
    NSArray* results = [context executeFetchRequest:request error:&error];
    if (!results){
        NSLog(@"EMPTY TABLE!!!!!!");
    }else{
        NSLog(@"%@\t%@\t%@\t%@\t%@\t",@"body_part",@"detect_date_time",@"nursing_status",@"value",@"transmission_status");
        for (DBDetection* data in results) {
            NSLog(@"%@\t%@\t%@\t%@\t%@\t",data.body_part,data.detect_date_time,data.nursing_status,data.value,data.transmission_status);
        }
    }
}


@end
