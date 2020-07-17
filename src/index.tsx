import { NativeModules } from 'react-native';

type AdManagerType = {
  multiply(a: number, b: number): Promise<number>;
};

const { AdManager } = NativeModules;

export default AdManager as AdManagerType;
