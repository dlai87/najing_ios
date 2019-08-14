//
//  AIButton.m
//  AiView
//
//  Created by dehualai on 8/30/16.
//  Copyright © 2016 com.aicure.aiview. All rights reserved.
//

#import "NJButton.h"
#import <math.h>
#import "UIContraints.h"

@interface NJButton()

@property float cornerRatio;
@property int border;


@end




@implementation NJButton 




-(void)setCornerRation:(float)ratio{
    self.cornerRatio = ratio;
}

-(void)setBorderWidth:(int)borderWidth{
    self.border = borderWidth;
}

-(void)setTheme:(int)theme{
    // if parameters not defined, use the default value.
    if(self.cornerRatio==0){
        self.cornerRatio=0.5;
    }
    if(self.border==0){
        self.border=2;
    }
    double cornerRadius = fmin(self.bounds.size.width, self.bounds.size.height ) * self.cornerRatio;
    switch (theme) {
        case NJBUTTON_THEME_DEFAULT:
        {
            self.layer.borderColor = (TAP_BAR_COLOR).CGColor;
            [self setBackgroundColor:TAP_BAR_COLOR];
            
            break;
        }
        case NJBUTTON_THEME_TOTAL_TRANSPARENT:
        {
            self.layer.borderColor = (TRANSPARENT).CGColor;
            [self setBackgroundColor:TRANSPARENT];
            break;
        }
        case NJBUTTON_THEME_WHITE:
        {
            self.layer.borderColor = (WHITE).CGColor;
            [self setBackgroundColor:WHITE];
            break;
        }
        case NJBUTTON_THEME_WHITE_OUTLINE:
        {
            self.layer.borderColor = (WHITE).CGColor;
            [self setBackgroundColor:TRANSPARENT];
            break;
        }
        case NJBUTTON_THEME_INVERSE:
        {
            self.layer.borderColor = (GRADIENT_COLOR_START).CGColor;
            [self setBackgroundColor:GRADIENT_COLOR_START];
            break;
        }
        case NJBUTTON_THEME_INVERSE_OUTLINE:
        {
            self.layer.borderColor = (GRADIENT_COLOR_START).CGColor;
            [self setBackgroundColor:TRANSPARENT];
            break;
        }
        default:
            break;
    }
    
    
    self.layer.cornerRadius = cornerRadius;
    self.layer.borderWidth = self.border;
    
    /*
    CALayer *shadowLayerButton1 = self.layer;
    shadowLayerButton1.shadowOffset = CGSizeMake(1.0f, 1.0f);
    //shadowLayerButton1.cornerRadius = cornerRadius;
    shadowLayerButton1.shadowColor = [[UIColor blackColor] CGColor];
    shadowLayerButton1.shadowRadius = cornerRadius;
    shadowLayerButton1.shadowOpacity = 0.30f;
    shadowLayerButton1.shadowPath = [[UIBezierPath bezierPathWithRect:shadowLayerButton1.bounds] CGPath];
    */
}



@end
