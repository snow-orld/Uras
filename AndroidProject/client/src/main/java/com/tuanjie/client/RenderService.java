package com.tuanjie.client;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.unity3d.renderservice.client.TuanjieRenderService;

public class RenderService {
    public TuanjieRenderService mRenderService;
    private static final String TAG = "URASClient";
    private final String servicePackageName;
    public RenderService(String serviceName) { servicePackageName = serviceName; }

    public void init(Context context) {
        // Create a renderService by invoking init function
        // renderService maintains the connection with service
        mRenderService = TuanjieRenderService.init(context, servicePackageName);
        mRenderService.addCallback(new TuanjieRenderService.Callback() {
            @Override
            public void onServiceConnected() {
                Log.i(TAG, "[onServiceConnected]");
            }

            @Override
            public void onServiceDisconnected() {
                Log.i(TAG, "[onServiceDisconnected]");
            }

            @Override
            public void onClientStopService() {
                Log.i(TAG, "[onClientStopService]");
            }

            @Override
            public void onServiceStartRenderView(int viewHashCode, int displayIndex) {
                Log.i(TAG, "[onServiceStartRenderView] " + viewHashCode);
            }

            @Override
            public void onSoftInputNotified(int displayIndex, String initialText, int type, boolean correction, boolean multiline, boolean secure, boolean alert, String textPlaceholder, int characterLimit, boolean isInputFieldHidden, boolean consumeOutsideTouches) {
                Log.i(TAG, "[onSoftInputNotified]");
            }

            @Override
            public void onHideSoftInput() {
                Log.i(TAG, "[onHideSoftInput]");
            }

            @Override
            public void onDisplayRegisteredByOther(int oldViewHashCode, int displayIndex, String newViewPkgName, int newViewHashCode) {

            }

            @Override
            public void onServicePausedOrResumed(String sourcePkgName, boolean isPaused) {

            }
        });
    }

    public void start() {
        // start service and bind service, client's tuanjieview shows camear content
        if (mRenderService != null) {
            Log.i(TAG, "ensureStarted");
            mRenderService.ensureStarted();
        }
    }

    public void stop() {
        // unbind service, if call clients unbind, service exists
        if (mRenderService != null) {
            Log.i(TAG, "ensureStopped");
            mRenderService.ensureStopped();
        }
    }

    public void updateDisplayResolution(int displayIndex) {

    }

    // Below is customized
    // 发送消息到 service 侧，然后 service 侧的 uras 库接收到会直接转发到 unity c# 侧
    // unity侧的场景中需要有一个名为 App 的 gameObject
    // App 的脚本中 有个函数名为 OnReceiveC2SData
    // 发送的数据的 key 为表中的 {groupName}.{methodName}
    //    如: public const string CommonIsNightMode = "Common.IsNightMode";
    // 发送的数据的 data 就是具体的数值的 ToString()
    private final String c2sGameObject = "App";
    private final String c2sMethodName = "OnReceiveC2SData";
    public void sendC2SMethodToServiceUnity(String key, String data) {
        try {
            if (mRenderService != null) {
                Log.i(TAG, "sendC2SMethodToServiceUnity: " + key);
                mRenderService.c2sSendMessage(c2sGameObject, c2sMethodName, _createPackage(key, data));
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static String _createPackage(String key, String data) {
        return String.format("{\"k\":\"%s\",\"v\":\"%s\"}", key, data);
    }
}
