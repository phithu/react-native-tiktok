import { NativeModules } from 'react-native';

const { TiktokModule } = NativeModules;

interface TiktokModuleInterface {
  login(): Promise<string>;
}

const TiktokAPI: TiktokModuleInterface = {
  login: () => {
    return new Promise((resolve, reject) => {
      TiktokModule.login((error: string, data: string) => {
        if (data) {
          resolve(data);
        }
        reject(new Error(error));
      });
    });
  },
};

export default TiktokAPI;
