//
//  Util.h
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>
#import <UIKit/UIKit.h>




#define FORMAT_DATE_TIME @"yyyy-MM-dd HH:mm:ss"
#define FORMAT_DATE @"yyyy-MM-dd"
#define FORMAT_TIME @"HH:mm:ss"

@interface Util : NSObject

+(BOOL) validateEmailFormat:(NSString*) emailStr;
+(BOOL) validatePhoneNumber:(NSString*) phoneStr; 
+(NSDate*) convertStringToDate:(NSString*)dateString withFormat:(NSString*)format;
+(NSString*) convertDateToString:(NSDate*)inputDate withFormat:(NSString*)format;
+(void)save:(NSManagedObjectContext*)context; 
+ (UIImage*)imageCompressWithSimple:(UIImage*)image scaledToSize:(CGSize)size; 
+(int)countDaysDifferent:(NSDate*)fromDateTime toDate:(NSDate*)toDateTime;
+(NSString*)getDate:(NSDate*)baseDate offset:(int)offset;
+(void)createDemoUserData:(NSManagedObjectContext*)context numDays:(int)numDays; 
+(NSString*) generateRandomString:(int)len;
+(NSString*) javaHashCode:(NSString*)str;

////// image /////////
+(void)getImageFromDevice:(UIViewController*)vc imagePicker:(id<UIImagePickerControllerDelegate, UINavigationControllerDelegate>)imagePicker;
+(void)saveImageToLocal:(UIImage*)image imageName:(NSString*)imageName;
+(UIImage*)getImageFromLocal:(NSString*)imageName;
+(void)displayCircleImage:(UIImageView*)image;
+ (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize;
////// image /////////

@end
