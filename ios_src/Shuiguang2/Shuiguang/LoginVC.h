//
//  LoginVC.h
//  Shuiguang
//
//  Created by dehualai on 3/3/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BasicVC.h"

@import Firebase;
@import GoogleSignIn;

@interface LoginVC : BasicVC <GIDSignInUIDelegate>

@end
