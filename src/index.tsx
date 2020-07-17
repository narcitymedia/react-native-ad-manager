import { NativeModules } from "react-native";

export const { simulatorTestId } = NativeModules.RNAdManager;
export { RNBanner, RNBannerRef } from "./components/RNBanner/RNBanner";
export { AdClickEvent, AdLoadErrorEvent, AdLoadEvent, RNBannerProps } from "./components/RNBanner/RNBannerProps";
export { AdSize, AdSizeName, NumericAdSize } from "./constants/AdSize";

