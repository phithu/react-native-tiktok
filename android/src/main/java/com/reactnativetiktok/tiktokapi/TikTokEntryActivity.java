package com.rntiktok.tiktokapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bytedance.sdk.open.tiktok.TikTokOpenApiFactory;
import com.bytedance.sdk.open.tiktok.api.TikTokOpenApi;
import com.bytedance.sdk.open.tiktok.authorize.model.Authorization;
import com.bytedance.sdk.open.tiktok.common.handler.IApiEventHandler;
import com.bytedance.sdk.open.tiktok.common.model.BaseReq;
import com.bytedance.sdk.open.tiktok.common.model.BaseResp;
import com.rntiktok.LoginCallback;
import com.rntiktok.LoginResult;
import com.rntiktok.TiktokModule;

public class TikTokEntryActivity extends Activity implements IApiEventHandler {

    TikTokOpenApi ttOpenApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttOpenApi = TikTokOpenApiFactory.create(this);
        ttOpenApi.handleIntent(getIntent(), this); // receive and parse callback
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof Authorization.Response) {
            TiktokModule manager = TiktokModule.get();
            if (manager != null) {
                LoginCallback callback = manager.getLoginCallback();
                if (callback != null) {
                    LoginResult result = new LoginResult();
                    switch (resp.errorCode) {
                        case 0:
                            Authorization.Response response = (Authorization.Response) resp;
                            result.setMsg("登录成功");
                            result.setInfo(response.authCode);
                            result.setCode(1);
                            break;
                        case -2:
                            result.setCode(0);
                            result.setMsg("你取消了授权");
                            break;
                        default:
                            result.setCode(-1);
                            result.setMsg(resp.errorMsg);
                            break;
                    }
                    callback.onCallback(result);
                }
            }
        }
        finish();
    }

    @Override
    public void onErrorIntent(@Nullable Intent intent) {
        Log.d("Tiktok", "Intent Error");
        finish();
    }
}