package com.reactnativeadmanager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.doubleclick.AppEventListener
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherAdView
import com.reactnativeadmanager.constants.AdEvent
import java.lang.Exception

class BannerView(context: Context): ReactViewGroup(context) {

	private var adView: PublisherAdView
	private var adRequestBuilder: PublisherAdRequest.Builder

	init {
		val view = PublisherAdView(context)
		view.setAdSizes(AdSize.BANNER)

		this.adView = view
		this.adRequestBuilder = PublisherAdRequest.Builder()
	}

	// region PRIVATE METHODS
	private fun loadAd() {
		this.setAdEventListeners()
		this.sendJSEvent(AdEvent.REQUEST)
		this.adView.loadAd(this.adRequestBuilder.build())
	}

	private fun addAdView() {
		if (this.childCount != 0) {
			Log.w(AdManagerModule.REACT_CLASS, "Tried to add view while a child is already present")
			return;
		}

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
		this.loadAd()
	}

	fun addBannerView() {
		this.addAdView()
	}

	fun destroyBanner() {
		this.destroyAdView()
	}

	fun removeBannerView() {
		this.removeAdView()
	}

	fun openDebugMenu() {
		if (this.adView.adUnitId.isNullOrEmpty()) {
			return
		}

		try {
			MobileAds.openDebugMenu(
				(context as ReactContext).currentActivity,
				this.adView.adUnitId
			)
		}

		catch (exception: Exception) {
			MobileAds.initialize(context)
			this.openDebugMenu()
		}
	}
	// endregion

	// region PROP SETTERS
	fun setAdUnitId(adUnitId: String) {
		this.adView.adUnitId = adUnitId
		this.maybeSendPropSetEvent()
	}

	fun setAdSizes(adSizes: ReadableArray) {
		val computedSizes = ArrayList<AdSize>()

		for (i in 0 until adSizes.size()) {
			val size = adSizes.getArray(i);

			if (size == null || size.size() != 2) {
				continue
			}

			computedSizes.add(
				AdSize(
					size.getInt(0),
					size.getInt(1)
				)
			)
		}

		this.adView.setAdSizes(*computedSizes.toTypedArray())
		this.maybeSendPropSetEvent()
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

	fun setContentURL(contentURL: String) {
		this.adRequestBuilder.setContentUrl(contentURL)
	}

	fun setTestDeviceIds(testDeviceIds: ReadableArray) {
		try {
			val devices = testDeviceIds.toArrayList().toMutableList() as MutableList<String>
			RequestConfiguration.Builder().setTestDeviceIds(devices).build()
		}

		catch (exception: Exception) {
			Log.w(AdManagerModule.REACT_CLASS, "Unable to parse testDeviceIds - ${exception.localizedMessage}")
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
			Log.w(AdManagerModule.REACT_CLASS, "Unable to send event ${event.name} - ${exception.localizedMessage}")
		}
	}

	private fun maybeSendPropSetEvent() {
		if (this.adView.adUnitId.isNullOrEmpty() || this.adView.adSizes.isEmpty()) {
			return;
		}

		this.sendJSEvent(AdEvent.PROPS_SET)
	}

	private fun getMessageForAdCode(code: Int): String {
		return when(code) {
			PublisherAdRequest.ERROR_CODE_INTERNAL_ERROR -> "Internal error"
			PublisherAdRequest.ERROR_CODE_INVALID_REQUEST -> "Invalid request"
			PublisherAdRequest.ERROR_CODE_NETWORK_ERROR -> "Network error"
			PublisherAdRequest.ERROR_CODE_NO_FILL -> "No fill"
			else -> "Could not retrieve message. Unknown code: $code"
		}
	}

	private fun setAdEventListeners() {
		this.adView.adListener = object: AdListener() {
			override fun onAdLoaded() {
				super.onAdLoaded()

				Log.d(AdManagerModule.REACT_CLASS, "Ad loaded")

				val width = adView.adSize.width
				val height = adView.adSize.height

				adView.measure(width, height);
				adView.layout(0, 0, adView.adSize.getWidthInPixels(context), adView.adSize.getHeightInPixels(context));

				val event = Arguments.createMap()
				event.putInt("width", width)
				event.putInt("height", height)

				sendJSEvent(AdEvent.LOADED, event)
			}

			override fun onAdFailedToLoad(code: Int) {
				super.onAdFailedToLoad(code)

				val errorMessage = getMessageForAdCode(code)
				Log.d(AdManagerModule.REACT_CLASS, "Ad failed to load - $errorMessage")

				val event = Arguments.createMap()
				event.putString("errorMessage", errorMessage)

				sendJSEvent(AdEvent.FAILED, event)
			}
		}

		this.adView.appEventListener = object: Activity(), AppEventListener {
			override fun onAppEvent(name: String?, info: String?) {
				Log.d(AdManagerModule.REACT_CLASS, "$name - $info")

				when(name) {
					AdEvent.CLICKED.name -> {
						Log.d(AdManagerModule.REACT_CLASS, "Ad clicked - $info")

						val event = Arguments.createMap()
						event.putString("url", info)

						sendJSEvent(AdEvent.CLICKED, event)
					}
					AdEvent.CLOSED.name -> {
						Log.d(AdManagerModule.REACT_CLASS, "Ad closed - $info")

						destroyAdView()
						sendJSEvent(AdEvent.CLOSED)
					}
				}
			}
		}
	}
	// endregion
}
