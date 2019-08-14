//
//  Detection+Operation.h
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "DBDetection.h"
#import "DetectionData.h"

@interface DBDetection (Operation)

+(void)updateTransmissionStatus:(NSManagedObjectContext*)context detectionData:(DetectionData*)detectionData;
+(NSArray*)getDetectionList:(NSManagedObjectContext*)context;
+(void)smartInsert:(NSManagedObjectContext*)context detectionData:(DetectionData*)detectionData;
+(void)clean:(NSManagedObjectContext*)context;
+(void)print:(NSManagedObjectContext*)context;


@end
