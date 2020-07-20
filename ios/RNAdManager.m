//
//  RNAdManager.m
//  AdManager
//
//  Created by Yanick Bélanger on 2020-07-17.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "React/RCTViewManager.h"

@interface RCT_EXTERN_MODULE(RNAdManager, NSObject)

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

@end

@interface RCT_EXTERN_REMAP_MODULE(RNBannerView, BannerViewManager, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(adId, NSString)
RCT_EXPORT_VIEW_PROPERTY(adSizes, NSArray<NSArray>)
RCT_EXPORT_VIEW_PROPERTY(targeting, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(testDeviceIds, NSArray<NSString>)

RCT_EXPORT_VIEW_PROPERTY(onAdClicked, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdClosed, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLoaded, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdFailedToLoad, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdRequest, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onPropsSet, RCTDirectEventBlock)

RCT_EXTERN_METHOD(addBannerView:(nonnull NSNumber *)node)
RCT_EXTERN_METHOD(loadBanner:(nonnull NSNumber *)node)
RCT_EXTERN_METHOD(destroyBanner:(nonnull NSNumber *)node)
RCT_EXTERN_METHOD(removeBannerView:(nonnull NSNumber *)node)
RCT_EXTERN_METHOD(openDebugMenu:(nonnull NSNumber *)node)

@end
