import React, { forwardRef, useImperativeHandle, useRef } from "react";
import { findNodeHandle, requireNativeComponent, UIManager } from "react-native";
import { AdState } from "../../constants/AdState";
import { ViewState } from "../../constants/ViewState";
import type { NativeAdClickEvent, NativeAdLoadErrorEvent, NativeAdLoadEvent, RNBannerProps } from "./RNBannerProps";

export interface RNBannerRef {
    /**
     * Requests a banner fill from the Google Ad Manager SDK
     */
    loadBanner(): void;

    /**
     * Cancels a banner fill
     */
    destroyBanner(): void;

    /**
     * Adds a native banner view to the layout tree
     */
    addBannerView(): void;

    /**
     * Removes a native banner view from the layout tree
     */
    removeBannerView(): void;

    /**
     * Opens the debug menu for the current ad instance
     */
    openDebugMenu(): void;
}

export const RNBanner = forwardRef<RNBannerRef, RNBannerProps>((props, ref)=> {
    const { targeting = {}, testDeviceIds = [], onAdRequest, onAdFailedToLoad, onAdLoaded, onAdClicked, onAdClosed, onPropsSet, ...rest } = props;

    const bannerViewRef = useRef<any>(null);
    const arePropsSet = useRef<boolean>(false);
    const isRequestedToAdd = useRef<boolean>(false);
    const isRequestedToLoad = useRef<boolean>(false);
    const adState = useRef<AdState>(AdState.DESTROYED);
    const viewState = useRef<ViewState>(ViewState.REMOVED);

    useImperativeHandle(ref, () => ({
        loadBanner,
        destroyBanner,
        addBannerView,
        removeBannerView,
        openDebugMenu
    }));

    /**
     * Requests a banner fill from the Google Ad Manager SDK
     */
    function loadBanner() {
        isRequestedToLoad.current = !arePropsSet.current;

        if (arePropsSet.current) {
            adState.current = AdState.REQUESTED;
            dispatchCommand("loadBanner");
        }
    }

    /**
     * Cancels a banner fill
     */
    function destroyBanner() {
        if (adState.current === AdState.DESTROYED) {
            warn("destroyBanner");
            return;
        }

        adState.current = AdState.DESTROYED;
        dispatchCommand("destroyBanner")
    }

    /**
     * Adds a native banner view to the layout tree
     */
    function addBannerView() {
        isRequestedToAdd.current = !arePropsSet.current;

        if (!arePropsSet.current) {
            return;
        }

        else if (viewState.current === ViewState.ADDED) {
            warn("addBannerView");
            return;
        }

        dispatchCommand("addBannerView");
    }

    /**
     * Removes a native banner view from the layout tree
     */
    function removeBannerView() {
        if (viewState.current === ViewState.REMOVED) {
            warn("removeBannerView");
            return;
        }

        viewState.current = ViewState.REMOVED;
        dispatchCommand("removeBannerView");
    }

    /**
     * Opens the debug menu for the current ad instance
     */
    function openDebugMenu() {
        if (!arePropsSet.current) {
            return;
        }

        dispatchCommand("openDebugMenu");
    };

    function handleAdRequest() {
        onAdRequest?.();
    }

    function handleAdFailedToLoad(event: NativeAdLoadErrorEvent) {
        onAdFailedToLoad?.(event.nativeEvent);
    }

    function handleAdLoaded(e: NativeAdLoadEvent) {
        onAdLoaded?.(e.nativeEvent);
    }

    function handleAdClicked(event: NativeAdClickEvent) {
        onAdClicked?.(event.nativeEvent);
    }

    function handleAdClosed() {
        onAdClosed?.();
    }

    function handlePropsSet() {
        arePropsSet.current = true;

        if (isRequestedToLoad.current) {
            loadBanner();
        }

        if (isRequestedToAdd.current) {
            addBannerView();
        }

        onPropsSet?.();
    }

    /**
     * Shows a warning regarding duplicate function call, only in development mode
     * @param functioName The name of the function that was called
     */
    function warn(functionName: string) {
        if (!__DEV__) {
            return;
        }

        console.warn(`You called ${functionName} even though it was already called. This probably means you have some kind of logic in place that is not working as expected.`);
    }

    /**
     * Sends a command to the native module
     * @param commandName The name of the command to invoke
     */
    function dispatchCommand(commandName: string, args: any[] = []) {
        if (!bannerViewRef.current) {
            return;
        }

        UIManager.dispatchViewManagerCommand(
            findNodeHandle(bannerViewRef.current),
            UIManager.getViewManagerConfig("RNBannerView").Commands[commandName],
            args
        );
    }

    return (
        <RNBannerView
            {...rest}
            ref={bannerViewRef}
            targeting={targeting}
            onAdLoaded={handleAdLoaded}
            onPropsSet={handlePropsSet}
            onAdClosed={handleAdClosed}
            onAdRequest={handleAdRequest}
            testDeviceIds={testDeviceIds}
            onAdClicked={handleAdClicked}
            onAdFailedToLoad={handleAdFailedToLoad}
        />
    );
});

interface RNBannerViewProps extends Omit<RNBannerProps, "onAdFailedToLoad" | "onAdLoaded" | "onAdClicked"> {
    onAdFailedToLoad(event: NativeAdLoadErrorEvent): void;
    onAdLoaded(event: NativeAdLoadEvent): void;
    onAdClicked(event: NativeAdClickEvent): void;
}

const RNBannerView = requireNativeComponent<RNBannerViewProps>("RNBannerView");