//
//  AuthsTask.h
//  Shuiguang
//
//  Created by dehualai on 3/1/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SyncTask.h"

#define TASK_REGISTER 0
#define TASK_LOGIN 1
#define PARAM_AUTH_TYPE @"auth_type"
#define TYPE_PWD 0
#define TYPE_WECHAT 1
#define PARAM_EMAIL @"email"
#define PARAM_USERNAME @"username"
#define PARAM_PASSWORD @"password"

@interface AuthsTask : SyncTask



@end
