//
//  AuthsTask.m
//  Shuiguang
//
//  Created by dehualai on 3/1/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "AuthsTask.h"
#import <BmobSDK/BmobUser.h>

@implementation AuthsTask


// Override 
-(void)sync:(NSDictionary*)params{

    NSNumber* task = (NSNumber*) [params objectForKey:PARAM_TASK];
    switch (task.intValue) {
        case TASK_REGISTER:
            [self authRegister:params];
            break;
        case TASK_LOGIN:
            [self authLogin:params];
            break;
        default:
            break;
    }
}



-(void)authRegister:(NSDictionary*)params{
    NSNumber* type = (NSNumber*) [params objectForKey:PARAM_AUTH_TYPE];
    switch (type.intValue) {
        case TYPE_PWD:
        {
            NSString* username = (NSString*) [params objectForKey:PARAM_USERNAME];
            NSString* email = (NSString*) [params objectForKey:PARAM_EMAIL];
            NSString* password = (NSString*) [params objectForKey:PARAM_PASSWORD];
            username = [[username stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
            email = [[email stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
            password = [password stringByReplacingOccurrencesOfString:@" " withString:@""];
            BmobUser *bUser = [[BmobUser alloc] init];
            [bUser setUsername:username];
            [bUser setEmail:email];
            [bUser setPassword:password];
            [bUser signUpInBackgroundWithBlock:^ (BOOL isSuccessful, NSError *error){
                if (isSuccessful){
                    NSLog(@"Sign up successfully");
                    if (self.handler) [self.handler onSuccess:bUser];
                } else {
                    NSLog(@"%@",error);
                    if (self.handler) [self.handler onFailure:error.domain errorCode:@(error.code)];
                }
            }];
        }
        break;
        case TYPE_WECHAT:
        {
        
        }
        break;
        default:
            break;
    }

}


-(void)authLogin:(NSDictionary*)params{
    NSNumber* type = (NSNumber*) [params objectForKey:PARAM_AUTH_TYPE];
    switch (type.intValue) {
        case TYPE_PWD:
        {
            NSString* username = (NSString*) [params objectForKey:PARAM_USERNAME];
            NSString* password = (NSString*) [params objectForKey:PARAM_PASSWORD];
            username = [[username stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
            password = [password stringByReplacingOccurrencesOfString:@" " withString:@""];
            [BmobUser loginInbackgroundWithAccount:username andPassword:password block:^(BmobUser *user, NSError *error) {
                if (user) {
                    NSLog(@"SUCCESS %@",user);
                    if (self.handler) [self.handler onSuccess:user];
                } else {
                    NSLog(@"FAILURE %@",error);
                    if (self.handler) [self.handler onFailure:error.domain errorCode:@(error.code)];
                }
            }];
        }
        break;
        case TYPE_WECHAT:
        {
        
        }
        break;
        default:
            break;
    }
}


@end
