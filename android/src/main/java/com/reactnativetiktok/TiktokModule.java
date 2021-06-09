package com.reactnativetiktok;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.bytedance.sdk.open.tiktok.TikTokOpenApiFactory;
import com.bytedance.sdk.open.tiktok.api.TikTokOpenApi;
import com.bytedance.sdk.open.tiktok.authorize.model.Authorization;

@ReactModule(name = TiktokModule.NAME)
public class TiktokModule extends ReactContextBaseJavaModule implements LoginCallback {
  public static final String NAME = "Tiktok";

  public static TiktokModule manager = null;

  LoginCallback callback;
  Callback jscallback;


  public TiktokModule(ReactApplicationContext reactContext) {
    super(reactContext);
    manager = this;
  }

  private void createDYManager(Callback callback) {
    this.jscallback = callback;
    this.setCallback((LoginCallback) this);
  }


  @ReactMethod
  public void login(Callback callBack) {
    this.createDYManager(callBack);
    final Activity activity = getCurrentActivity();
    TikTokOpenApi tiktokOpenApi = TikTokOpenApiFactory.create(activity);
    Authorization.Request request = new Authorization.Request();
    request.scope = "user.info.basic,video.list";
    request.state = "rntiktok";
    tiktokOpenApi.authorize(request);
  }

  public void setCallback(LoginCallback callback) {
    this.callback = callback;
  }

  public LoginCallback getLoginCallback() {
    return callback;
  }

  public static TiktokModule get() {
    return manager;
  }

  @Override
  public void onCallback(LoginResult result) {
    final String authCode = result.getInfo();
    if (!authCode.isEmpty()) {
      jscallback.invoke(null, authCode);
    } else {
      final String message = result.getMsg();
      jscallback.invoke(message, null);
    }
    jscallback = null;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }
}
