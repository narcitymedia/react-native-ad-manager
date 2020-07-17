//
//  RNAdManager.swift
//  AdManager
//
//  Created by Yanick Bélanger on 2020-07-17.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import GoogleMobileAds

@objc(RNAdManager)
class RNAdManager: NSObject {
	
	@objc
	func constantsToExport() -> [AnyHashable: Any] {
		return [
            "simulatorTestId": kGADSimulatorID
        ]
	}
}
