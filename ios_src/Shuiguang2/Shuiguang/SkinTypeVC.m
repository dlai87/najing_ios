//
//  SkinTypeVC.m
//  Shuiguang
//
//  Created by dehualai on 3/16/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "SkinTypeVC.h"
#import "DBUser+Operation.h"

@interface SkinTypeVC () <UIPickerViewDataSource, UIPickerViewDelegate>

@property (nonatomic, strong) NSArray* optionToDisplay;
@property (nonatomic, strong) NSArray* optionInEn ;
@property (nonatomic, strong) NSString* selectOption;


@property (weak, nonatomic) IBOutlet UILabel *messageLabel;
@property (weak, nonatomic) IBOutlet UIButton *cancelButton;
@property (weak, nonatomic) IBOutlet UIButton *okButton;
@property (weak, nonatomic) IBOutlet UIPickerView *selector;

@end

@implementation SkinTypeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.optionToDisplay = @[NSLocalizedString(@"Dry", nil),
                             NSLocalizedString(@"Normal", nil),
                             NSLocalizedString(@"Oily", nil)];
    self.optionInEn = @[@"dry", @"normal", @"oily"];
    self.selector.delegate = self;
    self.selector.dataSource = self;
    // default first option
    self.selectOption = [self.optionInEn firstObject];
}

#pragma mark -- override
-(void)updateSubview{
    
    
}

#pragma mark -- override
-(void)multiLanguage{
    self.messageLabel.text = NSLocalizedString(@"Please select your skin type.", nil);
    [self.cancelButton setTitle:NSLocalizedString(@"Cancel", nil) forState:UIControlStateNormal];
    [self.okButton setTitle:NSLocalizedString(@"OK", nil) forState:UIControlStateNormal];
    
}



- (IBAction)onCancelButtonPressed:(id)sender {
    [self goBack];

}

- (IBAction)onOkButtonPressed:(id)sender {
    if (self.selectOption) {
        NSArray* users = [DBUser getUserList:self.context];
        UserData* user = [users firstObject];
        user.skin_type = self.selectOption;
        [DBUser smartInsert:self.context user:user];
        [DBUser print:self.context];
        [self goBack];
        if(self.handler){
            [self.handler button2Pressed];
        }
    }
}

#pragma mark  pickerViewDelegate and datasource
// The number of columns of data
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

// The number of rows of data
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return self.optionToDisplay.count;
}

// The data to return for the row and component (column) that's being passed in
- (NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return self.optionToDisplay[row];
}

- (void)pickerView:(UIPickerView *)thePickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    self.selectOption = self.optionInEn[row];
}

@end
