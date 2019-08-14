//
//  UserData.m
//  Shuiguang
//
//  Created by dehualai on 2/26/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "UserData.h"

@implementation UserData

-(id)initWithUsername:(NSString*)username user_id:(NSString*)user_id{

    self = [super init];
    self.username = username;
    self.user_id = user_id;
    
    return self;
}
@end
