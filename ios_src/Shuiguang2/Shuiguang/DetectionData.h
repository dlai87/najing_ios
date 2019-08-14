//
//  DetectionData.h
//  Shuiguang
//
//  Created by dehualai on 2/26/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>

#define HAND @"hand"
#define FACE @"face"
#define EYES @"eyes"
#define PRE_NURSING @"pre_nursing"
#define POST_NURSING @"post_nursing"

@interface DetectionData : NSObject
// required
@property (nonatomic, strong) NSString* bodyPart;
@property (nonatomic, strong) NSDate* detectionTime;
@property (nonatomic, strong) NSNumber* value;
@property (nonatomic, strong) NSString* pre_post;
// optional
@property (nonatomic, strong) NSString* city;
@property (nonatomic, strong) NSString* transmission_status;

-(id)initWithBodyPart:(NSString*)bodyPart detectionTime:(NSDate*)detectionTime value:(NSNumber*)value prePost:(NSString*)prePost; 

@end
