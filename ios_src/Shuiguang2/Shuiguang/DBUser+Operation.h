//
//  User+Operation.h
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "DBUser.h"
#import "UserData.h"


@interface DBUser (Operation)

+(void)insert:(NSManagedObjectContext*)context user:(UserData*)user;
+(void)update:(NSManagedObjectContext*)context user:(UserData*)user;
+(NSString*)checkTransmissionStatus:(NSManagedObjectContext*)context;
+(void)updateTransmissionStatus:(NSManagedObjectContext*)context status:(NSString*)status;
+(NSArray*)getUserList:(NSManagedObjectContext*)context;
+(void)smartInsert:(NSManagedObjectContext*)context user:(UserData*)user;
+(void)clean:(NSManagedObjectContext*)context;
+(void)print:(NSManagedObjectContext*)context;


@end
