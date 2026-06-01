package com.tuanjie.testrt;

import android.content.Context;
import android.util.Log;

import com.unity3d.renderservice.client.TuanjieRenderService;

public class RenderService {
    private static final String TAG = "URASClient";
    public TuanjieRenderService mRenderService;
    private final String servicePackageName;

    public RenderService(String serviceName) { servicePackageName = serviceName; }

    public void init(Context context) {
        mRenderService = TuanjieRenderService.init(context, servicePackageName);
        mRenderService.enableAutoReconnect = true;
        mRenderService.addCallback(new TuanjieRenderService.Callback() {
            @Override
            public void onServiceConnected() {

            }

            @Override
            public void onServiceDisconnected() {

            }

            @Override
            public void onClientStopService() {

            }

            @Override
            public void onServiceStartRenderView(int viewHashCode, int displayIndex) {

            }

            @Override
            public void onSoftInputNotified(int displayIndex, String initialText, int type, boolean correction, boolean multiline, boolean secure, boolean alert, String textPlaceholder, int characterLimit, boolean isInputFieldHidden, boolean consumeOutsideTouches) {

            }

            @Override
            public void onHideSoftInput() {

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
}
