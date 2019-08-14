//
//  TextTableViewCell.h
//  USTC Summit 2016
//
//  Created by dehualai on 9/21/16.
//  Copyright © 2016 DehuaLai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MessageData.h"


@interface TextTableViewCell : UITableViewCell


@property (nonatomic, strong) UIViewController* parentViewController;
@property (nonatomic, strong) MessageData* data; 


@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *messageLabel;


+ (TextTableViewCell*) getCell;


@end
