import { NativeModules } from 'react-native';

type TiktokType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Tiktok } = NativeModules;

export default Tiktok as TiktokType;
