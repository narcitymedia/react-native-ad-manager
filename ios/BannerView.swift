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

class BannerView: UIView {
	// #region Private properties
	private var adRequest: DFPRequest = DFPRequest()
	private var bannerView: DFPBannerView = DFPBannerView() {
		didSet {
			self.onBannerViewChange()
		}
	}
	// #endregion
	
	// #region Public properties
	@objc var onAdClicked: RCTDirectEventBlock?
    @objc var onAdClosed: RCTDirectEventBlock?
    @objc var onAdLoaded: RCTDirectEventBlock?
    @objc var onAdFailedToLoad: RCTDirectEventBlock?
    @objc var onAdRequest: RCTDirectEventBlock?
	@objc var onPropsSet: RCTDirectEventBlock? {
		didSet {
			self.onPropsSetChange()
		}
	}
	
	@objc var adId: String? = nil {
        didSet {
			self.onAdIdChange()
        }
    }

	@objc var adSizes: Array<Array<Int>> = [] {
        didSet {
			self.onAdSizesChange()
        }
    }

	@objc var targeting: NSDictionary = [:] {
		didSet {
			self.onTargetingChange()
		}
	}
	
	@objc var contentURL: String = "" {
		didSet {
			self.onContentURLChange()
		}
	}
	
	@objc var testDeviceIds: Array<String> = [""] {
		didSet {
			self.onTestDeviceIdsChange()
		}
	}
	// #endregion
	
	override init(frame: CGRect) {
        super.init(frame: frame)
		
		self.onBannerViewChange()
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
	
	// #region Property change handlers
	private func onBannerViewChange() {
		let rootViewController = UIApplication.shared.delegate?.window??.rootViewController
		
		self.bannerView.delegate = self
		self.bannerView.appEventDelegate = self
		self.bannerView.rootViewController = rootViewController
		self.bannerView.translatesAutoresizingMaskIntoConstraints = false
	}
	
	private func onPropsSetChange() {
		self.maybeSendPropSetEvent()
	}
	
	private func onAdIdChange() {
		self.bannerView.adUnitID = self.adId
		self.maybeSendPropSetEvent()
	}
	
	private func onAdSizesChange() {
		var validAdSizes = [NSValue]()

		for sizes in self.adSizes {
            let width = sizes[0]
            let height = sizes[1]
            let customGADAdSize = GADAdSizeFromCGSize(CGSize(width: width, height: height))
            validAdSizes.append(NSValueFromGADAdSize(customGADAdSize))
        }
		
		self.bannerView.validAdSizes = validAdSizes
		self.maybeSendPropSetEvent()
	}
	
	private func onTargetingChange() {
		var customTargeting: [AnyHashable: Any] = [:]

		for key in self.targeting.allKeys {
			customTargeting[key as! String] = self.targeting[key]
        }
		
		self.adRequest.customTargeting = customTargeting
	}
	
	private func onContentURLChange() {
		self.adRequest.contentURL = self.contentURL
	}
	
	private func onTestDeviceIdsChange() {
		self.adRequest.testDevices = self.testDeviceIds
	}
	// #endregion

	// #region Private methods
	func loadAd(){
		bannerView.load(self.adRequest)
		
		if self.onAdRequest != nil {
			self.onAdRequest!([:])
		}
    }
	
    func addAdView(){
        self.addSubview(bannerView)
    }

    func destroyAdView(){
		self.bannerView.removeFromSuperview()
    }

    func removeAdView() {
        self.removeReactSubview(bannerView)
    }

	func maybeSendPropSetEvent(){
		if(self.adId != nil && self.adSizes.count > 0 && self.onPropsSet != nil) {
			self.onPropsSet!([:])
        }
    }
	// #endregion

	// #region Public methods
	@objc func loadBanner() {
		self.loadAd()
	 }
	
    @objc func addBannerView() {
		self.addAdView()
    }

    @objc func destroyBanner() {
		if self.bannerView != nil {
			self.destroyAdView()
        }
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
	// #endregion
}

extension BannerView: GADBannerViewDelegate {
	
	/**
	This is called by the Mobile Ads SDK when an ad successfully fills
	*/
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

	/**
	This is called by the Mobile Ads SDK when an ad fails to load for any reason
	*/
    func adView(_ bannerView: GADBannerView, didFailToReceiveAdWithError error: GADRequestError) {
        print("\(LOG_TAG): Ad failed to load. Reason: \(error.localizedDescription)")
		
		if self.onAdFailedToLoad != nil {
			onAdFailedToLoad!(["errorMessage": error.localizedDescription])
		}
    }
}

extension BannerView: GADAppEventDelegate {
	
	/**
	This is called by the Mobile Ads SDK to notify the application of certain ad events such as click and close
	*/
	func adView(_ banner: GADBannerView, didReceiveAppEvent name: String, withInfo info: String?) {
        switch name {
			case AD_CLICKED:
				self.handleAdClicked(url: info ?? "unknown")
				break
			case AD_CLOSED:
				self.handleAdClosed()
				break
			default:
				break
        }
    }
	
	func handleAdClicked(url: String) {
		print("\(LOG_TAG): Ad clicked")
		
		if self.onAdClicked != nil {
			self.onAdClicked!(["url": url])
		}
	}
	
	func handleAdClosed() {
		print("\(LOG_TAG): Ad closed")

		self.destroyAdView()
		
		if self.onAdClosed != nil {
			self.onAdClosed!([:])
		}
	}
}
