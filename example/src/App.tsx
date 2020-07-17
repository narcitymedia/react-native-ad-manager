import React, { useEffect, useRef, useState } from "react";
import { StyleSheet, View } from "react-native";
import { AdLoadEvent, AdSize, NumericAdSize, RNBanner, RNBannerRef, simulatorTestId } from "react-native-ad-manager";

export default function App() {
	const [size, setSize] = useState<NumericAdSize>([0, 0]);

	const banner = useRef<RNBannerRef>(null);

	useEffect(() => {
		banner.current?.loadBanner()

		return () => {
			banner.current?.removeBannerView();
			banner.current?.destroyBanner();
		}
	}, []);

	function onAdLoaded(event: AdLoadEvent) {
		setSize([event.width, event.height]);
		banner.current?.addBannerView();
	}

	return (
		<View style={styles.container}>
			<RNBanner
				ref={banner}
				adId="YOUR_AD_UNIT_ID"
				onAdLoaded={onAdLoaded}
				testDeviceIds={[simulatorTestId]}
				style={{ width: size[0], height: size[1] }}
				adSizes={[AdSize.BANNER, AdSize.MEDIUM_RECTANGLE]}
			/>
		</View>
	);
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		alignItems: "center",
		justifyContent: "center"
	}
});
