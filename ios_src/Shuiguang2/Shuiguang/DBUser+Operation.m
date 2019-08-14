//
//  User+Operation.m
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "DBUser+Operation.h"
#import "Util.h"

@implementation DBUser (Operation)

+(void)insert:(NSManagedObjectContext*)context user:(UserData*)user{
    DBUser * temp = [NSEntityDescription insertNewObjectForEntityForName:@"DBUser" inManagedObjectContext:context];
    temp.username = user.username;
    temp.wechat = user.wechat;
    temp.facebook = user.facebook;
    temp.google = user.google;
    temp.user_id = user.user_id;
    temp.avatar = user.avatar;
    temp.gender = user.gender;
    temp.date_of_birth = user.date_of_birth;
    temp.phone_number = user.phone;
    temp.email = user.email;
    temp.skin_type = user.skin_type; 
    temp.transmission_status = TRANS_STATUS_INSERTED;
    [Util save:context];
}

+(void)update:(NSManagedObjectContext*)context user:(UserData*)user{
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"DBUser"];
    NSError *error = nil;
    NSArray *results = [context executeFetchRequest:request error:&error];
    for(DBUser* data in results){
        data.username = user.username;
        data.wechat = user.wechat;
        data.facebook = user.facebook;
        data.google = user.google;
        data.user_id = user.user_id;
        data.avatar = user.avatar;
        data.gender = user.gender;
        data.date_of_birth = user.date_of_birth;
        data.phone_number = user.phone;
        data.email = user.email;
        data.skin_type = user.skin_type;
        data.transmission_status = TRANS_STATUS_UPDATED;
        
    }
    [Util save:context];
}


+(NSString*)checkTransmissionStatus:(NSManagedObjectContext*)context{
    NSString* status = TRANS_STATUS_INSERTED;
    NSFetchRequest* request = [NSFetchRequest fetchRequestWithEntityName:@"DBUser"];
    NSError* error = nil;
    NSArray* results = [context executeFetchRequest:request error:&error];
    if (!results){
        NSLog(@"Query error checkTransmissionStatus");
    }else{
        DBUser* data = [results firstObject];
        status = data.transmission_status;
    }
    return status;
}


+(void)updateTransmissionStatus:(NSManagedObjectContext*)context status:(NSString*)status{
    NSFetchRequest* request = [NSFetchRequest fetchRequestWithEntityName:@"DBUser"];
    NSError* error = nil;
    NSArray* results = [context executeFetchRequest:request error:&error];
    if (!results){
        NSLog(@"Query error updateTransmissionStatus");
    }else{
        DBUser* data = [results firstObject];
        data.transmission_status = status;
    }
    [Util save:context];
}


+(NSArray*)getUserList:(NSManagedObjectContext*)context{
    NSMutableArray* queryResult = [[NSMutableArray alloc]init];
    NSFetchRequest* request = [NSFetchRequest fetchRequestWithEntityName:@"DBUser"];
    NSError* error = nil;
    NSArray* results = [context executeFetchRequest:request error:&error];
    if (!results){
        NSLog(@"Query error getUserList");
    }else{
        for (DBUser* data in results) {
            UserData* userData = [[UserData alloc]init];
            userData.user_id = data.user_id;
            userData.username = data.username;
            userData.wechat = data.wechat;
            userData.facebook = data.facebook;
            userData.google = data.google;
            userData.gender = data.gender;
            userData.date_of_birth = data.date_of_birth;
            userData.phone = data.phone_number;
            userData.email = data.email;
            userData.avatar = data.avatar;
            userData.skin_type = data.skin_type;
            [queryResult addObject:userData];
        }
    }
    return queryResult ;

}

+(void)smartInsert:(NSManagedObjectContext*)context user:(UserData*)user{
    NSArray* users = [self getUserList:context];
    BOOL userExist = NO;
    for (UserData* userData in users){
        if ([userData.username isEqualToString:user.username]) {
            userExist = YES;
        }
    }
    
    if (userExist) {
        [self update:context user:user];
    }else{
        [self insert:context user:user]; 
    }
    
}


+(void)clean:(NSManagedObjectContext*)context{
    NSFetchRequest *request = [[NSFetchRequest alloc]initWithEntityName:@"DBUser"];
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
    NSLog(@"======= Table DBUser ========");
    NSFetchRequest* request = [NSFetchRequest fetchRequestWithEntityName:@"DBUser"];
    NSError* error = nil;
    NSArray* results = [context executeFetchRequest:request error:&error];
    if (!results){
        NSLog(@"EMPTY TABLE!!!!!!");
    }else{
    NSLog(@"%@\t%@\t%@\t%@\t%@\t%@\t%@\t%@\t%@\t",@"user_id",@"username",@"gender",@"date_of_birth",@"phone_number",@"email",@"avatar",@"skin_type", @"trans");
        for (DBUser* data in results) {
            NSLog(@"%@\t%@\t%@\t%@\t%@\t%@\t%@\t%@\t%@",data.user_id,data.username,data.gender,data.date_of_birth,data.phone_number,data.email,data.avatar,data.skin_type, data.transmission_status);
        }
    }

}

@end
