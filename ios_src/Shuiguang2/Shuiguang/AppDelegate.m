//
//  AppDelegate.m
//  Shuiguang
//
//  Created by dehualai on 2/23/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "AppDelegate.h"
#import "WXApi.h"
#import "UIContraints.h"
#import "AFNetworking.h"
#import "ViewController.h"
#import "UserData.h"
#import "DBUser+Operation.h"
#import <GoogleSignIn/GoogleSignIn.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>



// [START auth_import]


@interface AppDelegate () <WXApiDelegate, GIDSignInDelegate>

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    [WXApi startLogByLevel:WXLogLevelDetail logBlock:^(NSString *log) {
        NSLog(@"log : %@", log);
    }];
    //向微信注册
    [WXApi registerApp:WECHAT_SHUIGUANG_APP_ID  enableMTA:YES];
    
    
    // Google Sign In
    [GIDSignIn sharedInstance].clientID = GOOGLE_CLIENT_ID;
    [GIDSignIn sharedInstance].delegate = self;
    
    // facebook sign in
    [[FBSDKApplicationDelegate sharedInstance] application:application
                             didFinishLaunchingWithOptions:launchOptions];
    
    return YES;
}

/*
- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
    [WXApi handleOpenURL:url delegate:self];
    return YES;
}
*/


- (void)dealloc  
{  
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"wechatDidLoginNotification" object:nil];  
}


- (void)onResp:(BaseResp *)resp {
    NSLog(@"resp %d",resp.errCode);
    
    // 向微信请求授权后,得到响应结果
    if ([resp isKindOfClass:[SendAuthResp class]]) {
        SendAuthResp *temp = (SendAuthResp *)resp;
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        manager.responseSerializer.acceptableContentTypes = [NSSet setWithObject:@"text/plain"];
        NSString *accessUrlStr = [NSString stringWithFormat:@"%@/oauth2/access_token?appid=%@&secret=%@&code=%@&grant_type=authorization_code", WX_BASE_URL, WECHAT_SHUIGUANG_APP_ID, WECHAT_SHUIGUANG_APP_SECRET, temp.code];
        [manager GET:accessUrlStr parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
            NSLog(@"请求access的response = %@", responseObject);
            NSDictionary *accessDict = [NSDictionary dictionaryWithDictionary:responseObject];
            NSString *accessToken = [accessDict objectForKey:WX_ACCESS_TOKEN];
            NSString *openID = [accessDict objectForKey:WX_OPEN_ID];
            NSString *refreshToken = [accessDict objectForKey:WX_REFRESH_TOKEN];
            // 本地持久化，以便access_token的使用、刷新或者持续
            if (accessToken && ![accessToken isEqualToString:@""] && openID && ![openID isEqualToString:@""]) {
                [[NSUserDefaults standardUserDefaults] setObject:accessToken forKey:WX_ACCESS_TOKEN];
                [[NSUserDefaults standardUserDefaults] setObject:openID forKey:WX_OPEN_ID];
                [[NSUserDefaults standardUserDefaults] setObject:refreshToken forKey:WX_REFRESH_TOKEN];
                [[NSUserDefaults standardUserDefaults] synchronize]; // 命令直接同步到文件里，来避免数据的丢失
            }
            [self wechatLoginByRequestForUserInfo];
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"获取access_token时出错 = %@", error);
        }];
    }
}



- (void)wechatLoginByRequestForUserInfo {
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObject:@"text/plain"];
    NSString *accessToken = [[NSUserDefaults standardUserDefaults] objectForKey:WX_ACCESS_TOKEN];
    NSString *openID = [[NSUserDefaults standardUserDefaults] objectForKey:WX_OPEN_ID];
    NSString *userUrlStr = [NSString stringWithFormat:@"%@/userinfo?access_token=%@&openid=%@", WX_BASE_URL, accessToken, openID];
    // 请求用户数据
    [manager GET:userUrlStr parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"请求用户信息的response = %@", responseObject);
        NSDictionary* userDict = [NSDictionary dictionaryWithDictionary:responseObject];
        NSString* city = [userDict objectForKey:@"city"];
        NSString* country = [userDict objectForKey:@"country"];
        NSString* headimgurl = [userDict objectForKey:@"headimgurl"];
        NSString* nickname = [userDict objectForKey:@"nickname"];
        NSString* sex = [userDict objectForKey:@"sex"];
        NSString* unionid = [userDict objectForKey:@"unionid"];
        
        NSLog(@"WeChat Login User name %@", nickname); 
        
        if (unionid) {
            UserData* userData = [[UserData alloc]initWithUsername:nickname
                                                           user_id:[NSString stringWithFormat:@"%@%@", PREFIX_WECHAT,unionid]];
            [DBUser smartInsert:self.managedObjectContext user:userData];
        }
       
        
        [self goToSwitch];
        // NSMutableDictionary *userDict = [NSMutableDictionary dictionaryWithDictionary:responseObject];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"获取用户信息时出错 = %@", error);
    }];
}


-(void)goToSwitch{
    
    ViewController *VC = [[ViewController alloc] init];
    [(UINavigationController *)self.window.rootViewController pushViewController:VC animated:YES];
}



/// sign in open url

- (BOOL)application:(nonnull UIApplication *)application
            openURL:(nonnull NSURL *)url
            options:(nonnull NSDictionary<NSString *, id> *)options {
    
    switch ([[Global getInstance] signInMode]) {
        case SIGN_IN_MODE_WECHAT:
            {
                [WXApi handleOpenURL:url delegate:self];
            }
            break;
        case SIGN_IN_MODE_GOOGLE:
            {
            [[GIDSignIn sharedInstance] handleURL:url
                                sourceApplication:options[UIApplicationOpenURLOptionsSourceApplicationKey]
                                       annotation:options[UIApplicationOpenURLOptionsAnnotationKey]];
            }
            break;
        case SIGN_IN_MODE_FACEBOOK:
            {
                [[FBSDKApplicationDelegate sharedInstance] application:application
                                                               openURL:url
                                                     sourceApplication:options[UIApplicationOpenURLOptionsSourceApplicationKey]
                                                            annotation:options[UIApplicationOpenURLOptionsAnnotationKey]
                 ];
            }
            break;
            
        default:
            break;
    }

    return YES;
}




- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
    
    switch ([[Global getInstance] signInMode]) {
        case SIGN_IN_MODE_WECHAT:
        {
            [WXApi handleOpenURL:url delegate:self];
        }
            break;
        case SIGN_IN_MODE_GOOGLE:
        {
            [[GIDSignIn sharedInstance] handleURL:url
                                sourceApplication:sourceApplication
                                       annotation:annotation];
        }
            break;
        case SIGN_IN_MODE_FACEBOOK:
        {
            [[FBSDKApplicationDelegate sharedInstance] application:application
                                                           openURL:url
                                                 sourceApplication:sourceApplication
                                                        annotation:annotation];
        }
            break;
            
        default:
            break;
    }
    
    return YES;
    
}


- (void)signIn:(GIDSignIn *)signIn
didSignInForUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
    // Perform any operations on signed in user here.
    NSString *userId = user.userID;                  // For client-side use only!
    NSString *idToken = user.authentication.idToken; // Safe to send to the server
    NSString *fullName = user.profile.name;
    NSString *givenName = user.profile.givenName;
    NSString *familyName = user.profile.familyName;
    NSString *email = user.profile.email;
    // ...
    if(userId){
        UserData* userData = [[UserData alloc]initWithUsername:fullName
                                                       user_id:[NSString stringWithFormat:@"%@%@", PREFIX_GOOGLE,userId]];
        [DBUser smartInsert:self.managedObjectContext user:userData];
        NSLog(@"Google Sign in %@", fullName);
    }
        
    
    [self goToSwitch];
}

- (void)signIn:(GIDSignIn *)signIn
didDisconnectWithUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
    // Perform any operations when the user disconnects from app here.
    // ...
}
 


- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    
    [FBSDKAppEvents activateApp];
}


- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    // Saves changes in the application's managed object context before the application terminates.
    [self saveContext];
}


#pragma mark - Core Data stack

@synthesize managedObjectContext = _managedObjectContext;
@synthesize managedObjectModel = _managedObjectModel;
@synthesize persistentStoreCoordinator = _persistentStoreCoordinator;

- (NSURL *)applicationDocumentsDirectory {
    // The directory the application uses to store the Core Data store file. This code uses a directory named "com.dehua.MyTix" in the application's documents directory.
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

- (NSManagedObjectModel *)managedObjectModel {
    // The managed object model for the application. It is a fatal error for the application not to be able to find and load its model.
    if (_managedObjectModel != nil) {
        return _managedObjectModel;
    }
    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"Shuiguang" withExtension:@"momd"];
    _managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    return _managedObjectModel;
}

- (NSPersistentStoreCoordinator *)persistentStoreCoordinator {
    // The persistent store coordinator for the application. This implementation creates and return a coordinator, having added the store for the application to it.
    if (_persistentStoreCoordinator != nil) {
        return _persistentStoreCoordinator;
    }
    
    // Create the coordinator and store
    
    _persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:@"Shuiguang.sqlite"];
    NSError *error = nil;
    NSString *failureReason = @"There was an error creating or loading the application's saved data.";
    if (![_persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error]) {
        // Report any error we got.
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        dict[NSLocalizedDescriptionKey] = @"Failed to initialize the application's saved data";
        dict[NSLocalizedFailureReasonErrorKey] = failureReason;
        dict[NSUnderlyingErrorKey] = error;
        error = [NSError errorWithDomain:@"YOUR_ERROR_DOMAIN" code:9999 userInfo:dict];
        // Replace this with code to handle the error appropriately.
        // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }
    
    return _persistentStoreCoordinator;
}


- (NSManagedObjectContext *)managedObjectContext {
    // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.)
    if (_managedObjectContext != nil) {
        return _managedObjectContext;
    }
    
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if (!coordinator) {
        return nil;
    }
    _managedObjectContext = [[NSManagedObjectContext alloc] init];
    [_managedObjectContext setPersistentStoreCoordinator:coordinator];
    return _managedObjectContext;
}

#pragma mark - Core Data Saving support

- (void)saveContext {
    NSManagedObjectContext *managedObjectContext = self.managedObjectContext;
    if (managedObjectContext != nil) {
        NSError *error = nil;
        if ([managedObjectContext hasChanges] && ![managedObjectContext save:&error]) {
            // Replace this implementation with code to handle the error appropriately.
            // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
            abort();
        }
    }
}
@end
