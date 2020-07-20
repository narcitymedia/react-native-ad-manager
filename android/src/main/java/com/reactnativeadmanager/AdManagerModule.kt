package com.reactnativeadmanager

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.google.android.gms.ads.doubleclick.PublisherAdRequest

class AdManagerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

	companion object {
		const val MODULE_NAME = "RNAdManager"
	}

    override fun getName(): String {
        return AdManagerModule.MODULE_NAME
    }

	override fun getConstants(): MutableMap<String, Any> {
		return mutableMapOf<String, Any>(
			"simulatorTestId" to PublisherAdRequest.DEVICE_ID_EMULATOR
		)
	}
}
