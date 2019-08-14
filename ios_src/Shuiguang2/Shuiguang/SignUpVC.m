//
//  SignUpVC.m
//  Shuiguang
//
//  Created by dehualai on 3/4/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "SignUpVC.h"
#import "NJButton.h"
#import "AuthsTask.h"
#import "MBProgressHUD.h"
#import "UIContraints.h"
#import "Util.h"
#import "USER.h"
#import "UserData.h"
#import "DBUser+Operation.h"
#import "StoryBoardUtilities.h"
#import "ViewController.h"
#import "Global.h"
#import "AppDelegate.h"

#define NO_ERROR 9999
#define ERROR_FIELD_EMPTY 5000
#define ERROR_INVALID_EMAIL_FORMAT 5001
#define ERROR_USERNAME_EXIST 5002
#define ERROR_EMAIL_EXIST 5003
#define ERROR_PASSWORD_NOT_MATCH 5004

@interface SignUpVC () 


@property (weak, nonatomic) IBOutlet UITextField *usernameField;
@property (weak, nonatomic) IBOutlet UITextField *emailField;

@property (weak, nonatomic) IBOutlet UIView *usernameLayout;
@property (weak, nonatomic) IBOutlet UIView *emailLayout;
@property (weak, nonatomic) IBOutlet NJButton *startUsingAppButton;

@property (weak, nonatomic) IBOutlet UIButton *cancelButton;
@property (strong, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UIView *fieldView1;
@property (weak, nonatomic) IBOutlet UIView *fieldView2;

@property (weak, nonatomic) IBOutlet UILabel *errorMessageView;

@property (weak, nonatomic) IBOutlet UILabel *basicInfoLabel;


@end

@implementation SignUpVC

- (void)viewDidLoad {
    [super viewDidLoad];
}

#pragma mark -- override
-(void)dismissKeyboard {
    [self.usernameField resignFirstResponder];
    [self.emailField resignFirstResponder];
}


#pragma mark -- override
-(void)updateSubview{
    
    [UIUtil setGradientColor:self.fieldView1 startColor:TAP_BAR_COLOR endColor:TAP_BAR_COLOR angle:90];
    [UIUtil setGradientColor:self.fieldView2 startColor:TAP_BAR_COLOR endColor:TAP_BAR_COLOR angle:90];
    [UIUtil setGradientColor:self.bgView startColor:BACKGROUND_COLOR endColor:BACKGROUND_COLOR angle:90];
    [self.startUsingAppButton setTheme:NJBUTTON_THEME_DEFAULT];
}

#pragma mark -- override
-(void)multiLanguage{
    [self.basicInfoLabel setText:NSLocalizedString(@"Please provide some basic information", nil)]; 
    [self.usernameField setPlaceholder:NSLocalizedString(@"username", nil)];
    [self.emailField setPlaceholder:NSLocalizedString(@"email", nil)];
    [self.startUsingAppButton setTitle:NSLocalizedString(@"Start Using App", nil) forState:UIControlStateNormal];
    [self.cancelButton setTitle:NSLocalizedString(@"Back to the login screen", nil) forState:UIControlStateNormal];
    

}



- (IBAction)onStartUsingAppButtonPressed:(id)sender {
    
    switch ([self verifyInputFields]) {
        case ERROR_FIELD_EMPTY:
        {
            self.errorMessageView.text = NSLocalizedString(@"please fill all information", nil) ;
        }
            break;
        case ERROR_INVALID_EMAIL_FORMAT:
        {
            self.errorMessageView.text = NSLocalizedString(@"invalid email format", nil);
        }
            break;
        case NO_ERROR:
        {
            NSString* username = self.usernameField.text;
            username = [[username stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
            NSString* email = self.emailField.text;
            email = [[email stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
            
            UserData* userData = [[UserData alloc] initWithUsername:username
                                                            user_id:[NSString stringWithFormat:@"%@%@",PREFIX_UNSIGN_IN, [Util generateRandomString:16]]];
            userData.email = email;
            [DBUser smartInsert:self.context user:userData];
            
            AppDelegate *app = (AppDelegate*) [[UIApplication sharedApplication] delegate];
            [app goToSwitch];
        }
            break;
        default:
            break;
    }
}

- (IBAction)onCancelButtonPressed:(id)sender {
    [super goBack];
}



-(int)verifyInputFields{
    // clean hight light first
    self.usernameLayout.backgroundColor = TRANSPARENT;
    self.emailLayout.backgroundColor = TRANSPARENT;
  
    // get content
    NSString* username = self.usernameField.text;
    username = [[username stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
    NSString* email = self.emailField.text;
    email = [[email stringByReplacingOccurrencesOfString:@" " withString:@""] lowercaseString];
    
    
    if (username==nil || [username isEqualToString:@""] ||
        email==nil || [email isEqualToString:@""]
        ) {
        if(username==nil || [username isEqualToString:@""]) self.usernameLayout.backgroundColor = RED;
        if(email==nil || [email isEqualToString:@""]) self.emailLayout.backgroundColor = RED;
       
        return ERROR_FIELD_EMPTY;
    }
    if(![Util validateEmailFormat:email]){
        self.emailLayout.backgroundColor = RED;
        return ERROR_INVALID_EMAIL_FORMAT;
    }
    return NO_ERROR;

}




@end
