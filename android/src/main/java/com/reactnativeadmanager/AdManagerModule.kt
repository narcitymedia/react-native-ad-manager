package com.reactnativeadmanager

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.google.android.gms.ads.doubleclick.PublisherAdRequest

class AdManagerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

	companion object {
		const val REACT_CLASS = "RNAdManager"
	}

    override fun getName(): String {
        return AdManagerModule.REACT_CLASS
    }

	override fun getConstants(): MutableMap<String, Any> {
		return mutableMapOf<String, Any>(
			"simulatorTestId" to PublisherAdRequest.DEVICE_ID_EMULATOR
		)
	}
}
