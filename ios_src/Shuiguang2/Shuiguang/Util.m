//
//  Util.m
//  Shuiguang
//
//  Created by dehualai on 2/27/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "Util.h"
#import <AssetsLibrary/AssetsLibrary.h>
#import "DBDetection+Operation.h"
#import "DetectionData.h"

#define ARC4RANDOM_MAX 0x100000000

@implementation Util


+(BOOL) validateEmailFormat:(NSString*)emailStr{
    BOOL stricterFilter = NO;
    NSString *stricterFilterString = @"^[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$";
    NSString *laxString = @"^.+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2}[A-Za-z]*$";
    NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:emailStr];
}

+(BOOL) validatePhoneNumber:(NSString*) phoneStr{
    if (phoneStr.length >= 7 && [phoneStr rangeOfCharacterFromSet:[NSCharacterSet characterSetWithCharactersInString:@"0123456789+-."]].location != NSNotFound) {
        // this matches the criteria
        return YES;
    }
    return NO;
}

+(NSDate*) convertStringToDate:(NSString*)dateString withFormat:(NSString*)format{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:format];
    NSDate* dateFromString = [dateFormatter dateFromString:dateString];
    return dateFromString;
}

+(NSString*) convertDateToString:(NSDate*)inputDate withFormat:(NSString*)format{
    NSDateFormatter* df = [[NSDateFormatter alloc]init];
    [df setDateFormat:format];
    NSString *result = [df stringFromDate:inputDate];
    return result;
}


+(void)save:(NSManagedObjectContext*)context{
    NSError *saveError = nil;
    BOOL saving = [context save:&saveError];
    if (!saving)
        NSLog(@"Error: Saving error: %@",saveError);
}


+(int)countDaysDifferent:(NSDate*)fromDateTime toDate:(NSDate*)toDateTime{

  //  NSLog(@"fromDateTime %@ %@", fromDateTime, toDateTime);
    fromDateTime = [Util convertStringToDate:[Util convertDateToString:fromDateTime withFormat:FORMAT_DATE] withFormat:FORMAT_DATE];
    toDateTime = [Util convertStringToDate:[Util convertDateToString:toDateTime withFormat:FORMAT_DATE] withFormat:FORMAT_DATE];
    NSDate *fromDate;
    NSDate *toDate;
  //  NSLog(@"fromDateTime %@ %@", fromDateTime, toDateTime);

    NSCalendar *calendar = [NSCalendar currentCalendar];
    
    [calendar rangeOfUnit:NSCalendarUnitDay startDate:&fromDate
                 interval:NULL forDate:fromDateTime];
    [calendar rangeOfUnit:NSCalendarUnitDay startDate:&toDate
                 interval:NULL forDate:toDateTime];
    
    NSDateComponents *difference = [calendar components:NSCalendarUnitDay
                                               fromDate:fromDate toDate:toDate options:0];
    
    return (int)[difference day] + 1;

}


+(NSString*) generateRandomString:(int)len{
    NSString *alphabet = @"abcdefghijklmnopqrstuvwxyz0123456789";
    NSMutableString *randomString = [[NSMutableString alloc] init];
    for (int x = 0; x < len; x++) {
        [randomString appendFormat:@"%C", [alphabet characterAtIndex: arc4random_uniform((int)[alphabet length]) % [alphabet length]]];
    }
    return randomString;
}


+(NSString*) javaHashCode:(NSString*)str{
    int h = 0;
    for (int i = 0; i < (int)str.length; i++) {
        h = (31 * h) + [str characterAtIndex:i];
    }
    return [NSString stringWithFormat:@"iOS_%d", h];
}

+(float)getRandom{
    float minRange = 22;
    float maxRange = 38;
    
    float val = ((float)arc4random() / ARC4RANDOM_MAX)
    * (maxRange - minRange)
    + minRange;
    return val;
}



+(NSString*)getDate:(NSDate*)baseDate offset:(int)offset{
    NSDate* date = [[NSCalendar currentCalendar] dateByAddingUnit:NSCalendarUnitDay
                                                    value:offset
                                                   toDate:baseDate
                                                  options:0];
    return [Util convertDateToString:date withFormat:FORMAT_DATE];
}

+(void)createDemoUserData:(NSManagedObjectContext*)context numDays:(int)numDays{
    [DBDetection clean:context];
    
    for(int i = numDays; i >0;  i--){
        NSString* date = [Util getDate:[[NSDate alloc]init] offset:-i];
        NSString* time1 = [NSString stringWithFormat:@"%@ %@", date, @"08:18:00"];
        DetectionData* temp1 = [[DetectionData alloc]initWithBodyPart:FACE
                                                       detectionTime:[Util convertStringToDate:time1 withFormat:FORMAT_DATE_TIME]
                                                               value:@([Util getRandom])
                                                             prePost:PRE_NURSING];
        [DBDetection smartInsert:context detectionData:temp1];
        [DBDetection updateTransmissionStatus:context detectionData:temp1];
        
        NSString* time2 = [NSString stringWithFormat:@"%@ %@", date, @"08:28:00"];
        DetectionData* temp2 = [[DetectionData alloc]initWithBodyPart:HAND
                                                        detectionTime:[Util convertStringToDate:time2 withFormat:FORMAT_DATE_TIME]
                                                                value:@([Util getRandom])
                                                              prePost:PRE_NURSING];
        [DBDetection smartInsert:context detectionData:temp2];
        [DBDetection updateTransmissionStatus:context detectionData:temp2];
        
        NSString* time3 = [NSString stringWithFormat:@"%@ %@", date, @"08:38:00"];
        DetectionData* temp3 = [[DetectionData alloc]initWithBodyPart:EYES
                                                        detectionTime:[Util convertStringToDate:time3 withFormat:FORMAT_DATE_TIME]
                                                                value:@([Util getRandom])
                                                              prePost:PRE_NURSING];
        [DBDetection smartInsert:context detectionData:temp3];
        [DBDetection updateTransmissionStatus:context detectionData:temp3];
        
        NSString* time4 = [NSString stringWithFormat:@"%@ %@", date, @"09:18:00"];
        DetectionData* temp4 = [[DetectionData alloc]initWithBodyPart:FACE
                                                        detectionTime:[Util convertStringToDate:time4 withFormat:FORMAT_DATE_TIME]
                                                                value:@([Util getRandom])
                                                              prePost:POST_NURSING];
        [DBDetection smartInsert:context detectionData:temp4];
        [DBDetection updateTransmissionStatus:context detectionData:temp4];
        
        NSString* time5 = [NSString stringWithFormat:@"%@ %@", date, @"09:28:00"];
        DetectionData* temp5 = [[DetectionData alloc]initWithBodyPart:HAND
                                                        detectionTime:[Util convertStringToDate:time5 withFormat:FORMAT_DATE_TIME]
                                                                value:@([Util getRandom])
                                                              prePost:POST_NURSING];
        [DBDetection smartInsert:context detectionData:temp5];
        [DBDetection updateTransmissionStatus:context detectionData:temp5];

        
        NSString* time6 = [NSString stringWithFormat:@"%@ %@", date, @"09:38:00"];
        DetectionData* temp6 = [[DetectionData alloc]initWithBodyPart:EYES
                                                        detectionTime:[Util convertStringToDate:time6 withFormat:FORMAT_DATE_TIME]
                                                                value:@([Util getRandom])
                                                              prePost:POST_NURSING];
        [DBDetection smartInsert:context detectionData:temp6];
        [DBDetection updateTransmissionStatus:context detectionData:temp6];

    }
    
    [DBDetection print:context]; 

}

+ (UIImage*)imageCompressWithSimple:(UIImage*)image scaledToSize:(CGSize)size
{
    UIGraphicsBeginImageContext(size);
    [image drawInRect:CGRectMake(0,0,size.width,size.height)];
    UIImage* newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}



+(void)getImageFromDevice:(UIViewController*)vc imagePicker:(id<UIImagePickerControllerDelegate, UINavigationControllerDelegate>)imagePicker{
    
    NSString *cancelButtonTitle = NSLocalizedString(@"Cancel", nil);
    NSString *libraryImageButtonTitle = NSLocalizedString(@"Choose image from library", nil);
    NSString *cameraButtonTitle = NSLocalizedString(@"Take picture using camera", nil);
    
    UIImagePickerController* imagePickerController = [[UIImagePickerController alloc] init];
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    // Create the actions.
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:cancelButtonTitle style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
        NSLog(@"cancel");
    }];
    
    UIAlertAction *libraryAction = [UIAlertAction actionWithTitle:libraryImageButtonTitle style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        
        ALAssetsLibrary *lib = [[ALAssetsLibrary alloc] init];
        [lib enumerateGroupsWithTypes:ALAssetsGroupSavedPhotos usingBlock:^(ALAssetsGroup *group, BOOL *stop) {
            NSLog(@"%zd", [group numberOfAssets]);
        } failureBlock:^(NSError *error) {
            if (error.code == ALAssetsLibraryAccessUserDeniedError) {
                NSLog(@"user denied access, code: %zd", error.code);
            } else {
                NSLog(@"Other error code: %zd", error.code);
            }
        }];
        
        [imagePickerController setSourceType:UIImagePickerControllerSourceTypePhotoLibrary];
        [imagePickerController setDelegate:imagePicker];
        
        // Place image picker on the screen
        [vc presentViewController:imagePickerController animated:YES completion:nil];
        
        
    }];
    
    UIAlertAction *cameraAction = [UIAlertAction actionWithTitle:cameraButtonTitle style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        
        [imagePickerController setSourceType:UIImagePickerControllerSourceTypeCamera];
        [imagePickerController setDelegate:imagePicker];
        
        // Place image picker on the screen
        [vc presentViewController:imagePickerController animated:YES completion:nil];
    }];
    
    // Add the actions.
    [alertController addAction:cancelAction];
    [alertController addAction:libraryAction];
    [alertController addAction:cameraAction];
    
    [vc presentViewController:alertController animated:YES completion:nil];
    
    
}



+(void)saveImageToLocal:(UIImage*)image imageName:(NSString*)imageName{
    if (image != nil){
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
                                                             NSUserDomainMask, YES);
        NSString *documentsDirectory = [paths objectAtIndex:0];
        NSString* path = [documentsDirectory stringByAppendingPathComponent:
                          imageName ];
        NSData* data = UIImagePNGRepresentation(image);
        [data writeToFile:path atomically:YES];
        
    }
    
}


+(UIImage*)getImageFromLocal:(NSString*)imageName{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
                                                         NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString* path = [documentsDirectory stringByAppendingPathComponent:
                      imageName ];
    UIImage* image = [UIImage imageWithContentsOfFile:path];
    return image;
}


+(void)displayCircleImage:(UIImageView*)imageView{
    [imageView.layer setCornerRadius:imageView.frame.size.height/2];
    [imageView.layer setBorderWidth:0];
    [imageView.layer setMasksToBounds:YES];
    [imageView setClipsToBounds:YES];
}


+ (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize {
    //UIGraphicsBeginImageContext(newSize);
    // In next line, pass 0.0 to use the current device's pixel scaling factor (and thus account for Retina resolution).
    // Pass 1.0 to force exact pixel size.
    UIGraphicsBeginImageContextWithOptions(newSize, NO, 0.0);
    [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}
@end
