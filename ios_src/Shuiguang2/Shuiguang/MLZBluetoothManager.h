//
//  MLZBluetoothManager.h
//  Mlizhi
//
//  Created by gaoyuanliu on 14-1-22.
//  Copyright (c) 2014年 美立知科技. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreBluetooth/CoreBluetooth.h>

typedef NS_ENUM(NSInteger, BLUE_STATE){
    BLUE_STATE_RESET = 0,//重置
    BLUE_STATE_CONNECTTING,//正在连接
    BLUE_STATE_CONNECT_SUCCEESS,//连接成功
    BLUE_STATE_CONNECT_FAIL,//连接失败
    BLUE_STATE_CHECKING,//开始检测
    BLUE_STATE_COMPLETE,//检测完成
    BLUE_STATE_CLOSE,//关闭
    BLUE_STATE_MOVE//水波
};

@protocol MLZBluetoothManagerProtocol;
@interface MLZBluetoothManager : NSObject
{
    NSMutableData* data;
}
@property (nonatomic, strong)CBCentralManager *manager;
@property (nonatomic, strong)CBPeripheral *peripheral;
@property (nonatomic, assign)BOOL isSearching;
@property (nonatomic, assign)BOOL isConnected;
@property (nonatomic, assign)BOOL isStart;
@property (nonatomic, assign)id<MLZBluetoothManagerProtocol>delegate;

+ (id)shareInstance;
- (void)connectToBluetooth;
- (void)cancleBlue;

@end

@protocol MLZBluetoothManagerProtocol <NSObject>

@optional

- (void)bluetoothCurrentState:(BLUE_STATE)state withValue:(id)value;

@end
