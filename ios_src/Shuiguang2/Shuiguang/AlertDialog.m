//
//  AlertDialog.m
//  Shuiguang
//
//  Created by dehualai on 3/5/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "AlertDialog.h"
#import "UIContraints.h"
#import "Util.h"
#import "FCAlertView.h"


@interface AlertDialog()

@property id<ButtonActionHandler> handler;
@property (nonatomic, strong) FCAlertView* dialog;

@end

@implementation AlertDialog



-(id)init{
    self = [super init];
    self.dialog = [[FCAlertView alloc]init];
    self.dialog.hideDoneButton = YES;
    self.dialog.dismissOnOutsideTouch = NO;
    self.dialog.titleColor = TEXT_GRAY_DARK;
    self.dialog.subTitleColor = TEXT_GRAY_DARK;
    self.dialog.colorScheme = THEME_MINOR_COLOR;
    self.dialog.cornerRadius = 16;
    
    return self;
}



-(void)setButtons:(NSString*)buttonTitle1 button2:(NSString*)buttonTitle2 handler:(id<ButtonActionHandler>)handler{
    if (buttonTitle1) {
        [self.dialog addButton:buttonTitle1 withActionBlock:^{
            if(handler) [handler button1Pressed];
        }];
    }
    if (buttonTitle2) {
        [self.dialog addButton:buttonTitle2 withActionBlock:^{
            if(handler) [handler button2Pressed];
        }];
    }
    
}



-(void)showDialogOnView:(UIViewController*)view{
    [self.dialog showAlertInView:view
                       withTitle:self.title
                    withSubtitle:self.message
                 withCustomImage:nil
             withDoneButtonTitle:nil
                      andButtons:nil];
}

-(void)dismiss{
    
}



@end
