//
//  PhoneDialog.m
//  Shuiguang
//
//  Created by dehualai on 3/14/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "PhoneDialog.h"
#import "UIContraints.h"
#import "Util.h"
#import "FCAlertView.h"
#import "AlertDialog.h"
#import "DBUser+Operation.h"
#import "DBDetection+Operation.h"
#import "AppDelegate.h"
#import "StoryBoardUtilities.h"
#import "ViewController.h"
#import "Util.h"

@interface PhoneDialog()

@property (nonatomic, strong) FCAlertView* dialog;
@property (nonatomic, strong) UIViewController* viewController;
@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* message;

@end


@implementation PhoneDialog

-(id)init{
    self = [super init];
    self.dialog = [[FCAlertView alloc]init];
    self.dialog.hideDoneButton = YES;
    self.dialog.dismissOnOutsideTouch = NO;
    self.dialog.titleColor = TEXT_GRAY_DARK;
    self.dialog.subTitleColor = TEXT_GRAY_DARK;
    self.dialog.colorScheme = THEME_MINOR_COLOR;
    self.dialog.cornerRadius = 16;
    [self.dialog addTextFieldWithPlaceholder:NSLocalizedString(@"Phone Number", nil) andTextReturnBlock:^(NSString *text) {

    }];
    
    
    self.message = NSLocalizedString(@"Please enter your phone number:", nil);
    
    [self.dialog addButton:NSLocalizedString(@"Cancel", nil) withActionBlock:^{
        
    }];
    
    [self.dialog addButton:NSLocalizedString(@"OK", nil) withActionBlock:^{
        
        NSString* phone = self.dialog.textField.text;
        phone = [[phone stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
        
        if ([Util validatePhoneNumber:phone]) {
            
            AppDelegate *app = (AppDelegate*) [[UIApplication sharedApplication] delegate];
            NSManagedObjectContext *context = app.managedObjectContext;
            
            
            NSArray* users = [DBUser getUserList:context];
            UserData* user = [users firstObject];
            user.phone = phone;
            [DBUser smartInsert:context user:user];
            
            if(self.handler) [self.handler button2Pressed];
        }else{
            AlertDialog* alertDialog = [[AlertDialog alloc]init];
            [alertDialog setMessage:[NSString stringWithFormat:NSLocalizedString(@"The input '%@' is NOT a valid phone number", nil), phone]];
            [alertDialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
            [alertDialog showDialogOnView:self.viewController];
        }
        
        

        
    }];
    
    return self;
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
