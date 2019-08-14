// For License please refer to LICENSE file in the root of YALAnimatingTabBarController project

#import "DetectVC.h"
#import "blueTooth.h"
#import "MLZBluetoothManager.h"
#import "BAFluidView.h"
#import "UIContraints.h"
#import "NJButton.h"
#import "DetectionData.h"
#import "Util.h"
#import "UICountingLabel.h"
#import "AlertDialog.h"
#import "DBDetection+Operation.h"
#import "DetectionData.h"
#import "TransmissionUtil.h"
#import "UIUtil.h"

#define debug 1
#define WATER_VIEW_TAG 555
#define STATUS_GET_READY 0
#define STATUS_CONNECTING 1
#define STATUS_DETECTED 3




@interface DetectVC()<MLZBluetoothManagerProtocol>
{
    MLZBluetoothManager *blueManager;
    NSTimer* blueTimer;
}

@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (strong, nonatomic) IBOutlet UIView *bgView;

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *detectAreaLabel;
@property (weak, nonatomic) IBOutlet NJButton *buttonOutline1;
@property (weak, nonatomic) IBOutlet NJButton *handButton;
@property (weak, nonatomic) IBOutlet NJButton *faceButton;
@property (weak, nonatomic) IBOutlet NJButton *eyesButton;
@property (weak, nonatomic) IBOutlet UILabel *detectTimeLabel;
@property (weak, nonatomic) IBOutlet NJButton *beforeNursingButton;
@property (weak, nonatomic) IBOutlet NJButton *afterNursingButton;
@property (weak, nonatomic) IBOutlet NJButton *startButton;
@property (weak, nonatomic) IBOutlet UILabel *bluetoothStatusLabel;
@property (weak, nonatomic) IBOutlet NJButton *buttonOutline2;
@property int status;


@property (weak, nonatomic) IBOutlet UIView *waterView;
@property (weak, nonatomic) IBOutlet UIImageView *circleOutline;
@property (weak, nonatomic) IBOutlet UICountingLabel *percentLevel;



@property (nonatomic, strong) NSString* selectWhere;
@property (nonatomic, strong) NSString* selectWhen;

@end


@implementation DetectVC

- (void)viewDidLoad {
    
    [super viewDidLoad];
    self.status = STATUS_GET_READY;
    [self buttonGroup3];
    self.percentLevel.format = @"%d%%";
    self.selectWhen = nil;
    self.selectWhere = nil;

    
}


#pragma mark -- override
-(void)updateSubview{
    
    [UIUtil setGradientColor:self.headerView startColor:GRADIENT_COLOR_START endColor:GRADIENT_COLOR_END angle:90];
    [UIUtil setGradientColor:self.bgView startColor:BACKGROUND_COLOR endColor:BACKGROUND_COLOR angle:90];

    
    [self.buttonOutline1 setTheme:NJBUTTON_THEME_INVERSE_OUTLINE];
    [self.buttonOutline2 setTheme:NJBUTTON_THEME_INVERSE_OUTLINE];
    [self.faceButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    [self.handButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    [self.eyesButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    [self.beforeNursingButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    [self.afterNursingButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    [self.startButton setTheme:NJBUTTON_THEME_INVERSE];
    if([self.selectWhere isEqualToString:FACE] ){
        [self.faceButton setTheme:NJBUTTON_THEME_INVERSE];
    }
    if([self.selectWhere isEqualToString:HAND] ){
        [self.handButton setTheme:NJBUTTON_THEME_INVERSE];
    }
    if([self.selectWhere isEqualToString:EYES] ){
        [self.eyesButton setTheme:NJBUTTON_THEME_INVERSE];
    }
    if([self.selectWhen isEqualToString:PRE_NURSING] ){
        [self.beforeNursingButton setTheme:NJBUTTON_THEME_INVERSE];
    }
    if([self.selectWhen isEqualToString:POST_NURSING] ){
        [self.afterNursingButton setTheme:NJBUTTON_THEME_INVERSE];
    }
    
}

#pragma mark -- override
-(void)multiLanguage{
    self.titleLabel.text = NSLocalizedString(@"Detection", nil);
    self.detectAreaLabel.text = NSLocalizedString(@"Detect Area:", nil);
    [self.handButton setTitle:NSLocalizedString(@"Hands" , nil) forState:UIControlStateNormal];
    [self.faceButton setTitle:NSLocalizedString(@"Face" , nil) forState:UIControlStateNormal];
    [self.eyesButton setTitle:NSLocalizedString(@"Eyes" , nil) forState:UIControlStateNormal];
    self.detectTimeLabel.text = NSLocalizedString(@"Detect Time:", nil);
    [self.beforeNursingButton setTitle:NSLocalizedString(@"Before Nursing", nil) forState:UIControlStateNormal];
    [self.afterNursingButton setTitle:NSLocalizedString(@"After Nursing", nil) forState:UIControlStateNormal];
    [self.startButton setTitle:NSLocalizedString(@"START", nil) forState:UIControlStateNormal];
    //self.bluetoothStatusLabel.text = NSLocalizedString(@"Please enable your bluetooth function on your phone.", nil);

}

- (IBAction)onHandButtonPressed:(id)sender {
    self.selectWhere = HAND;
    [self buttonGroup1];
}

- (IBAction)onFaceButtonPressed:(id)sender {
    self.selectWhere = FACE;
    [self buttonGroup1];
}

- (IBAction)onEyesButtonPressed:(id)sender {
    self.selectWhere = EYES;
    [self buttonGroup1];
}

- (IBAction)onBeforeButtonPressed:(id)sender {
    self.selectWhen = PRE_NURSING;
    [self buttonGroup2];
}

- (IBAction)onAfterButtonPressed:(id)sender {
    self.selectWhen = POST_NURSING;
    [self buttonGroup2];
}

- (IBAction)onStartButtonPressed:(id)sender {
    
    if (self.selectWhen == nil || self.selectWhere == nil ) {
        AlertDialog* alertDialog = [[AlertDialog alloc]init];
        [alertDialog setTitle:nil];
        [alertDialog setMessage:NSLocalizedString(@"Please choose the correct detection AREA and TIME.", nil)];
        [alertDialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
        [alertDialog showDialogOnView:self];
    }else{
        [self test];
        self.status = STATUS_CONNECTING;
        [self buttonGroup3];
    }
}


-(void)buttonGroup1{
    if ([self.selectWhere isEqualToString:HAND]) {
        [self.handButton setTheme:NJBUTTON_THEME_INVERSE];
        [self.faceButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
        [self.eyesButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    }else if ([self.selectWhere isEqualToString:FACE]) {
        [self.handButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
        [self.faceButton setTheme:NJBUTTON_THEME_INVERSE];
        [self.eyesButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    }else if ([self.selectWhere isEqualToString:EYES]) {
        [self.handButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
        [self.faceButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
        [self.eyesButton setTheme:NJBUTTON_THEME_INVERSE];
    }else{
        [self.handButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
        [self.faceButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
        [self.eyesButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    }
}


-(void)buttonGroup2{
    if ([self.selectWhen isEqualToString:PRE_NURSING]) {
        [self.beforeNursingButton setTheme:NJBUTTON_THEME_INVERSE];
        [self.afterNursingButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    }else if ([self.selectWhen isEqualToString:POST_NURSING]) {
        [self.beforeNursingButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
        [self.afterNursingButton setTheme:NJBUTTON_THEME_INVERSE];
    }else {
        [self.beforeNursingButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
        [self.afterNursingButton setTheme:NJBUTTON_THEME_TOTAL_TRANSPARENT];
    }
}

-(void)buttonGroup3{
    for (UIView *subview in self.view.subviews){
        if(subview.tag == WATER_VIEW_TAG)
        [subview removeFromSuperview];
    }
    switch (self.status) {
            case STATUS_GET_READY:{
                self.startButton.hidden = NO;
                self.bluetoothStatusLabel.hidden = YES;
                self.circleOutline.hidden = YES;
                self.percentLevel.hidden = YES;
            }
            break;
            case STATUS_CONNECTING:{
                self.startButton.hidden = YES;
                self.bluetoothStatusLabel.hidden = NO;
                self.circleOutline.hidden = YES;
                self.percentLevel.hidden = YES;

            }
            break;
            case STATUS_DETECTED:{
                self.startButton.hidden = YES;
                self.bluetoothStatusLabel.hidden = YES;
                self.circleOutline.hidden = NO;
                self.percentLevel.hidden = NO;
            }
            break;
        default:
            break;
    }
}

- (void)test
{
    blueManager = [MLZBluetoothManager shareInstance];
    blueManager.delegate = self;
    [self connectBlue];
}

- (void)startSearch
{
    if(!blueTimer){
        blueTimer = [NSTimer scheduledTimerWithTimeInterval:3 target:self selector:@selector(connectBlue) userInfo:nil repeats:YES];
    }
    
}

- (void)connectBlue
{
    if (!blueManager.isSearching && !blueManager.isConnected){
        [blueManager connectToBluetooth];
    }
}

- (void)stopSearch
{
    if([blueTimer isValid]){
        [blueTimer invalidate];
        blueTimer = nil;
    }
}

- (void)bluetoothCurrentState:(BLUE_STATE)state withValue:(id)value
{
    switch (state) {
        case BLUE_STATE_CLOSE:{
            self.bluetoothStatusLabel.text = NSLocalizedString(@"Please enable your bluetooth function on your phone.", nil); //@"蓝牙关闭";
            [self startSearch];
        }
            break;
        case BLUE_STATE_CONNECTTING:{
            self.bluetoothStatusLabel.text = NSLocalizedString(@"Connecting the device.", nil); //@"正在连接";
        }
            break;
        case BLUE_STATE_CONNECT_SUCCEESS:{
            self.bluetoothStatusLabel.text = NSLocalizedString(@"Ready to detect, press the device on your skin.", nil);  //@"连接成功";
            [self stopSearch];
        }
            break;
        case BLUE_STATE_CONNECT_FAIL:{
            self.bluetoothStatusLabel.text =NSLocalizedString(@"Device not found. Please press the button on top of the device.", nil);   //@"连接失败";
            [self startSearch];
        }
            break;
        case BLUE_STATE_CHECKING:{
            self.bluetoothStatusLabel.text = NSLocalizedString(@"Detecting...", nil); //[NSString stringWithFormat:@"正在检测:%@", value];
        }
            break;
        case BLUE_STATE_COMPLETE:{
            [self presetValue:value];
            
            DetectionData * data = [[DetectionData alloc]init];
            data.bodyPart = self.selectWhere;
            data.pre_post = self.selectWhen;
            data.value = @([value intValue]/10);
            data.detectionTime = [[NSDate alloc]init];
            [DBDetection smartInsert:self.context detectionData:data];
            [DBDetection print:self.context]; 

            double DISMISS_RESULT_IN_SEC = 7.0;
            dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(DISMISS_RESULT_IN_SEC * NSEC_PER_SEC));
            dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
            
                NSString* str = [NSString stringWithFormat:@"%d%%", ([value intValue]/10)];
                AlertDialog* alertDialog = [[AlertDialog alloc]init];
                [alertDialog setTitle:nil];
                [alertDialog setMessage:[NSString stringWithFormat:@"%@%@", NSLocalizedString(@"The detection result for this time is : ", nil), str]];
                [alertDialog setButtons:NSLocalizedString(@"OK", nil) button2:nil handler:nil];
                [alertDialog showDialogOnView:self];
        
                [self dismissValue];
            });
            
            // send detection to AWS
            [TransmissionUtil syncDetectionAWS:self.context];
        }
            break;
        default:
            break;
    }
}

-(void)presetValue:(NSNumber*)value{
    self.status = STATUS_DETECTED;
    [self buttonGroup3];
    [self drawWaterLevel:value];
    [self.percentLevel countFrom:0 to:(value.intValue/10) withDuration:3.5f];
}

-(void)dismissValue{
    self.status = STATUS_GET_READY;
    self.selectWhere = nil;
    self.selectWhen = nil;
    [self buttonGroup3];
}

-(void)drawWaterLevel:(NSNumber*)value{

    float targetValue = value.intValue / 1000.0;
    
    float hWholeView = self.view.frame.size.height;
    float hWaterView = self.waterView.frame.size.height;
    float hDelta = hWholeView - hWaterView - self.waterView.frame.origin.y;
    
    float percent = (hWaterView * targetValue + hDelta) / hWholeView;
    
    
    
    NSLog(@"===x %f y %f w %f h %f", self.view.frame.origin.x, self.view.frame.origin.y , self.view.frame.size.width, self.view.frame.size.height);
    
    UIImage *maskingImage = [UIImage imageNamed:@"circle_mask"];
    maskingImage = [Util imageWithImage:maskingImage scaledToSize:self.waterView.frame.size];
    CALayer *maskingLayer = [CALayer layer];
    
    maskingLayer.frame = self.waterView.frame;
   
    NSLog(@"x %f y %f w %f h %f", maskingLayer.frame.origin.x, maskingLayer.frame.origin.y , maskingLayer.frame.size.width, maskingLayer.frame.size.height);
    NSLog(@"percent %f", percent);
    [maskingLayer setContents:(id)[maskingImage CGImage]];
    
    BAFluidView *view = [[BAFluidView alloc] initWithFrame:self.view.frame maxAmplitude:30 minAmplitude:5 amplitudeIncrement:5];
    [view.layer setMask:maskingLayer];
    view.tag = WATER_VIEW_TAG;
    
    view.strokeColor = SEMI_TRANSPARENT_WHITE;
    view.fillColor = SEMI_TRANSPARENT_WHITE;
    view.fillAutoReverse = NO;
    view.fillRepeatCount = 1;
    [view fillTo:@(percent)];
    [view startAnimation];
    [self.view addSubview:view];
}





@end
