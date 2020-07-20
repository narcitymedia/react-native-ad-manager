package com.reactnativeadmanager

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

class AdManagerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

	companion object {
		const val MODULE_NAME = "RNAdManager"
	}

    override fun getName(): String {
        return AdManagerModule.MODULE_NAME
    }

    // Example method
    // See https://facebook.github.io/react-native/docs/native-modules-android
    @ReactMethod
    fun multiply(a: Int, b: Int, promise: Promise) {

      promise.resolve(a * b)

    }


}
