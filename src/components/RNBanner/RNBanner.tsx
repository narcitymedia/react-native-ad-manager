import React, { forwardRef, useImperativeHandle, useRef } from "react";
import { findNodeHandle, requireNativeComponent, UIManager } from "react-native";
import { AdState } from "src/constants/AdState";
import { ViewState } from "src/constants/ViewState";
import type { RNBannerProps } from "./RNBannerProps";

interface RNBannerRef {
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
}

export const RNBanner = forwardRef<RNBannerRef, RNBannerProps>((props, ref)=> {
    const { targeting = {}, testDeviceIds = [], onAdRequest, onAdFailedToLoad, onAdLoaded, onAdClicked, onAdClosed, onNativeError, onPropsSet, ...rest } = props;

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
        removeBannerView
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

    function handleAdRequest() {

    }

    function handleAdFailedToLoad() {

    }

    function handleAdLoaded() {

    }

    function handleAdClicked() {

    }

    function handleAdClosed() {

    }

    function handleNativeError() {
        
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
            onNativeError={handleNativeError}
            onAdFailedToLoad={handleAdFailedToLoad}
        />
    );
});

const RNBannerView = requireNativeComponent<RNBannerProps>("RNBannerView");