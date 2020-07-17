import React, { useState } from "react";
import { StyleSheet, Text, View } from "react-native";
import { AdLoadEvent, AdSize, NumericAdSize, RNAutomaticBanner, simulatorTestId } from "react-native-ad-manager";

const AD_UNIT_ID = "YOUR_AD_UNIT_ID";

export default function App() {
	const [size, setSize] = useState<NumericAdSize>();

	function onAdLoaded(event: AdLoadEvent) {
		setSize([event.width, event.height]);
	}

	return (
		<View style={styles.container}>
			<Text style={styles.text}>
				{size ? `The current ad is ${size[0]}px by ${size[1]}px` : "Requesting ad..."}
			</Text>

			<RNAutomaticBanner
				adId={AD_UNIT_ID}
				onAdLoaded={onAdLoaded}
				testDeviceIds={[simulatorTestId]}
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
	},
	text: {
		fontSize: 18,
		fontWeight: "bold",
		marginBottom: 15
	}
});
