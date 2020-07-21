# React Native Ad Manager

A React Native wrapper around the native Google Ad Manager SDKs with full support for TypeScript.

# Motivation

A handful of Google Ad Manager wrappers for React Native already exist but they all have flaws such as poor typing, no support for content mapping, etc. This repository was inspired by [callosum-software/react-native-google-ad-manager](https://github.com/callosum-software/react-native-google-ad-manager) at first and then heavily refactored to improve code readability and organization, especially on iOS. 

## Installation

Considering this repository is private and the package has not been published to npm, you will need to add this to your `package.json`, under the `dependencies` key:

```json
"react-native-ad-manager": "narcitymedia/react-native-ad-manager"
```

## Usage

### Automatic banner

This component will automatically handle ad requests on mount, sizing as well as cleanup on unmount.

```typescript
import { RNAutomaticBanner, AdSize, AdLoadEvent, AdLoadErrorEvent, AdClickEvent, simulatorTestId } from "react-native-ad-manager";

export const SomeComponent: FC = () => {

    function onAdRequest() {
        console.log("A new ad request is in progress");
    }

    function onAdLoaded(event: AdLoadEvent) {
        console.log(`A ${event.width} x ${event.height} ad was succesfully loaded`);
    }

    function onAdFailedToLoad(event: AdLoadErrorEvent) {
        console.log(`An ad failed to load - ${event.errorMessage}`);
    }

    function onAdClicked(event: AdClickEvent) {
        console.log(`An ad has been clicked, redirecting to ${event.url}`);
    }

    function onAdClosed() {
        console.log("An ad has been closed");
    }

    return (
        <RNAutomaticBanner
            adId="YOUR_AD_UNIT_ID"
            onAdLoaded={onAdLoaded}
            onAdClosed={onAdClosed}
            onAdRequest={onAdRequest}
            onAdClicked={onAdClicked}
            testDeviceIds={[simulatorTestId]}
            onAdFailedToLoad={onAdFailedToLoad}
            adSizes={[AdSize.BANNER, AdSize.MEDIUM_RECTANGLE]}
        />
    );
}
```

### Manual banner

This component gives you more freedom by giving you full control over the whole process.

```typescript
import { RNBanner, RNBannerRef, AdSize, NumericAdSize, AdLoadEvent, AdLoadErrorEvent, AdClickEvent, simulatorTestId } from "react-native-ad-manager";

export const SomeComponent: FC = () => {
    const [size, setSize] = useState<NumericAdSize>([0, 0]);

    const banner = useRef<RNBannerRef>(null);

    useEffect(() => {
        banner.current?.loadBanner();

        return () => {
            banner.current?.removeBannerView();
            banner.current?.destroyBanner();
        }
    }, []);

    function onAdRequest() {
        console.log("A new ad request is in progress");
    }

    function onAdLoaded(event: AdLoadEvent) {
        banner.current?.addBannerView();
        setSize([event.width, event.height]);
        console.log(`A ${event.width} x ${event.height} ad was succesfully loaded`);
    }

    function onAdFailedToLoad(event: AdLoadErrorEvent) {
        console.log(`An ad failed to load - ${event.errorMessage}`);
    }

    function onAdClicked(event: AdClickEvent) {
        console.log(`An ad has been clicked, redirecting to ${event.url}`);
    }

    function onAdClosed() {
        console.log("An ad has been closed");
    }

    return (
        <RNBanner
            ref={banner}
            adId="YOUR_AD_UNIT_ID"
            onAdLoaded={onAdLoaded}
            onAdClosed={onAdClosed}
            onAdRequest={onAdRequest}
            onAdClicked={onAdClicked}
            testDeviceIds={[simulatorTestId]}
            onAdFailedToLoad={onAdFailedToLoad}
            style={{ width: size[0], height: size[1] }}
            adSizes={[AdSize.BANNER, AdSize.MEDIUM_RECTANGLE]}
        />
    );
}
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
