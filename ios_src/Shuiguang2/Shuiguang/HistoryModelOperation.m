//
//  HistoryModelOperation.m
//  Shuiguang
//
//  Created by dehualai on 3/22/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "HistoryModelOperation.h"
#import "DBDetection+Operation.h"
#import "Util.h"

#define TIME_RANGE_LAST_WEEK 1
#define TIME_RANGE_LAST_TWO_WEEKS 2
#define TIME_RANGE_LAST_MONTH 3
#define TIME_RANGE_ALL 4


@interface HistoryModelOperation()

@property int TIME_RANGE;
@property int targetDays;

@end

@implementation HistoryModelOperation


-(id)init{
    self = [super init];
    self.TIME_RANGE = TIME_RANGE_LAST_TWO_WEEKS;
    self.dataLabels = [[NSMutableArray alloc]init];
    return self;
}

-(NSDate*)startTime{
    NSDate *date = nil;
    
    switch (self.TIME_RANGE) {
            case TIME_RANGE_LAST_WEEK:{
                self.targetDays = 7;
                date = [[NSCalendar currentCalendar] dateByAddingUnit:NSCalendarUnitDay
                                                                value:-7
                                                               toDate:[NSDate date]
                                                              options:0];
            }
            break;
            case TIME_RANGE_LAST_TWO_WEEKS:{
                self.targetDays = 14;
                date = [[NSCalendar currentCalendar] dateByAddingUnit:NSCalendarUnitDay
                                                                value:-14
                                                               toDate:[NSDate date]
                                                              options:0];
                
            }
            break;
            case TIME_RANGE_LAST_MONTH:{
                self.targetDays = 30;
                date = [[NSCalendar currentCalendar] dateByAddingUnit:NSCalendarUnitDay
                                                                value:-30
                                                               toDate:[NSDate date]
                                                              options:0];
                
            }
            break;
            case TIME_RANGE_ALL:{
                self.targetDays = -1;
                date = [[NSCalendar currentCalendar] dateByAddingUnit:NSCalendarUnitYear
                                                                value:-100
                                                               toDate:[NSDate date]
                                                              options:0];
                
            }
            break;
        default:
            break;
            
    }
    return date;
}



-(NSArray*)getRawData:(NSManagedObjectContext*)context{
    NSArray* rawDataList = [DBDetection getDetectionList:context];
    NSMutableArray* filterList = [[NSMutableArray alloc]init];
    for(DetectionData* eachData in rawDataList){
        NSDate* date1 = [eachData detectionTime];
        NSDate* date2 = [self startTime]; 
        if ([[eachData detectionTime] compare:[self startTime]] != NSOrderedAscending ) {
            [filterList addObject:eachData];
        }
    }
    return filterList;
}

-(NSArray*)fillDummyIfNoEnoughData:(int)desiredLength currentLength:(int)currentLength data:(NSArray*)data{
    if (currentLength >= desiredLength) {
        return data;
    }
    NSMutableArray* newData = [[NSMutableArray alloc]init];
    for(int i = 0 ; i < desiredLength; i++){
        [newData addObject:@-1];
    }
    
    for (int i=0; i<[data count]; i++) {
        int index = currentLength-1-i;
        NSNumber* temp = [data objectAtIndex: index];
        newData[desiredLength-1-i] = temp;
    }
    
    for(int i = 0 ; i < [newData count]; i ++){
        if ([[newData objectAtIndex:i] intValue] < 0 || ![newData objectAtIndex:i]) {
            newData[i] = @0;
        }else{
            break;
        }
    }
    return newData;
}

-(NSDate*) firstDateOf:(NSArray*)list desiredTag:(NSString*)desiredTag{
    for(int i =0 ; i < [list count]; i++){
        DetectionData* data = [list objectAtIndex:i];
        if ([[data pre_post] isEqualToString:desiredTag]) {
            return [data detectionTime];
        }
    }
    return nil;
}


-(int)getTotalDays:(NSArray*)list desiredTag:(NSString*)desiredTag{
    NSDate* date = [self firstDateOf:list desiredTag:desiredTag];
    if (date!=nil) {
        return [Util countDaysDifferent:date toDate:[[NSDate alloc]init]];
    }
    return 0;
}





/**
 *
 *  Get data by Date
 *
 *  targetBodyPart : what part you looking for.  if null, means gets all parts
 *
 * */

-(NSDictionary*)getDataByDate:(NSManagedObjectContext*)context targetBodyPart:(NSString*)targetBodyPart{

    NSDate* targetFirstDate = [Util convertStringToDate:[Util getDate:[[NSDate alloc]init] offset:-self.targetDays] withFormat:FORMAT_DATE];
    // create labels
    self.dataLabels = [[NSMutableArray alloc]init];
    for(int i=0; i < self.targetDays; i ++){
        NSString* date = [Util getDate:targetFirstDate offset:i];
        if(i%2==0){
            self.dataLabels[i] = [Util convertDateToString:[Util convertStringToDate:date withFormat:FORMAT_DATE] withFormat:@"MM/dd"];
        }else{
            self.dataLabels[i] = @"";
        }
    }

    
    NSArray* rawDataList = [self getRawData:context];
    if (rawDataList && [rawDataList count] > 0 ) {
        NSDate* firstDatePre = [self firstDateOf:rawDataList desiredTag:PRE_NURSING];
        NSDate* firstDatePost = [self firstDateOf:rawDataList desiredTag:POST_NURSING];
        int totalDaysForPre = [self getTotalDays:rawDataList desiredTag:PRE_NURSING];
        int totalDaysForPost = [self getTotalDays:rawDataList desiredTag:POST_NURSING];
        
        
        NSMutableArray* preNursingRecord = [[NSMutableArray alloc]init];
        NSMutableArray* postNursingRecord = [[NSMutableArray alloc]init];
        for(int i = 0 ; i < totalDaysForPre; i++){
            [preNursingRecord addObject:@0];
        }
        for(int i = 0 ; i < totalDaysForPost; i++){
            [postNursingRecord addObject:@0];
        }
        
        for(int i =0; i < totalDaysForPre; i ++){
            // create data
            int preNursingSum = 0 ;
            int numPre = 0 ;
            NSString* date = [Util getDate:firstDatePre offset:i];
            for (DetectionData* eachData in rawDataList) {
                NSDate* tempDate = [eachData detectionTime];
                NSString* dateOnlyStr = [Util convertDateToString:tempDate withFormat:FORMAT_DATE];
                if([dateOnlyStr isEqualToString:date]){
                    if([PRE_NURSING isEqualToString:eachData.pre_post]){
                        if(targetBodyPart == nil || [targetBodyPart isEqualToString:eachData.bodyPart]){
                            preNursingSum += eachData.value.intValue;
                            numPre ++;
                        }
                    }
                }
            }
            if (numPre!=0) {
                preNursingRecord[i] = @(preNursingSum*1.0/numPre);
            }else{
                preNursingRecord[i] = @(0);
            }
        }
        
        // if value == 0 , use previous record , therefore avoid sudden drop on graphic
        for (int i=1 ; i < totalDaysForPre; i++) {
            if([[preNursingRecord objectAtIndex:i] intValue] == 0 ){
                preNursingRecord[i] = [preNursingRecord objectAtIndex:i-1];
            }
        }
        
        for(int i =0; i < totalDaysForPost; i ++){
            // create data
            int postNursingSum = 0 ;
            int numPost = 0 ;
            NSString* date = [Util getDate:firstDatePost offset:i];
            for (DetectionData* eachData in rawDataList) {
                NSDate* tempDate = [eachData detectionTime];
                NSString* dateOnlyStr = [Util convertDateToString:tempDate withFormat:FORMAT_DATE];
                if([dateOnlyStr isEqualToString:date]){
                    if([POST_NURSING isEqualToString:eachData.pre_post]){
                        if(targetBodyPart == nil || [targetBodyPart isEqualToString:eachData.bodyPart]){
                            postNursingSum += eachData.value.intValue;
                            numPost ++;
                        }
                    }
                }
            }
            if (numPost!=0) {
                postNursingRecord[i] = @(postNursingSum*1.0/numPost);
            }else{
                postNursingRecord[i] = @(0);
            }
        }
        
        // if value == 0 , use previous record , therefore avoid sudden drop on graphic
        for (int i=1 ; i < totalDaysForPost; i++) {
            if([[postNursingRecord objectAtIndex:i] intValue] == 0 ){
                postNursingRecord[i] = [postNursingRecord objectAtIndex:i-1];
            }
        }
        
        NSArray* preArr = [self fillDummyIfNoEnoughData:self.targetDays currentLength:totalDaysForPre data:preNursingRecord];
        NSArray* postArr = [self fillDummyIfNoEnoughData:self.targetDays currentLength:totalDaysForPost data:postNursingRecord];
        
   
        NSMutableDictionary* result = [[NSMutableDictionary alloc]init];
        [result setObject:preArr forKey:PRE_NURSING];
        [result setObject:postArr forKey:POST_NURSING];
        
        return result; 
        
    }
    return [self createEmptyRecord];
}


-(NSDictionary*)getDataByBodyPart:(NSManagedObjectContext*)context{
    NSArray* rawDataList = [self getRawData:context];
    NSMutableDictionary* record = [[NSMutableDictionary alloc]init];
    [record setObject:@0 forKey:HAND];
    [record setObject:@0 forKey:FACE];
    [record setObject:@0 forKey:EYES];
    
    for(DetectionData* eachData in rawDataList){
        if ([[eachData bodyPart]isEqualToString:HAND]) {
            NSNumber* curretVal = [record objectForKey:HAND];
            [record setObject:@(curretVal.intValue +1) forKey:HAND];
        }
        if ([[eachData bodyPart]isEqualToString:FACE]) {
            NSNumber* curretVal = [record objectForKey:FACE];
            [record setObject:@(curretVal.intValue +1) forKey:FACE];
        }
        if ([[eachData bodyPart]isEqualToString:EYES]) {
            NSNumber* curretVal = [record objectForKey:EYES];
            [record setObject:@(curretVal.intValue +1) forKey:EYES];
        }
    }
    return record;
}


-(NSDictionary*)createEmptyRecord{
    NSMutableArray* preNursingRecord = [[NSMutableArray alloc]init];
    NSMutableArray* postNursingRecord = [[NSMutableArray alloc]init];
    for(int i = 0 ; i < self.targetDays; i++){
        [preNursingRecord addObject:@0];
    }
    for(int i = 0 ; i < self.targetDays; i++){
        [postNursingRecord addObject:@0];
    }
    NSMutableDictionary* result = [[NSMutableDictionary alloc]init];
    [result setObject:preNursingRecord forKey:PRE_NURSING];
    [result setObject:postNursingRecord forKey:POST_NURSING];
    return result;
}

@end
