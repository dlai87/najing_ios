//
//  LogoutDialog.m
//  Shuiguang
//
//  Created by dehualai on 3/14/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "LogoutDialog.h"
#import "UIContraints.h"
#import "Util.h"
#import "FCAlertView.h"
#import "AlertDialog.h"
#import "DBUser+Operation.h"
#import "DBDetection+Operation.h"
#import "AppDelegate.h"
#import "StoryBoardUtilities.h"
#import "ViewController.h"

@interface LogoutDialog()

@property id<ButtonActionHandler> handler;
@property (nonatomic, strong) FCAlertView* dialog;
@property (nonatomic, strong) UIViewController* viewController;
@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* message;

@end


@implementation LogoutDialog


-(id)init{
    self = [super init];
    self.dialog = [[FCAlertView alloc]init];
    self.dialog.hideDoneButton = YES;
    self.dialog.dismissOnOutsideTouch = NO;
    self.dialog.titleColor = TEXT_GRAY_DARK;
    self.dialog.subTitleColor = TEXT_GRAY_DARK;
    self.dialog.colorScheme = THEME_MINOR_COLOR;
    self.dialog.cornerRadius = 16;
    
    self.message = NSLocalizedString(@"Are you sure to logout?", nil);
    
    [self.dialog addButton:NSLocalizedString(@"Cancel", nil) withActionBlock:^{

    }];
    
    [self.dialog addButton:NSLocalizedString(@"OK", nil) withActionBlock:^{
        AppDelegate *app = (AppDelegate*) [[UIApplication sharedApplication] delegate];
        NSManagedObjectContext *context = app.managedObjectContext;
        
        [DBUser clean:context];
        [DBDetection clean:context];
        
        UIViewController* navigationController = (UIViewController*)[StoryBoardUtilities viewControllerForStoryboardName:@"Main" class:[ViewController class]];
        [self.viewController.navigationController pushViewController:navigationController animated:NO];
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
