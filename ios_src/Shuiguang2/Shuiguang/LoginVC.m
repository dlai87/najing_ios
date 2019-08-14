//
//  LoginVC.m
//  Shuiguang
//
//  Created by dehualai on 3/3/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "LoginVC.h"
#import "NJButton.h"
#import "AuthsTask.h"
#import "MBProgressHUD.h"
#import "ForgetPasswordDialog.h"
#import "ButtonActionHandler.h"
#import "UIContraints.h"
#import "UserData.h"
#import "DBUser+Operation.h"
#import "AppDelegate.h"
#import "StoryBoardUtilities.h"
#import "ViewController.h"
#import "Util.h"
#import "TransmissionUtil.h"
#import "WXApi.h"
#import "AFNetworking.h"
#import "UIUtil.h"
#import <GoogleSignIn/GoogleSignIn.h>
#import "Global.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>



#define NO_ERROR 9999
#define ERROR_FIELD_EMPTY 5000

@interface LoginVC () <GIDSignInUIDelegate>


@property (strong, nonatomic) IBOutlet UIView *bgView;

@property (weak, nonatomic) IBOutlet UILabel *welcomeLabel;
@property (weak, nonatomic) IBOutlet UIButton *signUpButton;
@property (strong, nonatomic) FBSDKAccessToken* accessToken;

// @property (copy, nonatomic) void (^requestForUserInfoBlock)();

@property (weak, nonatomic) IBOutlet UIButton *googleLoginButton;
@property (weak, nonatomic) IBOutlet UIButton *loginWithWechat;
@property (weak, nonatomic) IBOutlet UIButton *loginWithGoogle;
@property (weak, nonatomic) IBOutlet UIButton *loginWithFacebook;





@end

@implementation LoginVC



- (void)viewDidLoad {
    [super viewDidLoad];
    
    [GIDSignIn sharedInstance].uiDelegate = self;

}

#pragma mark -- override
-(void)dismissKeyboard {
    
}

#pragma mark -- override
-(void)updateSubview{
    [UIUtil setGradientColor:self.bgView startColor:BACKGROUND_COLOR endColor:BACKGROUND_COLOR angle:90];
    [UIUtil setGradientColor:self.loginWithWechat startColor:BUTTON_COLOR endColor:BUTTON_COLOR angle:90];
    [UIUtil setGradientColor:self.loginWithGoogle startColor:BUTTON_COLOR endColor:BUTTON_COLOR angle:90];
    [UIUtil setGradientColor:self.loginWithFacebook startColor:BUTTON_COLOR endColor:BUTTON_COLOR angle:90];

    if (![WXApi isWXAppInstalled]){
        // if wechat not install , disable wechat login function
        self.loginWithWechat.hidden = YES;
    }else{
        self.loginWithWechat.hidden = NO; 
    }

}

#pragma mark -- override
-(void)multiLanguage{
    self.welcomeLabel.text = NSLocalizedString(@"Welcome", nil);
    [self.loginWithWechat setTitle:NSLocalizedString(@"Login with WeChat" , nil) forState:UIControlStateNormal];
    [self.loginWithGoogle setTitle:NSLocalizedString(@"Login with Google" , nil) forState:UIControlStateNormal];
    [self.loginWithFacebook setTitle:NSLocalizedString(@"Login with Facebook" , nil) forState:UIControlStateNormal];
    [self.signUpButton setTitle:NSLocalizedString(@"Use without Login" , nil) forState:UIControlStateNormal];
}


- (IBAction)googleSignInButtonPressed:(id)sender {
    [[Global getInstance] setSignInMode:SIGN_IN_MODE_GOOGLE] ; 
    [[GIDSignIn sharedInstance] signIn];
}


- (IBAction)facebookLoginPressed:(id)sender {
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    [login
     logInWithReadPermissions: @[@"public_profile", @"email"]
     fromViewController:self
     handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
         if (error) {
             NSLog(@"Process error");
         } else if (result.isCancelled) {
             NSLog(@"Cancelled");
         } else {
             NSLog(@"Logged in");
             if ([FBSDKAccessToken currentAccessToken]) {
                 [[[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:@{@"fields":@"first_name, last_name, email"}]
                  startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
                      if (!error) {
                          
                          if (result) {
                              NSLog(@"fetched user:%@", result);
                              NSString* firstname = [result objectForKey:@"first_name"];
                              NSString* lastname = [result objectForKey:@"last_name"];
                              NSString* fid = [result objectForKey:@"id"];
                              NSString* email = [result objectForKey:@"email"];
                              
                              UserData* userData = [[UserData alloc]initWithUsername:[NSString stringWithFormat:@"%@ %@",firstname, lastname]
                                                                             user_id:[NSString stringWithFormat:@"%@%@", PREFIX_FACEBOOK,fid]];
                              userData.email = email;
                              [DBUser smartInsert:self.context user:userData];
                              
                              AppDelegate *app = (AppDelegate*) [[UIApplication sharedApplication] delegate];
                              [app goToSwitch];
                          }
                          
                      }
                  }];
             }
         }
     }];
}



- (IBAction)weChatLogin:(id)sender {
    
    [[Global getInstance] setSignInMode:SIGN_IN_MODE_WECHAT] ;
    
    NSString *accessToken = [[NSUserDefaults standardUserDefaults] objectForKey:WX_ACCESS_TOKEN];
    NSString *openID = [[NSUserDefaults standardUserDefaults] objectForKey:WX_OPEN_ID];
    // 如果已经请求过微信授权登录，那么考虑用已经得到的access_token
    if (accessToken && openID) {
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        manager.responseSerializer.acceptableContentTypes = [NSSet setWithObject:@"text/plain"];
        NSString *refreshToken = [[NSUserDefaults standardUserDefaults] objectForKey:WX_REFRESH_TOKEN];
        NSString *refreshUrlStr = [NSString stringWithFormat:@"%@/oauth2/refresh_token?appid=%@&grant_type=refresh_token&refresh_token=%@", WX_BASE_URL, WECHAT_SHUIGUANG_APP_ID, refreshToken];
        [manager GET:refreshUrlStr parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
            NSLog(@"请求reAccess的response = %@", responseObject);
            NSDictionary *refreshDict = [NSDictionary dictionaryWithDictionary:responseObject];
            NSString *reAccessToken = [refreshDict objectForKey:WX_ACCESS_TOKEN];
            // 如果reAccessToken为空,说明reAccessToken也过期了,反之则没有过期
            if (reAccessToken) {
                // 更新access_token、refresh_token、open_id
                [[NSUserDefaults standardUserDefaults] setObject:reAccessToken forKey:WX_ACCESS_TOKEN];
                [[NSUserDefaults standardUserDefaults] setObject:[refreshDict objectForKey:WX_OPEN_ID] forKey:WX_OPEN_ID];
                [[NSUserDefaults standardUserDefaults] setObject:[refreshDict objectForKey:WX_REFRESH_TOKEN] forKey:WX_REFRESH_TOKEN];
                [[NSUserDefaults standardUserDefaults] synchronize];
                // 当存在reAccessToken不为空时直接执行AppDelegate中的wechatLoginByRequestForUserInfo方法
               // !self.requestForUserInfoBlock ? : self.requestForUserInfoBlock();
                AppDelegate *app = (AppDelegate*) [[UIApplication sharedApplication] delegate];
                [app wechatLoginByRequestForUserInfo];
            }
            else {
                [self wechatLogin];
            }
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"用refresh_token来更新accessToken时出错 = %@", error);
        }];
    }
    else {
        [self wechatLogin];
    }
    
}

- (void)wechatLogin {
    
    if ([WXApi isWXAppInstalled]) {
        SendAuthReq *req = [[SendAuthReq alloc] init];
        req.scope = @"snsapi_userinfo";
        req.state = @"GSTDoctorApp";

        [WXApi sendReq:req];
    }
    else {
        //[self setupAlertController];
    }
    
}


-(void)requestUserInfoByToken:(NSString *)token andOpenid:(NSString *)openID{
    
}

#pragma mark - 设置弹出提示语
- (void)setupAlertController {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"温馨提示" message:@"请先安装微信客户端" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *actionConfirm = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:nil];
    [alert addAction:actionConfirm];
    [self presentViewController:alert animated:YES completion:nil];
}


@end
