//
//  IndicatorViewCell.m
//  Shuiguang
//
//  Created by dehualai on 3/24/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "IndicatorViewCell.h"

@implementation IndicatorViewCell



+ (IndicatorViewCell*) getCell
{
    IndicatorViewCell * cell = [[[NSBundle mainBundle] loadNibNamed:@"IndicatorViewCell" owner:self options:nil] objectAtIndex:0];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [cell setUserInteractionEnabled:YES];
    return cell;
}


- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
