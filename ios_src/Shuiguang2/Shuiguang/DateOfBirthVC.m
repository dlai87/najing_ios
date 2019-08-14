//
//  DateOfBirthVC.m
//  Shuiguang
//
//  Created by dehualai on 3/15/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "DateOfBirthVC.h"
#import "DBUser+Operation.h"
#import "Util.h"
#import "UserData.h"



@interface DateOfBirthVC ()

@property (weak, nonatomic) IBOutlet UILabel *message;
@property (weak, nonatomic) IBOutlet UIDatePicker *datePicker;
@property (weak, nonatomic) IBOutlet UIButton *cancelButton;
@property (weak, nonatomic) IBOutlet UIButton *okButton;

@end

@implementation DateOfBirthVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.datePicker.datePickerMode = UIDatePickerModeDate;
    [self.datePicker setMaximumDate:[NSDate date]];

}

#pragma mark -- override
-(void)updateSubview{
    
    
}

#pragma mark -- override
-(void)multiLanguage{
    self.message.text = NSLocalizedString(@"Please select your date of birth, you cannot change the date of birth once it is setted.", nil);
    [self.cancelButton setTitle:NSLocalizedString(@"Cancel", nil) forState:UIControlStateNormal];
    [self.okButton setTitle:NSLocalizedString(@"OK", nil) forState:UIControlStateNormal];
    
}


- (IBAction)onCancelButtonPressed:(id)sender {
    [self goBack];
}

- (IBAction)onOkButtonPressed:(id)sender {
    NSDate* dateSelected = self.datePicker.date;
    if (dateSelected) {
        NSString* dateStr = [Util convertDateToString:dateSelected withFormat:FORMAT_DATE];
        NSArray* users = [DBUser getUserList:self.context];
        UserData* user = [users firstObject];
        user.date_of_birth = dateStr;
        [DBUser smartInsert:self.context user:user];
        [self goBack];
        if(self.handler){
            [self.handler button2Pressed]; 
        }
    }
}






@end
