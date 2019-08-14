//
//  AlertDialog.h
//  Shuiguang
//
//  Created by dehualai on 3/5/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ButtonActionHandler.h"
#import <UIKit/UIKit.h>


@interface AlertDialog : NSObject

@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* message;

-(id)init;
-(void)setButtons:(NSString*)buttonTitle1 button2:(NSString*)buttonTitle2 handler:(id<ButtonActionHandler>)handler;
-(void)showDialogOnView:(UIViewController*)view;
-(void)dismiss;
@end
