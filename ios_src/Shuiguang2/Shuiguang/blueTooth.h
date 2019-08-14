//
//  blueTooth.h
//  blueTooth
//
//  Created by carl on 14/12/1.
//  Copyright (c) 2014年 美立知科技. All rights reserved.
//

#import <Foundation/Foundation.h>



@interface blueTooth : NSObject

/*传入蓝牙数据特征值返回对应的解密值
 返回值：0   连接蓝牙
 返回值：2   开始接收数据
 错误值：3   接触少于5秒
 错误值：4   接触不稳定
 错误值：5   传入值错误
 正常值：100~1000
*/
/*
测试数据 <d9133137 000000> 2
<cf0a4d5e 551819> 333
<438661c1 cd5b5a>   406
*/
//拿到蓝牙数据,NSData或NSString类型

+ (NSInteger)deCodeBlueToothData:(NSData* )data;

//btStr 16进制字符串
+ (NSInteger)deCodeBlueToothStr:(NSString* )btStr;

@end
