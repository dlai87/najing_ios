//
//  USERTask.m
//  aimm
//
//  Created by dehualai on 1/20/18.
//  Copyright © 2018 NanoMed. All rights reserved.
//

#import "USERTask.h"
#import <CoreData/CoreData.h>
#import "DBUser+Operation.h"
#import "AWSDynamoDBObjectMapper.h"
#import "USER.h"


@interface USERTask()
@property (nonatomic, strong) NSManagedObjectContext* context;
@end



@implementation USERTask

-(id)initWithContext:(NSManagedObjectContext*)context{
    self = [super init];
    self.context = context;
    return self;
}


// Override
-(void)sync:(NSDictionary*)params{
    
    NSNumber* task = (NSNumber*) [params objectForKey:PARAM_TASK];
    switch (task.intValue) {
        case TASK_PUSH:
            [self push];
            break;
        default:
            break;
    }
}




-(void)push{
    NSArray* users = [DBUser getUserList:self.context];
    if ([users count] > 0 ){
        [DBUser print:self.context];
        NSString* transmissionStatus = [DBUser checkTransmissionStatus:self.context];
        if ([TRANS_STATUS_UPDATED isEqualToString:transmissionStatus]
            || [TRANS_STATUS_INSERTED isEqualToString:transmissionStatus]) {
            UserData* user = [users firstObject];
            
            AWSDynamoDBObjectMapper* dynamoDbObjectMapper = [AWSDynamoDBObjectMapper defaultDynamoDBObjectMapper];
            // Create data object using data models you downloaded from Mobile Hub
            
            USER* awsUser = [[USER alloc]init];
            awsUser._userId = [user user_id];
            awsUser._email = [user email];
            awsUser._phone = [user phone];
            awsUser._gender = [user gender];
            awsUser._dateOfBirth = [user date_of_birth];
            awsUser._wechat = [user wechat];
            awsUser._facebook = [user facebook];
            awsUser._google = [user google];
            awsUser._username = [user username];
            awsUser._platform = @"iOS";
            
            [[dynamoDbObjectMapper save:awsUser]
             continueWithBlock:^id(AWSTask *task) {
                 if (task.error) {
                     NSLog(@"The request failed. Error: [%@]", task.error);
                 } else {
                     //Do something with task.result or perform other operations.
                     NSLog(@"save user success");
                 }
                 return nil;
             }];
        }
        
    }
    
}




@end
