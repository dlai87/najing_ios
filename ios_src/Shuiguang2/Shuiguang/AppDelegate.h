//
//  AppDelegate.h
//  Shuiguang
//
//  Created by dehualai on 2/23/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "AWSMobileClient.h"




//@import Firebase;
//@import GoogleSignIn;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)wechatLoginByRequestForUserInfo; 
- (void)saveContext;
- (void)goToSwitch; 

@end

