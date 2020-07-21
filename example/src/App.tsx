import React from "react";
import { SafeAreaView, ScrollView, StyleSheet, Text, useWindowDimensions, View } from "react-native";
import { AdSize, RNAutomaticBanner, simulatorTestId } from "react-native-ad-manager";

const AD_UNIT_ID = "/6499/example/banner";

export default function App() {
	const window = useWindowDimensions();

	return (
		<SafeAreaView style={styles.safe}>
			<ScrollView style={styles.container} contentContainerStyle={{ alignItems: "center" }}>
				<View style={styles.item}>
					<Text style={styles.text}>
						Banner
					</Text>

					<RNAutomaticBanner
						adId={AD_UNIT_ID}
						adSizes={[AdSize.BANNER]}
						testDeviceIds={[simulatorTestId]}
					/>
				</View>

				<View style={styles.item}>
					<Text style={styles.text}>
						Large Banner
					</Text>

					<RNAutomaticBanner
						adId={AD_UNIT_ID}
						adSizes={[AdSize.LARGE_BANNER]}
						testDeviceIds={[simulatorTestId]}
					/>
				</View>

				<View style={styles.item}>
					<Text style={styles.text}>
						Medium Rectangle
					</Text>

					<RNAutomaticBanner
						adId={AD_UNIT_ID}
						testDeviceIds={[simulatorTestId]}
						adSizes={[AdSize.MEDIUM_RECTANGLE]}
					/>
				</View>

				{window.width > AdSize.FULL_BANNER[0] &&
					<View style={styles.item}>
						<Text style={styles.text}>
							Full Banner
						</Text>

						<RNAutomaticBanner
							adId={AD_UNIT_ID}
							adSizes={[AdSize.FULL_BANNER]}
							testDeviceIds={[simulatorTestId]}
						/>
					</View>
				}

				{window.width > AdSize.LEADERBOARD[0] &&
					<View style={styles.item}>
						<Text style={styles.text}>
							Leaderboard
						</Text>

						<RNAutomaticBanner
							adId={AD_UNIT_ID}
							adSizes={[AdSize.LEADERBOARD]}
							testDeviceIds={[simulatorTestId]}
						/>
					</View>
				}
			</ScrollView>
		</SafeAreaView>
	);
}

const styles = StyleSheet.create({
	safe: {
		flex: 1,
	},
	container: {
		flex: 1,
		padding: 15
	},
	item: {
		marginBottom: 15
	},
	text: {
		fontSize: 18,
		marginBottom: 15,
		fontWeight: "bold",
		textAlign: "center"
	}
});
