//
//  Global.h
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>

#define PREFIX_WECHAT @"W_"
#define PREFIX_GOOGLE @"G_"
#define PREFIX_FACEBOOK @"F_"
#define PREFIX_UNSIGN_IN @"R_"


#define TRANS_STATUS_INSERTED @"inserted"
#define TRANS_STATUS_UPDATED @"updated"
#define TRANS_STATUS_TRANSMITTED @"transmitted"

#define SIGN_IN_MODE_WECHAT 1
#define SIGN_IN_MODE_GOOGLE 2
#define SIGN_IN_MODE_FACEBOOK 3

@interface Global : NSObject

@property BOOL DEMO_MODE;
@property int signInMode;

+(id) getInstance;



@end
