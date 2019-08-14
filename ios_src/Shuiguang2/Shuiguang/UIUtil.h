//
//  UIUtil.h
//  aimm
//
//  Created by dehualai on 12/2/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "UIContraints.h"


@interface UIUtil : NSObject

+(void) setGradientColor:(UIView*)view startColor:(UIColor*)startColor endColor:(UIColor*)endColor;
+(void) setGradientColor:(UIView*)view startColor:(UIColor*)startColor endColor:(UIColor*)endColor angle:(int)angle;
+(UIColor*) getTempGradientColorWithStartColor:(UIColor*)startColor endColor:(UIColor*)endColor total:(int)total current:(int)current; 
@end
