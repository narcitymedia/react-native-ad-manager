package com.reactnativeadmanager

import android.util.Log
import android.view.View
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.reactnativeadmanager.constants.AdEvent
import com.reactnativeadmanager.constants.Command

class BannerViewManager: ViewGroupManager<BannerView>() {

	companion object {
		const val REACT_CLASS = "RNBannerView"
	}

	override fun getName(): String {
		return BannerViewManager.REACT_CLASS
	}

	override fun createViewInstance(reactContext: ThemedReactContext): BannerView {
		return BannerView(reactContext);
	}

	override fun addView(parent: BannerView?, child: View?, index: Int) {
		throw RuntimeException("RNBannerView cannot have subviews")
	}

	override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any>? {
		return MapBuilder.builder<String, Any>()
			.put(AdEvent.CLICKED.name,
				MapBuilder.of(
					"phasedRegistrationNames",
					MapBuilder.of("bubbled", "onAdClicked")))
			.put(AdEvent.CLOSED.name,
				MapBuilder.of(
					"phasedRegistrationNames",
					MapBuilder.of("bubbled", "onAdClosed")))
			.put(AdEvent.FAILED.name,
				MapBuilder.of(
					"phasedRegistrationNames",
					MapBuilder.of("bubbled", "onAdFailedToLoad")))
			.put(AdEvent.LOADED.name,
				MapBuilder.of(
					"phasedRegistrationNames",
					MapBuilder.of("bubbled", "onAdLoaded")))
			.put(AdEvent.REQUEST.name,
				MapBuilder.of(
					"phasedRegistrationNames",
					MapBuilder.of("bubbled", "onAdRequest")))
			.put(AdEvent.PROPS_SET.name,
				MapBuilder.of(
					"phasedRegistrationNames",
					MapBuilder.of("bubbled", "onPropsSet")))
			.build()
	}

	override fun getCommandsMap(): MutableMap<String, Int> {
		return MapBuilder.of(
			"addBannerView", Command.ADD_BANNER_VIEW.ordinal,
			"destroyBanner", Command.DESTROY_BANNER.ordinal,
			"loadBanner", Command.LOAD_BANNER.ordinal,
			"removeBannerView", Command.REMOVE_BANNER_VIEW.ordinal,
			"openDebugMenu", Command.OPEN_DEBUG_MENU.ordinal
		)
	}

	override fun receiveCommand(view: BannerView, commandId: Int, args: ReadableArray?) {
		when(commandId) {
			Command.ADD_BANNER_VIEW.ordinal -> view.addBannerView()
			Command.DESTROY_BANNER.ordinal -> view.destroyBanner()
			Command.LOAD_BANNER.ordinal -> view.loadBanner()
			Command.REMOVE_BANNER_VIEW.ordinal -> view.removeBannerView()
			Command.OPEN_DEBUG_MENU.ordinal -> view.openDebugMenu()
			else -> Log.d(AdManagerModule.REACT_CLASS, "Unknown command identifier $commandId")
		}
	}

	@ReactProp(name = "adId")
	fun setAdId(view: BannerView, adId: String) {
		view.setAdUnitId((adId))
	}

	@ReactProp(name = "adSizes")
	fun setAdSizes(view: BannerView, adSizes: ReadableArray) {
		view.setAdSizes(adSizes)
	}

	@ReactProp(name = "contentURL")
	fun setContentURL(view: BannerView, contentURL: String) {
		view.setContentURL(contentURL)
	}

	@ReactProp(name = "targeting")
	fun setTargeting(view: BannerView, targeting: ReadableMap) {
		view.setTargeting(targeting)
	}

	@ReactProp(name = "testDeviceIds")
	fun setTestDeviceIds(view: BannerView, testDeviceIds: ReadableArray) {
		view.setTestDeviceIds(testDeviceIds)
	}
}
