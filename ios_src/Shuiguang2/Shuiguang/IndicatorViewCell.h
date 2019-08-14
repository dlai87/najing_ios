//
//  IndicatorViewCell.h
//  Shuiguang
//
//  Created by dehualai on 3/24/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface IndicatorViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *label0;
@property (weak, nonatomic) IBOutlet UIView *color1;
@property (weak, nonatomic) IBOutlet UILabel *label1;
@property (weak, nonatomic) IBOutlet UIView *color2;
@property (weak, nonatomic) IBOutlet UILabel *label2;



+ (IndicatorViewCell*) getCell;

@end
