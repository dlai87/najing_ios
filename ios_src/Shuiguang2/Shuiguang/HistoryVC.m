//
//  HistoryVC.m
//  Shuiguang
//
//  Created by dehualai on 3/20/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "HistoryVC.h"
#import "TableViewCell.h"
#import "HistoryModelOperation.h"
#import "DetectionData.h"
#import "UIContraints.h"
#import "IndicatorViewCell.h"

@interface HistoryVC ()<UITableViewDataSource,UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (strong, nonatomic) IBOutlet UIView *bgView;


@property (nonatomic, strong) NSArray* xAixs;
@property (nonatomic, strong) NSArray* handData;
@property (nonatomic, strong) NSArray* faceData;
@property (nonatomic, strong) NSArray* eyesData;


@end

@implementation HistoryVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSString *cellName = NSStringFromClass([TableViewCell class]);
    [self.tableView registerNib:[UINib nibWithNibName:cellName bundle:nil] forCellReuseIdentifier:cellName];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    
    HistoryModelOperation* opr = [[HistoryModelOperation alloc]init];
    NSDictionary* hand = [opr getDataByDate:self.context targetBodyPart:HAND];
    self.handData = @[[hand objectForKey:PRE_NURSING], [hand objectForKey:POST_NURSING]];
    NSDictionary* face = [opr getDataByDate:self.context targetBodyPart:FACE];
    self.faceData = @[[face objectForKey:PRE_NURSING], [face objectForKey:POST_NURSING]];
    NSDictionary* eyes = [opr getDataByDate:self.context targetBodyPart:EYES];
    self.eyesData = @[[eyes objectForKey:PRE_NURSING], [eyes objectForKey:POST_NURSING]];
    self.xAixs = opr.dataLabels;
}




#pragma mark -- override
-(void)updateSubview{
    [UIUtil setGradientColor:self.headerView startColor:GRADIENT_COLOR_START endColor:GRADIENT_COLOR_END angle:90];
   // [UIUtil setGradientColor:self.bgView startColor:BACKGROUND_COLOR endColor:BACKGROUND_COLOR angle:90];
   // [UIUtil setGradientColor:self.tableView startColor:BACKGROUND_COLOR endColor:BACKGROUND_COLOR angle:90];
}

#pragma mark -- override
-(void)multiLanguage{
    self.titleLabel.text = NSLocalizedString(@"History", nil);
}


#pragma mark - UITableView Datasource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1; 
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 6;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSInteger row = indexPath.row;
    if (row%2==0) {
        NSArray* data = nil ;
        switch (row) {
            case 0:
                data = self.handData;
                break;
            case 2:
                data = self.faceData;
                break;
            case 4:
                data = self.eyesData;
                break;
            default:
                break;
        }
        TableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([TableViewCell class])];
        if (data == nil) {
            NSMutableArray* newData = [[NSMutableArray alloc]init];
            for(int i = 0 ; i < 14; i++){
                [newData addObject:@0];
            }
            [cell configData:newData xAixs:self.xAixs];
        }else{
            [cell configData:data xAixs:self.xAixs];
        }
        cell.backgroundColor = TRANSPARENT;
        return cell;
    }else{
        IndicatorViewCell* cell = [IndicatorViewCell getCell];
        cell.backgroundColor = TRANSPARENT;
        cell.label1.text = NSLocalizedString(@"Before Nursing", nil);
        cell.color1.backgroundColor = CHART_LINE_GREEN;
        cell.label2.text = NSLocalizedString(@"After Nursing", nil);
        cell.color2.backgroundColor = CHART_LINE_BLUE;
        switch (row) {
            case 1:
            {
                cell.label0.text = NSLocalizedString(@"Hands", nil);
            }
                break;
            case 3:
            {
                cell.label0.text = NSLocalizedString(@"Face", nil);
            }
                break;
            case 5:
            {
                cell.label0.text = NSLocalizedString(@"Eyes", nil);
            }
                break;
            default:
                break;
        }
        
        return cell;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSInteger row = indexPath.row;
    if (row%2==0) {
        return 170;
    }else{
        return 40;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    return nil;
}




@end
