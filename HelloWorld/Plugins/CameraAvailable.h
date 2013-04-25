//
//  CameraAvailable.h
//
//
//  Created by Lukas Klein on 08-19-11.
//  MIT Licensed
//  Copyright (c) Lukas Klein

#import
#ifdef PHONEGAP_FRAMEWORK
#import
#else
#import "PGPlugin.h"
#endif

@interface CameraAvailable : PGPlugin { }

- (void)hasCamera:(NSArray*)arguments withDict:(NSDictionary*)options;

@end