//
//  MLZBluetoothManager.m
//  Mlizhi
//
//  Created by gaoyuanliu on 14-1-22.
//  Copyright (c) 2014年 美立知科技. All rights reserved.
//

#import "MLZBluetoothManager.h"
#import "blueTooth.h"

@interface MLZBluetoothManager ()<CBCentralManagerDelegate, CBPeripheralDelegate>

@end

static MLZBluetoothManager *blueManager;
@implementation MLZBluetoothManager

+ (id)shareInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (blueManager == nil)
        {
            blueManager = [[MLZBluetoothManager alloc] init];
        }
    });
    return blueManager;
}

- (void)connectToBluetooth
{
    self.isSearching = YES;
    
    if (!self.manager) {
        //判断系统版本,注意方法有所变更
        self.manager = [[CBCentralManager alloc] initWithDelegate:self queue:nil options:nil];
    }
}

- (void)cancleBlue
{
    if(self.isConnected){
        [self.manager cancelPeripheralConnection:self.peripheral];
        self.isConnected=NO;
        self.isSearching=NO;
    }
}


#pragma mark CBPeripheralManagerDelegate
//搜索设备
- (void)centralManagerDidUpdateState:(CBCentralManager *)central
{
    switch (central.state)
    {
        case CBCentralManagerStatePoweredOn:
            //scans for any peripheral
            //第二个参数为nil时会搜索所有设备
            [self.manager scanForPeripheralsWithServices:nil options:nil];
            if (_delegate && [_delegate respondsToSelector:@selector(bluetoothCurrentState: withValue:)])
            {
                [_delegate bluetoothCurrentState:BLUE_STATE_CONNECTTING withValue:nil];//正在连接
            }
            break;
            
        case CBCentralManagerStatePoweredOff:
        {
            //bluetoothConnectedFailed
            
            if (_delegate && [_delegate respondsToSelector:@selector(bluetoothCurrentState:withValue:)])
            {
                [_delegate bluetoothCurrentState:BLUE_STATE_CLOSE withValue:nil];
            }
            self.isSearching = NO;
            self.isConnected = NO;
            self.manager = nil;
        }
            break;
        default:
            break;
    }

}


//发现设备
- (void)centralManager:(CBCentralManager *)central didDiscoverPeripheral:(CBPeripheral *)peripheral advertisementData:(NSDictionary *)advertisementData RSSI:(NSNumber *)RSSI
{
    //判断设备名称是否为mlizhi      B3D0D674-12B3-34A5-7ED3-DBF55780749
    if ([peripheral.name isEqualToString:@"MyService"] || [peripheral.name isEqualToString:@"mlizhi"])
    {
        [self.manager stopScan];
        if(self.peripheral!=peripheral){
            self.peripheral = peripheral;
            [self.manager connectPeripheral:peripheral options:nil];
            self.peripheral.delegate = self;
        }
    }
}

//连接设备成功
- (void)centralManager:(CBCentralManager *)central
  didConnectPeripheral:(CBPeripheral *)peripheral
{
    
    self.isSearching = NO;
    self.isConnected = YES;
    
    if (_delegate && [_delegate respondsToSelector:@selector(bluetoothCurrentState:withValue:)])
    {
        [_delegate bluetoothCurrentState:BLUE_STATE_CONNECT_SUCCEESS withValue:nil];
    }
    [self.peripheral discoverServices:nil];
}

//断开设备连接
- (void)centralManager:(CBCentralManager *)central didDisconnectPeripheral:(CBPeripheral *)peripheral error:(NSError *)error
{
    self.isConnected = NO;
    self.isSearching = NO;
    self.manager = nil;
    
    if (_delegate && [_delegate respondsToSelector:@selector(bluetoothCurrentState:withValue:)])
    {
        [_delegate bluetoothCurrentState:BLUE_STATE_CONNECT_FAIL withValue:nil];
    }
}

#pragma mark ----------猥琐的分割线----------
#pragma mark --CBPeripheralDelegate
//搜索蓝牙服务
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverServices:(NSError *)error
{
    for (CBService *service  in peripheral.services)
    {
        [self.peripheral discoverCharacteristics:nil forService:service];
    }

}

//搜索蓝牙特征

- (void)peripheral:(CBPeripheral *)peripheral didDiscoverCharacteristicsForService:(CBService *)service error:(NSError *)error
{
    for (CBCharacteristic *characteristic in service.characteristics)
    {
        //监听特征值是否发生变化
        if([characteristic.UUID isEqual:[CBUUID UUIDWithString:@"1601"]]){

            [self.peripheral readValueForCharacteristic:characteristic];
        }else if([characteristic.UUID isEqual:[CBUUID UUIDWithString:@"2a37"]]){
            [self.peripheral setNotifyValue:YES forCharacteristic:characteristic];
        }else{
        
        }
        
    }
}

//读取特征值
- (void)peripheral:(CBPeripheral *)peripheral didUpdateValueForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
    
    NSLog(@"data: %@", characteristic.value);
    NSInteger value = [blueTooth deCodeBlueToothData:characteristic.value];
    static NSInteger lastValue = -1;
    if(lastValue!=value){
        if (_delegate && [_delegate respondsToSelector:@selector(bluetoothCurrentState:withValue:)])
        {
            if(value==3 || value==4){
                [_delegate bluetoothCurrentState:BLUE_STATE_RESET withValue:[NSNumber numberWithInteger:value]];
            }else if(value==2){
                [_delegate bluetoothCurrentState:BLUE_STATE_CHECKING withValue:[NSNumber numberWithInteger:value]];
            }else{
                [_delegate bluetoothCurrentState:BLUE_STATE_COMPLETE withValue:[NSNumber numberWithInteger:value]];
            }
            
        }
        lastValue = value;
        }
    [self.peripheral readValueForCharacteristic:characteristic];
}

//监听通知
- (void)peripheral:(CBPeripheral *)peripheral didUpdateNotificationStateForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
    [self.peripheral readValueForCharacteristic:characteristic];
}


#pragma mark ----------猥琐的分割线----------
#pragma mark --BLEUtility
- (NSString *) CBUUIDToString:(CBUUID *)inUUID {
    unsigned char i[16];
    [inUUID.data getBytes:i];
    if (inUUID.data.length == 2) {
        
        return [NSString stringWithFormat:@"%02hhx%02hhx",i[0],i[1]];
    }
    else {
        uint32_t g1 = ((i[0] << 24) | (i[1] << 16) | (i[2] << 8) | i[3]);
        uint16_t g2 = ((i[4] << 8) | (i[5]));
        uint16_t g3 = ((i[6] << 8) | (i[7]));
        uint16_t g4 = ((i[8] << 8) | (i[9]));
        uint16_t g5 = ((i[10] << 8) | (i[11]));
        uint32_t g6 = ((i[12] << 24) | (i[13] << 16) | (i[14] << 8) | i[15]);
        return [NSString stringWithFormat:@"%08x-%04hx-%04hx-%04hx-%04hx%08x",g1,g2,g3,g4,g5,g6];
    }
    return nil;
}

@end
