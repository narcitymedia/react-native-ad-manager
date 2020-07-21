import type { NativeSyntheticEvent, StyleProp, ViewStyle } from "react-native";
import type { NumericAdSize } from "src/constants/AdSize";

export interface RNBannerProps {
    /** 
     * The ad unit ID from which to serve ads.
     * @type {string}
     */
    adId: string;

    /**
     * The available sizes for a particular ad instance.
     * Accepts custom number tuples as well as constants from the `AdSize` enum.
     * @type {[number, number][]}
     */
    adSizes: NumericAdSize[];

    /**
     * Custom targeting properties for a particular ad instance.
     * @type {object}
     */
    targeting?: Record<string, string | any[]>;

    /**
     * A URL that represents the content currently being viewed on screen.
     * This URL will be used for "content-mapping" which provides better fill rates.
     * @type {string}
     */
    contentURL?: string;

    /**
     * A list of device IDs on which test ads can be served.
     * Use the `simulatorTestId` constant to enable this on emulator/simulator.
     * @type {string[]}
     */
    testDeviceIds?: string[];

    /**
     * Style to apply
     * @type {object}
     */
    style?: StyleProp<ViewStyle>;

    /**
     * Function to be called when an ad is requested
     */
    onAdRequest?(): void;

    /**
     * Function to be called when an ad fails to load.
     * @param {object} event The native event details containing the error message.
     */
    onAdFailedToLoad?(event: AdLoadErrorEvent): void;

    /**
     * Function to be called when an ad successfully fills.
     * @param {object} event The native event details containing the size of the ad.
     */
    onAdLoaded?(event: AdLoadEvent): void;

    /**
     * Function to be called when an ad is clicked
     * @param {object} event The native event details containing the URL of the clicked ad.
     */
    onAdClicked?(event: AdClickEvent): void;

    /**
     * Function to be called when an ad is closed
     */
    onAdClosed?(): void;

    /**
     * Function to be called when both `adId` and `adSizes` are set natively.
     */
    onPropsSet?(): void;
}

export type AdLoadEvent = { width: number, height: number };
export type NativeAdLoadEvent = NativeSyntheticEvent<AdLoadEvent>;

export type AdLoadErrorEvent = { errorMessage: string };
export type NativeAdLoadErrorEvent = NativeSyntheticEvent<AdLoadErrorEvent>;

export type AdClickEvent = { url: string };
export type NativeAdClickEvent = NativeSyntheticEvent<AdClickEvent>;