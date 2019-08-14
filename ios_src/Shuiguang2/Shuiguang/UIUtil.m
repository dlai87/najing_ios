//
//  UIUtil.m
//  aimm
//
//  Created by dehualai on 12/2/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "UIUtil.h"

@implementation UIUtil

+(void) setGradientColor:(UIView*)view startColor:(UIColor*)startColor endColor:(UIColor*)endColor{
    CAGradientLayer *gradient = [CAGradientLayer layer];
    gradient.frame = view.bounds;
    gradient.startPoint = CGPointZero;
    gradient.endPoint = CGPointMake(1, 1);
    gradient.colors = [NSArray arrayWithObjects:(id)[startColor CGColor],(id)[endColor CGColor], nil];
    //[view.layer addSublayer:gradient];
    
    [view.layer insertSublayer:gradient atIndex:0];
}


+(void) setGradientColor:(UIView*)view startColor:(UIColor*)startColor endColor:(UIColor*)endColor angle:(int)angle{
    CAGradientLayer *gradient = [CAGradientLayer layer];
    gradient.frame = view.bounds;
    gradient.startPoint = CGPointZero;
    if (angle == 0) {
        gradient.endPoint = CGPointMake(1, 0);
    }else if(angle == 90){
        gradient.endPoint = CGPointMake(0, 1);
    }else{
        gradient.endPoint = CGPointMake(1, 1);
    }
    gradient.colors = [NSArray arrayWithObjects:(id)[startColor CGColor],(id)[endColor CGColor], nil];
    [view.layer insertSublayer:gradient atIndex:0];
}

+(UIColor*) getTempGradientColorWithStartColor:(UIColor*)startColor endColor:(UIColor*)endColor total:(int)total current:(int)current{
    const CGFloat *startColors = CGColorGetComponents(startColor.CGColor);
    const CGFloat *endColors = CGColorGetComponents(endColor.CGColor);
    return [UIColor colorWithRed:(startColors[0] + (endColors[0] - startColors[0]) * current / total)
                           green:(startColors[1] + (endColors[1] - startColors[1]) * current / total)
                            blue:(startColors[2] + (endColors[2] - startColors[2]) * current / total)
                           alpha:(startColors[3] + (endColors[3] - startColors[3]) * current / total)];
    
}


@end
