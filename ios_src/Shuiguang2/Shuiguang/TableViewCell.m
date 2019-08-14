//
//  TableViewCell.m
//  UUChartView
//
//  Created by shake on 15/1/4.
//  Copyright (c) 2015年 uyiuyao. All rights reserved.
//

#import "TableViewCell.h"
#import "UUChart.h"
#import "UIContraints.h"

@interface TableViewCell ()<UUChartDataSource>
{
    NSIndexPath *path;
    UUChart *chartView;
}

@property (nonatomic, strong) NSArray* dataToDisplay;
@property (nonatomic, strong) NSArray* xAxis;

@end

@implementation TableViewCell


-(void)configData:(NSArray*)dataToDisplay xAixs:(NSArray*)xAixs{
    self.dataToDisplay = dataToDisplay;
    self.xAxis = xAixs;
    if (chartView) {
        [chartView removeFromSuperview];
        chartView = nil;
    }
    
    chartView = [[UUChart alloc]initWithFrame:CGRectMake(10, 10, [UIScreen mainScreen].bounds.size.width-20, 150)
                                   dataSource:self
                                        style:UUChartStyleLine];
    [chartView showInView:self.contentView];
    
}


#pragma mark - @required
//横坐标标题数组
- (NSArray *)chartConfigAxisXLabel:(UUChart *)chart{
    return self.xAxis;
}

//数值多重数组
- (NSArray *)chartConfigAxisYValue:(UUChart *)chart{
    return self.dataToDisplay;
}

#pragma mark - @optional
//颜色数组
- (NSArray *)chartConfigColors:(UUChart *)chart{
    return @[CHART_LINE_GREEN,CHART_LINE_BLUE];
}
//显示数值范围
- (CGRange)chartRange:(UUChart *)chart{
    return CGRangeMake(60, 0);
}

#pragma mark 折线图专享功能

//标记数值区域
- (CGRange)chartHighlightRangeInLine:(UUChart *)chart{
    //if (path.row == 2) {
    //    return CGRangeMake(25, 75);
    //}
    return CGRangeZero;
}

//判断显示横线条
- (BOOL)chart:(UUChart *)chart showHorizonLineAtIndex:(NSInteger)index{
    return YES;
}

//判断显示最大最小值
- (BOOL)chart:(UUChart *)chart showMaxMinAtIndex:(NSInteger)index{
    return false;
}
@end
