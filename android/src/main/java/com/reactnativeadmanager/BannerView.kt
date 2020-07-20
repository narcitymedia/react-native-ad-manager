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

	private var adView: PublisherAdView = PublisherAdView(context)
	private var adRequestBuilder: PublisherAdRequest.Builder = PublisherAdRequest.Builder()

	// region PRIVATE METHODS
	private fun loadAd() {
		this.sendJSEvent(AdEvent.REQUEST);
		this.adView.loadAd(this.adRequestBuilder.build())
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
		this.loadAd()
	}

	fun addBannerView() {
		this.adView.adListener = object: AdListener() {
			override fun onAdLoaded() {
				super.onAdLoaded()

				Log.d(AdManagerModule.MODULE_NAME, "Ad loaded")

				val event = Arguments.createMap()
				event.putInt("width", adView.adSize.width)
				event.putInt("height", adView.adSize.height)

				sendJSEvent(AdEvent.LOADED, event)
			}

			override fun onAdFailedToLoad(code: Int) {
				val errorMessage = getMessageForAdCode(code)
				Log.d(AdManagerModule.MODULE_NAME, "Ad failed to load - $errorMessage")

				val event = Arguments.createMap()
				event.putString("errorMessage", errorMessage)

				sendJSEvent(AdEvent.FAILED, event)
			}
		}

		this.adView.appEventListener = object: Activity(), AppEventListener {
			override fun onAppEvent(name: String?, info: String?) {
				when(name) {
					AdEvent.CLICKED.name -> {
						Log.d(AdManagerModule.MODULE_NAME, "Ad clicked - $info")

						val event = Arguments.createMap()
						event.putString("url", info)

						sendJSEvent(AdEvent.CLICKED, event)
					}
					AdEvent.CLOSED.name -> {
						Log.d(AdManagerModule.MODULE_NAME, "Ad closed - $info")

						destroyAdView()
						sendJSEvent(AdEvent.CLOSED)
					}
				}
			}
		}

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

		MobileAds.openDebugMenu(
			(context as ReactContext).currentActivity,
			this.adView.adUnitId
		)
	}
	// endregion

	// region PROP SETTERS
	fun setAdUnitId(adUnitId: String) {
		if (this.adView.adUnitId.isNullOrEmpty()) {
			this.maybeSendPropSetEvent()
		}

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

		if (this.adView.adSizes.isEmpty()) {
			this.maybeSendPropSetEvent()
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
	// endregion
}
