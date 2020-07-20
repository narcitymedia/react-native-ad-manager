//
//  BannerViewManager.swift
//  AdManager
//
//  Created by Yanick Bélanger on 2020-07-17.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import GoogleMobileAds

@objc(BannerViewManager)
class BannerViewManager: RCTViewManager {
	
	override static func requiresMainQueueSetup() -> Bool {
        return true
    }
	
	override func constantsToExport() -> [AnyHashable : Any] {
        return [
            "simulatorTestId": kGADSimulatorID
        ]
    }
	
	override func view() -> UIView! {
        return BannerView()
    }
    
    @objc func addBannerView(_ node: NSNumber) {
        DispatchQueue.main.async {
            let component = self.bridge.uiManager.view(forReactTag: node) as! BannerView
            component.addBannerView()
        }
    }
    
    @objc func loadBanner(_ node: NSNumber) {
        DispatchQueue.main.async {
            let component = self.bridge.uiManager.view(forReactTag: node) as! BannerView
            component.loadBanner()
        }
    }
    
    @objc func destroyBanner(_ node: NSNumber) {
        DispatchQueue.main.async {
            let component = self.bridge.uiManager.view(forReactTag: node) as! BannerView
            component.destroyBanner()
        }
    }
    
    @objc func removeBannerView(_ node: NSNumber) {
        DispatchQueue.main.async {
            let component = self.bridge.uiManager.view(forReactTag: node) as! BannerView
            component.removeBannerView()
        }
    }
	
	@objc func openDebugMenu(_ node: NSNumber) {
		DispatchQueue.main.async {
            let component = self.bridge.uiManager.view(forReactTag: node) as! BannerView
            component.openDebugMenu()
        }
	}
}
