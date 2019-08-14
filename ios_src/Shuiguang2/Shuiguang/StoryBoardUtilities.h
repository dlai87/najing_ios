//
//  StoryBoardUtilities.h
//  AiView
//
//  Created by dehualai on 4/26/16.
//  Copyright © 2016 com.aicure.aiview. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <objc/runtime.h>
#import <UIKit/UIKit.h>


@interface StoryBoardUtilities : NSObject

+ (UIViewController*)viewControllerForStoryboardName:(NSString*)storyboardName class:(id)vcClass;

@end
