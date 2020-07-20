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
		this.ensureAdViewCreated()
		this.adView?.adUnitId = adUnitId
	}

	fun setAdSizes(adSizes: ReadableArray) {
		this.ensureAdViewCreated()
	}

	fun setTestDeviceIds(testDeviceIds: ReadableArray) {
		this.ensureAdViewCreated()
	}

	fun setTargeting(targeting: ReadableMap) {
		this.ensureAdViewCreated()
	}
	// endregion

	// region UTILITY METHODS
	private fun ensureAdViewCreated() {
		if (this.adView != null) {
			return;
		}

		this.adView = PublisherAdView(this.context)
	}
	// endregion
}
