//
//  StoryBoardUtilities.m
//  AiView
//
//  Created by dehualai on 4/26/16.
//  Copyright © 2016 com.aicure.aiview. All rights reserved.
//

#import "StoryBoardUtilities.h"

@implementation StoryBoardUtilities

+ (UIViewController*)viewControllerForStoryboardName:(NSString*)storyboardName class:(id)vcClass
{
    UIStoryboard* storyboard = [UIStoryboard storyboardWithName:storyboardName bundle:nil];
    
    NSString* className = nil;
    
    if ([vcClass isKindOfClass:[NSString class]])
        className = [NSString stringWithFormat:@"%@", vcClass];
    else
        className = [NSString stringWithFormat:@"%s", class_getName([vcClass class])];
    
    UIViewController* viewController = [storyboard instantiateViewControllerWithIdentifier:[NSString stringWithFormat:@"%@", className]];
    
    return viewController;
}

@end
