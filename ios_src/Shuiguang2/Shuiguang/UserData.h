//
//  UserData.h
//  Shuiguang
//
//  Created by dehualai on 2/26/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface UserData : NSObject

// required
@property (nonatomic, strong) NSString* username;
@property (nonatomic, strong) NSString* user_id;
// optional
@property (nonatomic, strong) NSString* gender;
@property (nonatomic, strong) NSString* date_of_birth;
@property (nonatomic, strong) NSString* phone;
@property (nonatomic, strong) NSString* email;
@property (nonatomic, strong) NSString* avatar;
@property (nonatomic, strong) NSString* skin_type;
@property (nonatomic, strong) NSString* wechat;
@property (nonatomic, strong) NSString* facebook;
@property (nonatomic, strong) NSString* google;


-(id)initWithUsername:(NSString*)username user_id:(NSString*)user_id;



@end
