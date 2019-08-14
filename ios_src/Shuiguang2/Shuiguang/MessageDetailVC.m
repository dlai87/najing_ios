//
//  MessageDetailVC.m
//  Shuiguang
//
//  Created by dehualai on 3/18/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "MessageDetailVC.h"

@interface MessageDetailVC ()

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (strong, nonatomic) IBOutlet UIView *bgView;


@end

@implementation MessageDetailVC


#pragma mark -- override
-(void)updateSubview{
    if (self.url) {
        [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL fileURLWithPath:self.url]]];
    }
    
    [UIUtil setGradientColor:self.headerView startColor:GRADIENT_COLOR_START endColor:GRADIENT_COLOR_END angle:90];
    [UIUtil setGradientColor:self.bgView startColor:BACKGROUND_COLOR endColor:BACKGROUND_COLOR angle:90];
}

#pragma mark -- override
-(void)multiLanguage{
    self.titleLabel.text = NSLocalizedString(@"Message", nil);
    
}


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}


- (IBAction)onBackButtonPressed:(id)sender {
    [self goBack];
}


@end
