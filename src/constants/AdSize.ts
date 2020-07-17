
export type AdSizeName = "BANNER" | "FULL_BANNER" | "LARGE_BANNER" | "LEADERBOARD" | "MEDIUM_RECTANGLE";
export type NumericAdSize = [number, number];

export const AdSize: Record<AdSizeName, NumericAdSize> = {
    BANNER: [320, 50],
    LARGE_BANNER: [320, 100],
    MEDIUM_RECTANGLE: [300, 250],
    FULL_BANNER: [468, 60],
    LEADERBOARD: [728, 90]
}