//
//  TextTableViewCell.m
//  USTC Summit 2016
//
//  Created by dehualai on 9/21/16.
//  Copyright © 2016 DehuaLai. All rights reserved.
//

#import "TextTableViewCell.h"
#import "StoryBoardUtilities.h"
#import "MessageDetailVC.h"

@interface TextTableViewCell()

@end


@implementation TextTableViewCell


+ (TextTableViewCell*) getCell
{
    TextTableViewCell * cell = [[[NSBundle mainBundle] loadNibNamed:@"TextTableViewCell" owner:self options:nil] objectAtIndex:0];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}


- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (IBAction)onButtonPressed:(id)sender {
    
    MessageDetailVC* navigationController = (MessageDetailVC*)[StoryBoardUtilities viewControllerForStoryboardName:@"Major" class:[MessageDetailVC class]];
    navigationController.url = self.data.html_url;
    [self.parentViewController.navigationController pushViewController:navigationController animated:NO];

}


@end
