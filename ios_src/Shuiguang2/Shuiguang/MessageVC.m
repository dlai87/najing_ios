// For License please refer to LICENSE file in the root of YALAnimatingTabBarController project

#import "MessageVC.h"
#import "InfoDataSource.h"
#import "MessageDetailVC.h"
#import "StoryBoardUtilities.h"

#define debug 1



@interface MessageVC()

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (strong, nonatomic) IBOutlet UIView *bgView;


@property (nonatomic, strong) NSMutableArray* messageDatas;

@end

@implementation MessageVC

#pragma mark - YALTabBarInteracting


- (void)viewDidLoad {
    [super viewDidLoad];
    [self createMessageList]; 
    self.tableView.dataSource = self; //[InfoDataSource getInstance] ;
    self.tableView.delegate = self;
    
}

#pragma mark -- override
-(void)updateSubview{
    
    [UIUtil setGradientColor:self.headerView startColor:GRADIENT_COLOR_START endColor:GRADIENT_COLOR_END angle:90];
    [UIUtil setGradientColor:self.bgView startColor:BACKGROUND_COLOR endColor:BACKGROUND_COLOR angle:90];
    
}

#pragma mark -- override
-(void)multiLanguage{
    self.titleLabel.text = NSLocalizedString(@"Message", nil);

}


#pragma mark - UITableView Delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

    
    MessageDetailVC* navigationController = (MessageDetailVC*)[StoryBoardUtilities viewControllerForStoryboardName:@"Major" class:[MessageDetailVC class]];
   // navigationController.url = self.data.html_url;
    [self.parentViewController.navigationController pushViewController:navigationController animated:NO];

    
   // [self performSegueWithIdentifier:@"goToMessageDetails" sender:self];
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    TextTableViewCell *tableCell = (TextTableViewCell*) cell;
    tableCell.parentViewController = self;

}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    float height = 98 ;
    switch (indexPath.row) {
        default:
            break;
    }
    return height;
}


#pragma mark - UITableView DataSource

-(void)createMessageList{
    self.messageDatas = [[NSMutableArray alloc]init];
    
    NSString* langSubFolder = @"zh";
    NSLocale *locale = [NSLocale currentLocale];
    //  NSString* langCode = locale.languageCode;
    
    NSString* langCode = [[NSLocale currentLocale] localeIdentifier];
    
    
    if ([langCode containsString:@"zh"]) {
        langSubFolder = @"zh";
    }else{
        langSubFolder = @"en";
    }
    
    
    MessageData* mData2 = [[MessageData alloc]init];
    [mData2 setTitle:NSLocalizedString(@"What is Najing?", nil)];
    [mData2 setBrief:NSLocalizedString(@"It's applied for the field of transdermal drug delivery...", nil)];
    NSString *url2 = [[NSBundle mainBundle]pathForResource:@"message_2" ofType:@"html" inDirectory:[NSString stringWithFormat:@"message/%@/", langSubFolder]];
    [mData2 setHtml_url:url2];
    [self.messageDatas addObject:mData2];
    
    MessageData* mData3 = [[MessageData alloc]init];
    [mData3 setTitle:NSLocalizedString(@"The Operation Procedure Of the Najing Bright and Moist", nil)];
    [mData3 setBrief:NSLocalizedString(@"1.face-cleaning, take out the nano-crystal chip...", nil)];
    NSString *url3 = [[NSBundle mainBundle]pathForResource:@"message_3" ofType:@"html" inDirectory:[NSString stringWithFormat:@"message/%@/", langSubFolder]];
    [mData3 setHtml_url:url3 ];
    [self.messageDatas addObject:mData3];
    
    MessageData* mData4 = [[MessageData alloc]init];
    [mData4 setTitle:NSLocalizedString(@"The Principle of the Nano-crystal Chip Branded by \"Najing\"", nil)];
    [mData4 setBrief:NSLocalizedString(@"The skin functinoned by Najing nano-crystal chips opening hundreds of channels...", nil)];
    NSString *url4 = [[NSBundle mainBundle]pathForResource:@"message_4" ofType:@"html" inDirectory:[NSString stringWithFormat:@"message/%@/", langSubFolder]];
    [mData4 setHtml_url:url4];
    [self.messageDatas addObject:mData4];
    
    MessageData* mData5 = [[MessageData alloc]init];
    [mData5 setTitle:NSLocalizedString(@"Dr. Xu : the Technological Leader of Nano-crystal chip in the world", nil)];
    [mData5 setBrief:NSLocalizedString(@"Dr. Xu, CEO of Natong, the world-class scientist and tech-leader...", nil)];
    NSString *url5 = [[NSBundle mainBundle]pathForResource:@"message_5" ofType:@"html" inDirectory:[NSString stringWithFormat:@"message/%@/", langSubFolder]];
    [mData5 setHtml_url:url5 ];
    [self.messageDatas addObject:mData5];
    
    MessageData* mData6 = [[MessageData alloc]init];
    [mData6 setTitle:NSLocalizedString(@"The Second Seminar about Accleratiing Penetrating and Clinical Application of Nano-tech", nil)];
    [mData6 setBrief:NSLocalizedString(@"More than forty clinical experts from ...", nil)];
    NSString *url6 = [[NSBundle mainBundle]pathForResource:@"message_6" ofType:@"html" inDirectory:[NSString stringWithFormat:@"message/%@/", langSubFolder]];
    [mData6 setHtml_url:url6];
    [self.messageDatas addObject:mData6];
    
    
    MessageData* mData1 = [[MessageData alloc]init];
    [mData1 setTitle:NSLocalizedString(@"Experts opinion", nil)];
    [mData1 setBrief:NSLocalizedString(@"What do the doctors from top level hospital say?", nil)];
    NSString *url1 = [[NSBundle mainBundle]pathForResource:@"message_1" ofType:@"html" inDirectory:[NSString stringWithFormat:@"message/%@/", langSubFolder]];
    [mData1 setHtml_url:url1];
    [self.messageDatas addObject:mData1];
    
    
    MessageData* mData7 = [[MessageData alloc]init];
    [mData7 setTitle:NSLocalizedString(@"Awards:", nil)];
    [mData7 setBrief:NSLocalizedString(@"Tones of award that won by NanoTech", nil)];
    NSString *url7 = [[NSBundle mainBundle]pathForResource:@"message_7" ofType:@"html" inDirectory:[NSString stringWithFormat:@"message/%@/", langSubFolder]];
    [mData7 setHtml_url:url7 ];
    [self.messageDatas addObject:mData7];
    
    
    MessageData* mData8 = [[MessageData alloc]init];
    [mData8 setTitle:NSLocalizedString(@"First publishment of Normative Product from Najing Officially", nil)];
    [mData8 setBrief:NSLocalizedString(@"How to identify a real Najing product...", nil)];
    NSString *url8 = [[NSBundle mainBundle]pathForResource:@"message_8" ofType:@"html" inDirectory:[NSString stringWithFormat:@"message/%@/", langSubFolder]];
    [mData8 setHtml_url:url8];
    [self.messageDatas addObject:mData8];
    
    
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.messageDatas count];
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (TextTableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    TextTableViewCell* cell = nil;
    
    TextTableViewCell* textCell = [TextTableViewCell getCell];
    MessageData* messageData = [self.messageDatas objectAtIndex:indexPath.row];
    textCell.data = messageData;
    textCell.titleLabel.text = messageData.title;
    textCell.messageLabel.text = messageData.brief;
    cell = textCell;
    cell.backgroundColor = [UIColor clearColor];
    return cell;
    
}






@end
