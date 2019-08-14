//
//  TransmissionUtil.h
//  Shuiguang
//
//  Created by dehualai on 3/25/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface TransmissionUtil : NSObject

+(void)syncAll:(NSManagedObjectContext*)context;
+(void)syncDetection:(NSManagedObjectContext*)context;
+(void)syncUserInfo:(NSManagedObjectContext*)context;
+(void)syncUserAWS:(NSManagedObjectContext*)context; 
+(void)syncDetectionAWS:(NSManagedObjectContext*)context;


@end
