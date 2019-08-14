//
//  AIButton.h
//  AiView
//
//  Created by dehualai on 8/30/16.
//  Copyright © 2016 com.aicure.aiview. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>


#define NJBUTTON_THEME_DEFAULT 0
#define NJBUTTON_THEME_TOTAL_TRANSPARENT 1
#define NJBUTTON_THEME_WHITE 2
#define NJBUTTON_THEME_WHITE_OUTLINE 3
#define NJBUTTON_THEME_INVERSE 4
#define NJBUTTON_THEME_INVERSE_OUTLINE 5

@interface NJButton : UIButton

-(void)setTheme:(int)theme;
-(void)setCornerRation:(float)ratio;
-(void)setBorderWidth:(int)borderWidth;


@end
