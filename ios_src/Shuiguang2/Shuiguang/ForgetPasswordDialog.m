//
//  ForgetPasswordDialog.m
//  Shuiguang
//
//  Created by dehualai on 3/4/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "ForgetPasswordDialog.h"
#import "UIContraints.h"
#import "Util.h"
#import "FCAlertView.h"
#import <BmobSDK/BmobUser.h>
#import "AlertDialog.h"

@interface ForgetPasswordDialog()

@property id<ButtonActionHandler> handler;
@property (nonatomic, strong) FCAlertView* dialog;
@property (nonatomic, strong) UIViewController* viewController;

@end

@implementation ForgetPasswordDialog

-(id)init{
    self = [super init];
    self.dialog = [[FCAlertView alloc]init];
    self.dialog.hideDoneButton = YES;
    self.dialog.dismissOnOutsideTouch = NO;
    self.dialog.titleColor = TEXT_GRAY_DARK;
    self.dialog.subTitleColor = TEXT_GRAY_DARK;
    self.dialog.colorScheme = THEME_MINOR_COLOR;
    self.dialog.cornerRadius = 16;

    
    [self.dialog addTextFieldWithPlaceholder:NSLocalizedString(@"Email", nil) andTextReturnBlock:^(NSString *text) {
        NSLog(@"The Email Address is: %@", text);
    }];
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
            NSString* email = self.dialog.textField.text;
            email = [[email stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
            NSLog(@"EMAIL %@", email);
            if ([Util validateEmailFormat:email]) {
                [BmobUser requestPasswordResetInBackgroundWithEmail:email];
                
                AlertDialog* alertDialog = [[AlertDialog alloc]init];
                [alertDialog setMessage:[NSString stringWithFormat:NSLocalizedString(@"We have send a link to '%@' , please check and reset your password.", nil), email]];
                [alertDialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
                [alertDialog showDialogOnView:self.viewController];
                
                if(handler) [handler button2Pressed];
            }else{
                AlertDialog* alertDialog = [[AlertDialog alloc]init];
                [alertDialog setMessage:[NSString stringWithFormat:NSLocalizedString(@"The input '%@' is NOT a valid email address", nil), email]];
                [alertDialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
                [alertDialog showDialogOnView:self.viewController];
            }
        }];
    }
    
}



-(void)showDialogOnView:(UIViewController*)view{
    self.viewController = view;
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
