//
//  InfoDataSource.h
//  USTC Summit 2016
//
//  Created by dehualai on 9/21/16.
//  Copyright © 2016 DehuaLai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "TextTableViewCell.h"


@interface InfoDataSource : NSObject<UITableViewDataSource>
@property (nonatomic, strong) id<UITextViewDelegate> delegate;
+ (InfoDataSource*)getInstance;
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section;
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView;
- (TextTableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath;

@end
