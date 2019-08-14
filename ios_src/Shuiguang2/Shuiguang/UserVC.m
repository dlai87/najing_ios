// For License please refer to LICENSE file in the root of YALAnimatingTabBarController project

#import "UserVC.h"
#import "Util.h"
#import "UIContraints.h"
#import "DBUser+Operation.h"
#import "UserData.h"
#import "AppDelegate.h"
#import "AlertDialog.h"
#import "LogoutDialog.h"
#import "ButtonActionHandler.h"
#import "EmailDialog.h"
#import "PhoneDialog.h"
#import "DateOfBirthVC.h"
#import "GenderVC.h"
#import "SkinTypeVC.h"
#import "ChangePasswordVC.h"

#define debug 1


@interface UserVC() <UIImagePickerControllerDelegate,UINavigationControllerDelegate, ButtonActionHandler>


@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (strong, nonatomic) IBOutlet UIView *bgView;

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *username;

@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;

@property (weak, nonatomic) IBOutlet UILabel *emailLabel;
@property (weak, nonatomic) IBOutlet UIButton *emailButton;
@property (weak, nonatomic) IBOutlet UILabel *phoneLabel;
@property (weak, nonatomic) IBOutlet UIButton *phoneButton;
@property (weak, nonatomic) IBOutlet UILabel *dateOfBirthLabel;
@property (weak, nonatomic) IBOutlet UIButton *dateOfBirthButton;
@property (weak, nonatomic) IBOutlet UILabel *genderLabel;
@property (weak, nonatomic) IBOutlet UIButton *genderButton;
@property (weak, nonatomic) IBOutlet UILabel *skinTypeLabel;
@property (weak, nonatomic) IBOutlet UIButton *skinTypeButton;

@property (weak, nonatomic) IBOutlet UIView *rowView1;
@property (weak, nonatomic) IBOutlet UIView *rowView2;
@property (weak, nonatomic) IBOutlet UIView *rowView4;
@property (weak, nonatomic) IBOutlet UIView *rowView5;
@property (weak, nonatomic) IBOutlet UIView *rowView6;



@end

@implementation UserVC

#pragma mark - UserVC



- (void)viewDidLoad {
    [super viewDidLoad];
}


-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [self queryDatabase];

}

#pragma mark -- override
-(void)updateSubview{
    
    [Util displayCircleImage:self.avatarImageView];
    [UIUtil setGradientColor:self.headerView startColor:GRADIENT_COLOR_START endColor:GRADIENT_COLOR_END angle:90];
    [UIUtil setGradientColor:self.bgView startColor:BACKGROUND_COLOR endColor:BACKGROUND_COLOR angle:90];
    [UIUtil setGradientColor:self.rowView1 startColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START endColor:ROW_COLOR_END total:5 current:0]
                    endColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:1]
                       angle:90];
    [UIUtil setGradientColor:self.rowView2 startColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:1]
                    endColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:2]
                       angle:90];
    [UIUtil setGradientColor:self.rowView4 startColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:2]
                    endColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:3]
                       angle:90];
    [UIUtil setGradientColor:self.rowView5 startColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:3]
                    endColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:4]
                       angle:90];
    [UIUtil setGradientColor:self.rowView6 startColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:4]
                    endColor:[UIUtil getTempGradientColorWithStartColor:ROW_COLOR_START  endColor:ROW_COLOR_END total:5 current:5]
                       angle:90];
}

#pragma mark -- override
-(void)multiLanguage{
    self.titleLabel.text = NSLocalizedString(@"User Settings", nil);
    
}


- (IBAction)onAvatarButtonPressed:(id)sender {
    [Util getImageFromDevice:self imagePicker:self];
}


- (IBAction)onLogoutButtonPressed:(id)sender {
    LogoutDialog* logoutDialog = [[LogoutDialog alloc]init];
    [logoutDialog showDialogOnView:self];
    
}

- (IBAction)onEmailButtonPressed:(id)sender {
    EmailDialog* emailDialog = [[EmailDialog alloc]init];
    emailDialog.handler = self;
    [emailDialog showDialogOnView:self]; 
}

- (IBAction)onPhoneButtonPressed:(id)sender {
    PhoneDialog* phoneDialog = [[PhoneDialog alloc]init];
    phoneDialog.handler = self;
    [phoneDialog showDialogOnView:self]; 
}



- (IBAction)onDateOfBirthButtonPressed:(id)sender {
    [self performSegueWithIdentifier:@"goToDateOfBirthVC" sender:self];

}

- (IBAction)onGenderButtonPressed:(id)sender {
    [self performSegueWithIdentifier:@"goToGenderVC" sender:self];

}

- (IBAction)onSkinTypeButtonPressed:(id)sender {
    [self performSegueWithIdentifier:@"goToSkinTypeVC" sender:self];    
}


#pragma mark - private methods
- (UIImage *)imageByCenterCroppingImage:(UIImage *)image
{
    
    CGSize size = CGSizeMake(MIN(image.size.width, image.size.height), MIN(image.size.width, image.size.height));
    
    // not equivalent to image.size (which depends on the imageOrientation)!
    double refWidth = CGImageGetWidth(image.CGImage);
    double refHeight = CGImageGetHeight(image.CGImage);
    
    double x = (refWidth - size.width) / 2.0;
    double y = (refHeight - size.height) / 2.0;
    
    CGRect cropRect = CGRectMake(x, y, size.height, size.width);
    CGImageRef imageRef = CGImageCreateWithImageInRect([image CGImage], cropRect);
    
    UIImage *cropped = [UIImage imageWithCGImage:imageRef scale:0.0 orientation:image.imageOrientation];
    CGImageRelease(imageRef);
    
    return cropped;
}


-(UserData*)queryUserDataFromDB{
    [DBUser print:self.context];
    NSArray* userList = [DBUser getUserList:self.context];
    return [userList firstObject];
}


-(void) queryDatabase{

    UserData* userData = [self queryUserDataFromDB];
    
    if (userData) {
        if([userData username]) self.username.text = [userData username];
        if([userData email]) {
            self.emailLabel.text = [userData email];
            [self.emailButton setTitle:NSLocalizedString(@"Edit", nil) forState:UIControlStateNormal];
        }else{
            [self.emailButton setTitle:NSLocalizedString(@"Add", nil) forState:UIControlStateNormal];
        }
        if([userData phone]){
            self.phoneLabel.text = [userData phone];
            [self.phoneButton setTitle:NSLocalizedString(@"Edit", nil) forState:UIControlStateNormal];
        }else{
            [self.phoneButton setTitle:NSLocalizedString(@"Add", nil) forState:UIControlStateNormal];
        }
        if([userData date_of_birth]){
            self.dateOfBirthLabel.text = [userData date_of_birth];
            self.dateOfBirthButton.hidden = YES;
        }else{
            [self.dateOfBirthButton setTitle:NSLocalizedString(@"Add", nil) forState:UIControlStateNormal];
        }
        if([userData gender]){
            self.genderLabel.text = NSLocalizedString([userData gender], nil) ;
            self.genderButton.hidden = YES;
        }else{
            [self.genderButton setTitle:NSLocalizedString(@"Add", nil) forState:UIControlStateNormal];
        }
        if([userData skin_type]){
            self.skinTypeLabel.text = NSLocalizedString([userData skin_type], nil);
            [self.skinTypeButton setTitle:NSLocalizedString(@"Edit", nil) forState:UIControlStateNormal];
        }else{
            [self.skinTypeButton setTitle:NSLocalizedString(@"Add", nil) forState:UIControlStateNormal];
        }
        
    }
    
}

#pragma mark - ButtonActionHandler
-(void)button1Pressed{
    // cancel button, do nothing
}

-(void)button2Pressed{
    // ok button , refresh views
    [self queryDatabase];
}


#pragma mark - Image Picker Delegate

- (void) imagePickerController:(UIImagePickerController *)picker
         didFinishPickingImage:(UIImage *)image
                   editingInfo:(NSDictionary *)editingInfo
{
    
    if (image != nil) {
        image = [self imageByCenterCroppingImage:image];
        dispatch_async(dispatch_get_main_queue(), ^{
            self.avatarImageView.image = image;
        });
    }
    
    [self dismissViewControllerAnimated:YES completion:nil];
}


#pragma Prepare for segue
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if ([segue.destinationViewController isKindOfClass:[DateOfBirthVC class]]){
        DateOfBirthVC* tempController = (DateOfBirthVC*) segue.destinationViewController;
        tempController.handler = self;
    }
    if ([segue.destinationViewController isKindOfClass:[GenderVC class]]){
        GenderVC* tempController = (GenderVC*) segue.destinationViewController;
        tempController.handler = self;
    }
    if ([segue.destinationViewController isKindOfClass:[SkinTypeVC class]]){
        SkinTypeVC* tempController = (SkinTypeVC*) segue.destinationViewController;
        tempController.handler = self;
    }
    if ([segue.destinationViewController isKindOfClass:[ChangePasswordVC class]]){
        ChangePasswordVC* tempController = (ChangePasswordVC*) segue.destinationViewController;
        tempController.rootVC = self;
    }
}



@end
