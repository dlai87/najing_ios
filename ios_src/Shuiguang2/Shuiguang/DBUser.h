//
//  User.h
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <CoreData/CoreData.h>
#import "Global.h"


@interface DBUser : NSManagedObject

@property (nonatomic, retain) NSString* avatar;
@property (nonatomic, retain) NSString* date_of_birth;
@property (nonatomic, retain) NSString* email;
@property (nonatomic, retain) NSString* gender;
@property (nonatomic, retain) NSString* phone_number;
@property (nonatomic, retain) NSString* skin_type;
@property (nonatomic, retain) NSString* user_id;
@property (nonatomic, retain) NSString* username;
@property (nonatomic, retain) NSString* wechat;
@property (nonatomic, retain) NSString* facebook;
@property (nonatomic, retain) NSString* google;
@property (nonatomic, retain) NSString* transmission_status;



@end
