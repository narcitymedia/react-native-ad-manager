package com.reactnativeadmanager

import android.content.Context
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherAdView
import com.reactnativeadmanager.constants.AdEvent
import java.lang.Exception
import java.util.Arrays


class BannerView(context: Context): ReactViewGroup(context) {

	private var adView: PublisherAdView = PublisherAdView(context)
	private var adRequestBuilder: PublisherAdRequest.Builder = PublisherAdRequest.Builder()

	// region PRIVATE METHODS
	private fun loadAd() {

	}

	private fun addAdView() {
		this.addView(this.adView)
	}

	private fun destroyAdView() {
		this.adView.destroy()
	}

	private fun removeAdView() {
		this.removeView(this.adView)
	}
	// endregion

	// region PUBLIC METHODS
	fun loadBanner() {
		this.sendJSEvent(AdEvent.REQUEST);
		this.adView.loadAd(this.adRequestBuilder.build())
	}

	fun addBannerView() {

	}

	fun destroyBanner() {

	}

	fun removeBannerView() {

	}

	fun openDebugMenu() {
		if (this.adView.adUnitId.isNullOrEmpty()) {
			return
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
	private fun sendJSEvent(event: AdEvent, args: WritableMap? = Arguments.createMap()) {
		try {
			(context as ReactContext)
				.getJSModule(RCTEventEmitter::class.java)
				.receiveEvent(
					this.id,
					event.name,
					args
				)
		}

		catch (exception: Exception) {
			Log.w(AdManagerModule.MODULE_NAME, "Unable to send event ${event.name} - ${exception.localizedMessage}")
		}
	}

	private fun setAdListeners() {

	}
	// endregion
}
