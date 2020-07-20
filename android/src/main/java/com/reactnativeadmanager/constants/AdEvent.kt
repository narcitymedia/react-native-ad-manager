package com.reactnativeadmanager.constants

enum class AdEvent(eventName: String) {
	REQUEST("AD_REQUEST"),
	FAILED("AD_FAILED"),
	LOADED("AD_LOADED"),
	CLICKED("AD_CLICKED"),
	CLOSED("AD_CLOSED"),
	NATIVE_ERROR("NATIVE_ERROR"),
	PROPS_SET("PROPS_SET")
}
