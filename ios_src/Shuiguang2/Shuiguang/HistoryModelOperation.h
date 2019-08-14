//
//  HistoryModelOperation.h
//  Shuiguang
//
//  Created by dehualai on 3/22/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@interface HistoryModelOperation : NSObject

@property (nonatomic, strong) NSMutableArray* dataLabels;

-(NSDictionary*)getDataByDate:(NSManagedObjectContext*)context targetBodyPart:(NSString*)targetBodyPart;
-(NSDictionary*)getDataByBodyPart:(NSManagedObjectContext*)context; 

    
@end
