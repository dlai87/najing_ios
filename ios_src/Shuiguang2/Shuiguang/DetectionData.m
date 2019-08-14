//
//  DetectionData.m
//  Shuiguang
//
//  Created by dehualai on 2/26/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "DetectionData.h"


@implementation DetectionData

-(id)initWithBodyPart:(NSString*)bodyPart detectionTime:(NSDate*)detectionTime value:(NSNumber*)value prePost:(NSString*)prePost{

    self = [super init];
    self.bodyPart = bodyPart;
    self.detectionTime = detectionTime;
    self.value = value;
    self.pre_post = prePost; 
    
    return self;
}
@end
