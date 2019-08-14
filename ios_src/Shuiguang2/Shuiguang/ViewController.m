//
//  ViewController.m
//  Shuiguang
//
//  Created by dehualai on 2/23/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "ViewController.h"
#import "StoryBoardUtilities.h"
#import "LoginVC.h"
#import "DBUser+Operation.h"
#import "AppDelegate.h"
#import "YALFoldingTabBarController.h"
#import "YALTabBarItem.h"
#import "UIContraints.h"
#import "YALAnimatingTabBarConstants.h"
#import "TransmissionUtil.h"
#import "UIUtil.h"

@interface ViewController ()

@property (strong, nonatomic) NSManagedObjectContext* context;



@end

@implementation ViewController

- (NSManagedObjectContext*) context
{
    if (!_context)
    {
        AppDelegate *app = (AppDelegate*) [[UIApplication sharedApplication] delegate];
        _context = app.managedObjectContext;
    }
    return _context;
}


- (void)viewDidLoad {
    [super viewDidLoad];
}

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [self startApp];
}


-(void)startApp{
    
    
    NSArray* userList = [DBUser getUserList:self.context];
    if ([userList count] <= 0 ) {
        UIViewController* navigationController = (UIViewController*)[StoryBoardUtilities viewControllerForStoryboardName:@"Login" class:[LoginVC class]];
        [self.navigationController pushViewController:navigationController animated:NO];
    }else{
        
        [TransmissionUtil syncAll:self.context];
        

        YALFoldingTabBarController* tabBarController = (YALFoldingTabBarController*)[StoryBoardUtilities viewControllerForStoryboardName:@"Major" class:[YALFoldingTabBarController class]];
        
        
        //prepare leftBarItems
        YALTabBarItem *item1 = [[YALTabBarItem alloc] initWithItemImage:[UIImage imageNamed: @"chats_icon"]
                                                          leftItemImage:nil
                                                         rightItemImage:nil];
        
        
        YALTabBarItem *item2 = [[YALTabBarItem alloc] initWithItemImage:[UIImage imageNamed: @"search_icon"]
                                                          leftItemImage:nil
                                                         rightItemImage:nil];
        
        tabBarController.leftBarItems = @[item1, item2];
        
        //prepare rightBarItems
        YALTabBarItem *item3 = [[YALTabBarItem alloc] initWithItemImage:[UIImage imageNamed:@"nearby_icon"]
                                                          leftItemImage:nil
                                                         rightItemImage:nil];
        
        
        YALTabBarItem *item4 = [[YALTabBarItem alloc] initWithItemImage:[UIImage imageNamed:@"profile_icon"]
                                                          leftItemImage:nil
                                                         rightItemImage:nil];
        
        tabBarController.rightBarItems = @[item3, item4];
        
        tabBarController.centerButtonImage = [UIImage imageNamed:@"plus_icon"];
        
        // Default start from Detection View
        tabBarController.selectedIndex = 1;
        
        //customize tabBarView
        tabBarController.tabBarView.extraTabBarItemHeight = YALExtraTabBarItemsDefaultHeight;
        tabBarController.tabBarView.offsetForExtraTabBarItems = YALForExtraTabBarItemsDefaultOffset;
        tabBarController.tabBarView.backgroundColor = BACKGROUND_COLOR;
        
        tabBarController.tabBarView.tabBarColor = TAP_BAR_COLOR;
        tabBarController.tabBarViewHeight = YALTabBarViewDefaultHeight;
        tabBarController.tabBarView.tabBarViewEdgeInsets = YALTabBarViewHDefaultEdgeInsets;
        tabBarController.tabBarView.tabBarItemsEdgeInsets = YALTabBarViewItemsDefaultEdgeInsets;
        
        [self.navigationController pushViewController:tabBarController animated:NO];
    }
    
}

@end
