package com.reactnativeadmanager

import android.content.Context
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.gms.ads.doubleclick.PublisherAdView

class BannerView(context: Context): ReactViewGroup(context) {

	private var adView: PublisherAdView? = null;

	// region PRIVATE METHODS
	private fun loadAd() {

	}

	private fun addAdView() {

	}

	private fun destroyAdView() {

	}

	private fun removeAdView() {

	}
	// endregion

	// region PUBLIC METHODS
	fun loadBanner() {

	}

	fun addBannerView() {

	}

	fun destroyBanner() {

	}

	fun removeBannerView() {

	}

	fun openDebugMenu() {

	}
	// endregion

	// region PROP SETTERS
	fun setAdUnitId(adUnitId: String) {

	}

	fun setAdSizes(adSizes: ReadableArray) {

	}

	fun setTestDeviceIds(testDeviceIds: ReadableArray) {

	}

	fun setTargeting(targeting: ReadableMap) {

	}
	// endregion

	// region UTILITY METHODS

	// endregion
}
