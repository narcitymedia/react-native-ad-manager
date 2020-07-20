package com.reactnativeadmanager

import android.content.Context
import android.util.Log
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherAdView
import java.lang.Exception
import java.util.Arrays


class BannerView(context: Context): ReactViewGroup(context) {

	private var adView: PublisherAdView = PublisherAdView(context);
	private var adRequestBuilder: PublisherAdRequest.Builder = PublisherAdRequest.Builder();

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
		if (this.adView.adUnitId.isNullOrEmpty()) {
			return;
		}

		MobileAds.openDebugMenu(
			(context as ReactContext).currentActivity,
			this.adView.adUnitId
		)
	}
	// endregion

	// region PROP SETTERS
	fun setAdUnitId(adUnitId: String) {
		this.adView.adUnitId = adUnitId
	}

	fun setAdSizes(adSizes: ReadableArray) {
		val computedSizes: List<AdSize> = adSizes.toArrayList().map {
			try {
				val size = it as Array<Int>
				AdSize(size[0], size[1])
			}

			catch (exception: Exception) {
				Log.w(AdManagerModule.MODULE_NAME, "Unable to parse ad size - ${exception.localizedMessage}")
				AdSize.INVALID
			}
		}

		this.adView.setAdSizes(*computedSizes.toTypedArray())
	}

	fun setTestDeviceIds(testDeviceIds: ReadableArray) {
		try {
			val devices = testDeviceIds.toArrayList().toMutableList() as MutableList<String>
			RequestConfiguration.Builder().setTestDeviceIds(devices).build()
		}

		catch (exception: Exception) {
			Log.w(AdManagerModule.MODULE_NAME, "Unable to parse testDeviceIds - ${exception.localizedMessage}")
		}
	}

	fun setTargeting(targeting: ReadableMap) {
		targeting.toHashMap().forEach { entry ->
			if (entry.value is ReadableArray) {
				this.adRequestBuilder.addCustomTargeting(
					entry.key,
					(entry.value as ReadableArray)
						.toArrayList()
						.map { it.toString() }
						.toMutableList()
				)
			}

			else {
				this.adRequestBuilder.addCustomTargeting(entry.key, entry.value.toString())
			}
		}
	}
	// endregion

	// region UTILITY METHODS

	// endregion
}
