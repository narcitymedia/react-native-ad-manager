import type { NativeSyntheticEvent, StyleProp, ViewStyle } from "react-native";
import type { NumericAdSize } from "src/constants/AdSize";

export interface RNBannerProps {
    adId: string;
    adSizes: NumericAdSize[];
    targeting?: Record<string, string | any[]>;
    style?: StyleProp<ViewStyle>;
    testDeviceIds?: string[];

    onAdRequest?(): void;
    onAdFailedToLoad?(event: AdLoadErrorEvent): void;
    onAdLoaded?(event: AdLoadEvent): void;
    onAdClicked?(event: AdClickEvent): void;
    onAdClosed?(): void;
    onPropsSet?(): void;
}

export type AdLoadEvent = { width: number, height: number };
export type NativeAdLoadEvent = NativeSyntheticEvent<AdLoadEvent>;

export type AdLoadErrorEvent = { errorMessage: string };
export type NativeAdLoadErrorEvent = NativeSyntheticEvent<AdLoadErrorEvent>;

export type AdClickEvent = { url: string };
export type NativeAdClickEvent = NativeSyntheticEvent<AdClickEvent>;