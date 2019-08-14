//
//  SyncTaskHandler.h
//  Shuiguang
//
//  Created by dehualai on 3/1/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol SyncTaskHandler <NSObject>

-(void)onSuccess:(id)object;
-(void)onFailure:(NSString*)message errorCode:(NSNumber*)errorCode;

@end
