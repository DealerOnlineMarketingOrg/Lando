//
//  CameraAvailable.m
//
//
//  Created by Lukas Klein on 08-19-11.
//  MIT Licensed
//  Copyright (c) 2011 Lukas Klein

#import "CameraAvailable.h"

@interface CameraAvailable (Private)
-(void) callbackWithFuntion:(NSString *)function withData:(NSString *)value;
@end

@implementation CameraAvailable

- (void)hasCamera:(NSArray*)arguments withDict:(NSDictionary*)options
{
NSUInteger argc = [arguments count];

if (argc < 1) {
return;
}
bool hascamera = [UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera];

NSString *callBackFunction = [arguments objectAtIndex:0];
[self callbackWithFuntion:callBackFunction withData:[NSString stringWithFormat:@"{available: %@}", (hascamera ? @"true" : @"false")]];
}

-(void) callbackWithFuntion:(NSString *)function withData:(NSString *)value{
if (!function || [@"" isEqualToString:function]){
return;
}

NSString* jsCallBack = [NSString stringWithFormat:@"%@(%@);", function, value];
[self writeJavascript: jsCallBack];
}

@end
