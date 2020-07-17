import React, { FC, useEffect, useRef, useState } from "react";
import { NumericAdSize } from "src/constants/AdSize";
import { RNBanner, RNBannerRef } from "../RNBanner/RNBanner";
import { AdLoadEvent, RNBannerProps } from "../RNBanner/RNBannerProps";

export const RNAutomaticBanner: FC<RNBannerProps> = props => {
    const { onAdLoaded, style, ...rest } = props;
    const [size, setSize] = useState<NumericAdSize>([0, 0]);

    const banner = useRef<RNBannerRef>(null);

    useEffect(() => {
        banner.current?.loadBanner();
    }, [props.adId]);

    function handleAdLoaded(event: AdLoadEvent) {
        setSize([event.width, event.height]);
        banner.current?.addBannerView();
        onAdLoaded?.(event);
    }

    return (
        <RNBanner
            {...rest}
            ref={banner}
            onAdLoaded={handleAdLoaded}
            style={[{ width: size[0], height: size[1] }, style]}
        />
    );
}