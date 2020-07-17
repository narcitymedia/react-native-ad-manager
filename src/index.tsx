import { NativeModules } from "react-native";

export const { simulatorTestId } = NativeModules.RNAdManager;
export { RNAutomaticBanner } from "./components/RNAutomaticBanner/RNAutomaticBanner";
export { RNBanner, RNBannerRef } from "./components/RNBanner/RNBanner";
export { AdClickEvent, AdLoadErrorEvent, AdLoadEvent, RNBannerProps } from "./components/RNBanner/RNBannerProps";
export { AdSize, AdSizeName, NumericAdSize } from "./constants/AdSize";

