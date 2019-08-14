//
//  EmailDialog.h
//  Shuiguang
//
//  Created by dehualai on 3/14/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "ButtonActionHandler.h"


@interface EmailDialog : NSObject


@property id<ButtonActionHandler> handler;

-(id)init;
-(void)showDialogOnView:(UIViewController*)view;
-(void)dismiss;

@end
