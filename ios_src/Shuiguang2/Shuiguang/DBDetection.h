//
//  Detection.h
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <CoreData/CoreData.h>
#import "Global.h"

@interface DBDetection : NSManagedObject

@property (nonatomic, retain) NSString* body_part;
@property (nonatomic, retain) NSString* detect_date_time;
@property (nonatomic, retain) NSString* nursing_status;
@property (nonatomic, retain) NSNumber* value ;
@property (nonatomic, retain) NSString* transmission_status;


@end
