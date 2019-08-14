//
//  BasicVC.h
//  Shuiguang
//
//  Created by dehualai on 3/4/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Util.h"
#import "UIUtil.h"


@interface BasicVC : UIViewController

@property (strong, nonatomic) NSManagedObjectContext* context;


-(void)dismissKeyboard; 
-(void)multiLanguage;
-(void)updateSubview;
-(void)goBack;

@end
