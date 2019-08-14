//
//  UserInfoTask.m
//  Shuiguang
//
//  Created by dehualai on 3/25/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "UserInfoTask.h"
#import <CoreData/CoreData.h>
#import "DBUser+Operation.h"
#import "User.h"


@interface UserInfoTask()

@property (nonatomic, strong) NSManagedObjectContext* context;

@end

@implementation UserInfoTask

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
        if ([TRANS_STATUS_UPDATED isEqualToString:transmissionStatus]) {
            UserData* user = [users firstObject];
            
            /*
            User* bmobUser = [[User alloc]init] ;
            
            [bmobUser setObjectId:[user user_id]];
            [bmobUser setEmail:[user email]];
            [bmobUser setObject:[user phone] forKey:KEY_PHONE_NUMBER] ;
            [bmobUser setObject:[user date_of_birth] forKey:KEY_DATE_OF_BIRTH] ;
            [bmobUser setObject:[user gender] forKey:KEY_GENDER] ;
            [bmobUser setObject:[user skin_type] forKey:KEY_SKIN_TYPE] ;
            
            [bmobUser updateInBackgroundWithResultBlock:^(BOOL isSuccessful, NSError *error) {
                NSLog(@"update USER INFOR SUCCESS %d", isSuccessful);
                NSLog(@"error %@",[error description]);
                if(isSuccessful){
                    [DBUser updateTransmissionStatus:self.context status:TRANS_STATUS_TRANSMITTED];
                }
            }];
             
             */
        }
    
    }

}

@end
