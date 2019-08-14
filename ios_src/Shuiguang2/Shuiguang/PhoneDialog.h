//
//  PhoneDialog.h
//  Shuiguang
//
//  Created by dehualai on 3/14/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ButtonActionHandler.h"
#import <UIKit/UIKit.h>


@interface PhoneDialog : NSObject
@property id<ButtonActionHandler> handler;

-(id)init;
-(void)showDialogOnView:(UIViewController*)view;
-(void)dismiss;
@end
