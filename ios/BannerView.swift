//
//  BannerView.swift
//  AdManager
//
//  Created by Yanick Bélanger on 2020-07-17.
//  Copyright © 2020 Facebook. All rights reserved.
//

import UIKit
import Foundation
import GoogleMobileAds

let LOG_TAG = "RNAdManager"

let AD_CLICKED = "AD_CLICKED"
let AD_CLOSED = "AD_CLOSED"
let AD_FAILED = "AD_FAILED"
let AD_LOADED = "AD_LOADED"

class BannerView: UIView, GADAppEventDelegate, GADBannerViewDelegate {
	var adRequest: DFPRequest = DFPRequest()
	var bannerView: DFPBannerView = DFPBannerView()
	
	@objc var onAdClicked: RCTDirectEventBlock?
    @objc var onAdClosed: RCTDirectEventBlock?
    @objc var onAdLoaded: RCTDirectEventBlock?
    @objc var onAdFailedToLoad: RCTDirectEventBlock?
    @objc var onAdRequest: RCTDirectEventBlock?
    @objc var onPropsSet: RCTDirectEventBlock? {
        didSet {
			self.sendIfPropsSet()
        }
    }
	
	@objc var adId: String? = nil {
        didSet {
			self.bannerView.adUnitID = self.adId
			self.sendIfPropsSet()
        }
    }

    @objc var adSizes: Array<Array<Int>>? = nil {
        didSet {
			self.sendIfPropsSet()
        }
    }

    @objc var targeting: NSDictionary = [:]
    @objc var testDeviceIds: Array<String> = [""]
	
	override init(frame: CGRect) {
        super.init(frame: frame)
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func sendIfPropsSet(){
        if(adSizes != nil && adId != nil && onPropsSet != nil){
            onPropsSet!([:])
        }
    }

    func addAdView(){
        self.addSubview(bannerView)
    }

    func destroyAdView(){
		self.bannerView.removeFromSuperview()
    }

    func prepareBannerView(){
        let rootViewController = UIApplication.shared.delegate?.window??.rootViewController

        var validAdSizes = [NSValue]()
        let adSizesArray = adSizes ?? []

        for sizes in adSizesArray {
            let width = sizes[0]
            let height = sizes[1]
            let customGADAdSize = GADAdSizeFromCGSize(CGSize(width: width, height: height))
            validAdSizes.append(NSValueFromGADAdSize(customGADAdSize))
        }
		
        bannerView.delegate = self
        bannerView.appEventDelegate = self
        bannerView.validAdSizes = validAdSizes
        bannerView.rootViewController = rootViewController
        bannerView.translatesAutoresizingMaskIntoConstraints = false
    }

    func removeAdView() {
        self.removeReactSubview(bannerView)
    }

    func loadAd(){
		self.prepareBannerView()
        var customTargeting: [AnyHashable: Any] = [:]

		for key in self.targeting.allKeys {
			customTargeting[key as! String] = self.targeting[key]
        }
		
		self.adRequest.testDevices = testDeviceIds
		self.adRequest.customTargeting = customTargeting

		bannerView.load(self.adRequest)
		
		if self.onAdRequest != nil {
			self.onAdRequest!([:])
		}
    }

    func adViewDidReceiveAd(_ bannerView: GADBannerView) {
        print("\(LOG_TAG): Ad loaded")
		
		if (self.onAdLoaded == nil) {
			return;
		}
		
        onAdLoaded!([
			"width": self.bannerView.adSize.size.width,
			"height": self.bannerView.adSize.size.height
		])
    }

    func adView(_ banner: GADBannerView, didReceiveAppEvent name: String, withInfo info: String?) {
        switch name {
			case AD_CLICKED:
				print("\(LOG_TAG): Ad clicked")
				
				if self.onAdClicked != nil {
					self.onAdClicked!(["url": info])
				}
				break
			case AD_CLOSED:
				print("\(LOG_TAG): Ad closed")
				destroyAdView()
				
				if self.onAdClosed != nil {
					self.onAdClosed!([:])
				}
				break
			default:
				break
        }
    }

    func adView(_ bannerView: GADBannerView, didFailToReceiveAdWithError error: GADRequestError) {
        print("\(LOG_TAG): Ad failed to load. Reason: \(error.localizedDescription)")
		
		if self.onAdFailedToLoad != nil {
			onAdFailedToLoad!(["errorMessage": error.localizedDescription])
		}
    }

    @objc func addBannerView() {
		self.addAdView()
    }

    @objc func destroyBanner() {
		if self.bannerView != nil {
			self.destroyAdView()
        }
    }

    @objc func loadBanner() {
		self.loadAd()
    }

    @objc func removeBannerView() {
		self.removeAdView()
    }
	
	@objc func openDebugMenu() {
		if self.adId == nil {
			return
		}
		
		let rootViewController = UIApplication.shared.delegate?.window??.rootViewController
		let debugViewController = GADDebugOptionsViewController(adUnitID: self.adId!)
		rootViewController?.present(debugViewController, animated: true, completion: nil)
	}
}
