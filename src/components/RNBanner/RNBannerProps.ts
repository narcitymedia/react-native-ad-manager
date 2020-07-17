import type { StyleProp, ViewStyle } from "react-native";
import type { NumericAdSize } from "src/constants/AdSize";

export interface RNBannerProps {
    adId: string;
    adSizes: NumericAdSize[];
    targeting?: object;
    style?: StyleProp<ViewStyle>;
    testDeviceIds?: string[];

    onAdRequest?(): void;
    onAdFailedToLoad?(): void;
    onAdLoaded?(): void;
    onAdClicked?(): void;
    onAdClosed?(): void;
    onNativeError?(): void;
    onPropsSet?(): void;
}