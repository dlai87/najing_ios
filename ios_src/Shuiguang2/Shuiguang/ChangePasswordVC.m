//
//  ChangePasswordVC.m
//  Shuiguang
//
//  Created by dehualai on 3/25/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "ChangePasswordVC.h"
#import "AlertDialog.h"
#import <BmobSDK/BmobUser.h>
#import "DBUser+Operation.h"



@interface ChangePasswordVC ()

@property (weak, nonatomic) IBOutlet UILabel *messageLabel;
@property (weak, nonatomic) IBOutlet UITextField *currentPasswordInput;
@property (weak, nonatomic) IBOutlet UITextField *passwordInput;
@property (weak, nonatomic) IBOutlet UITextField *confirmPasswordInput;

@property (weak, nonatomic) IBOutlet UIButton *cancelButton;
@property (weak, nonatomic) IBOutlet UIButton *okButton;

@end

@implementation ChangePasswordVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

#pragma mark -- override
-(void)dismissKeyboard {
    [self.currentPasswordInput resignFirstResponder];
    [self.passwordInput resignFirstResponder];
    [self.confirmPasswordInput resignFirstResponder];
}

#pragma mark -- override
-(void)updateSubview{
}

#pragma mark -- override
-(void)multiLanguage{
    self.messageLabel.text = NSLocalizedString(@"Change Password", nil);
    [self.currentPasswordInput setPlaceholder:NSLocalizedString(@"current password", nil)];
    [self.passwordInput setPlaceholder:NSLocalizedString(@"new password", nil)];
    [self.confirmPasswordInput setPlaceholder:NSLocalizedString(@"confirm new password", nil)];
    [self.cancelButton setTitle:NSLocalizedString(@"Cancel", nil) forState:UIControlStateNormal];
    [self.okButton setTitle:NSLocalizedString(@"OK", nil) forState:UIControlStateNormal];
    
    
}

- (IBAction)onCancelButtonPressed:(id)sender {
    [self goBack];
}

- (IBAction)onOkButtonPressed:(id)sender {
    [self goBack];
    NSString* currentPassword = self.currentPasswordInput.text;
    NSString* password = self.passwordInput.text;
    NSString* confirmPassword = self.confirmPasswordInput.text;
    
    
    if(currentPassword == nil || [currentPassword isEqualToString:@""]
       || password == nil || [password isEqualToString:@""]
       || confirmPassword == nil || [confirmPassword isEqualToString:@""]

       ){
        AlertDialog * dialog = [[AlertDialog alloc]init];
        [dialog setTitle:NSLocalizedString(@"Failure", nil)];
        [dialog setMessage:NSLocalizedString(@"Please try again", nil)];
        [dialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
        [dialog showDialogOnView:self];
        return;
    }
    
    if(![password isEqualToString:confirmPassword]){
        AlertDialog * dialog = [[AlertDialog alloc]init];
        [dialog setTitle:NSLocalizedString(@"Failure", nil)];
        [dialog setMessage:NSLocalizedString(@"The new passwords you put are NOT the same.", nil)];
        [dialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
        [dialog showDialogOnView:self];
        return;
    }
    
    NSArray* users = [DBUser getUserList:self.context];
    if ([users count] > 0 ){
        
        NSString* username = [[users firstObject]username];
        
        BmobUser *user = [BmobUser currentUser];
        [user updateCurrentUserPasswordWithOldPassword:currentPassword newPassword:password block:^(BOOL isSuccessful, NSError *error) {
            if (isSuccessful) {
                [BmobUser loginInbackgroundWithAccount:username andPassword:password block:^(BmobUser *user, NSError *error) {
                    if (error) {
                        AlertDialog * dialog = [[AlertDialog alloc]init];
                        [dialog setTitle:NSLocalizedString(@"Failure", nil)];
                        [dialog setMessage:NSLocalizedString(@"Please try again", nil)];
                        [dialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
                        [dialog showDialogOnView:self.rootVC];
                    } else {
                        AlertDialog * dialog = [[AlertDialog alloc]init];
                        [dialog setTitle:NSLocalizedString(@"Success", nil)];
                        [dialog setMessage:NSLocalizedString(@"You can start using your new password now.", nil)];
                        [dialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
                        [dialog showDialogOnView:self.rootVC];
                    }
                }];
            } else {
                NSLog(@"change password error:%@",error);
                AlertDialog * dialog = [[AlertDialog alloc]init];
                [dialog setTitle:NSLocalizedString(@"Failure", nil)];
                [dialog setMessage:NSLocalizedString(@"The current password you put is NOT correct. Please try again", nil)];
                [dialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
                [dialog showDialogOnView:self.rootVC];

            }
        }];
    }
    
    
    
}

@end
